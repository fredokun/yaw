(ns yaw.world
  (:import [gameEngine World][gameEngine MyItem])
  (:gen-class))


(defn start-yaw
  []
  (let [world (.newInstance World)
        thread (future (.init world))]
    (atom {:world world :thread thread})
    ))


(defn register-callback
  [world callback]
  (.registerCallback (:world @world) callback))

(defn createCube [world]
      (.CreateCube world))

(defn translate [item x y z]
      (. translate item x y z))
(defn rotate [item x y z]
      (. rotate item x y z))
	


