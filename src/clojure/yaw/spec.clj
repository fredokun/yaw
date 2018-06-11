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
