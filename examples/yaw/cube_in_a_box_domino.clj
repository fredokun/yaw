
(ns yaw.cube-in-a-box-domino
  "A simple 3D example using a reagent-like approach."
  (:require 
   [clojure.set :as set]
   [yaw.world :as world]
   [yaw.reaction :as react]
   [yaw.render :as render]))

(declare +myctrl+)

;;; =====================
;;; The state part
;;; =====================

(react/register-state  ::cube-state nil)

(def init-cube-state
  {:pos [0 0 -5]
   :delta [0.02 -0.04 0.03]})

;;; =====================
;;; Subscription(s)
;;; =====================

(react/register-subscription 
 ::cube-changed
 (fn [db]
   (::cube-state  db)))

;;; ====================
;;; Event handlers
;;; ====================

(react/register-event
 :react/initialize
 (fn []
   (react/init-state ::cube-state init-cube-state)))

(react/register-event
 :react/frame-update
 (fn []
   (react/dispatch [::move-cube])))

(declare update-cube-state)

(react/register-event
 ::move-cube
 (fn []
   (react/update-state ::cube-state 
                       (fn [cube-state]
                         (update-cube-state cube-state)))))

(defn inter? [s1 s2]
  (if (not-empty (set/intersection s1 s2))
    true
    false))

;; (inter? #{} #{:a})
;; => false

;; (inter? #{:a} #{:b})
;; => false

;; (inter? #{:a} #{:a :b})
;; => true

(defn update-delta [checks [dx dy dz]]
  [(if (inter? #{:underflow-x :overflow-x} checks)
     (- dx) dx)
   (if (inter? #{:underflow-y :overflow-y} checks)
     (- dy) dy)
   (if (inter? #{:underflow-z :overflow-z} checks)
     (- dz) dz)])

(defn update-cube-state [{pos :pos
                          delta :delta}]
  (let [checks (pos-check pos)
        delta' (update-delta checks delta)]
    {:pos (mapv + pos delta')
     :delta delta'}))

(defn limit-set [comp coord limit key]
  (if (comp coord limit)
    #{key}
    #{}))

;; (limit-set < 2 3 :underflow)
;; => #{:underflow}

;; (limit-set < 3 2 :underflow)
;; => #{}

(defn bounds-checker [min-x max-x min-y max-y min-z max-z]
  (fn [[x y z]]
    (set/union (limit-set < x min-x :underflow-x)
               (limit-set > x max-x :overflow-x)
               (limit-set < y min-y :underflow-y)
               (limit-set > y max-y :overflow-y)
               (limit-set < z min-z :underflow-z)
               (limit-set > z max-z :overflow-z))))
                       
(def pos-check (bounds-checker min-x max-x min-y max-y min-z max-z))

;; (pos-check [0 0 -7])
;; => #{}

;; (pos-check [-3 0 -7])
;; => #{:underflow-x}

;; (pos-check [-3 3 -7])
;; => #{:overflow-y :underflow-x}

;;; =====================
;;; The view part
;;; =====================

(def min-x -2)
(def max-x 2)
(def min-y -2)
(def max-y 2)
(def min-z -9)
(def max-z -5)

(defn marker
  "Create a cubic marker"
  [pos id]
  [:item (keyword (str *ns*) (str "marker-" id)) 
   {:mesh :mesh/box 
    :pos pos 
    :rot [0 0 0] 
    :mat :white
    :scale 0.05}])

(defn the-cube
  "Create a cube with its position linked to the `pos` reactive atom."
  []
  (let [cube-state (react/subscribe +myctrl+ [::cube-changed])]
    [:item :test/box {:mesh :mesh/box
                      :pos (:pos @cube-state)
                      :rot [0 0 0]
                      :mat :red
                      :scale 0.3}]))

(defn scene []
  [:scene
   [:ambient {:color :white :i 0.4}]
   [:sun {:color :red :i 1 :dir [-1 0 0]}]
   [:light ::light {:color :yellow :pos [0.5 0 -4]}]
   [marker [min-x min-y max-z] "1"]
   [marker [min-x max-y max-z] "2"]
   [marker [max-x max-y max-z] "3"]
   [marker [max-x min-y max-z] "4"]
   [marker [min-x min-y min-z] "5"]
   [marker [min-x max-y min-z] "6"]
   [marker [max-x max-y min-z] "7"]
   [marker [max-x min-y min-z] "8"]
   [the-cube]])


(defn limit-set [comp coord limit key]
  (if (comp coord limit)
    #{key}
    #{}))

;; (limit-set < 2 3 :underflow)
;; => #{:underflow}

;; (limit-set < 3 2 :underflow)
;; => #{}

(defn bounds-checker [min-x max-x min-y max-y min-z max-z]
  (fn [[x y z]]
    (set/union (limit-set < x min-x :underflow-x)
               (limit-set > x max-x :overflow-x)
               (limit-set < y min-y :underflow-y)
               (limit-set > y max-y :overflow-y)
               (limit-set < z min-z :underflow-z)
               (limit-set > z max-z :overflow-z))))
                       
(def pos-check (bounds-checker min-x max-x min-y max-y min-z max-z))

;; (pos-check [0 0 -7])
;; => #{}

;; (pos-check [-3 0 -7])
;; => #{:underflow-x}

;; (pos-check [-3 3 -7])
;; => #{:overflow-y :underflow-x}


;;; =====================
;;; The main part
;;; =====================

(def +myctrl+ (w/start-universe!))
(react/activate! +myctrl+ [scene])
;; (react/dispatch :react/initialize)
