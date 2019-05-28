(ns yaw.pad-ball-4
  (:require 
   [clojure.set :as set]
   [yaw.world :as world]
   [yaw.reaction :as react]
   [yaw.render :as render]
   [yaw.keyboard :as kbd]))

(def +myctrl+ (world/start-universe!))

(declare update-ball-state)

;;; =====================
;;; The state part
;;; =====================

(def init-pad1-state
  {:pos [3 0 -5]
   :delta [0 0.04 0]})

(def init-pad2-state
  {:pos [-3 0 -5]
   :delta [0 0.04 0]})

(def init-ball-state
  {:pos [-2 0 -5]
   :delta [0.04 0 0]})

(react/register-state ::pad1-state {:pos [3 0 -5]
                                    :delta [0 0.05 0]})

(react/register-state ::pad2-state {:pos [-3 0 -5]
                                    :delta [0 0.05 0]})

(react/register-state ::ball-state {:pos [-2 0 -5]
                                    :delta [0.02 0 0]})

(react/register-state ::pad1-action :nil)
(react/register-state ::pad2-action :nil)

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
 (fn [_]
   (let [pad1-action (react/read-state ::pad1-action)
         pad2-action (react/read-state ::pad2-action)]
     (do
       (react/dispatch [::move-ball])
       (cond
         (= pad1-action :up) (react/dispatch [::move-up-pad1])
         (= pad1-action :down) (react/dispatch [::move-down-pad1]))
       (cond
         (= pad2-action :up) (react/dispatch [::move-up-pad2])
         (= pad2-action :down) (react/dispatch [::move-down-pad2]))))))


;;{
;; How to handle the keyboard input:
;; * The pad at the right side move with the up and down arrow
;; * The pad at the left side move with E and D
;;
;; The inputs only change the atoms "pad2-input" and "pad1-input"
;; who can contain :up, :down or :nil
;; When pressing the key to move up (resp. move down), the atom becomes :up (resp. down)
;; until the key is released or when the key to move down (resp. move up) is pressed.
;; If it's released, the atom become :nil and if the key to move down (resp. move up) is pressed, it becomes :down (resp :up).
;; If the atom contains :up (resp. :down) and the key to move down (resp. move up) is released, the atom doesn't change.
;;
;; The event frame-update move the pads following the values of the atoms
;;}
(react/register-event
 :react/key-update
 (fn [kbd-state]
   (cond
     (and (:up kbd-state) (:down kbd-state))
     (react/update-state ::pad1-action (fn [_] :nil))
     ;; up-arrow
     (:up kbd-state) (react/update-state ::pad1-action (fn [_] :up))
     ;; down-arrow
     (:down kbd-state) (react/update-state ::pad1-action (fn [_] :down))
     :else (react/update-state ::pad1-action (fn [_] :nil)))
   (cond
     (and (:e kbd-state) (:d kbd-state))
     (react/update-state ::pad2-action (fn [_] :nil))
     ;; key E
     (:e kbd-state) (react/update-state ::pad2-action (fn [_] :up))
     ;; key D
     (:d kbd-state) (react/update-state ::pad2-action (fn [_] :down))
     :else (react/update-state ::pad2-action (fn [_] :nil)))))

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

;;=======================
;;Hitbox collision events
;;=======================

;;Event called when the ball collide with the upper part of Pad 1
(react/register-event
 ::ball-collision-top1
 (fn []
   (react/update-state ::ball-state
                       (fn [{pos :pos
                             delta :delta}]
                         (let [delta' (assoc delta 0 (- (Math/abs (get delta 0))))
                               delta' (assoc delta' 1 0.01)]
                           {:pos pos :delta delta'})))))

;;Event called when the ball collide with the middle part of Pad 1
(react/register-event
 ::ball-collision-middle1
 (fn []
   (react/update-state ::ball-state
                       (fn [{pos :pos
                             delta :delta}]
                         {:pos pos :delta (assoc delta 0 (- (Math/abs (get delta 0))))}))))

;;Event called when the ball collide with the lower part of Pad 1
(react/register-event
 ::ball-collision-bottom1
 (fn []
   (react/update-state ::ball-state
                       (fn [{pos :pos
                             delta :delta}]
                         (let [delta' (assoc delta 0 (- (Math/abs (get delta 0))))
                               delta' (assoc delta' 1 -0.01)]
                           {:pos pos :delta delta'})))))

;;Event called when the ball collide with the upper part of Pad 2
(react/register-event
 ::ball-collision-top2
 (fn []
   (react/update-state ::ball-state
                       (fn [{pos :pos
                             delta :delta}]
                         (let [delta' (assoc delta 0 (Math/abs (get delta 0)))
                               delta' (assoc delta' 1 0.01)]
                           {:pos pos :delta delta'})))))

;;Event called when the ball collide with the middle part of Pad 2
(react/register-event
 ::ball-collision-middle2
 (fn []
   (react/update-state ::ball-state
                       (fn [{pos :pos
                             delta :delta}]
                         {:pos pos :delta (assoc delta 0 (Math/abs (get delta 0)))}))))

;;Event called when the ball collide with the bottom part of Pad 2
(react/register-event
 ::ball-collision-bottom2
 (fn []
   (react/update-state ::ball-state
                       (fn [{pos :pos
                             delta :delta}]
                         (let [delta' (assoc delta 0 (Math/abs (get delta 0)))
                               delta' (assoc delta' 1 -0.01)]
                           {:pos pos :delta delta'})))))

;;Event called when the ball collide with the upper wall
(react/register-event
 ::ball-collision-wall1
 (fn []
   (react/update-state ::ball-state
                       (fn [{pos :pos
                             delta :delta}]
                         (let [delta' (assoc delta 1 (- (Math/abs (get delta 1))))]
                           {:pos pos :delta delta'})))))

;;Event called when the ball collide with the bottom wall
(react/register-event
 ::ball-collision-wall2
 (fn []
   (react/update-state ::ball-state
                       (fn [{pos :pos
                             delta :delta}]
                         (let [delta' (assoc delta 1 (Math/abs (get delta 1)))]
                           {:pos pos :delta delta'})))))

;;; =====================
;;; The view part
;;; =====================

(defn mk-pad-kw [prefix id]
  (keyword "test" (str "pad-" prefix "-" id)))

;; (mk-pad-kw "group" 1)
;; => :test/pad-group-1

(defn pad-keywords [id]
  [(mk-pad-kw "group" id)
   (mk-pad-kw "item" id)
   (mk-pad-kw "hitbox-top" id)
   (mk-pad-kw "hitbox-middle" id)
   (mk-pad-kw "hitbox-bottom" id)])

(defn the-pad [id]
  (let [[group item htop hmid hbot] (pad-keywords id)]
    (fn [state]
      [:group group
       {:pos (:pos @state)
        :rot [0 0 0]
        :scale 1}
       [:item item
        {:mesh :mesh/cuboid
         :pos [0 0 0]
         :rot [0 0 0]
         :mat :red
         :scale 0.3}]
       [:hitbox htop
        {:pos [0 0.6 0]
         :scale 0.6
         :length [1 1 1]}]
       [:hitbox hmid
        {:pos [0 0 0]
         :scale 0.6
         :length [1 1 1]}]
       [:hitbox hbot
        {:pos [0 -0.6 0]
         :scale 0.6
         :length [1 1 1]}]])))

(def the-pad1 (the-pad 1))
(def the-pad2 (the-pad 2))

(defn the-ball
  [state]
  [:group :test/ball {:pos (:pos @state)
                      :rot [0 0 0]
                      :scale 1}
   [:item :test/box {:mesh :mesh/box
                     :pos [0 0 0]
                     :rot [0 0 0]
                     :mat :yellow
                     :scale 0.2}]
   [:hitbox :test/ball-hitbox {:pos [0 0 0]
                               :scale 0.4
                               :length [1 1 1]}
    ;; :collisions [[:pad-group1 :hitbox-top  #(react/dispatch [::ball-collision-top1])]
    ;;              [:test/pad-group1 :test/pad-hitbox-middle-1 #(react/dispatch [::ball-collision-middle1])]
    ;;              ... etc ...]  

    [:test/pad-group-1 :test/pad-hitbox-top-1 #(react/dispatch [::ball-collision-top1])]
     [:test/pad-group-1 :test/pad-hitbox-middle-1 #(react/dispatch [::ball-collision-middle1])]
     [:test/pad-group-1 :test/pad-hitbox-bottom-1 #(react/dispatch [::ball-collision-bottom1])]
     [:test/pad-group-2 :test/pad-hitbox-top-2 #(react/dispatch [::ball-collision-top2])]
     [:test/pad-group-2 :test/pad-hitbox-middle-2 #(react/dispatch [::ball-collision-middle2])]
     [:test/pad-group-2 :test/pad-hitbox-bottom-2 #(react/dispatch [::ball-collision-bottom2])]
     [:test/wall-group-1 :test/wall-hitbox-1 #(react/dispatch [::ball-collision-wall1])]
     [:test/wall-group-2 :test/wall-hitbox-2 #(react/dispatch [::ball-collision-wall2])]
     ]])

(defn the-wall
  [state id]
  [:group (keyword "test" (str "wall-group-" id)) {:pos (:pos state)
                                                   :rot [0 0 0]
                                                   :scale 1}
   [:item (keyword "test" (str "wall-" id)) {:mesh :mesh/cuboid
                                             :pos [0 0 0]
                                             :rot (:rot state)
                                             :mat :white
                                             :scale (:scale state)}]
   [:hitbox (keyword "test" (str "wall-hitbox-" id)) {:pos [0 0 0]
                                                      :rot (:rot state)
                                                      :scale (* 2 (:scale state))
                                                      :length [0.99 2.99 0.99]}]
   ])

(defn scene []
  [:scene
   [:ambient {:color :white :i 0.4}]
   [:sun {:color :red :i 1 :dir [-1 0 0]}]
   [:light ::light {:color :yellow :pos [0.5 0 -4]}]
   [the-wall {:pos [0 4.5 -5] :rot [0 0 90] :scale 2} 1]
   [the-wall {:pos [0 -4.5 -5] :rot [0 0 90] :scale 2} 2]
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
