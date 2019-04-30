(ns yaw.spec
  (:require [clojure.spec.alpha :as s]
            [yaw.util :as u]))
                                        ; NUMBERS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(s/def :yaw.spec.values.number/gen
  number?)
(s/def :yaw.spec.values.number/norm
  #(<= -1 % 1))
(s/def :yaw.spec.values.number/pnorm
  #(<= 0 % 1))

                                        ; VECTORS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(s/def :yaw.spec.values.vector/gen
  (s/tuple :yaw.spec.values.number/gen
           :yaw.spec.values.number/gen
           :yaw.spec.values.number/gen))
(s/def :yaw.spec.values.vector/norm
  (s/tuple :yaw.spec.values.number/norm
           :yaw.spec.values.number/norm
           :yaw.spec.values.number/norm))
(s/def :yaw.spec.values.vector/pnorm
  (s/tuple :yaw.spec.values.number/pnorm
           :yaw.spec.values.number/pnorm
           :yaw.spec.values.number/pnorm))

                                        ; MESHES ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(s/def :yaw.spec.meshes/vertices
  (s/map-of keyword? :yaw.spec.values.vector/gen))

(s/def :yaw.spec.meshes.tri/n
  :yaw.spec.values.vector/norm)
(s/def :yaw.spec.meshes.tri/v
  (s/tuple keyword? keyword? keyword?))
(s/def :yaw.spec.meshes/tri
  (s/keys :req-un [:yaw.spec.meshes.tri/n
                   :yaw.spec.meshes.tri/v]))

(s/def :yaw.spec.meshes/tris
  (s/coll-of :yaw.spec.meshes/tri))

(s/def :yaw.spec.meshes/geometry
  (s/keys :req-un [:yaw.spec.meshes/vertices :yaw.spec.meshes/tris]))

;; Wrap the geometry in a `mesh` (`object`?) that will also
;; hold texture and material info
;; --- todo --> add material / texture
(s/def :yaw.spec.meshes/mesh
  (s/keys :req-un [:yaw.spec.meshes/geometry]))

                                        ; SCENE ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

                                        ; PARAMS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/def :yaw.spec.values/pos :yaw.spec.values.vector/gen)
(s/def :yaw.spec.values/rot :yaw.spec.values.vector/gen)
(s/def :yaw.spec.values/scale number?)
(s/def :yaw.spec.values/dim :yaw.spec.values.vector/gen)
(s/def :yaw.spec.values/texture string?)
;;HitBox
(s/def :yaw.spec.values/length :yaw.spec.values.vector/gen)
(s/def :yaw.spec.values/group-id keyword?)
(s/def :yaw.spec.values/hitbox-id keyword?)
(s/def :yaw.spec.values/collision-handler fn?)

(s/def :yaw.spec.values/color
  (s/conformer
   (fn [v]
     (if (keyword? v)
       (get u/kw->rgb v :clojure.spec.alpha/invalid)
       (s/conform :yaw.spec.values.vector/pnorm v)))))

(s/def :yaw.spec.values/mat
  (s/or :color :yaw.spec.values/color
        :texture :yaw.spec.values/texture))

(s/def :yaw.spec.values/dir :yaw.spec.values.vector/norm)

(s/def :yaw.spec.values/target
  (s/or :item qualified-keyword?
        :vec :yaw.spec.values.vector/gen))


(s/def :yaw.spec.values/fov number?)

(s/def :yaw.spec.values/live boolean?)

;; intensity
(s/def :yaw.spec.values/i #(<= 0 %))

;; mesh
(s/def :yaw.spec.values/mesh qualified-keyword?) ;; exhaustive list?

                                        ; CAMERAS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/def :yaw.spec.scene/camera
  (s/cat :tag #{:camera}
         :id-kw qualified-keyword?
         :params (s/keys :req-un [:yaw.spec.values/pos
                                  :yaw.spec.values/target]
                         :opt-un [:yaw.spec.values/fov
                                  :yaw.spec.values/live])))

                                        ; ITEMS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/def :yaw.spec.scene/item
  (s/cat :tag #{:item}
         :id-kw qualified-keyword?
         :params (s/keys :req-un [:yaw.spec.values/mesh
                                  :yaw.spec.values/pos]
                         :opt-un [:yaw.spec.values/rot
                                  :yaw.spec.values/scale
                                  :yaw.spec.values/mat])))

                                        ; LIGHTS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/def :yaw.spec.scene.light/id
  (s/and number? #(< % 5) #(>= % 0)))
(s/def :yaw.spec.scene.light/ambient
  (s/cat :tag #{:ambient}
         :params (s/keys :req-un [:yaw.spec.values/color]
                         :opt-un [:yaw.spec.values/i])))
(s/def :yaw.spec.scene.light/sun
  (s/cat :tag #{:sun}
         :params (s/keys :req-un [:yaw.spec.values/dir
                                  :yaw.spec.values/color]
                         :opt-un [:yaw.spec.values/i])))

(s/def :yaw.spec.scene.light/spot
  (s/cat :tag #{:spot}
         :id-kw qualified-keyword?
         :params (s/keys :req-un [:yaw.spec.values/pos
                                  :yaw.spec.values/dir
                                  :yaw.spec.values/color]
                         :opt-un [:yaw.spec.values/i])))

(s/def :yaw.spec.scene.light/point
  (s/cat :tag #{:light}
         :id-kw qualified-keyword?
         :params (s/keys :req-un [:yaw.spec.values/pos
                                  :yaw.spec.values/color]
                         :opt-un [:yaw.spec.values/i])))

(s/def :yaw.spec.scene/light
  (s/or :ambient :yaw.spec.scene.light/ambient
        :sun :yaw.spec.scene.light/sun
        :spot :yaw.spec.scene.light/spot
        :point :yaw.spec.scene.light/point))

                                        ; HITBOXES ;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(s/def :yaw.spec.scene/collision
  (s/keys :req-un [:yaw.spec.values/group-id
                   :yaw.spec.values/hitbox-id
                   :yaw.spec.values/collision-handler]))

(s/def :yaw.spec.scene/hitbox
  (s/cat :tag #{:hitbox}
         :id-kw qualified-keyword?
         :params (s/keys :req-un [:yaw.spec.values/pos
                                  :yaw.spec.values/length]
                         :opt-un [:yaw.spec.values/rot
                                  :yaw.spec.values/scale])
         :on-collision (s/? (s/spec (s/+ (s/spec :yaw.spec.scene/collision))))))

                                        ; GROUPS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/def :yaw.spec.scene/group
  (s/cat :tag #{:group}
         :id-kw qualified-keyword?
         :params (s/keys :req-un [:yaw.spec.values/pos]
                         :opt-un [:yaw.spec.values/rot
                                  :yaw.spec.values/scale])
         :items (s/+ (s/spec :yaw.spec.scene/item))
         :hitboxes (s/* (s/spec :yaw.spec.scene/hitbox))))

                                        ; SKYBOX ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/def :yaw.spec.scene/skybox
  (s/keys :req-un [:yaw.spec.values/dim
                   :yaw.spec.values/color]))

                                        ; OBJECT ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/def :yaw.spec.scene/object
  (s/or :camera :yaw.spec.scene/camera
        :item :yaw.spec.scene/item
        :light :yaw.spec.scene/light
        :group :yaw.spec.scene/group))

                                        ; SCENE ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/def :yaw.spec.scene/scene
  (s/cat
   ;; :tag #{:scene}
   ;;       :params (s/? (s/keys :opt-un [:yaw.spec.scene/skybox
   ;;                                     :yaw.spec.values/camera]))
   :content (s/* (s/spec (s/or :content :yaw.spec.scene/object
                               :separator #{:scene})))))

                                        ; INTERNAL REPRESENTATION ;;;;;;;;;;;;;

;; /!\ === === WARNING === === /!\
;; These specs exist only as an instructive declaration of the contents
;; of the structure.
;; Conforming out of those will get awfully verbose results with lots of redundancy

(s/def :yaw.spec.values.conformed/mat
  (s/or :color (s/cat :tag #{:color} :value :yaw.spec.values.vector/pnorm)
        :texture (s/cat :tag #{:texture} :value string?)))

(s/def :yaw.spec.values.conformed/target
  (s/or :item (s/cat :tag #{:item} :value qualified-keyword?)
        :vec (s/cat :tag #{:vec} :value :yaw.spec.values.vector/gen)))

(s/def :yaw.scene.internal/item-value
  (s/keys :req-un [:yaw.spec.values/pos
                   :yaw.spec.values/mesh]
          :opt-un [:yaw.spec.values/rot
                   :yaw.spec.values.conformed/mat
                   :yaw.spec.values/scale]))

(s/def :yaw.scene.internal/items
  (s/map-of qualified-keyword?
            :yaw.scene.internal/item-value))

(s/def :yaw.scene.internal/camera-value
  (s/keys :req-un [:yaw.spec.values/pos
                   :yaw.spec.values.conformed/target]
          :opt-un [:yaw.spec.values/fov]))

(s/def :yaw.scene.internal/cameras
  (s/map-of qualified-keyword?
            :yaw.scene.internal/camera-value))

(s/def :yaw.scene.internal.lights/ambient
  (s/nilable (s/keys :req-un [:yaw.spec.values/color]
                     :opt-un [:yaw.spec.values/i])))

(s/def :yaw.scene.internal.lights/sun
  (s/nilable (s/keys :req-un [:yaw.spec.values/color
                              :yaw.spec.values/dir]
                     :opt-un [:yaw.spec.values/i])))

(s/def :yaw.scene.internal.lights/point
  (s/keys :req-un [:yaw.spec.values/color
                   :yaw.spec.values/pos]
          :opt-un [ :yaw.spec.values/i]))

(s/def :yaw.scene.internal.lights/points
  (s/map-of qualified-keyword?
            :yaw.scene.internal.lights/point))

(s/def :yaw.scene.internal.lights/spot
  (s/keys :req-un [:yaw.spec.values/color :yaw.spec.values/pos :yaw.spec.values/dir]
          :opt-un [ :yaw.spec.values/i]))

(s/def :yaw.scene.internal.lights/spots
  (s/map-of qualified-keyword?
            :yaw.scene.internal.lights/spot))

(s/def :yaw.scene.internal/lights
  (s/keys :req-un [:yaw.scene.internal.lights/ambient
                   :yaw.scene.internal.lights/sun
                   :yaw.scene.internal.lights/points
                   :yaw.scene.internal.lights/spots]))

; Groups are still an uncertain idea
(s/def :yaw.scene.internal/scene
  (s/keys :req-un [:yaw.scene.internal/items
                   :yaw.scene.internal/lights
                   :yaw.scene.internal/cameras]))
