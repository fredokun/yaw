(ns clojure.yaw.ratom-test
  (:require  [yaw.core :as y]
             [clojure.test :as t]))

(defn lone-box
  "Single box in the center of the canvas."
  [zatom]
  [:scene [:item :test/box {:mesh :mesh/box :pos [0 0 -5] :rot [0 @zatom 0] :mat :white}]])


;; The following test, if you evaluate it in bulk,
;; will display a cube at 40°
;; because the reactions happen immediately
;; however, re-evaluating the last line will make the cube turn.

(def +myctrl+ (y/start-universe!))
(def +rat+ (y/reactive-atom +myctrl+ 0))
;; Look at the println output (simulation of rendered scene)
(println "<<<<<FIRST RENDER>>>>>>")
(y/render! +myctrl+ [lone-box +rat+])
(println "<<<<<FIRST SWAP>>>>>>")
(swap! +rat+ (partial + 20))
(println "<<<<<SECOND SWAP>>>>>>")
(swap! +rat+ (partial + 20))
