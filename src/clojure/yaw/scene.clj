(ns clojure.yaw.scene
  (:require [yaw.world :as w]
            [yaw.mesh :as mesh]
            [yaw.spec]
            [clojure.spec.alpha :as s]))

(defn explode
  "Explodes a vector [x y z] tuple into (:x x :y y :z z)"
  [[x y z]]
  (list :x x :y y :z z))

(defn kw-rgb
  "Returns a rgb tuple from a keyword color"
  [color]
  (case color
      :black [0 0 0]
      :red [1 0 0]
      :blue [0 0 1]
      :green [0 1 0]
      :yellow [1 1 0]
      :cyan [0 1 1]
      :magenta [1 0 1]
      :white [1 1 1]))

(defn color
  "Returns a rgb tuple from any sort of color"
  [[kw v]]
  (case kw
    :kw (kw-rgb v)
    :rgb v))

(def empty-item-map
  {:cameras []
   :lights {:ambient nil :sun nil :points [] :spots []}
   :groups {}
   :items {}})
(defn item-map
  "Reducing function that adds a conformed item to a pre-existing map"
  ([] empty-item-map)
  ([m [kw v]]
   (case kw
     :item (assoc-in m [:items (:id-kw v)] (:params v)))))

(defn display
  "Opens a window and displays a 3D scene in it"
  [scene]
  (let [scene (s/conform :scene/scene scene)
        params (get scene :params {})
        skybox (get params :skybox nil)
        camera (get params :camera nil)
        items (get scene :items [])
        univ (w/start-universe!)
        world (:world @univ)]
    (if (some? skybox)
      (w/set-skybox! world
                     :scale (:scale skybox)
                     :color (color (:color skybox))))
    (let [items (reduce item-map empty-item-map items)]
      (println items)
      (run! (fn [[k v]]
              (let [m (apply w/create-simple-mesh! world (mesh/mesh (:mesh v)) )
                    i (w/create-item! world :id (str k)
                                      :position (get v :pos [0 0 0])
                                      :scale (get v :scale 1))]
                (apply w/rotate! i (explode (get v :rot [0 0 0])))))
            (:items items)))))


(display [:scene {:skybox {:color :red :scale [30 30 30]}}
          [:item :test/box {:mesh :mesh/box :color :white :pos [0 0 -5] :rot [0 45 0]}]])

