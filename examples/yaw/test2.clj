(ns yaw.test2
  "A simple 3D example using a reagent-like approach."
  (:require
   [clojure.set :as set]
   [yaw.world :as w]
   [yaw.reaction :as r]
   [yaw.render :as render]))


(defn the-group
  "Create a cube with its position linked to the `pos` reactive atom."
  [state]
  [:group :test/group {:pos (:pos state)
                       :rot [0 0 0]
                       :scale 1}
   [:item :test/box {:mesh :mesh/box
                     :pos [0 0 -5]
                     :rot [0 0 0]
                     :mat :red
                     :scale 0.3}]
   [:item :test/box2 {:mesh :mesh/box
                      :pos [-2 0 -5]
                      :rot [0 0 0]
                      :mat :red
                      :scale 0.3}]
   [:hitbox :test/hitbox {:pos [0 0 -5]
                          :scale 1
                          :length [1 1 1]}]
   ])



(defn scene
  []
  [:scene
   [:ambient {:color :white :i 0.4}]
   [:sun {:color :red :i 1 :dir [-1 0 0]}]
   [:light ::light {:color :yellow :pos [0.5 0 -4]}]
   [the-group {:pos [0 0 -5]}]
   ])

(def +myctrl+ (w/start-universe!))

(render/render! +myctrl+ [scene])

;; (def +update+ (r/create-update-ratom +myctrl+))


;; (remove-watch +update+ :yaw.reaction/propagation)

;; (add-watch +update+ :yaw.reaction/propagation
;;            (fn [_ _ _ _]
;;              (swap! +cube-state+
;;                     update-cube-state)))



