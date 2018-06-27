(ns yaw.scene
  (:require [yaw.util :as u]
            [yaw.world :as w :refer [empty-item-map]]
            [yaw.mesh :as mesh]
            [yaw.spec]
            [clojure.spec.alpha :as s]
            [clojure.data]))

(defn item-map
  "Reducing function that adds a conformed item to a pre-existing map"
  ([] empty-item-map)
  ([m [kw v]]
   (case kw
     :separator m
     :content (let [[kw v] v]
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
                                             (assoc (:params v) :id-n (:id-n v)))))))))
  ([*xs*]
   (let [xs (s/conform :yaw.spec.scene/scene *xs*)]
     (if (s/invalid? xs)
       (throw (ex-info "Invalid scene" (s/explain-data :yaw.spec.scene/scene *xs*)))
       (reduce item-map empty-item-map (:content xs))))))

(defn display-scene
  "Opens a window and displays a 3D scene in it"
  [scene]
  (let [scene (s/conform :scene/scene scene)
        params (get scene :params {})
        skybox (get params :skybox nil)
        camera (get params :camera nil)
        items (get scene :content [])
        univ (w/start-universe!)
        world (:world @univ)]
    (if (some? skybox)
      (w/set-skybox! world
                     :scale (:scale skybox)
                     :color (:color skybox)))
    (let [items (reduce item-map empty-item-map items)]
      (if (some? (-> items :lights :ambient))
        (let [{:keys [color i]
               :or {color [:kw :white]
                    i 0.3}} (-> items :lights :ambient)]
          (w/set-ambient-light! world :color color :i i)))
      (if (some? (-> items :lights :sun))
        (let [{:keys [color i dir]
               :or {color [:kw :white]
                    i 0.6
                    dir [-1 -1 -1]}} (-> items :lights :sun)]
          (w/set-sun! world :color color :i i :direction dir)))

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
                 :color (:color v)
                 :i (get v :i 0.3)
                 :position (:pos v))))
            (-> items :lights :points))
      (run! (fn [[k v]]
              (let [n (:id-n v)]
                (w/set-spot-light!
                 world
                 n
                 :color (:color v)
                 :i (get v :i 0.3)
                 :position (:pos v)
                 :direction (:dir v)
                 :angle (get v :angle 20))))
            (-> items :lights :spots))

      (run! (fn [[k v]]
              (let [m ((:mesh v) (:meshes @univ))
                    m (w/create-simple-mesh!
                       world
                       :geometry m
                       :rgb (second (get v :mat [:color [1 1 1]])))
                    i (w/create-item!
                       world
                       :id (str k)
                       :position (:pos v)
                       :scale (get v :scale 1)
                       :mesh m)]
                (apply w/rotate! i (u/explode (get v :rot [0 0 0])))))
            (:items items)))))

(defn get-new
  [old new]
  new)

(defn vec-diff
  [old new]
  (mapv u/?- new old))

(defn action-value
  "Conjoins the action associated with the difference between two values into `xs`.
  `id` and `action` are the keywords used to populate the returned action.
  `dif` is a function to compute the action's value based on `old` and `new`
  (in this order)
  `xs` is "
  [xs action id old new dif]
  (if-not (= old new)
    (conj xs [action id (dif old new)])
    xs))

(defn- diff-items
  "Reduces the given old and new item maps to a sequence of effect representations it conjoins to the accumulator"
  [acc old new]
  (reduce-kv (fn [d id v]
               (if (not (contains? old id))
                 (conj d [:item/add id v])
                 (reduce-kv (fn [d p v]
                              (case p
                                :rot (action-value d :item/rotate id
                                                   (-> old id :rot) v
                                                   vec-diff)
                                :pos (action-value d :item/translate id
                                                   (-> old id :pos) v
                                                   vec-diff)
                                :mat (action-value d :item/remat id
                                                   (-> old id :mat) v
                                                   get-new)
                                :scale (action-value d :item/rescale id
                                                     (-> old id :scale) v
                                                     get-new)
                                :mesh (action-value d :item/remesh id
                                                    (-> old id :mesh) v
                                                    get-new)))
                            d v)))
             acc new))

(defn- diff-cameras
  "Reduces the given old and new camera maps to a sequence of effect representations it conjoins to the accumulator"
  [acc old new]
  (reduce-kv (fn [d id v]
               (if (not (contains? old id))
                 (conj d [:cam/add id v])
                 (reduce-kv (fn [d p v]
                              (case p
                                :target (action-value d :cam/retarget id
                                                      (-> old id :target) v
                                                      get-new)
                                :pos (action-value d :cam/translate id
                                                   (-> old id :pos) v
                                                   vec-diff)
                                :fov (action-value d :cam/refov id
                                                   (-> old id :fov) v
                                                   get-new)))
                            d v)))
             acc new))

(defn- diff-points
  [acc old new]
  (reduce-kv (fn [d id v]
               (if (not (contains? old id))
                 (conj d [:light/add id v])
                 (reduce-kv (fn [d p v]
                              (case p
                                :color (action-value d :light/recolor id
                                                     (-> old id :color) v
                                                     get-new)
                                :pos (action-value d :light/translate id
                                                   (-> old id :pos) v
                                                   vec-diff)
                                :i (action-value d :light/intensity id
                                                 (-> old id :i) v
                                                 get-new)
                                d))
                            d v)))
             acc new))

(defn- diff-spots
  [acc old new]
  (reduce-kv (fn [d id v]
               (if (not (contains? old id))
                 (conj d [:spot/add id v])
                 (reduce-kv (fn [d p v]
                              (case p
                                :color (action-value d :spot/recolor id
                                                     (-> old id :color) v
                                                     get-new)
                                :pos (action-value d :spot/translate id
                                                   (-> old id :pos) v
                                                   vec-diff)
                                :i (action-value d :spot/intensity id
                                                 (-> old id :i) v
                                                 get-new)
                                :dir (action-value d :spot/redirect id
                                                   (-> old id :dir) v
                                                   get-new)
                                d))
                            d v)))
             acc new))

(defn- diff-ambient
  [acc old new]
  (if (= old new)
    acc
    (if (nil? old)
      (conj acc [:ambient/set new])
      (if (nil? new)
        acc
        (reduce-kv (fn [d p v]
                     (case p
                       :color (conj d [:ambient/recolor v])
                       :i (conj d [:ambient/intensity v])
                       d))
                   acc new)))))

(defn- diff-sun
  [acc old new]
  (if (= old new)
    acc
    (if (nil? old)
      (conj acc [:sun/set new])
      (if (nil? new)
        acc
        (reduce-kv (fn [d p v]
                     (case p
                       :color (conj d [:sun/recolor v])
                       :i (conj d [:sun/intensity v])
                       :dir (conj d [:sun/redirect v])
                       d))
                   acc new)))))

(defn- diff-lights
  "Reduces the given old and new light maps to a sequence of effect representations it conjoins to the accumulator"
  [acc old new]
  (reduce-kv (fn [d k v]
               (case k
                 :ambient (diff-ambient d (:ambient old) v)
                 :sun (diff-sun d (:sun old) v)
                 :points (diff-points d (:points old) v)
                 :spots (diff-spots d (:spots old) v)
                 d))
             acc new))

(defn diff
  [scene-old scene-new]
  (diff-items (diff-cameras (diff-lights [:diff]
                                         (:lights scene-old)
                                         (:lights scene-new))
                            (:cameras scene-old)
                            (:cameras scene-new))
              (:items scene-old)
              (:items scene-new)))

;; (defn diff
;;   "Gives the diff of two intermediary scenes"
;;   [scene-old scene-new]
;;   (let [[to-del to-add _] (clojure.data/diff scene-old scene-new)]
;;     (diff-items (diff-cameras (diff-lights [:diff]
;;                                            (get to-del :lights {})
;;                                            (get to-add :lights {}))
;;                               (get to-del :cameras {})
;;                               (get to-add :cameras {}))
;;                 (get to-del :items {})
;;                 (get to-add :items {}))))

(defn display-diff
  "Takes a universe and a diff, and executes the effects described by the diff"
  [univ diff]
  (let [[tag & actions] diff]
    (if (not= tag :diff)
      (throw (ex-info "Invalid Diff"
                      {:expected :diff
                       :actual tag}))
      (run!
       (fn [[action & details]]
         (case action
           :item/add (let [[id params] details
                           params (merge {:mat [:color [1 1 1]] :scale 1 :pos [0 0 0] :rot [0 0 0]}
                                         params)
                           m (get (:meshes @univ) (:mesh params))
                           m (w/create-simple-mesh!
                              (:world @univ)
                              :geometry m
                              :rgb (second (:mat params)))
                           i (w/create-item!
                              (:world @univ)
                              :id (str id)
                              :position (:pos params)
                              :scale (:scale params)
                              :mesh m)]
                       (swap! univ assoc-in [:items id] i)
                       (swap! univ assoc-in [:data :items id] params)
                       (apply w/rotate! i (u/explode (:rot params))))
           :item/remat (let [[id [type value]] details
                             i (get-in @univ [:items id])]
                         (case type
                           :color (let [col value]
                                    (swap! univ assoc-in [:data :items id :mat] [:color col])
                                    (w/set-item-color! i col))
                           :texture (swap! univ assoc-in [:data :items id :mat] [:texture value])))
           :item/translate (let [[id [x y z]] details
                                 i (get-in @univ [:items id])]
                             (swap! univ update-in [:data :items id :pos] #(mapv + % [x y z]))
                             (w/translate! i :x x :y y :z z))
           :item/rotate (let [[id [x y z]] details
                              i (get-in @univ [:items id])]
                          (swap! univ update-in [:data :items id :rot] #(mapv + % [x y z]))
                          (w/rotate! i :x x :y y :z z))
           :item/remesh (throw (ex-info "Unimplemented action"))
           :item/rescale (throw (ex-info "Unimplemented action"))
           :camera/add (let [[id params] details
                             params (merge {:fov 60} params)
                             c (w/create-camera! params)]
                         (swap! univ assoc-in [:items id] c)
                         (swap! univ assoc-in [:data :cameras id] params))
           :camera/translate (let [[id [x y z]] details
                                   i (get-in @univ [:items id])]
                               (swap! univ update-in [:data :cameras id :pos] #(mapv + % [x y z]))
                               (w/rotate! i :x x :y y :z z))))
       actions))))
