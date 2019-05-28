(ns yaw.reaction
  (:require [clojure.set :as set]
            [yaw.ratom :as ratom]
            [yaw.render :as render :refer [render!]]
            [yaw.keyboard :as kbd]))

;;{
;; # Reactive atoms
;;
;; Once again inspired by reagent, we introduce a notion of *reactive atom* which
;; is a specialiized form of a Clojure atom.
;;}

(defn reaction-handler [ratom controller dep-comps old-value new-value]
  ;;(println "Reaction triggerred")
  ;;(println "old value:" old-value)
  ;;(println "new value:" new-value)
  (if (= new-value old-value)
    (do ;;(println "No need for re-rendering (same state after a swap)")
      )
    ;; rendering starts now
    (let [components (:components @controller)]
      ;;(println "Rendering triggered by reaction")
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
        ratom (atom 0.0)]
    (.registerUpdateCallback world (reify yaw.engine.UpdateCallback
                                     (update [this delta-time]
                                       ;; (println "Update! " delta-time " ms")
                                       (swap! ratom (fn [_] delta-time)))))
    ratom))

(defn create-keyboard-atom [universe]
  (let [world (:world @universe)
        keyboard-state (atom #{})]
    (.registerInputCallback
     world
     (reify yaw.engine.InputCallback
       (sendKey [this key scancode action mode]
         (cond
           (and (= action (kbd/actioncode :pressed))
                (not (contains? @keyboard-state (kbd/key key)))) (swap! keyboard-state conj (kbd/key key))
           (and (= action (kbd/actioncode :released))
                (contains? @keyboard-state (kbd/key key))) (swap! keyboard-state disj (kbd/key key))))))
    keyboard-state))


;;==============
;; Reframe-like
;;==============

(defonce app-db (atom {}))
(defonce subscriptions (atom {}))
(defonce event-handlers (atom {}))
(def event-queue (agent []))


(defn register-state [id val]
  (swap! app-db (fn [old]
                  (assoc old id (atom val)))))

(defn register-subscription [id f]
  (swap! subscriptions (fn [old]
                         (assoc old id f))))

(defn register-event [id f]
  (swap! event-handlers (fn [old]
                          (assoc old id f))))

(defn init-state  [id val]
  (swap! (id @app-db) (fn [_] val)))

(defn update-state [id val]
  (swap! (id @app-db) val))

(defn read-state 
  ([] @app-db)
  ([id] @(get @app-db id)))

(defn subscribe [controller v]
  (let [id (first v)
        fun (get @subscriptions id)
        state (fun @app-db)
        ratom (reactive-atom controller @state)]
    (do
      ;; TODO Find a way to use a unique key
      (add-watch state :k (fn [_ _ _ new]
                            (swap! ratom (fn [_] new))))
      ratom)))

(defn handle-event [queue]
  (if (pos-int? (count queue))
    (let [[id args] (first queue)
          fun (get @event-handlers id)]
      (if-not (nil? fun)
        (apply fun args))
      (send event-queue rest)
      queue)
    []))

;; TODO: handle args, remove the wrapping inside a vector
;; TODO (later): don't use an agent but rather an atom with an
;; immutable queue (with limited size)...
(defn dispatch [[id & args]]
  (when (keyword? id)
    (send event-queue conj [id args])
    (send event-queue handle-event)))


(defn dispatch-sync
  "Treatement the event synchronously instead of putting it in the file"
  [[id & args]]
  (if (keyword? id)
    (let [fun (get @event-handlers id)]
      (if-not (nil? fun)
        (apply fun args)))))

;;{
;; TODO : comments in markdown
;;}

(defn activate! [controller vscene]
  (dispatch-sync [:react/initialize])
  (render/render! controller vscene)
  (let [update (create-update-ratom controller)
        keyboard (create-keyboard-atom controller)]
    (add-watch update :update-yaw (fn [_ _ _ delta-time]
                                    (dispatch-sync [:react/frame-update delta-time])))
    (add-watch keyboard :keyboard-yaw (fn [_ _ _ keyboard-state]
                                        (dispatch-sync [:react/key-update keyboard-state])))))

