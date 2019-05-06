(ns yaw.collision-test
  "A simple 3D example using a reagent-like approach."
  (:require 
   [clojure.set :as set]
   [yaw.world :as w]
   [yaw.reaction :as r]
   [yaw.render :as render]))

(defn the-group
  "Create a group with its position linked to the `pos` reactive atom."
  [id state]
  [:group id {:pos (:pos @state)
              :rot [0 0 0]}
   [:item :test/box {:mesh :mesh/box
                     :pos [0 0 0]
                     :rot [0 0 0]
                     :mat :red
                     :scale 0.3}]
   [:hitbox :test/hitbox1 {:pos [0 0 0]
                          :scale 0.6
                          :length [1 1 1]}
    [{:group-id :test/group2 :hitbox-id :test/hitbox2
      :collision-handler #(println "COLLISION")}]]])

(defn scene
  [group-state]
  [:scene
   [:ambient {:color :white :i 0.4}]
   [:sun {:color :red :i 1 :dir [-1 0 0]}]
   [:light ::light {:color :yellow :pos [0.5 0 -4]}]
   [the-group :test/group1 group-state]
   [:group :test/group2 {:pos [2 0 -5]
                         :rot [0 0 0]
                         :scale 1}
    [:item :test/box2 {:mesh :mesh/box
                       :pos [0 0 0]
                       :rot [0 0 0]
                       :mat :red
                       :scale 0.3}]
    [:hitbox :test/hitbox2 {:pos [0 0 0]
                            :scale 0.6
                            :length [1 1 1]}]]])

(def +myctrl+ (w/start-universe!))

(def +group-state+ (r/reactive-atom +myctrl+ {:pos [-2 0 -5] :delta [0.02 0 0]}))

(render/render! +myctrl+ [scene +group-state+])

(def +update+ (r/create-update-ratom +myctrl+))

(add-watch +update+ :yaw.reaction/propagation
           (fn [_ _ _ _]
             (swap! +group-state+
                    (fn [{pos :pos delta :delta}]
                      {:pos (mapv + pos delta) :delta delta}))))
