(ns clojure.yaw.cube-in-cube-test
  (:require  [yaw.world :as w]
             [yaw.reaction :as r]
             [yaw.render :as render]
             [clojure.test :as t]))

(defn vertice
  "Create a vertice of the virtual box"
  [pos id]
  [:item (keyword (str "test/vertice" id)) {:mesh :mesh/box 
                                            :pos pos 
                                            :rot [0 0 0] 
                                            :mat :white
                                            :scale 0.05}])

(defn box
  "Single box in the center of the canvas."
  [atom]
  [:item :test/box {:mesh :mesh/box
                    :pos @atom
                    :rot [0 0 0]
                    :mat :red
                    :scale 0.2}])

(defn scene
  [pos]
  [:scene
   [:ambient {:color :white :i 1}]
   [vertice [-2 -2 -5] "1"]
   [vertice [-2 2 -5] "2"]
   [vertice [2 2 -5] "3"]
   [vertice [2 -2 -5] "4"]
   [vertice [-2 -2 -9] "5"]
   [vertice [-2 2 -9] "6"]
   [vertice [2 2 -9] "7"]
   [vertice [2 -2 -9] "8"]
   [box pos]])

(defn pos-ok
  "Return a true is the cube is in the vitual box, else false"
  [[x y z]]
  (if (or (<= x -2) (>= x 2))
    false
    (if (or (<= y -2) (>= y 2))
      false
      (if (or (<= z -2) (>= z 2))
        false
        true))))

(def +myctrl+ (w/start-universe!))

(def +pos+ (r/reactive-atom +myctrl+ [0 0 -5]))

(def +vector+ (atom [-0.01 -0.01 0.02]))

(def +update+ (r/create-update-ratom +myctrl+))

(render/render! +myctrl+ [scene +pos+])

;; (add-watch +pos+ :yaw.reaction/propagation
;;            (fn [_ _ _ _ [x y z]]
;;              (if-not (pos-ok [x y z])
;;                (let [[old_vx old_vy old_vz] @vector
;;                      new_vx (if (or (and (<= x -2) (< old_vx 0))
;;                                     (and (>= x 2) (> old_vx 0)))
;;                               (- 0 old_vx)
;;                               old_vx)
;;                      new_vy (if (or (and (<= y -2) (< old_vy 0))
;;                                     (and (>= y 2) (> old_vy 0)))
;;                               (- 0 old_vy)
;;                               old_vy)
;;                      new_vz (if (or (and (<= z -2) (< old_vz 0))
;;                                     (and (>= z 2) (> old_vz 0)))
;;                               (- 0 old_vz)
;;                               old_vz)]
;;                  (swap! +vector+
;;                         (fn [_]
;;                           [new_vx new_vy new_vz]))))))

;; (add-watch +pos+ :yaw.reaction/propagation
;;            (fn [_ _ _ [x y z]]
;;              (if-not (pos-ok [x y z])
;;                (swap! +vector+
;;                       (fn [vx vy vz]
;;                         [(- vx) (- vy) (- vz)])))))

(add-watch +pos+ :yaw.reaction/propagation
           (fn [_ _ _[x y z]]
             (if-not (pos-ok [x y z])
               (swap! +vector+
                      (fn [vx vy vz]
                        [(- vx) (- vy) (- vz)])))))

(add-watch +update+ :yaw.reaction/propagation
           (fn [_ _ _ new]
             (swap! +pos+
                    (fn [pos]
                      (vec (map + pos @+vector+))))))
