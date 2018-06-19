(ns clojure.yaw.scene-test
  (:require [clojure.yaw.scene :as sut]
            [clojure.test :as t]))

;; As it is really awkward to test for openGL rendering,
;; these are not formal tests.
;; Going to have to "play test"

;; Empty scene
(sut/display-scene [:scene])

;; Red skybox
(sut/display-scene [:scene {:skybox {:color :red :scale [10 10 10]}}])

;; Lone white box at (0,0,-5)
(sut/display-scene
 [:scene
  [:item :test/box {:mesh :mesh/box :pos [0 0 -5] :mat :white}]])

;; Small cone at the center, cube to the upper-right, with a sun from the left
(sut/display-scene
 [:scene
  [:ambient {:color :white :i 0.1}]
  [:sun {:color :yellow :i 1 :dir [1 0 0]}]
  [:item :test/cone {:mesh :mesh/cone :mat :white :pos [0 0 -5] :rot [-90 0 0]}]
  [:item :test/box {:mesh :mesh/box :pos [1 1 -7] :rot [20 20 20] :mat :blue}]])

;; Two cameras, second one active, focusing on a cube
(sut/display-scene
 [:scene {:camera :test/cam2}
  [:item :test/focus {:mesh :mesh/box :pos [0 0 0] :mat :white}]
  [:camera :test/cam1 {:fov 90 :pos [-2 2 2] :target :test/focus}]
  [:camera :test/cam2 {:pos [2 2 2] :target :test/focus}]])
