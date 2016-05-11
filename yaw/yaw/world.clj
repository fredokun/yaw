(ns yaw.world
  (:import [gameEngine World][gameEngine.items MyItem][gameEngine.camera CameraManagement]
  [gameEngine.light LightManagement][gameEngine.camera Camera][gameEngine.items ItemManagement])
  (:gen-class))


(defn start-yaw
  []
  (let [world (.newInstance World)
        thread (future (.init world))]
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
      (CameraManagement/addCamera world))
      
(defn setLiveCamera [world camera]
      (CameraManagement/setLiveCamera world camera))

(defn getCamera [world numero]
      (CameraManagement/getCamera world numero))

(defn getListCamera [world]
      (vec (.getListCamera world)))

(defn getLiveCamera [world]
      (CameraManagement/getLiveCamera world))

(defn removeCameraNumber [world numero]
      (CameraManagement/removeCamera world numero))

(defn removeCamera [world camera]
      (CameraManagement/removeCamera world camera))

(defn setPositionCamera [camera x y z]
      (.setPosition camera (new org.joml.Vector3f x y z)))
      
;; Objects Management
(defn createBlock [world r g b xL yL zL scale]
      (ItemManagement/createBlock world r g b xL yL zL scale))

(defn createHalfBlock [world r g b xL yL zL scale]
      (ItemManagement/createHalfBlock world r g b xL yL zL scale))

(defn createPyramid [world r g b xL yL zL scale]
      (ItemManagement/createPyramid world r g b xL yL zL scale))
      
(defn removeItem [world item]
      (ItemManagement/removeItem world item))
(defn getListItems [world]
      (vec (.getListItems(.getSceneVertex world))))
(defn translate [item x y z]
      (.translate item x y z))
(defn rotate [item x y z]
      (.rotate item x y z))
(defn setPosition [item x y z]
      (.setPosition item x y z))
(defn setColor [item r g b]
      (.setColor item r g b))

;;CallBack Management
(defn registerCallback [world keyString function]
      (.registerCallback (.getCallback world) keyString function))

;; Save Tools
;; Each object is converted to a Clojure vector to be saved. The vector
;; contains generally in its first position the Class of the object, which is 
;; followed by the arguments to be given to the constructor during loading.

;; Save Tools for Items
(defn meshToVector [mesh]
	"Converts Meshes into a savable vector. Predefined Meshes not yet supported."
	(if (= (.getClass mesh) gameEngine.Mesh)
		(let [material (.getMaterial mesh)
					color (.getColor material)]
					(vector (.getClass mesh) (vec (.getVertices mesh)) (.x color) (.y color) (.z color) (.getReflectance material) (vec (.getNormales mesh)) (vec (.getIndices mesh)) (.getWeight mesh)))
		(vector (.getClass mesh))
	))

(defn myItemToVector [myItem]
	"Converts a MyItem into a savable vector."
	(let [rotation (.getRotation myItem)
				translation (.getTranslation myItem)]
	(vector (.getScale myItem) (.x rotation) (.y rotation) (.z rotation) (.x translation) (.y translation) (.z translation))))

(defn createMyItemVector [itemListVec]
	"Saves all MyItem in the given List (as vector) in EDN format."
	(if (= (.length itemListVec) 1)
		(vector (meshToVector (.getAppearance (first itemListVec))) (myItemToVector (first itemListVec)))
		(conj (createMyItemVector (pop itemListVec)) (myItemToVector (last itemListVec)))
	))

(defn saveMeshMap [meshMapVec]
	"Saves all items of the given meshMap (as vector) in EDN format."
	(if (= (.length meshMapVec) 0)
		(vector)
		(if (= (.length meshMapVec) 1)
			(vector (createMyItemVector (vec (.getValue (first meshMapVec)))))
			(conj (saveMeshMap (pop meshMapVec)) (createMyItemVector (vec (.getValue (first meshMapVec))))))
	))

;; Save Tools for Cameras
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

;; Save Tools for Lights
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

;; Save Functions
(defn saveItems [world]
	"Returns a vector containing all items of the given world in EDN format."
	(let [sceneVertex (.getSceneVertex world)
				meshMapVec (vec (.getMapMesh sceneVertex))]
				(saveMeshMap meshMapVec)
		))

(defn saveCameras [world]
	"Returns a vector containing all cameras of the given world in EDN format."
	(createCameraVector (vec (.getListCamera world)))
	)

(defn saveLights [world]
	"Returns a vector containing all lights of the given world in EDN format."
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
		(vector ambiantVector sunVector pointLightVector spotLightVector)
	))
	
(defn saveFile [filename world]
	"Saves all items, cameras and lights of the given world into a file, in EDN format."
	(let [itemsVector (saveItems world)
				camerasVector (saveCameras world)
				lightsVector (saveLights world)]
		(spit filename (with-out-str (pr (vector itemsVector camerasVector lightsVector))))
	))

;; Load Tools
(defn ednToObject [ednData]
	"Loads an object previously saved in a vector."
	(if (vector? ednData)
    (let [c (resolve (first ednData))
    					a (ednToObject (rest (lazy-seq ednData)))]
        (clojure.lang.Reflector/invokeConstructor c (into-array a)))
		(if (= ednData '())
				'()
				(if (vector? (first ednData))
					(conj (ednToObject (rest ednData)) (into-array (first ednData)))
					(conj (ednToObject (rest ednData)) (first ednData))
        )
    )
  ))

;; Load Tools for Items
(defn loadMesh [mesh]
	"Loads a Mesh. Does not support subclasses of Mesh yet."
	(let [loadedMesh (gameEngine.Mesh. (float-array (get mesh 1)) (get mesh 2) (get mesh 3) (get mesh 4) (get mesh 5) (float-array (get mesh 6)) (int-array (get mesh 7)) (get mesh 8))]
		loadedMesh
))

(defn loadGenericItems [mesh items world]
	"Loads GenericItems, associating them with their Mesh, and adds them to the sceneVertex."
	(let [item (first items)
				sceneVertex (.getSceneVertex world)]
		(if (= (.size items) 1)
			 (.add sceneVertex (gameEngine.items.GenericItem. mesh (get item 0) (get item 1) (get item 2) (get item 3) (get item 4) (get item 5) (get item 6)))
			(do
				(.add sceneVertex (gameEngine.items.GenericItem. mesh (get item 0) (get item 1) (get item 2) (get item 3) (get item 4) (get item 5) (get item 6)))
				(loadGenericItems mesh (rest items) world)))
	))

(defn loadMeshItems [meshItems world]
	"Loads items sharing one Mesh."
	(let [mesh (loadMesh (first meshItems))]
		(loadGenericItems mesh (rest meshItems) world)))
  
(defn loadMeshMap [meshMapVec world]
	"Loads all items of the given meshMap (as vector)."
	(if (= (.length meshMapVec) 0)
		nil
		(if (= (.length meshMapVec) 1)
			(loadMeshItems (first meshMapVec) world)
			(do
				(loadMeshItems (last meshMapVec) world)
				(loadMeshMap (pop meshMapVec) world)))
	))

;; Load Tools for Cameras
(defn addCameras [cameraList world index]
	"Adds all cameras contained in the provided list to the World."
	(if (= cameraList '())
			nil
			(do
				(.setCamera world index (ednToObject (first cameraList)))
				(addCameras (rest cameraList) world (+ index 1)))
  ))

;; Load Tools for Lights
(defn addPointLights [pointLights sceneLight index]
	"Adds all pointLights contained in the provided list to the SceneLight."
	(if (= pointLights '())
			nil
			(do
				(.setPointTable sceneLight (ednToObject (first pointLights)) index)
				(addPointLights (rest pointLights) sceneLight (+ index 1)))
  ))
  
(defn addSpotLights [spotLights sceneLight index]
	"Adds all spotLights contained in the provided list to the SceneLight."
	(if (= spotLights '())
			nil
			(do
				(.setSpotTable sceneLight (ednToObject (first spotLights)) index)
				(addSpotLights (rest spotLights) sceneLight (+ index 1)))
  ))

;; Load Functions
(defn loadItems [loadedItems world]
	"Loads all MyItem objects from an EDN vector created with saveItems."
	(loadMeshMap loadedItems world)
	)

(defn loadCameras [loadedCameras world]
	"Loads all Camera objects from an EDN vector created with saveCameras."
	(addCameras (lazy-seq loadedCameras) world 0)
  )
		
(defn loadLights [loadedLights world]
	"Loads all lights from an EDN vector created with saveLights."
	(let [sceneLight (.getSceneLight world)]
		(.setAmbiant sceneLight (ednToObject (get loadedLights 0)))
		(.setSun sceneLight (ednToObject (get loadedLights 1)))
		(addPointLights (lazy-seq (get loadedLights 2)) sceneLight 0)
		(addSpotLights (lazy-seq (get loadedLights 3)) sceneLight 0)
	))
	
(defn loadFile [filename world]
	"Reinitializes the world and loads items, cameras and lights contained in the given file."
		(let [loadedVector (read-string (slurp filename))]
		(.init world)
		(loadItems (get loadedVector 0) world)
		(loadCameras (get loadedVector 1) world)
		(loadLights (get loadedVector 2) world)
	))
