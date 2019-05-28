(ns yaw.scene
  (:require [yaw.util :as u]
            [yaw.world :as w :refer [empty-item-map]]
            [yaw.mesh :as mesh]
            [yaw.spec]
            [clojure.spec.alpha :as s]
            [clojure.data]))

;;{
;; The function take a scene and check if the scene is correct.
;; After the check, it takes each element of the scene in order to create a structure used later
;; for the diff.

;; At the beginning, the structure looks like this:
;; {:cameras {}, :lights {:ambient nil, :sun nil, :points {}, :spots {}}, :groups {}, :items {}}
;; then it's filled with the element of the scene
;;}
(defn item-map
  "Reducing function that adds a conformed item to a pre-existing map"
  ([] empty-item-map)
  ([m [kw v]]
   (case kw
     :separator m
     :content (let [[kw v] v]
                (case kw
                  :group (assoc-in m [:groups (:id-kw v)] {:params (:params v) :items (:items v) :hitboxes (:hitboxes v)})
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

;;{
;; The function will mark every differences between the groups of the old and the new scene
;; with every actions needed to switch the old one to the new one
;;}
(defn- diff-groups
  "Reduces the given old and new group maps to a sequence of effect representations it conjoins to the accumulator"
  [acc old new]
  (reduce-kv (fn [d id v]
               (if (not (contains? old id))
                 ;;If in the old scene the group didn't exist, we mark that we want to create it in the new scene
                 (conj d [:group/add id v])
                 ;;Else we check and mark the differences
                 (reduce-kv (fn [d keyword v]
                              (case keyword
                                :params (reduce-kv (fn [d p v]
                                                     (case p
                                                       ;;We check if there is a diffence in the rotation
                                                       ;;If yes we mark the difference
                                                       :rot (action-value d :group/rotate id
                                                                          (-> old id :params :rot) v
                                                                          vec-diff)
                                                       ;;Same with the position
                                                       :pos (action-value d :group/translate id
                                                                          (-> old id :params :pos) v
                                                                          vec-diff)
                                                       ;;Same with the scale
                                                       :scale (action-value d :group/rescale id
                                                                            (-> old id :params :scale) v
                                                                            get-new)))
                                                   d v)
                                ;;TODO later if we want move an item of the group independently
                                :items d
                                ;;Same with the hitboxes
                                :hitboxes d))
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
                                                   get-new)
                                :live (action-value d :cam/set id
                                                    (-> old id :live) v
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
  "Mark every differences between `scene-old` and `scene-new` and return the struture
  describing every actions needed to switch the old scene to the new scene"
  [scene-old scene-new]
  (diff-groups (diff-items (diff-cameras (diff-lights [:diff]
                                                      (:lights scene-old)
                                                      (:lights scene-new))
                                         (:cameras scene-old)
                                         (:cameras scene-new))
                           (:items scene-old)
                           (:items scene-new))
               (:groups scene-old)
               (:groups scene-new)))

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

(defn apply-collision
  "This function take a `group` an its `hitboxes` and looks in the `univ`
  if they are in collision with the concerned hitboxes, apply the collision handlers
  if they are"
  [univ group hitboxes]
  (if-not (nil? hitboxes)
    (run! (fn [{id :id-kw on-collision :on-collision}]
            ;;For each hitbox, we look if it has collision handlers
            (if-not (nil? on-collision)
              ;;If yes, we begin with fetching the hitbox
              (let [hitbox (w/fetch-hitbox! group id)]
                (run! (fn [{group-id :group-id hitbox-id :hitbox-id collision-handler :collision-handler}]
                        ;;Then for each collision handler, we fetch the group then the hitbox specified
                        (let [group-collided? (get-in @univ [:groups group-id])
                              hitbox-collided? (w/fetch-hitbox! group-collided? hitbox-id)]
                          (if (w/check-collision! (:world @univ) hitbox hitbox-collided?)
                            ;;If the hitboxes are in collision, we execute the collision-handler
                            (collision-handler))))
                      on-collision))))
          hitboxes)))

(defn display-diff!
  "Takes a universe and a diff, and executes the effects described by the diff"
  [univ diff]
  (let [[tag & actions] diff]
    (if (not= tag :diff)
      (throw (ex-info "Invalid Diff"
                      {:expected :diff
                       :actual tag}))
      (run!
       (fn [[action & details]]
         ;; (println details)
         (case action
           :group/add (let [;;Destructuring the variable details in order to retrieve informations
                            [id {params :params items :items hitboxes :hitboxes}] details
                            ;;Retrieve the parameters of the group, with default values when parameters are not defined
                            params (merge {:scale 1 :pos [0 0 0] :rot [0 0 0]} params)
                            ;;Create a group that will be added later in the world
                            group (w/new-group! (:world @univ) id)]
                        ;;For each item specified in the group, we create it and add it in the group
                        (run! (fn [{id :id-kw params :params}]
                                (let [params (merge {:mat [:color [1 1 1]] :scale 1 :pos [0 0 0] :rot [0 0 0]}
                                                    params)
                                      m (get (:meshes @univ) (:mesh params))
                                      m (w/create-simple-mesh!
                                         (:world @univ)
                                         :geometry m
                                         :rgb (second (:mat params)))
                                      i (w/create-item!
                                         (:world @univ)
                                         (str id)
                                         :position (:pos params)
                                         :scale (:scale params)
                                         :mesh m)]
                                  (apply w/rotate! i (u/explode (:rot params)))
                                  (w/group-add! group (str id) i))) items)
                        ;;Same with the hitboxes
                        (run! (fn [{id :id-kw params :params}]
                                (let [params (merge {:scale 1 :pos [0 0 0] :rot [0 0 0] :length [1 1 1]}
                                                    params)
                                      h (w/create-hitbox!
                                         (:world @univ)
                                         id
                                         :position (:pos params)
                                         :length (:length params)
                                         :scale (:scale params))]
                                  (apply w/rotate! h (u/explode (:rot params)))
                                  (w/group-add! group (str id) h))) hitboxes)
                        ;;We now place the group in the world
                        ;;and since the items and hitboxes are linked to the group, they are also moved
                        (apply w/translate! group (u/explode (:pos params)))
                        ;; To uncomment later when rotate with [0 0 0] works
                        ;; (apply w/rotate! group (u/explode (:rot params)))
                        (swap! univ assoc-in [:groups id] group)
                        (swap! univ assoc-in [:data :groups id] {:params params :items items :hitboxes hitboxes}))
           :group/translate (let [[id [x y z]] details
                                  group (get-in @univ [:groups id])
                                  hitboxes (get-in @univ [:data :groups id :hitboxes])]
                              (swap! univ update-in [:data :groups id :params :pos] #(mapv + % [x y z]))
                              (w/translate! group :x x :y y :z z)
                              (apply-collision univ group hitboxes))
           :group/rotate (let [[id [x y z]] details
                               group (get-in @univ [:groups id])
                               hitboxes (get-in @univ [:data :groups id :hitboxes])]
                           (swap! univ update-in [:data :groups id :params :rot] #(mapv + % [x y z]))
                           (w/rotate! group :x x :y y :z z)
                           (apply-collision univ group hitboxes))
           :group/rescale (throw (ex-info "Unimplemented action"))
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
                              (str id)
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

           :cam/add (let [[id params] details
                          params (merge {:fov 60} params)
                          live (:live params)
                          params (update
                                  (dissoc params :live)
                                  :target
                                  (fn [[tk tv]]
                                    (case tk
                                      :item (get-in univ [:data :items tv :pos])
                                      :vec tv)))
                          c (w/create-camera! params)]
                      (swap! univ assoc-in [:items id] c)
                      (swap! univ assoc-in [:data :cameras id] params)
                      (w/add-camera! (:world @univ) c)
                      (when live (w/set-camera! (:world @univ) c)))
           :cam/translate (let [[id [x y z]] details
                                i (get-in @univ [:items id])]
                            (swap! univ update-in [:data :cameras id :pos] #(mapv + % [x y z]))
                            (w/rotate! i :x x :y y :z z))
           :cam/retarget (let [[id [type *value*]] details
                               i (get-in @univ [:items id])
                               value (case type
                                       :item (get-in @univ [:items *value* :pos])
                                       :vec *value*)]
                           (swap! univ assoc-in [:data :cameras id :target] [type *value*])
                           (w/set-camera-target! i value))
           :cam/refov (let [[id value] details
                            i (get-in @univ [:items id])]
                        (swap! univ assoc-in [:data :cameras id :fov] value)
                        (w/set-camera-fov! i value))
           :cam/set (let [[id value] details
                          i (get-in @univ [:items id])]
                      (swap! univ assoc-in [:data :cameras id :live] value)
                      (when value (w/set-camera! (:world @univ) i)))

           :light/add (let [[id params] details
                            n (count (get-in @univ [:data :lights :points]))
                            l (w/create-point-light! params)]
                        (swap! univ assoc-in [:data :lights :points id] params)
                        (w/set-point-light! (:world @univ) n l))
           :spot/add (let [[id params] details
                           n (count (get-in @univ [:data :lights :spots]))
                           l (w/create-spot-light! params)]
                       (swap! univ assoc-in [:data :lights :spots id] params)
                       (w/set-spot-light! (:world @univ) n l))
           :ambient/set (let [[params] details
                              a (w/create-ambient-light! params)]
                          (swap! univ assoc-in [:data :lights :ambient] params)
                          (w/set-ambient-light! (:world @univ) a))
           :sun/set (let [[params] details
                          s (w/create-sun-light! params)]
                      (swap! univ assoc-in [:data :lights :sun] params)
                      (w/set-sun! (:world @univ) s))))
       actions))))

(defn display-scene!
  [univ scene]
  (let [d (diff (:data @univ) (item-map scene))]
    ;;(println "DIFF:")
    ;;(println d)
    (display-diff! univ d)))

