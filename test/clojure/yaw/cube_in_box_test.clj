(ns clojure.yaw.cube-in-box-test
  (:require  [yaw.world :as w]
             [yaw.reaction :as r]
             [yaw.render :as render]
             [clojure.test :as t]))

(def min-x -2)
(def max-x 2)
(def min-y -2)
(def max-y 2)
(def min-z -9)
(def max-z -5)

;;Create a vertice with an identifier in a position
(defn vertice
  "Create a vertice of the virtual box"
  [pos id]
  [:item (keyword (str "test/vertice" id)) {:mesh :mesh/box 
                                            :pos pos 
                                            :rot [0 0 0] 
                                            :mat :white
                                            :scale 0.05}])

(defn my-cube
  "Create a cube with its position linked to a ratom "
  [atom]
  [:item :test/box {:mesh :mesh/box
                    :pos @atom
                    :rot [0 0 0]
                    :mat :red
                    :scale 0.3}])

(defn scene
  ;;We're going to create a cube with 8 others cubes
  ;;that show the vertices of the virtual box
  ;;(We didn't find how to create a transparent box)
  [pos]
  [:scene
   [:ambient {:color :white :i 0.4}]
   [:sun {:color :red :i 1 :dir [-1 0 0]}]
   [:light ::light {:color :yellow :pos [0.5 0 -4]}]
   [vertice [min-x min-y max-z] "1"]
   [vertice [min-x max-y max-z] "2"]
   [vertice [max-x max-y max-z] "3"]
   [vertice [max-x min-y max-z] "4"]
   [vertice [min-x min-y min-z] "5"]
   [vertice [min-x max-y min-z] "6"]
   [vertice [max-x max-y min-z] "7"]
   [vertice [max-x min-y min-z] "8"]
   [my-cube pos]])

(defn pos-ok?
  "Return a true is the cube is in the vitual box, else false"
  [[x y z]]
  (if (or (<= x min-x) (>= x max-x))
    false
    (if (or (<= y min-y) (>= y max-y))
      false
      (if (or (<= z min-z) (>= z max-z))
        false
        true))))

(def +myctrl+ (w/start-universe!))

(def +pos+ (r/reactive-atom +myctrl+ [0 0 -5]))

(def +vector+ (atom [0.02 -0.04 0.03]))

(def +update+ (r/create-update-ratom +myctrl+))

(render/render! +myctrl+ [scene +pos+])

(add-watch +update+ :yaw.reaction/propagation
           (fn [_ _ _ _]
             (swap! +pos+
                    (fn [[x y z]]
                      ;;is the cube in the virtual box ?
                      (if (not (pos-ok? [x y z]))
                        ;;if it's outside, we change the value of the vector
                        (let [[old_vx old_vy old_vz] @+vector+
                              ;;we calculate a value for vx of the vector
                              ;;it's still vx if x is still between min-x and max-x
                              ;;-vx else 
                              new_vx (if (or (and (<= x min-x) (< old_vx 0))
                                             (and (>= x max-x) (> old_vx 0)))
                                       (- old_vx)
                                       old_vx)
                              ;;same with vy
                              new_vy (if (or (and (<= y min-y) (< old_vy 0))
                                             (and (>= y max-y) (> old_vy 0)))
                                       (- old_vy)
                                       old_vy)
                              ;;same with vz
                              new_vz (if (or (and (<= z min-z) (< old_vz 0))
                                             (and (>= z max-z) (> old_vz 0)))
                                       (- old_vz)
                                       old_vz)]
                          (do
                            ;;we update the vector with its new values
                            ;;and we modify the position of the cube with it
                            (swap! +vector+
                                   (fn [_]
                                     [new_vx new_vy new_vz]))
                            (mapv + [x y z] @+vector+)))
                        ;;if it's inside, we just update its position
                        (mapv + [x y z] @+vector+))))))


;; Need to change the redefinition of add-watch for ratoms
;; It seems that trying to add another watcher on +pos+ will remove the former watcher
;; So we can use the following implementation
;; (add-watch +pos+ :yaw.reaction/propagation
;;            (fn [_ _ _ _ [x y z]]
;;              (if-not (pos-ok? [x y z])
;;                (let [[old_vx old_vy old_vz] @+vector+
;;                      ;;we calculate a value for vx of the vector
;;                      ;;it's still vx if x is still between min-x and max-x
;;                      ;;-vx else 
;;                      new_vx (if (or (and (<= x min-x) (< old_vx 0))
;;                                     (and (>= x max-x) (> old_vx 0)))
;;                               (- old_vx)
;;                               old_vx)
;;                      ;;same with vy
;;                      new_vy (if (or (and (<= y min-y) (< old_vy 0))
;;                                     (and (>= y max-y) (> old_vy 0)))
;;                               (- old_vy)
;;                               old_vy)
;;                      ;;same with vz
;;                      new_vz (if (or (and (<= z min-z) (< old_vz 0))
;;                                     (and (>= z max-z) (> old_vz 0)))
;;                               (- old_vz)
;;                               old_vz)]
;;                  (swap! +vector+
;;                         (fn [_]
;;                           [new_vx new_vy new_vz]))))))

;; (add-watch +update+ :yaw.reaction/propagation
;;            (fn [_ _ _ new]
;;              (swap! +pos+
;;                     (fn [pos]
;;                       (mapv + pos @+vector+)))))
