(ns yaw.pad-ball-2
  "A simple 3D example using a reagent-like approach."
  (:require 
   [clojure.set :as set]
   [yaw.world :as world]
   [yaw.reaction :as react]
   [yaw.render :as render]))

(def +myctrl+ (world/start-universe!))

;;; =====================
;;; The state part
;;; =====================

(react/register-state ::pad1-state {:pos [3 0 -5]
                                    :delta [0 0.05 0]})

(react/register-state ::pad2-state {:pos [-3 0 -5]
                                    :delta [0 0.05 0]})

(react/register-state ::ball-state {:pos [-2 0 -5]
                                    :delta [0.02 0 0]})

(def init-pad1-state
  {:pos [3 0 -5]
   :delta [0 0.05 0]})

(def init-pad2-state
  {:pos [-3 0 -5]
   :delta [0 0.05 0]})

(def init-ball-state
  {:pos [-2 0 -5]
   :delta [0.02 0 0]})

;;; =====================
;;; Subscription(s)
;;; =====================

(react/register-subscription
 ::pad1-changed
 (fn [db]
   (::pad1-state  db)))

(react/register-subscription
 ::pad2-changed
 (fn [db]
   (::pad2-state  db)))

(react/register-subscription
 ::ball-changed
 (fn [db]
   (::ball-state  db)))

;;; ====================
;;; Event handlers
;;; ====================

(react/register-event
 :react/initialize
 (fn []
   (react/init-state ::pad1-state init-pad1-state)
   (react/init-state ::pad2-state init-pad2-state)
   (react/init-state ::ball-state init-ball-state)))

(react/register-event
 :react/frame-update
 (fn []
   (react/dispatch [::move-ball])))

(declare update-ball-state)

(react/register-event
 ::move-up-pad1
 (fn []
   (react/update-state ::pad1-state 
                       (fn [{pos :pos
                             delta :delta}]
                         {:pos (mapv + pos delta)
                          :delta delta}))))
(react/register-event
 ::move-down-pad1
 (fn []
   (react/update-state ::pad1-state 
                       (fn [{pos :pos
                             delta :delta}]
                         {:pos (mapv - pos delta)
                          :delta delta}))))


(react/register-event
 ::move-up-pad2
 (fn []
   (react/update-state ::pad2-state 
                       (fn [{pos :pos
                             delta :delta}]
                         {:pos (mapv + pos delta)
                          :delta delta}))))
(react/register-event
 ::move-down-pad2
 (fn []
   (react/update-state ::pad2-state 
                       (fn [{pos :pos
                             delta :delta}]
                         {:pos (mapv - pos delta)
                          :delta delta}))))


(react/register-event
 ::move-ball
 (fn []
   (react/update-state ::ball-state
                       (fn [{pos :pos
                             delta :delta}]
                         {:pos (mapv + pos delta) :delta delta}))))

(react/register-event
 ::ball-collision
 (fn []
   (react/update-state ::ball-state
                       (fn [{pos :pos
                             delta :delta}]
                         {:pos pos :delta (mapv - delta)}))))

(world/register-input-callback! (:world @+myctrl+)
 (fn [key _ action _]
   (cond
    (and (= key 265) ; HACK: GLFW constant for up arrow!
            (or (= action 2) (= action 1))) ; HACK: GLFW_PRESS or REPEATED
     (react/dispatch [::move-up-pad1])
    (and (= key 264) ; HACK: GLFW constant for down arrow!
            (or (= action 2) (= action 1))) ; HACK: GLFW_PRESS or REPEATED
      (react/dispatch [::move-down-pad1])
    (and (= key 87) ; HACK: GLFW constant for z!
            (or (= action 2) (= action 1))) ; HACK: GLFW_PRESS or REPEATED
     (react/dispatch [::move-up-pad2])
    (and (= key 83) ; HACK: GLFW constant for s!
            (or (= action 2) (= action 1))) ; HACK: GLFW_PRESS or REPEATED
      (react/dispatch [::move-down-pad2]))))


;;; =====================
;;; The view part
;;; =====================

;; TODO Why can't we use this function twice to define both pads ?
; (defn the-pad
;   [id state]
;   [:group (keyword "test" (str "pad-group-" id)) 
;           {:pos (:pos @state)
;             :rot [0 0 0]
;             :scale 1}
;    [:item (keyword "test" (str "pad-" id))
;           {:mesh :mesh/cuboid
;             :pos [0 0 0]
;             :rot [0 0 0]
;             :mat :red
;             :scale 0.3}]
;    [:hitbox (keyword "test" (str "hitbox-" id))
;             {:pos [0 0 0]
;               :scale 0.6
;               :length [1 3 1]}]])
              
(defn the-pad1
  [state]
  [:group :test/pad-group-1
          {:pos (:pos @state)
            :rot [0 0 0]
            :scale 1}
   [:item :test/pad-item-1
          {:mesh :mesh/cuboid
            :pos [0 0 0]
            :rot [0 0 0]
            :mat :red
            :scale 0.3}]
   [:hitbox :test/pad-hitbox-1
            {:pos [0 0 0]
              :scale 0.6
              :length [1.05 3.01 1.01]}]])
              
(defn the-pad2
  [state]
  [:group :test/pad-group-2
          {:pos (:pos @state)
            :rot [0 0 0]
            :scale 1}
   [:item :test/pad-item-2
          {:mesh :mesh/cuboid
            :pos [0 0 0]
            :rot [0 0 0]
            :mat :red
            :scale 0.3}]
   [:hitbox :test/pad-hitbox-2
            {:pos [0 0 0]
              :scale 0.6
              :length [1.05 3.01 1.01]}]])

(defn the-ball
  [state]
  [:group :test/ball {:pos (:pos @state)
                      :rot [0 0 0]
                      :scale 1}
   [:item :test/box {:mesh :mesh/box
                     :pos [0 0 0]
                     :rot [0 0 0]
                     :mat :white
                     :scale 0.3}]
   [:hitbox :test/ball-hitbox {:pos [0 0 0]
                              :scale 0.6
                              :length [1 1 1]}
    [{:group-id :test/pad-group-1 :hitbox-id :test/pad-hitbox-1 :collision-handler #(react/dispatch [::ball-collision])}
     {:group-id :test/pad-group-2 :hitbox-id :test/pad-hitbox-2 :collision-handler #(react/dispatch [::ball-collision])}]]])


(defn scene []
  [:scene
   [:ambient {:color :white :i 0.4}]
   [:sun {:color :red :i 1 :dir [-1 0 0]}]
   [:light ::light {:color :yellow :pos [0.5 0 -4]}]
   (let [pad-state1 (react/subscribe +myctrl+ [::pad1-changed])]
    [the-pad1 pad-state1])
   (let [pad-state2 (react/subscribe +myctrl+ [::pad2-changed])]
    [the-pad2 pad-state2])
   (let [ball-state (react/subscribe +myctrl+ [::ball-changed])]
     [the-ball ball-state])
    ])

;;; =====================
;;; The main part
;;; =====================

(react/activate! +myctrl+ [scene])
 ;; (react/dispatch :react/initialize)
