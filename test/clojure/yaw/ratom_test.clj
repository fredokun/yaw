(ns clojure.yaw.ratom-test
  (:require  [yaw.world :as w]
             [yaw.reaction :as r]
             [yaw.render :as render]
             [clojure.test :as t]))

(defn osc-lin
  [mnv mxv t period]
  (let [slope (/ (- mxv mnv) period)]
    (+ mnv (* t slope))))

(defn lone-box
  "Single box in the center of the canvas."
  [pos zatom]
  [:scene [:item :test/box {:mesh :mesh/box :pos pos :rot [0 @zatom 0] :mat :white}]])

(defn scene
  [zatom]
  [:scene
   [:ambient {:color :blue :i 0.3}]
   [:sun {:color :red :i 0.8 :dir [-1 0 0]}]
   [:light ::light {:color :yellow :pos [0.5 0 -4]}]
   [lone-box [0 0 -5] zatom]])


(defonce +myctrl+ (w/start-universe!))

(defonce +rot+ (r/reactive-atom +myctrl+ 0))

(defonce +oscillator+ (r/reactive-atom +myctrl+ {:v 0.0 :t 0.0}))

(defonce +update+ (r/create-update-ratom +myctrl+))

(render/render! +myctrl+ [scene +rot+])

(add-watch +update+ :yaw.reaction/propagation
           (fn [_ _ _ new]
             (swap! +oscillator+
                    (fn [{:keys [v t]}]
                      (let [tm (+ t new)]
                        {:v (osc-lin 0 360 tm 5) :t tm})))))

(add-watch +oscillator+ :yaw.reaction/propagation
           (fn [_ _ _ _ new]
             (swap! +rot+
                    (fn [_] (:v new)))))

