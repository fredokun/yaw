(ns yaw.spec
  (:require [clojure.spec.alpha :as s]))

(s/def :coord/gen number?)
(s/def :coord/norm (s/and :coord/gen #(and (<= % 1) (>= % -1))))

(s/def :vector/norm (s/tuple :coord/norm :coord/norm :coord/norm))

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

;; Here's how you'd define a cube centered around [0 0 0]
;; (s/valid? ::geometry
;;           {:vertices {:a [1 1 1]
;;                       :b [1 -1 1]
;;                       :c [-1 -1 1]
;;                       :d [-1 1 1]
;;                       :e [1 1 -1]
;;                       :f [1 -1 -1]
;;                       :g [-1 -1 -1]
;;                       :h [-1 1 -1]}
;;            :tris [{:n [0 0 1]
;;                    :v [:a :c :b]}
;;                    {:n [0 0 1]
;;                     :v [:a :d :c]}
;;                    {:n [0 0 -1]
;;                     :v [:e :f :h]}
;;                    {:n [0 0 -1]
;;                     :v [:f :g :h]}
;;                    {:n [0 1 0]
;;                     :v [:a :e :h]}
;;                    {:n [0 1 0]
;;                     :v [:a :h :d]}
;;                    {:n [0 -1 0]
;;                     :v [:b :c :f]}
;;                    {:n [0 -1 0]
;;                     :v [:f :c :g]}
;;                    {:n [1 0 0]
;;                     :v [:a :f :e]}
;;                    {:n [1 0 0]
;;                     :v [:a :b :f]}
;;                    {:n [-1 0 0]
;;                     :v [:d :h :c]}
;;                    {:n [-1 0 0]
;;                     :v [:c :h :g]}]})

;; GLOBAL PARAMETERS
(s/def :params/pos :vector/gen)
(s/def :params/rot :vector/gen)
(s/def :params/scale :vector/gen)
(s/def :params/color (s/or :kw #{:red :blue :yellow :white :black} ;;more?
                           :rgb :vector/norm))
(s/def :params/dir :vector/norm)

(s/def :params/target (s/or :item qualified-keyword?
                            :vec :vector/gen))
(s/def :params/fov number?)

;; intensity
(s/def :params/i number?)

;; mesh
(s/def :params/mesh #{:mesh/box :mesh/cone :mesh/pyramid}) ;; exhaustive list?

;; CAMERAS
(s/def :scene/camera (s/cat :tag #{:camera}
                            :id-kw qualified-keyword?
                            :params (s/keys :req-un [:params/pos :params/target]
                                            :opt-un [:params/fov])))
;; (s/conform :scene/camera [:camera :test/cam1 {:pos [0 2 -3] :target [0 0 0] :fov 90}])
;; => {:tag :camera, :id-kw :test/cam1, :params {:pos [0 2 -3], :target [:vec [0 0 0]], :fov 90}}

;; (s/conform :scene/camera [:camera :test/cam2 {:pos [0 2 -3] :target :test/item}])
;; => {:tag :camera, :id-kw :test/cam2, :params {:pos [0 2 -3], :target [:item :test/item]}}

;; OBJECTS
(s/def :scene/item (s/cat :tag #{:item}
                          :id-kw qualified-keyword?
                          :params (s/keys :req-un [:params/mesh :params/pos]
                                          :opt-un [:params/rot :params/scale :params/color])))

;; (s/conform :scene/item [:item :test/item {:mesh :mesh/box :pos [0 0 0] :rot [20 0 20] :color :red}])
;; => {:tag :item, :id-kw :test/item, :params {:mesh :mesh/box, :pos [0 0 0], :rot [20 0 20], :color [:kw :red]}}

;; (s/conform :scene/item [:item :test/item2 {:mesh :mesh/cone :pos [0 2 0] :scale [1 2 1] :color [0 1 0.9]}])
;; => {:tag :item, :id-kw :test/item2, :params {:mesh :mesh/cone, :pos [0 2 0], :scale [1 2 1], :color [:rgb [0 1 0.9]]}}

;; LIGHTS
(s/def :scene/ambient-light (s/cat :tag #{:ambient}
                                   :params (s/keys :req-un [:params/color]
                                                   :opt-un [:params/i])))
(s/def :scene/sun-light (s/cat :tag #{:sun}
                               :params (s/keys :req-un [:params/dir :params/color]
                                               :opt-un [:params/i])))

(s/def :scene/spot-light (s/cat :tag #{:spot}
                                :id-kw qualified-keyword?
                                :params (s/keys :req-un [:params/pos :params/dir :params/color]
                                                :opt-un [:params/i])))
(s/def :scene/point-light (s/cat :tag #{:light}
                                 :id-kw qualified-keyword?
                                 :params (s/keys :req-un [:params/pos :params/color]
                                                 :opt-un [:params/i])))

;; (s/conform :scene/ambient-light [:ambient {:color :blue :i 0.3}])
;; => {:tag :ambient, :params {:color [:kw :blue], :i 0.3}}

;; (s/conform :scene/sun-light [:sun {:color [0 0 1] :dir [0 0 -1]}])
;; => {:tag :sun, :params {:color [:rgb [0 0 1]], :dir [0 0 -1]}}

;; (s/conform :scene/spot-light [:spot :test/spot1 {:color [1 0 0] :dir [-1 -1 0] :i 0.4 :pos [-2 4 0]}])
;; => {:tag :spot, :id-kw :test/spot1, :params {:color [:rgb [1 0 0]], :dir [-1 -1 0], :i 0.4, :pos [-2 4 0]}}

;; (s/conform :scene/point-light [:light :test/point1 {:color [0 1 0] :pos [0 0 2]}])
;; => {:tag :light, :id-kw :test/point1, :params {:color [:rgb [0 1 0]], :pos [0 0 2]}}

