(ns yaw.pad-ball-4
  (:require 
   [clojure.set :as set]
   [yaw.world :as world]
   [yaw.reaction :as react]
   [yaw.render :as render]
   [yaw.keyboard :as kbd]))

(def +myctrl+ (world/start-universe!))

(declare pad2-input)
(declare pad1-input)
(declare update-ball-state)

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
   :delta [0 0.04 0]})

(def init-pad2-state
  {:pos [-3 0 -5]
   :delta [0 0.04 0]})

(def init-ball-state
  {:pos [-2 0 -5]
   :delta [0.04 0 0]})

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
   (do
     (react/dispatch [::move-ball])
     (cond
       (= @pad1-input :up) (react/dispatch [::move-up-pad1])
       (= @pad1-input :down) (react/dispatch [::move-down-pad1]))
     (cond
       (= @pad2-input :up) (react/dispatch [::move-up-pad2])
       (= @pad2-input :down) (react/dispatch [::move-down-pad2])))))

(comment
  (react/register-event
   :react/frame-update
   (fn [_] ;; le delta est passé en argument
     (let [pad1 (react/read-state ::pad1-state)
           pad2 (react/read-state ::pad2-state)]
       (do
         (react/dispatch [::move-ball])
         (cond
           (:move-down pad1) (react/dispatch-sync [::move-down-pad1])
           (:move-up pad1) (react/dispatch-sync [::move-down-pad1]))
         (cond
           (:move-down pad2) (react/dispatch-sync [::move-down-pad2])
           (:move-up pad2) (react/dispatch-sync [::move-down-pad2]))))))

  (react/register-event 
   :kbd/key-update
   (fn [kdb-state]
     (cond
       (#{:kdb/up-arrow :kbd/down-arrow} (:keydowns kbd-state)) nil
       ;; up-arrow
       (:kbd/up-arrow (:keys kbd-state)) 
       (react/update-state ::pad1-state (fn [st] (assoc st :move-up true)))
       ;; down-arrow
       (:kbd/down-arrow (:keys kbd-state)) 
       (react/update-state ::pad1-state (fn [st] (assoc st :move-down true)))
       ;; etc ..;
       )))
)


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

;;==============
;;Keyboard input
;;==============

(defonce pad2-input (atom :nil))
(defonce pad1-input (atom :nil))

;;{
;; How to handle the keyboard input:
;; * The pad at the right side move with the up and down arrow
;; * The pad at the left side move with Z and S
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
(world/register-input-callback! (:world @+myctrl+)
                                (fn [key _ action _]
                                  (cond

                                    ;;Pad1 Move Up
                                    (and (= key (kbd/key :up-arrow)) ; HACK: GLFW constant for up arrow!
                                         (= action 1)) ; HACK: GLFW_PRESS
                                    (swap! pad1-input (fn [_] :up))
                                    (and (= key 265) ; HACK: GLFW constant for up arrow!
                                         (= action 0)) ; HACK: GLFW_RELEASE
                                    (swap! pad1-input (fn [old]
                                                             (if (= old :up)
                                                               :nil
                                                               old)))

                                    ;;Pad1 Move Down
                                    (and (= key 264) ; HACK: GLFW constant for down arrow!
                                         (= action 1)) ; HACK: GLFW_PRESS
                                    (swap! pad1-input (fn [_] :down))
                                    (and (= key 264) ; HACK: GLFW constant for down arrow!
                                         (= action 0)) ; HACK: GLFW_RELEASE
                                    (swap! pad1-input (fn [old]
                                                             (if (= old :down)
                                                               :nil
                                                               old)))

                                    ;;Pad2 Move Up
                                    (and (= key 87) ; HACK: GLFW constant for Z!
                                         (= action 1)) ; HACK: GLFW_PRESS
                                    (swap! pad2-input (fn [_] :up))
                                    (and (= key 87) ; HACK: GLFW constant for Z!
                                         (= action 0)) ; HACK: GLFW_RELEASE
                                    (swap! pad2-input (fn [old]
                                                            (if (= old :up)
                                                              :nil
                                                              old)))

                                    ;;Pad2 Move Down
                                    (and (= key 83) ; HACK: GLFW constant for down arrow!
                                         (= action 1)) ; HACK: GLFW_PRESS
                                    (swap! pad2-input (fn [_] :down))
                                    (and (= key 83) ; HACK: GLFW constant for down arrow!
                                         (= action 0)) ; HACK: GLFW_RELEASE
                                    (swap! pad2-input (fn [old]
                                                            (if (= old :down)
                                                              :nil
                                                              old))))))

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
    [{:group-id :test/pad-group-1 :hitbox-id :test/pad-hitbox-top-1 :collision-handler #(react/dispatch [::ball-collision-top1])}
     {:group-id :test/pad-group-1 :hitbox-id :test/pad-hitbox-middle-1 :collision-handler #(react/dispatch [::ball-collision-middle1])}
     {:group-id :test/pad-group-1 :hitbox-id :test/pad-hitbox-bottom-1 :collision-handler #(react/dispatch [::ball-collision-bottom1])}
     {:group-id :test/pad-group-2 :hitbox-id :test/pad-hitbox-top-2 :collision-handler #(react/dispatch [::ball-collision-top2])}
     {:group-id :test/pad-group-2 :hitbox-id :test/pad-hitbox-middle-2 :collision-handler #(react/dispatch [::ball-collision-middle2])}
     {:group-id :test/pad-group-2 :hitbox-id :test/pad-hitbox-bottom-2 :collision-handler #(react/dispatch [::ball-collision-bottom2])}
     {:group-id :test/wall-group-1 :hitbox-id :test/wall-hitbox-1 :collision-handler #(react/dispatch [::ball-collision-wall1])}
     {:group-id :test/wall-group-2 :hitbox-id :test/wall-hitbox-2 :collision-handler #(react/dispatch [::ball-collision-wall2])}
     ]]])

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
