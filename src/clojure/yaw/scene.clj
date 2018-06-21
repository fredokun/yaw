(ns clojure.yaw.scene
  (:require
   [yaw.util :as u]
   [yaw.world :as w]
   [yaw.mesh :as mesh]
   [yaw.spec]
   [clojure.spec.alpha :as s]
   [clojure.data]))

(defn color-rgb
  "Returns a rgb tuple from any sort of color"
  [[kw v]]
  (case kw
    :kw (get u/color-kw v)
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

(defn display-scene
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
              (println k v)
              (let [c (w/create-camera!
                       (update v :target
                               (fn [[tk tv]]
                                 (case tk
                                   :item (:pos (get (:items items) tv))
                                   :vec tv))))]
                (w/add-camera! world c)
                (if (= k camera)
                  (w/set-camera! world c))))
            (:cameras items))

      (run! (fn [[k v]]
              (let [n (:id-n v)]
                (w/set-point-light!
                 world
                 n
                 :color (color-rgb (:color v))
                 :i (get v :i 0.3)
                 :position (:pos v))))
            (-> items :lights :points))
      (run! (fn [[k v]]
              (let [n (:id-n v)]
                (w/set-spot-light!
                 world
                 n
                 :color (color-rgb (:color v))
                 :i (get v :i 0.3)
                 :position (:pos v)
                 :direction (:dir v)
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
                       :position (:pos v)
                       :scale (get v :scale 1)
                       :mesh m)]
                (apply w/rotate! i (u/explode (get v :rot [0 0 0])))))
            (:items items)))))

(defn- diff-items
  "Reduces the given old and new item maps to a sequence of effect representations it conjoins to the accumulator"
  [acc old new]
  (reduce-kv (fn [d k v]
               (if (not (contains? old k))
                 (conj d [:item/add k v])
                 (reduce-kv (fn [d p v]
                              (case p
                                :rot (conj d [:item/rotate k (mapv u/-? v (-> old k :rot))])
                                :pos (conj d [:item/translate k (mapv u/-? v (-> old k :pos))])
                                :mat (conj d [:item/remat k v])
                                :scale (conj d [:item/rescale k v])))
                            d v)))
             (reduce-kv (fn [d k v]
                          (if (not (contains? new k))
                            (conj d [:item/remove k])
                            d))
                        acc old)
             new))

(defn- diff-cameras
  "Reduces the given old and new item maps to a sequence of effect representations it conjoins to the accumulator"
  [acc old new]
  (reduce-kv (fn [d k v]
               (if (not (contains? old k))
                 (conj d [:cam/add k v])
                 (reduce-kv (fn [d p v]
                              (case p
                                :target (conj d [:cam/retarget k v])
                                :pos (conj d [:cam/translate k (mapv u/-? v (-> old k :pos))])
                                :fov (conj d [:cam/refov k v])))
                            d v)))
             (reduce-kv (fn [d k v]
                          (if (not (contains? new k))
                            (conj d [:cam/remove k])
                            d))
                        acc old)
             new))

(defn diff
  "Gives the diff of two intermediary scenes"
  [scene-old scene-new]
  (let [[to-del to-add _] (clojure.data/diff scene-old scene-new)]
    (diff-items (diff-cameras [:diff]
                              (:cameras to-del)
                              (:cameras to-add))
                (:items to-del)
                (:items to-add))))
