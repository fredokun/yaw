(ns yaw.spec
  (:require [clojure.spec.alpha :as s]))

(s/def :coord/gen number?)
(s/def :coord/norm (s/and :coord/gen #(and (<= % 1) (>= % -1))))

(s/def :vector/norm (s/tuple :coord/norm :coord/norm :coord/norm))

(s/def :vector/gen (s/tuple :coord/gen :coord/gen :coord/gen))

;; maybe use `keyword?` instead?
(s/def ::vertices (s/map-of ident? :vector/gen))

(s/def :face/n :vector/norm)
(s/def :face/v (s/coll-of ident?))
(s/def ::face (s/keys :req-un [:face/n :face/v]))

(s/def ::faces (s/map-of ident? ::face))

(s/def ::geometry (s/keys :req-un [::vertices ::faces]))

;; Wrap the geometry in a `mesh` (`object`?) that will also
;; hold texture and material info
(s/def ::mesh (s/keys :req-un [::geometry]))

;; Here's how you'd define a cube centered around [0 0 0]
;; One issue with this is the splitting into triangles.
;; We don't want to bother the user with triangles
;;   this allows the abstraction from triangles,
;;   but then we'd need some way to split a quand into 2 tri,
;;   and have other splitting methods for other faces too.
;; (s/valid? ::geometry
;;           {:vertices {:a [1 1 1]
;;                       :b [1 -1 1]
;;                       :c [-1 -1 1]
;;                       :d [-1 1 1]
;;                       :e [1 1 -1]
;;                       :f [1 -1 -1]
;;                       :g [-1 -1 -1]
;;                       :h [-1 1 -1]}
;;            :faces {:A {:n [0 0 1]
;;                        :v [:a :b :c :d]}
;;                    :B {:n [0 0 -1]
;;                        :v [:e :f :g :h]}
;;                    :C {:n [0 1 0]
;;                        :v [:a :e :h :d]}
;;                    :D {:n [0 -1 0]
;;                        :v [:b :f :g :c]}
;;                    :E {:n [1 0 0]
;;                        :v [:a :e :f :b]}
;;                    :F {:n [-1 0 0]
;;                        :v [:d :c :g :h]}}})
