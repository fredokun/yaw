(ns yaw.world
  (:import [gameEngine World][gameEngine MyItem][gameEngine.camera CameraGestion])
  (:gen-class))


(defn start-yaw
  []
  (let [world (.newInstance World)
        thread (future (.init world))]
	(.init world)
	(.start (Thread. world))
    (atom {:world world :thread thread})
    ))


(defn register-callback
  [world callback]
  (.registerCallback (:world @world) callback))

(defn createCube [world]
      (.CreateCube world))
      
(defn addCamera [world]
      (CameraGestion/addCamera world))
(defn setLiveCamera [world camera]
      (CameraGestion/setLiveCamera world camera))

(defn getCamera [world numero]
      (CameraGestion/getCamera world numero))

(defn translate [item x y z]
      (.translate item x y z))
(defn rotate [item x y z]
      (.rotate item x y z))
	


