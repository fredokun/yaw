(ns yaw.cube-rotation-in-a-box
  "A simple 3D example using a reagent-like approach."
  (:require 
   [clojure.set :as set]
   [yaw.world :as w]
   [yaw.reaction :as r]
   [yaw.render :as render]))

(def min-x -2)
(def max-x 2)
(def min-y -2)
(def max-y 2)
(def min-z -9)
(def max-z -5)

(def rotation {:x 2 :y 1 :z 3})

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
  [state]
  [:item :test/box {:mesh :mesh/box
                    :pos (:pos @state)
                    :rot (:rot @state)
                    :mat :red
                    :scale 0.3}])


(defn scene
  [cube-state]
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
   [the-cube cube-state]])

(def +myctrl+ (w/start-universe!))

(def +cube-state+ (r/reactive-atom +myctrl+ {:pos [0 0 -5]
                                             :rot [0 0 0]
                                             :delta [0.02 -0.04 0.03]}))

(render/render! +myctrl+ [scene +cube-state+])

(def +update+ (r/create-update-ratom +myctrl+))

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

(defn update-rot [[x y z]]
  [(mod (+ x (:x rotation)) 360)
   (mod (+ y (:y rotation)) 360)
   (mod (+ z (:z rotation)) 360)])

(defn update-cube-state [{pos :pos
                          rot :rot
                          delta :delta}]
  (let [checks (pos-check pos)
        rotation' (update-rot rot)
        delta' (update-delta checks delta)]
    {:pos (mapv + pos delta')
     :rot rotation'
     :delta delta'}))
    
;; (remove-watch +update+ :yaw.reaction/propagation)

(add-watch +update+ :yaw.reaction/propagation
           (fn [_ _ _ _]
             (swap! +cube-state+
                    update-cube-state)))



