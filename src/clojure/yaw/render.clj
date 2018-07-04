(ns yaw.render
  (:require [clojure.set :as set]
            [yaw.ratom :as ratom :refer [reactive-atom?]]
            [yaw.scene :as ysc]))

;;{
;; ## Component render
;;
;; We say that a component is rendered when the scene it described
;; is produced, which means that all its children component must
;; also be rendered. This is of course obtained by invoking
;; the component and its subcomponents.
;;
;; Components are invoked on two distinct occasions:
;;
;;  1. when a parent component invoke them
;;  2. when a signal handler invoke them directly (cf. the signal handler section)
;;
;; For performance reason, you do not want to render the component
;; too often. 
;;
;; The (reagent inspired) philosophy here is: if the arguments of a component
;; do not change between two calls, then we do not have to render it, we can
;; just reuse its previous value.
;;}

;; CONTROLLER
;; ----------
;; Will be inside the the universe

;; (defn make-controller []
;;   (atom {:components {}}))

;; XXX: nested #(...) are not allowed ...
;; (defn register-component! [controller component]
;;   (swap! controller #(update % :components #(assoc % component {}))))

(defn register-component! [controller component]
  (swap! controller #(update % :components (fn [comps] (assoc comps component {})))))

(defn unregister-component! [controller component]
  (swap! controller
         (fn [ctrl]
           (update ctrl :components
                   (fn [comps]
                     (if (get comps component)
                       (dissoc comps component)
                       (throw (ex-info "No such component in controller" {:controller ctrl
                                                                          :component component}))))))))
;;{
;;
;; ## Renderer
;;
;; The component renderer takes a possibly partially
;; rendered component, i.e. the result of a component instantiation,
;; and produces a complete scene and an updated controller
;;}

(defn register-ratoms-map [ratoms-map ratoms component]
  (reduce (fn [rmap ratom]
            (when (contains? ratoms-map ratom)
              (throw (ex-info "Reactive atom has already a dependent parent component" {:ratom ratom
                                                                                        :parent-component (get ratoms-map ratom)
                                                                                        :child-component component})))
            (assoc rmap ratom component)) ratoms-map ratoms))

(defn unregister-ratoms-map [ratoms-map component]
  (reduce (fn [rmap [ratom comp]]
            (if (= comp component)
              rmap
              (assoc rmap ratom comp))) {} ratoms-map))

(defn render-component
  [ctrl component]
  (loop [elements (list component), ratoms-map {}, rendering [], rendered #{}, seen #{}, ctrl ctrl]
    (if (seq elements)
      (cond
        ;; if the first element is a scene, splice it into the rest
        (= (ffirst elements) :scene)
        (recur (concat (rest (first elements)) (rest elements))
               ratoms-map
               (conj rendering :scene)
               rendered
               seen
               ctrl)
        ;; if it's the return from a component call
        (= (ffirst elements) ::return)
        (let [component (second (first elements))]
          (recur (rest elements)
                 (unregister-ratoms-map ratoms-map component)
                 rendering
                 rendered
                 seen
                 ctrl))
        ;; if it's a component call
        (fn? (ffirst elements))
        (let [component (ffirst elements)
              args (rest (first elements))
              ratoms (filter reactive-atom? args)
              new-ratoms (filter #(not (contains? ratoms-map %)) ratoms)
              ratom-args (mapv deref ratoms)]
          (println "[render-component] component=" component)
          (println "  => args=" args)
          (println "  => ratom-args=" ratom-args)
          (if-let [comp-infos (get (:components ctrl) component)]
            (do (when (:previous-available comp-infos)
                  (println " => previous args= " (:previous-args comp-infos))
                  (println " => previous ratom args= " (:previous-ratom-args comp-infos)))
                (if (and (:previous-available comp-infos)
                         (= (:previous-args comp-infos) args)
                         (= (:previous-ratom-args comp-infos) ratom-args))
                  ;; no need for a new rendering
                  (do
                    (println "  ---> No need for new rendering <---")
                    (recur (cons (:previous-render comp-infos) (cons [::return component] (rest elements)))
                           (register-ratoms-map ratoms-map new-ratoms component)
                           rendering
                           rendered
                           (conj seen component)
                           ctrl))
                  ;; need to update the component
                  (let [invoked (apply component args)]
                    (println "  ---> Rendering required <---")
                    (recur (cons invoked (cons [::return component] (rest elements)))
                           (register-ratoms-map ratoms-map new-ratoms component)
                           rendering
                           (conj rendered component)
                           (conj seen component)
                           (assoc-in ctrl [:components component]
                                     (assoc comp-infos
                                            :previous-available true
                                            :previous-args (vec args)
                                            :previous-ratom-args ratom-args
                                            :previous-render invoked))))))
            ;; the component is unknown
            (let [ratoms (filter reactive-atom? args)
                  new-ratoms (filter #(not (contains? ratoms-map %)) ratoms)
                  _ (doseq [new-ratom new-ratoms]
                      (ratom/depend-component! new-ratom component))
                  invoked (apply component args)
                  ratom-args (mapv deref ratoms)]
              (println "  ---> Registering unknown component <---")
              (recur (cons invoked (cons [::return component] (rest elements)))
                     (register-ratoms-map ratoms-map new-ratoms component)
                     rendering
                     (conj rendered component)
                     (conj seen component)
                     (assoc-in ctrl [:components component]
                               {:previous-available true
                                :previous-args (vec args)
                                :previous-render invoked
                                :previous-ratom-args ratom-args})))))
        ;; otherwise it is an item, already rendering
        :else
        (recur (rest elements)
               ratoms-map
               (conj rendering (first elements))
               rendered
               seen
               ctrl))
      ;; no more elements to render
      [rendering rendered seen ctrl])))
;;{
;; The main rendering function follows.
;;}

(defn render!
  [controller root-component]
  (let [results (atom {})]
    (swap! controller
           (fn [ctrl]
             (let [[rendering rendered seen ctrl'] (render-component ctrl root-component)]
               (swap! results #(assoc %
                                      :rendering rendering
                                      :rendered rendered
                                      :seen seen))
               ctrl')))
    (let [results-map @results
          rendering (:rendering results-map)
          rendered (:rendered results-map)
          seen (:seen results-map)]
      (println "=== BEGIN RENDER ===")
      (ysc/display-scene! controller rendering)
      (println "=== END RENDER ===")
      [rendered seen])))


