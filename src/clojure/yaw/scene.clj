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

(defn color-rgb
  "Returns a rgb tuple from any sort of color"
  [[kw v]]
  (case kw
    :kw (kw-rgb v)
    :rgb v))

(def empty-item-map
  {:cameras {}
   :lights {:ambient nil :sun nil :points {} :spots {}}
   :groups {}
   :items {}})
(defn item-map
  "Reducing function that adds a conformed item to a pre-existing map"
  ([] empty-item-map)
  ([m [kw v]]
   (case kw
     :item (assoc-in m [:items (:id-kw v)] (:params v))
     :camera (assoc-in m [:cameras (:id-kw v)] (:params v))
     :light (let [[kw v] v]
              (case kw
                :ambient (assoc-in m [:lights :ambient] (:params v))
                :sun (assoc-in m [:lights :sun] (:params v))
                :point (assoc-in m
                                 [:lights :points (:id-kw v)]
                                 (assoc (:params v) :id-n (:id-n v)))
                :spot (assoc-in m
                                [:lights :spots (:id-kw v)]
                                (assoc (:params v) :id-n (:id-n v))))))))

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
                     :color (color-rgb (:color skybox))))
    (let [items (reduce item-map empty-item-map items)]
      (if (some? (-> items :lights :ambient))
        (let [{:keys [color i]
               :or {color [:kw :white]
                    i 0.3}} (-> items :lights :ambient)]
          (w/set-ambient-light! world :color (color-rgb color) :i i)))
      (if (some? (-> items :lights :sun))
        (let [{:keys [color i dir]
               :or {color [:kw :white]
                    i 0.6
                    dir [-1 -1 -1]}} (-> items :lights :sun)]
          (w/set-sun! world :color (color-rgb color) :i i :direction dir)))
      (run! (fn [[k v]]
              (let [n (:id-n v)]
                (w/set-point-light!
                 world
                 n
                 :color (color-rgb (get v :color [:kw :white]))
                 :i (get v :i 0.3)
                 :position (get v :pos [0 0 0]))))
            (-> items :lights :points))
      (run! (fn [[k v]]
              (let [n (:id-n v)]
                (w/set-spot-light!
                 world
                 n
                 :color (color-rgb (get v :color [:kw :white]))
                 :i (get v :i 0.3)
                 :position (get v :pos [0 0 0])
                 :direction (get v :dir [0 0 -1])
                 :angle (get v :angle 20))))
            (-> items :lights :spots))
      (run! (fn [[k v]]
              (let [m (mesh/mesh (:mesh v))
                    m (w/create-simple-mesh!
                       world
                       :geometry m
                       :rgb (color-rgb (second (get v :mat [:color [:kw :white]]))))
                    i (w/create-item!
                       world
                       :id (str k)
                       :position (get v :pos [0 0 0])
                       :scale (get v :scale 1)
                       :mesh m)]
                (apply w/rotate! i (explode (get v :rot [0 0 0])))))
            (:items items)))))

(display [:scene
          {:skybox {:color [0.3 0 0.3] :scale [30 30 30]}}
          [:ambient {:color :white :i 0.1}]
          [:light :test/point 0 {:i 0.5 :color :red :pos [1 1 -5]}]
          [:spot :test/spot 0 {:i 0.6 :color [0 0 1] :pos [0 0 0] :dir [0 0 -1] :angle 1}]
          [:sun {:color :yellow :i 0.4 :dir [-1 -1 -1]}]
          [:item :test/cone {:mesh :mesh/cone :mat :white :pos [0 0 -5] :rot [-90 0 0]}]
          [:item :test/box {:mesh :mesh/box :pos [2 1 -7] :mat :blue}]])
