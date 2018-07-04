(ns yaw.reaction
  (:require [clojure.set :as set]
            [yaw.ratom :as ratom]
            [yaw.render :as render :refer [render!]]))

;;{
;; ## Reactive atoms
;;
;; Once again inspired by reagent, we introduce a notion of *reactive atom* which
;; is a specialiized form of a Clojure atom.
;;}

(defn reaction-handler [ratom controller dep-comps old-value new-value]
  (println "Reaction triggerred")
  (println "old value:" old-value)
  (println "new value:" new-value)
  (if (= new-value old-value)
    (println "No need for re-rendering (same state after a swap)")
    ;; rendering starts now
    (let [components (:components @controller)]
      (println "Rendering triggered by reaction")
      (loop [comps dep-comps, already-rendered #{}]
        (when (seq comps)
          ;; (if (already-rendered (first comps))
          ;; already recomputed (XXX: probably buggy optimization)
          ;;(recur (rest comps) already-rendered)
          ;; not yet recomputed
          (let [component (first comps)
                comp-infos (get components component)]
            (when (not (:previous-available comp-infos))
              (throw (ex-info "Component has not been rendered previously" {:component component
                                                                            :comp-infos comp-infos})))
            (let [[rendered seen] (render! controller (into [component] (:previous-args comp-infos)))]
              (recur (rest comps) (set/union already-rendered rendered)
                                        ;)
                     ))))))))


(defn reactive-atom [controller init-val]
  (let [rat (ratom/make-ratom controller reaction-handler init-val)]
    (add-watch rat ::reaction reaction-handler)
    rat))



(defn create-update-ratom [universe]
  (let [world (:world @universe)
        ratom (reactive-atom universe 0.0)]
    (.registerUpdateCallback world (reify yaw.engine.UpdateCallback
                                     (update [this delta-time]
                                       (println "Update! " delta-time " ms")
                                       (swap! ratom (fn [_] delta-time)))))
    ratom))
