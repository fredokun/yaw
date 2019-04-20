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

;;{
;; Inside the controller map, the components are all listed under
;; the key `:components`. If is a map with the component references
;; as keys and an information map as value. Initially the map is empty.
;; if it is not empty, then it corresponds to save values of arguments
;; so that we can check if something has changed or not...
;;}

(defn register-component!
  "Register the specified scene `component` into the `controller` atom."
  [controller component]
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
;; ## Renderer
;;
;; The component renderer takes a possibly partially
;; rendered component, i.e. the result of a component instantiation,
;; and produces a complete scene and an updated controller
;;}

;; auxiliary functions to maintain the `ratoms-map` in the component renderer
(declare register-ratoms-map
         unregister-ratoms-map)

(defn render-component
  "Renders the scene `component` in the context of the controller `ctrl`."
  [ctrl component]
  (loop [elements (list component)
         , ratoms-map {}  ; we build a map associating each referenced ratom to the components that depend on it
         , rendering []   ; this is finally the items to effectively render
         , rendered #{}   ; we are careful note to render twice the same item
         , seen #{}       ; we also track all the items we encountered
         , ctrl ctrl]     ; the controller state is optionally updated at each step
    ;; the loop proceeds for the component and all its subcomponents (if any)
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
                 (unregister-ratoms-map ratoms-map component) ; the dependency is removed after the return
                 rendering
                 rendered
                 seen
                 ctrl))
        ;; if it's a component call, i.e.  [component args ...]  with component a function
        (fn? (ffirst elements))
        (let [component (ffirst elements)
              args (rest (first elements))
              ratoms (filter reactive-atom? args) ; we analyze all reactive atoms referenced in the arguments
              new-ratoms (filter #(not (contains? ratoms-map %)) ratoms) ; some ratoms are already tracked
              ratom-args (mapv deref ratoms)] ; we take all the current values of the referenced ratoms
          ;;(println "[render-component] component=" component)
          ;;(println "  => args=" args)
          ;;(println "  => ratom-args=" ratom-args)
          ;; We check if there is some available component information (values of a previous call)
          (if-let [comp-infos (get (:components ctrl) component)]
            (do #_(when (:previous-available comp-infos)
                  (println " => previous args= " (:previous-args comp-infos))
                  (println " => previous ratom args= " (:previous-ratom-args comp-infos)))
                ;; In the component infos do we have the values from a previous call?
                ;; moreover, are these previous values the same as the current values
                (if (and (:previous-available comp-infos)
                         (= (:previous-args comp-infos) args) ;
                         (= (:previous-ratom-args comp-infos) ratom-args))
                  ;; if it is the case there is no need for a new rendering of the whole component
                  (do
                    ;; (println "  ---> No need for new rendering <---")
                    (recur (cons (:previous-render comp-infos) (cons [::return component] (rest elements)))
                           ;; XXX: old code does not work since the component returns directly (?)
                           ;; (register-ratoms-map ratoms-map new-ratoms component) ; the dependencies are still listed
                           ratoms-map ;; unchanged is ok (guessing ...)
                           rendering
                           rendered
                           (conj seen component) ; we saw this component already
                           ctrl))
                  ;; otherwise, we need to update the component hence we make an explicit call
                  (let [invoked (apply component args)]
                    ;;(println "  ---> Rendering required <---")
                    (recur (cons invoked (cons [::return component] (rest elements))) ; we will return after handling the subcomponents
                           (register-ratoms-map ratoms-map new-ratoms component) ; we register the new dependencies
                           rendering
                           (conj rendered component) ; the component was asked to be rendered
                           (conj seen component)     ; we saw it
                           ;; we update the controller with the new argument values
                           (assoc-in ctrl [:components component]
                                     (assoc comp-infos
                                            :previous-available true
                                            :previous-args (vec args)
                                            :previous-ratom-args ratom-args
                                            :previous-render invoked))))))
            ;; the component is unknown, there is no previous information available
            (let [;; ratoms (filter reactive-atom? args) ; XXX old code : already done above !
                  ;; new-ratoms (filter #(not (contains? ratoms-map %)) ratoms) ; XXX: same
                  ;; We add the component as depending on all the newly discovered ratoms
                  _ (doseq [new-ratom new-ratoms]
                      (ratom/depend-component! new-ratom component))
                  ;; we then perform the call explicitly
                  invoked (apply component args)
                  ;; ratom-args (mapv deref ratoms)
                  ]
              ;;(println "  ---> Registering unknown component <---")
              ;; this is exactly the same continuation as the previous case (re-invokation)
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
        ;; otherwise it is an item that must be rendered anyway (except that a diff will be computed)
        :else
        (recur (rest elements)
               ratoms-map
               (conj rendering (first elements))
               rendered
               seen
               ctrl))
      ;; no more elements to render
      [rendering rendered seen ctrl])))


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
      ;;(println "=== BEGIN RENDER ===")
      (ysc/display-scene! controller rendering)
      ;;(println "=== END RENDER ===")
      [rendered seen])))


