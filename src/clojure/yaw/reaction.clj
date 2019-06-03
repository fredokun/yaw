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
        keyboard-state (atom {:keysdown #{}})]
    (.registerInputCallback
     world
     (reify yaw.engine.InputCallback
       (sendKey [this key scancode action mode]
         (let [action (kbd/action action)
               key (kbd/key key)]
           (if (or (= action :press) (= action :release))
             (swap! keyboard-state
                    (fn [old-state]
                      (case action
                        :press (assoc old-state
                                      :key key
                                      :action action
                                      :keysdown (conj (:keysdown old-state) key))
                        :release (assoc old-state
                                        :key key
                                        :action action
                                        :keysdown (disj (:keysdown old-state) key))))))))))
    keyboard-state))


;;==============
;; Reframe-like
;;==============

(defonce app-db (atom {}))
(defonce subscriptions (atom {}))
(defonce event-handlers (atom {}))
(def event-queue (agent []))

(defn register-state
  "Register a state `id` in ap-db with the value `val`"
  [id val]
  (swap! app-db (fn [old]
                  (assoc old id (atom val)))))

(defn register-subscription
  "Register a subcription `id` in subscriptions with a function
  `f` returning a state of ap-db"
  [id f]
  (swap! subscriptions (fn [old]
                         (assoc old id f))))

(defn register-event
  "Register an event `id` in event-handlers with its handler `f`"
  [id f]
  (swap! event-handlers (fn [old]
                          (assoc old id f))))

;; init-state isn't really use, we should use use update-state instead
(defn init-state [id val]
  (swap! (id @app-db) (fn [_] val)))

(defn update-state
  "Change the value of the  state `id` by using a function `f`
  that takes the former value and calculate a new one"
  [id f]
  (swap! (id @app-db) f))

(defn read-state
  "Read app-db or read a state `id` in app-db"
  ([] @app-db)
  ([id] @(get @app-db id)))

;;v is a vector, maybe we can find a value to pass args
;;or we need to remove the vector and just pass the id
(defn subscribe
  "Subscribe to a state in app-db by giving the id of the subscription
  and return a ratom linked with the state"
  [controller v]
  (let [id (first v)
        fun (get @subscriptions id)
        state (fun @app-db)
        ratom (reactive-atom controller @state)]
    (do
      ;; Use Ratom address as unique id for the watch
      ;; maybe find a cleaner way to do so
      (add-watch state (keyword (str ratom)) (fn [_ _ _ new]
                                               (swap! ratom (fn [_] new))))
      ratom)))

(defn handle-event
  "Function to treat events of the agent"
  [queue]
  (if (pos-int? (count queue))
    (let [[id args] (first queue)
          fun (get @event-handlers id)]
      (if-not (nil? fun)
        (apply fun args))
      (rest queue))
    queue))

;; TODO (later): don't use an agent but rather an atom with an
;; immutable queue (with limited size)...
(defn dispatch
  "Send an event to the agent in order to be treated asynchronously"
  [[id & args]]
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

(defn activate!
  "Function to start and render a scene"
  [controller vscene]
  (dispatch-sync [:react/initialize])
  (render/render! controller vscene)
  (let [update (create-update-ratom controller)
        keyboard (create-keyboard-atom controller)]
    (add-watch update :update-yaw (fn [_ _ _ delta-time]
                                    ;;Maybe just dispatch instead of dispatch-sync ?
                                    (dispatch-sync [:react/frame-update delta-time])))
    (add-watch keyboard :keyboard-yaw (fn [_ _ _ keyboard-state]
                                        ;;Maybe just dispatch instead of dispatch-sync ?
                                        (dispatch-sync [:react/key-update keyboard-state])))))

