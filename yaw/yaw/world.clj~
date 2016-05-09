(ns yaw.world
  (:import [gameEngine World][gameEngine MyItem][gameEngine.camera CameraGestion][gameEngine.light LightManagement][gameEngine Camera])
  (:gen-class))


(defn start-yaw
  []
  (let [world (.newInstance World)
        thread (future (.init world))]
	(.init world)
	(.start (Thread. world))
    (atom {:world world :thread thread})
    ))

;; Calleback Management
(defn register-callback
  [world callback]
  (.registerCallback (:world @world) callback))

;; Light Management

(defn setSunLight [world r g b intensity x y z]
      (LightManagement/setSunLight world r g b intensity x y z))

(defn setAmbiantLight [world r g b intensity]
      (LightManagement/setAmbiantLight world r g b intensity))

(defn addSpotLight [world r g b x y z  intensity constantA linearAtt quadraticAtt xcone ycone zcone cutoffAngle number]
      (LightManagement/addSpotLight world r g b x y z  intensity constantA linearAtt quadraticAtt xcone ycone zcone cutoffAngle number))
      
(defn addPointLight [world r g b x y z intensity constantAtt linearAtt quadraticAtt number]
      (LightManagement/addPointLight world r g b x y z intensity constantAtt linearAtt quadraticAtt number))


;; Camera Management
(defn addCamera [world]
      (CameraGestion/addCamera world))
      
(defn setLiveCamera [world camera]
      (CameraGestion/setLiveCamera world camera))

(defn getCamera [world numero]
      (CameraGestion/getCamera world numero))

(defn getLiveCamera [world]
      (CameraGestion/getLiveCamera world))

(defn removeCameraNumber [world numero]
      (CameraGestion/removeCamera world numero))

(defn removeCamera [world camera]
      (CameraGestion/removeCamera world camera))

(defn setPositionCamera [camera x y z]
      (.setPosition camera (new org.joml.Vector3f x y z)))
      
;; Objects Manangement

(defn createCube [world]
      (.CreateCube world))

(defn translate [item x y z]
      (.translate item x y z))
(defn rotate [item x y z]
      (.rotate item x y z))
	


