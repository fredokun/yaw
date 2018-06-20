(ns yaw.spec
  (:require [clojure.spec.alpha :as s]))

(s/def :coord/gen number?)
(s/def :coord/norm (s/and :coord/gen #(and (<= % 1) (>= % -1))))
(s/def :coord/pnorm (s/and :coord/gen #(and (<= % 1) (>= % 0))))

(s/def :vector/norm (s/tuple :coord/norm :coord/norm :coord/norm))
(s/def :vector/pnorm (s/tuple :coord/pnorm :coord/pnorm :coord/pnorm))

(s/def :vector/gen (s/tuple :coord/gen :coord/gen :coord/gen))

;; maybe use `keyword?` instead?
(s/def ::vertices (s/map-of ident? :vector/gen))

(s/def :tri/n :vector/norm)
(s/def :tri/v (s/coll-of ident? :count 3))
(s/def ::tri (s/keys :req-un [:tri/n :tri/v]))

(s/def ::tris (s/coll-of ::tri))

(s/def ::geometry (s/keys :req-un [::vertices ::tris]))

;; Wrap the geometry in a `mesh` (`object`?) that will also
;; hold texture and material info
(s/def ::mesh (s/keys :req-un [::geometry]))

;; GLOBAL PARAMETERS
(s/def :params/pos :vector/gen)
(s/def :params/rot :vector/gen)
(s/def :params/scale :vector/gen)
(s/def :params/color (s/or :kw #{:red :green :blue :yellow :cyan :magenta :white :black } ;;more?
                           :rgb :vector/pnorm))
(s/def :params/texture string?)
(s/def :params/mat (s/or :color :params/color
                         :texture :params/texture))

(s/def :params/dir :vector/norm)

(s/def :params/target (s/or :item qualified-keyword?
                            :vec :vector/gen))
(s/def :params/fov number?)

;; intensity
(s/def :params/i #(or (pos? %) (zero? %)))

;; mesh
(s/def :params/mesh #{:mesh/box :mesh/cone :mesh/pyramid}) ;; exhaustive list?

;; CAMERAS
(s/def :scene/camera (s/cat :tag #{:camera}
                            :id-kw qualified-keyword?
                            :params (s/keys :req-un [:params/pos :params/target]
                                            :opt-un [:params/fov])))

;; OBJECTS
(s/def :scene/item (s/cat :tag #{:item}
                          :id-kw qualified-keyword?
                          :params (s/keys :req-un [:params/mesh :params/pos]
                                          :opt-un [:params/rot :params/scale :params/mat])))

;; LIGHTS
(s/def :scene/light-id (s/and number? #(< % 5) #(>= % 0)))
(s/def :scene/ambient-light (s/cat :tag #{:ambient}
                                   :params (s/keys :req-un [:params/color]
                                                   :opt-un [:params/i])))
(s/def :scene/sun-light (s/cat :tag #{:sun}
                               :params (s/keys :req-un [:params/dir :params/color]
                                               :opt-un [:params/i])))

(s/def :scene/spot-light (s/cat :tag #{:spot}
                                :id-kw qualified-keyword?
                                :id-n :scene/light-id
                                :params (s/keys :req-un [:params/pos :params/dir :params/color]
                                                :opt-un [:params/i])))

(s/def :scene/point-light (s/cat :tag #{:light}
                                 :id-kw qualified-keyword?
                                 :id-n :scene/light-id
                                 :params (s/keys :req-un [:params/pos :params/color]
                                                 :opt-un [:params/i])))

(s/def :scene/light (s/or :ambient :scene/ambient-light
                          :sun :scene/sun-light
                          :spot :scene/spot-light
                          :point :scene/point-light))

;; GROUPS
(s/def :scene/group (s/cat :tag #{:group}
                           :id-kw qualified-keyword?
                           :params (s/keys :req-un [:params/pos]
                                           :opt-un [:params/rot :params/scale])
                           :items (s/+ (s/spec :scene/item))))

;; SKYBOX
(s/def :scene/skybox (s/keys :req-un [:params/scale :params/color]))

;; GENERAL
(s/def :scene/object (s/or :camera :scene/camera
                           :item :scene/item
                           :light :scene/light
                           :group :scene/group))

;; SCENE
(s/def :scene/scene (s/cat :tag #{:scene}
                           :params (s/? (s/keys :opt-un [:scene/skybox :params/camera]))
                           :items (s/* (s/spec :scene/object))))
