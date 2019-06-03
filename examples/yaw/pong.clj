(ns yaw.pong
  (:require 
   [clojure.set :as set]
   [yaw.world :as world]
   [yaw.reaction :as react]
   [yaw.render :as render]
   [yaw.keyboard :as kbd]))

(def +myctrl+ (world/start-universe!))
(def S 0.005)
(def A 0.01)
(def MAX-X 0.15)
(def MAX-Y 0.04)
(def nb-ticks 200)

(declare update-ball-state)
(declare move-pad)
(declare modif-ball)

;;; =====================
;;; The states part
;;; =====================

(def init-pad1-state
  {:pos [3 0 -5]
   :delta [0 0.08 0]})

(def init-pad2-state
  {:pos [-3 0 -5]
   :delta [0 0.08 0]})

(def init-ball-state
  {:pos [0 0 -5]
   :delta [0.04 0 0]})

;; state of the left pad
(react/register-state ::pad1-state init-pad1-state)

;; state containing the movement of the left pad
(react/register-state ::pad1-action :nil)

;; state of the right pad
(react/register-state ::pad2-state init-pad2-state)

;; state containing the movement of the right pad
(react/register-state ::pad2-action :nil)

;; state of the ball
(react/register-state ::ball-state init-ball-state)

;; a counter in order to know when we need to increase the speed of the ball
(react/register-state ::counter 0)

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

;;; ==============
;;; Event handlers
;;; ==============

;;{
;; The :react/frame-update event does 4 actions:
;; * It dispatches an event to move the ball
;; * It dispatches an event to move the right pad
;; * It dispatches an event to move the left pad
;; * It increments ::counter
;; * If the state ::counter is modulo nb-tick, it increases the speed of the ball
;;}
(react/register-event
 :react/frame-update
 (fn [_]
   (let [pad1-action (react/read-state ::pad1-action)
         pad2-action (react/read-state ::pad2-action)
         counter (react/read-state ::counter)]
     (do
       (react/update-state ::counter inc)
       (if (zero? (mod (react/read-state ::counter) nb-ticks))
         (react/update-state ::ball-state (fn [old]
                                            (let [x (get-in old [:delta 0])]
                                              (if (neg? x)
                                                (assoc-in old [:delta 0] (Math/max (- MAX-X) (- x S)))
                                                (assoc-in old [:delta 0] (Math/min MAX-X (+ x S))))))))
       (react/dispatch [::move-ball])
       (if-not (= pad1-action :nil)
         (react/dispatch [::move-pad1 pad1-action]))
       (if-not (= pad2-action :nil)
         (react/dispatch [::move-pad2 pad2-action]))))))

;;{
;; How to handle the keyboard input:
;; * The pad at the right side move with the up and down arrow
;; * The pad at the left side move with E and D
;;
;; The inputs only change the state "::pad1-action" and "::pad1-action"
;; who can contain :up, :down or :nil
;; When the key to move up and the key to move down are pressed simultanously,
;; the state becomes :nil
;; When pressing the key to move up (resp. move down), the state becomes :up (resp. down)
;; until the key is released
;; The event frame-update move the pads following the states
;;}
(react/register-event
 :react/key-update
 (fn [kbd-state]
   ;; Pad 1 action (right side)
   (cond
     ;; if we want to move up and down at the same time
     (and (:up (:keysdown kbd-state)) (:down (:keysdown kbd-state))) (react/update-state ::pad1-action (fn [_] :nil))
     ;; up-arrow
     (:up (:keysdown kbd-state)) (react/update-state ::pad1-action (fn [_] :up))
     ;; down-arrow
     (:down (:keysdown kbd-state)) (react/update-state ::pad1-action (fn [_] :down))
     ;; no moves
     :else (react/update-state ::pad1-action (fn [_] :nil)))
   ;;Pad 2 action (left side)
   (cond
     ;; if we want to move up and down at the same time
     (and (:e (:keysdown kbd-state)) (:d (:keysdown kbd-state))) (react/update-state ::pad2-action (fn [_] :nil))
     ;; E key
     (:e (:keysdown kbd-state)) (react/update-state ::pad2-action (fn [_] :up))
     ;; D key
     (:d (:keysdown kbd-state)) (react/update-state ::pad2-action (fn [_] :down))
     ;; no moves
     :else (react/update-state ::pad2-action (fn [_] :nil)))))

;;{
;; Event to move the left pad, the handler takes a direction in order
;; to know where to move it
;;}
(react/register-event
 ::move-pad1
 (fn [direction]
   (react/update-state ::pad1-state #(move-pad direction %))))

;;{
;; Event to move the right pad, the handler takes a direction in order
;; to know where to move it
;;}
(react/register-event
 ::move-pad2
 (fn [direction]
   (react/update-state ::pad2-state #(move-pad direction %))))

;;{
;; Event to move the ball
;;}
(react/register-event
 ::move-ball
 (fn []
   (react/update-state ::ball-state
                       (fn [{pos :pos
                             delta :delta}]
                         {:pos (mapv + pos delta) :delta delta}))))

;;{
;; Event called when the ball collides with something
;; The handler takes 3 arguments:
;; * `obj` : what kind of object the ball collided
;; * `side` : what side is the object
;; * `part` : what part of the object (if it's precised)
;;}
(react/register-event
 ::ball-collision
 (fn [obj side part]
   (react/update-state ::ball-state #(modif-ball obj side part %))))

;;{
;; Event called when the ball need to be reinitialize after one side scored
;;}
(react/register-event
 ::score
 (fn [scorer]
   (react/update-state ::counter (fn [_] 0))
   (react/update-state ::ball-state (fn [_]
                                      (case scorer
                                        :left-pad init-ball-state
                                        :right-pad (assoc-in init-ball-state
                                                             [:delta 0]
                                                             (- (get-in init-ball-state [:delta 0]))))))))

;;===========
;; Functions
;;===========

(defn move-pad [direction {pos :pos delta :delta}]
  (let [op (case direction
             :up +
             :down -)]
    {:pos (mapv op pos delta)
     :delta delta}))

(defn modif-ball [obj side part {pos :pos delta :delta}]
  (case obj
    :wall (let [y (case side
                    :top (- (Math/abs (get delta 1)))
                    :bottom (Math/abs (get delta 1)))]
            {:pos pos :delta (assoc delta 1 y)})
    :pad (let [x (case side
                   :right (- (Math/abs (get delta 0)))
                   :left (Math/abs (get delta 0)))
               y (case part
                   :top (Math/min MAX-Y (+ (get delta 1) A))
                   :middle (get delta 1)
                   :bottom (Math/max (- MAX-Y) (- (get delta 1) A)))
               z (get delta 2)]
           {:pos pos :delta [x y z]})))

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
    [:test/pad-group-1 :test/pad-hitbox-top-1 #(react/dispatch [::ball-collision :pad :right :top])]
    [:test/pad-group-1 :test/pad-hitbox-middle-1 #(react/dispatch [::ball-collision :pad :right :middle])]
    [:test/pad-group-1 :test/pad-hitbox-bottom-1 #(react/dispatch [::ball-collision :pad :right :bottom])]
    [:test/pad-group-2 :test/pad-hitbox-top-2 #(react/dispatch [::ball-collision :pad :left :top])]
    [:test/pad-group-2 :test/pad-hitbox-middle-2 #(react/dispatch [::ball-collision :pad :left :middle])]
    [:test/pad-group-2 :test/pad-hitbox-bottom-2 #(react/dispatch [::ball-collision :pad :left :bottom])]
    [:test/wall-group-1 :test/wall-hitbox-1 #(react/dispatch [::ball-collision :wall :top nil])]
    [:test/wall-group-2 :test/wall-hitbox-2 #(react/dispatch [::ball-collision :wall :bottom nil])]
    [:test/wall-group-3 :test/wall-hitbox-3 #(react/dispatch [::score :left-pad])]
    [:test/wall-group-4 :test/wall-hitbox-4 #(react/dispatch [::score :right-pad])]]])

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
                                                      :length [1 3 1]}]
   ])

(defn scene []
  [:scene
   [:ambient {:color :white :i 0.7}]
   [:sun {:color :red :i 1 :dir [-1 0 0]}]
   [:light ::light {:color :yellow :pos [0.5 0 -4]}]
   [the-wall {:pos [0 4.5 -5] :rot [0 0 90] :scale 2} 1]
   [the-wall {:pos [0 -4.5 -5] :rot [0 0 90] :scale 2} 2]
   [the-wall {:pos [8 0 -5] :rot [0 0 0] :scale 2} 3]
   [the-wall {:pos [-8 0 -5] :rot [0 0 0] :scale 2} 4]
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
