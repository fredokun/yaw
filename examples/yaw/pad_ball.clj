(ns yaw.pad-ball
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

(react/register-state ::ball-state {:pos [-2 0 -5]
                                    :delta [0.02 0 0]})

(def init-pad1-state
  {:pos [3 0 -5]
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
   (react/init-state ::ball-state init-ball-state)))

(react/register-event
 :react/frame-update
 (fn []
   (react/dispatch [::move-ball])))

(declare update-pad1-state)
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
      (react/dispatch [::move-down-pad1]))))


;;; =====================
;;; The view part
;;; =====================

(defn the-pad
  "Create a pad with its position linked to the `pos` reactive atom."
  [id state]
  [:group id {:pos (:pos @state)
                      :rot [0 0 0]
                      :scale 1}
   [:item :test/pad {:mesh :mesh/cuboid
                     :pos [0 0 0]
                     :rot [0 0 0]
                     :mat :red
                     :scale 0.3}]
   [:hitbox :test/pad-hitbox {:pos [0 0 0]
                              :scale 0.6
                              :length [1 3 1]}]])

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
    [{:group-id :test/pad1 :hitbox-id :test/pad-hitbox :collision-handler #(react/dispatch [::ball-collision])}]]])


(defn scene []
  [:scene
   [:ambient {:color :white :i 0.4}]
   [:sun {:color :red :i 1 :dir [-1 0 0]}]
   [:light ::light {:color :yellow :pos [0.5 0 -4]}]
   (let [pad-state1 (react/subscribe +myctrl+ [::pad1-changed])]
    [the-pad :test/pad1 pad-state1])
   (let [ball-state (react/subscribe +myctrl+ [::ball-changed])]
     [the-ball ball-state])
    ])

;;; =====================
;;; The main part
;;; =====================

(react/activate! +myctrl+ [scene])
 ;; (react/dispatch :react/initialize)
