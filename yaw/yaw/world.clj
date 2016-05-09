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

;; Save Tools
;; Each object is converted to a Clojure vector to be saved. The vector
;; contains in its first position the Class of the object, which is followed
;; by the arguments to be given to the constructor during loading.
(defn pointLightToVector [pl]
	"Converts a PointLight into a savable vector."
	(let [color (.getColor pl)
				position (.getPosition pl)]
		(vector (.getClass pl) (.x color) (.y color) (.z color) (.x position) (.y position) (.z position) (.getIntensity pl) (.getConstantAtt pl) (.getLinearAtt pl) (.getQuadraticAtt pl))
  ))
	
(defn createPointLightVector [pointLightArrVec]
	"Creates a vector containing all PointLights, converted into savable vectors."
	(if (= (.length pointLightArrVec) 1)
		(vector (pointLightToVector (first pointLightArrVec)))
		(conj (createPointLightVector (pop pointLightArrVec)) (pointLightToVector (last pointLightArrVec)))
	))

(defn spotLightToVector [sl]
	"Converts a SpotLight into a savable vector."
	(let [color (.getColor sl)
				position (.getPosition sl)
				conedir (.getConedir sl)]
		(vector (.getClass sl) (.x color) (.y color) (.z color) (.x position) (.y position) (.z position) (.getIntensity sl) (.getConstantAtt sl) (.getLinearAtt sl) (.getQuadraticAtt sl) (.x conedir) (.y conedir) (.z conedir) (.getCutoffAngle sl))
  ))

(defn createSpotLightVector [spotLightArrVec]
	"Creates a vector containing all SpotLights, converted into savable vectors."
	(if (= (.length spotLightArrVec) 1)
		(vector (spotLightToVector (first spotLightArrVec)))
		(conj (createSpotLightVector (pop spotLightArrVec)) (spotLightToVector (last spotLightArrVec)))
	))

(defn cameraToVector [cam]
	"Converts a Camera into a savable vector."
  (let [position (.getPosition cam)]
    (vector (.getClass cam) (.getFieldOfView cam) (.getzNear cam) (.getzFar cam) (.x position) (.y position) (.z position))
  ))
  
(defn createCameraVector [cameraListVec]
	"Creates a vector containing all Cameras, converted into savable vectors."
  (if (= (.length cameraListVec) 1)
    (vector (cameraToVector (first cameraListVec)))
    (conj (createCameraVector (pop cameraListVec)) (cameraToVector (last cameraListVec)))
  ))

;; Save
(defn saveCameras [filename world]
	"Saves all cameras of the given world in EDN format."
	(let
		[cameraVector (createCameraVector (vec (.getListCamera world)))]
		(spit filename (with-out-str (pr cameraVector)))
	))

(defn saveLights [filename world]
	"Saves all lights of the given world in EDN format."
	(let [sceneLight (.getSceneLight world)
				ambiantLight (.getAmbiantLight sceneLight)
				aColor (.getColor ambiantLight)
				sun (.getSun sceneLight)
				sColor (.getColor sun)
				sDirection (.getDirection sun)
				;; Representation of the ambiantLight as a Clojure vector
			 	ambiantVector [(.getClass ambiantLight) (.x aColor) (.y aColor) (.z aColor) (.getIntensity ambiantLight)]
			 	;;Representation of the Sun as a Clojure vector
			 	sunVector [(.getClass sun) (.x sColor) (.y sColor) (.z sColor) (.getIntensity sun) (.x sDirection) (.y sDirection) (.z sDirection)]	
			 	;; Representation of PointLights as a Clojure vector
			 	pointLightVector (createPointLightVector (vec (.getPointTable sceneLight)))
			 	;; Representation of spotLights as a Clojure vector
			 	spotLightVector (createSpotLightVector (vec (.getSpotTable sceneLight)))
		 	]
		;; Save to file
		(spit filename (with-out-str (pr (vector ambiantVector sunVector pointLightVector spotLightVector))))
	))

;; Load Tools
(defn ednToObject [ednData]
	"Loads an object previously saved in a vector."
	(if (vector? ednData)
    (let [c (resolve (first ednData))
    					a (ednToObject (rest (lazy-seq ednData)))]
    		(println "Creating object of class: " c " with args: " a)
        (clojure.lang.Reflector/invokeConstructor c (into-array a)))
		(if (= ednData '())
				'()
				(if (vector? (first ednData))
					(conj (ednToObject (rest ednData)) (ednToObject (first ednData)))
					(conj (ednToObject (rest ednData)) (first ednData))
        )
    )
  ))
						
		
(defn loadEdnVect [ednVect]
	"Loads all objects from all vectors contained in the provided vector. Only for demonstration purposes."
	(if (= (.length ednVect) 0)
			nil
			(if (= (.length ednVect) 1)
				(println "Loaded object: " (.toString (ednToObject (first ednVect))))
				(do
					(println "Loaded object: " (.toString (ednToObject (last ednVect))))
					(loadEdnVect (pop ednVect)))
      )
  ))
  
(defn loadEdnList [ednList]
	"Loads all objects from all vectors contained in the provided list. Only for demonstration purposes."
	(if (= ednList '())
			nil
			(do
				(println "Loaded object: " (.toString (ednToObject (first ednList))))
				(loadEdnList (rest ednList)))
  ))

;; Load
(defn loadCameras [filename]
	"Loads all Camera objects from a file created with saveCameras. Only for demonstration purposes."
	(let [loadedCameras (read-string (slurp filename))]
		(loadEdnList (lazy-seq loadedCameras))
  ))
		
(defn loadLights [filename]
	"Loads all lights from a file created with saveLights. Only for demonstration purposes."
	(let [loadedLights (read-string (slurp filename))]
		(println "Ambiant: " (.toString (ednToObject (get loadedLights 0))))
		(println "Sun: " (.toString (ednToObject (get loadedLights 1))))
		(println "PointLights: ")
		(loadEdnList (lazy-seq (get loadedLights 2)))
		(println "SpotLights: ")
		(loadEdnList (lazy-seq (get loadedLights 3)))
	))

