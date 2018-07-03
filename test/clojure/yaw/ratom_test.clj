(ns clojure.yaw.ratom-test
  (:require  [yaw.core :as y]
             [clojure.test :as t]))

(defn lone-box
  "Single box in the center of the canvas."
  [pos zatom]
  [:scene [:item :test/box {:mesh :mesh/box :pos pos :rot [0 @zatom 0] :mat :white}]])

(defn scene
  [zatom]
  [:scene
   [:ambient {:color :blue :i 0.3}]
   [:sun {:color :red :i 0.8 :dir [-1 0 0]}]
   [:light ::light {:color :yellow :pos [1 1 -4]}]
   [lone-box [0 0 -5] zatom]])

;; The following test, if you evaluate it in bulk,
;; will display a cube at 40°
;; because the reactions happen immediately
;; however, re-evaluating the last line will make the cube turn.

(def +myctrl+ (y/start-universe!))
(def +rat+ (y/reactive-atom +myctrl+ 0))
;; Look at the println output (simulation of rendered scene)
(println "<<<<<FIRST RENDER>>>>>>")
(y/render! +myctrl+ [scene +rat+])

(println "<<<<<FIRST SWAP>>>>>>")
(swap! +rat+ (partial + 20))
(println "<<<<<SECOND SWAP>>>>>>")
(swap! +rat+ (partial + 20))

(doseq [x (range 360)]
  (swap! +rat+ (fn [_] x))
  (Thread/sleep 30))

(yaw.world/create-block! (:world @+myctrl+) :position [0 0 -5])
(.getPosition (get-in @+myctrl+ [:items ::main-cam]))
(.getFieldOfView (get-in @+myctrl+ [:items ::main-cam]))

(= (get-in @+myctrl+ [:items ::main-cam])
   (yaw.world/camera (:world @+myctrl+)))

(.getFieldOfView (yaw.world/camera (:world @+myctrl+)))
