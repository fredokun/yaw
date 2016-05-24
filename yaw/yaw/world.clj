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

;; Light Management----------------------------------------------------------------------------------

(defn setSunLight [world r g b intensity x y z]
      (LightManagement/setSunLight world r g b intensity x y z))

(defn setAmbiantLight [world r g b intensity]
      (LightManagement/setAmbiantLight world r g b intensity))

(defn addSpotLight [world r g b x y z intensity constantA linearAtt quadraticAtt xcone ycone zcone cutoffAngle number]
      (LightManagement/addSpotLight world r g b x y z  intensity constantA linearAtt quadraticAtt xcone ycone zcone cutoffAngle number))
      
(defn addPointLight [world r g b x y z intensity constantAtt linearAtt quadraticAtt number]
      (LightManagement/addPointLight world r g b x y z intensity constantAtt linearAtt quadraticAtt number))
      
(defn getSpotLightList [world]
      (.getSpotTable (.getSceneLight world)))
      
(defn getPointLightList [world]
      (.getPointTable (.getSceneLight world)))

(defn getIntensity [pointlight]
      (.getIntensity pointlight))
      
(defn setIntensity [pointlight intensity]
      (.setIntensity pointlight intensity))

(defn getConstantAtt [pointlight]
      (.getConstantAtt pointlight))

(defn setConstantAtt [pointlight att]
      (.setConstantAtt pointlight att))

(defn getLinearAtt [pointlight]
      (.getLinearAtt pointlight))

(defn setLinearAtt [pointlight att]
      (.setLinearAtt pointlight att))

(defn getQuadraticAtt [pointlight]
      (.getQuadraticAtt pointlight))

(defn setQuadraticAtt [pointlight att]
      (.setQuadraticAtt pointlight att))
      
(defn getConedir [spotlight]
       (let [pos (.getConedir spotlight)]
      (vec ((.x pos) (.y pos) (.z pos)))))

(defn setConedir [spotlight x y z]
      (.setConedir spotlight x y z))

(defn setConedirVector [spotlight vector]
      (.setConedir spotlight vector))

(defn getCutoffAngle [spotlight]
      (.getCutoffAngle spotlight))

(defn setCutoffAngle [spotlight float]
      (.setCutoffAngle spotlight float))

(defn setSpecularPower [world power]
      (.setSpecularPower (.getSceneLight world) power))

(defn getSpecularPower [world]
      (.getSpecularPower (.getSceneLight world)))

;; Camera Management--------------------------------------------------------------
(defn createCamera [world]
      (CameraManagement/addCamera world))

(defn addCamera [world camera]
      (CameraManagement/addCamera world camera))
      
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

(defn setOrientation [camera x y z]
      (.setOrientation camera x y z))

(defn setOrientationVector [camera vector]
      (.setOrientation camera vector))
      
;; Objects Management------------------------------------------------

(defn createGround [world r g b width length height]
      (ItemManagement/createGround world r g b width length height))

(defn createBlock [world r g b xL yL zL scale]
      (ItemManagement/createBlock world r g b xL yL zL scale))

(defn createHalfBlock [world r g b xL yL zL scale]
      (ItemManagement/createHalfBlock world r g b xL yL zL scale))

(defn createPyramid [world r g b xL yL zL scale]
      (ItemManagement/createPyramid world r g b xL yL zL scale))
      
(defn createTetraedre [world r g b scale]
      (ItemManagement/createTetraedreReg world r g b scale))

(defn createOctaedre [world r g b scale]
      (ItemManagement/createOctaedreReg world r g b scale))

(defn clone [world item]
      (ItemManagement/clone world item))
      
(defn removeItem [world item]
      (ItemManagement/removeItem world item))
      
(defn getListItems [world]
      (vec (.getListItems(.getSceneVertex world))))
      
(defn setScale [item scale]
      (.setScale item scale))

(defn getScale [item]
      (.getScale item))

(defn getReflectance [item]
      (.getReflectance item))
      
(defn setReflectance [item refl]
      (.setReflectance item refl))

(defn getColor [item]
 (let [pos (.getColor item)]
      (vector (.x pos) (.y pos) (.z pos))))
      
(defn setColor [item r g b]
      (.setColor item r g b))
      
(defn setColorVector [item vector]
      (.setColor item vector))
      
(defn setRotation [item x y z]
      (.setRotation x y z))
      
(defn setRotationVector [item vector]
      (.setRotation item vector))
      
(defn getRotation [item]
       (let [pos (.getRotation item)]
      (vector (.x pos) (.y pos) (.z pos))))

(defn revolveAroundVector [item center degX degY degZ]
      (.revolveAround item center degX degY degZ))

(defn revolveAround [item centerX centerY centerZ degX degY degZ]
      (.revolveAround item (new org.joml.Vector3f centerX centerY centerZ) degX degY degZ))

(defn repelByVector [item center dist]
      (.repelBy item center dist))

(defn repelBy [item centerX centerY centerZ dist]
      (.repelBy item (new org.joml.Vector3f centerX centerY centerZ) dist))
      
;; Group Item -----------------------------------------------
(defn getListGroup [world]
      (vec (.getListGroup world)))
      
(defn removeGroup [world group]
      (ItemManagement/deleteGroupe world group))
      
(defn createGroup [world]
      (ItemManagement/createGroup world))
      
(defn addItem [group item]
     (.add group item))

(defn removeFromGroup [group item]
     (.remove group item))

(defn seperate [group distance]
      (.separate group distance))
      
(defn multScale [group scale]
      (.multScale group scale))

(defn groupItems [group]
      (.getItems group))

;;Multiple usage [Camera, Item, groupe]-----------------------------
(defn rotate [item x y z]
      (.rotate item x y z))

;; Multiple usage [Camera, Item, Light, groupe]----------------------
(defn translate [item x y z]
      (.translate item x y z))
      
(defn setPosition [item x y z]
      (.setPosition item x y z))
      
(defn setPositionVector [item vector]
      (.setPosition item vector))
      
(defn getPosition [item]
      (let [pos (.getPosition item)]
      (vector (.x pos) (.y pos) (.z pos))))


;;CallBack Management
(defn registerCallback [world keyString function]
      (.registerCallback (.getCallback world) keyString function))

(defn clearCallback [world keyString]
      (.clearCallback (.getCallback world) keyString))

(defn clearFunctionOfKey [world keyString function]
      (.clearFunctionOfKey (.getCallback world) keyString function))

;;Tools
(defn createVector [r g b]
      (new org.joml.Vector3f r g b))

;; Save Tools
;; Each object is converted to a Clojure vector to be saved. The vector
;; contains generally in its first position the Class of the object, which is 
;; followed by the arguments to be given to the constructor during loading.

;; Save Tools for Items
(defn meshToVector [mesh]
	"Converts Meshes into a savable vector. Special Meshes not yet entirely supported."
	(println "Mesh to vector called with " (.getClass mesh))
	(if (= (.getClass mesh) gameEngine.meshs.Mesh) ;; If custom Mesh
		(let [material (.getMaterial mesh)
					color (.getColor material)]
			(vector (.getClass mesh) (vec (.getVertices mesh)) (.x color) (.y color) (.z color) (.getReflectance material) (vec (.getNormales mesh)) (vec (.getIndices mesh)) (.getWeight mesh)))
		(if (= (.getClass mesh) gameEngine.meshs.BlockMesh) ;; If BlockMesh
			(let [material (.getMaterial mesh)
					color (.getColor material)]
				(vector gameEngine.meshGenerator.BlockGenerator (.xLength mesh) (.yLength mesh) (.zLength mesh) (.x color) (.y color) (.z color) (.getReflectance material)))
			(let [material (.getMaterial mesh) ;; Else, treat as custom Mesh
						color (.getColor material)]
				(vector (.getClass mesh) (vec (.getVertices mesh)) (.x color) (.y color) (.z color) (.getReflectance material) (vec (.getNormales mesh)) (vec (.getIndices mesh)) (.getWeight mesh))))
  ))

(defn myItemToVector [myItem]
	"Converts a MyItem into a savable vector."
	(let [rotation (.getRotation myItem)
				translation (.getPosition myItem)]
	(vector (.getScale myItem) (.x rotation) (.y rotation) (.z rotation) (.x translation) (.y translation) (.z translation))
	))

(defn createMyItemVector [mesh itemListVec]
	"Saves all MyItem in the given List (as vector) in EDN format."
	(if (= (.length itemListVec) 1)
		(vector (meshToVector mesh) (myItemToVector (first itemListVec)))
		(conj (createMyItemVector mesh (pop itemListVec)) (myItemToVector (last itemListVec)))
	))

(defn saveMeshMap [meshMapVec]
	"Saves all items of the given meshMap (as vector) in EDN format."
	(if (= (.length meshMapVec) 0)
		(vector)
		(if (= (.length meshMapVec) 1)
			(vector (createMyItemVector (.getKey (first meshMapVec)) (vec (.getValue (first meshMapVec)))))
			(conj (saveMeshMap (pop meshMapVec)) (createMyItemVector (.getKey (last meshMapVec)) (vec (.getValue (last meshMapVec))))))
	))

;; Save Tools for Groups
(defn createGroupVector [groupItemListVec allItemsList]
	"Converts a GroupItem into a savable vector."
	(if (= (.length groupItemListVec) 0)
		(vector)
		(if (= (.length groupItemListVec) 1)
			(vector (.indexOf allItemsList (first groupItemListVec)))
			(conj (createGroupVector (pop groupItemListVec) allItemsList) (.indexOf allItemsList (last groupItemListVec))))
	))

(defn saveGroupsList [groupsListVec allItemsList]
	"Saves all groups of the given groupsList (as vector) in EDN format."
	(if (= (.length groupsListVec) 0)
		(vector)
		(if (= (.length groupsListVec) 1)
			(vector (createGroupVector (vec (.getItems (first groupsListVec))) allItemsList))
			(conj (saveGroupsList (pop groupsListVec) allItemsList) (createGroupVector (vec (.getItems (last groupsListVec))) allItemsList)))
	))

;; Save Tools for Cameras
(defn cameraToVector [cam]
	"Converts a Camera into a savable vector."
  (let [position (.getPosition cam)]
    (vector (.getClass cam) (.getFieldOfView cam) (.getzNear cam) (.getzFar cam) (.x position) (.y position) (.z position))
  ))
  
(defn createCameraVector [cameraListVec]
	"Creates a vector containing all Cameras, converted into savable vectors."
  (if (= (.length cameraListVec) 0)
    (vector)
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
	(println "Items")
	(let [meshMapVec (vec (.getMapMesh (.getSceneVertex world)))]
				(saveMeshMap meshMapVec)
		))

(defn saveGroups [world]
	"Returns a vector containing all groups of the given world in EDN format."
	(println "Groups")
	(saveGroupsList (vec (.getListGroup world)) (.getListItems (.getSceneVertex world)))
	)

(defn saveCameras [world]
;; 1) Charger LiveCamera 2) Vider liste 3) charger lsite
	"Returns a vector containing all cameras of the given world in EDN format."
	(println "Cameras")
	(vector (cameraToVector (.getCamera world)) (createCameraVector (vec (.getListCamera world)))
	))

(defn saveLights [world]
	"Returns a vector containing all lights of the given world in EDN format."
	(println "Lights")
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
		(vector (.specularPower sceneLight) ambiantVector sunVector pointLightVector spotLightVector)
	))
	
(defn saveFile [filename world]
	"Saves all items, cameras and lights of the given world into a file, in EDN format."
	(let [itemsVector (saveItems world)
				groupsVector (saveGroups world)
				camerasVector (saveCameras world)
				lightsVector (saveLights world)]
		(spit filename (with-out-str (pr (vector itemsVector groupsVector camerasVector lightsVector))))
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
	"Loads a Mesh. Does not support all subclasses of Mesh yet."
	(if (= (get mesh 0) gameEngine.meshs.Mesh)
		(gameEngine.meshs.Mesh. (float-array (get mesh 1)) (get mesh 2) (get mesh 3) (get mesh 4) (get mesh 5) (float-array (get mesh 6)) (int-array (get mesh 7)) (get mesh 8))
		(gameEngine.meshGenerator.BlockGenerator/generate (get mesh 1) (get mesh 2) (get mesh 3) (get mesh 4) (get mesh 5) (get mesh 6) (get mesh 7)))
)

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
	(if (= (.size meshMapVec) 0)
		nil
		(if (= (.size meshMapVec) 1)
			(loadMeshItems (first meshMapVec) world)
			(do
				(loadMeshItems (first meshMapVec) world)
				(loadMeshMap (rest meshMapVec) world)))
	))

;; Load Tools for Groups
(defn createNGroups [n world]
	"Creates n groups in the world."
	(println "createNGroups called with " n world)
	(if (= n 0)
		nil
		(do
			(ItemManagement/createGroup world)
			(createNGroups (- n 1) world))
	))


(defn addItemsToGroup [groupItemsList groupId world]
	"Adds items from the groupItemsList vector to the group corresponding to the given groupId."
	(println "addItemsToGroup called with " groupItemsList groupId world)
	(cond (> (.size groupItemsList) 0)
		(let [groupsList (.getListGroup world)
					itemsList (.getListItems (.getSceneVertex world))]
			(addItem (.get groupsList groupId) (.get itemsList (first groupItemsList)))
			(addItemsToGroup (rest groupItemsList) groupId world))
	))


(defn addItemsToGroups [groupsList curId world]
	"Adds items from the groupsList vector to corresponding groups."
	(println "addItemsToGroups called with " groupsList curId world)
	(cond (> (.length groupsList) 0)
		(do
			(addItemsToGroup (lazy-seq (last groupsList)) curId world)
			(addItemsToGroups (pop groupsList) (- curId 1) world))
	))

;; Load Tools for Cameras
(defn addCameras [cameraList world index]
	"Adds all cameras contained in the provided list to the World."
	(if (= cameraList '())
			nil
			(do
				(CameraManagement/addCamera world (ednToObject (first cameraList)))
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
	(loadMeshMap (lazy-seq loadedItems) world)
	)
	
(defn loadGroups [loadedGroups world]
	"Loads all groups from an EDN vector created with saveGroups."
	(println "loadGroups called with " loadedGroups world)
	(cond (> (.size loadedGroups) 0)
		(do
			(createNGroups (.size loadedGroups) world)
			(addItemsToGroups loadedGroups (- (.size loadedGroups) 1) world))
	))

(defn loadCameras [loadedCameras world]
	"Loads all Camera objects from an EDN vector created with saveCameras."
	(.setCamera world (ednToObject (get loadedCameras 0)))
	(.emptyListCamera world)
	(addCameras (lazy-seq (get loadedCameras 1)) world 0)
  )
		
(defn loadLights [loadedLights world]
	"Loads all lights from an EDN vector created with saveLights."
	(let [sceneLight (.getSceneLight world)]
		(set! (.specularPower sceneLight) (get loadedLights 0))
		(.setAmbiant sceneLight (ednToObject (get loadedLights 1)))
		(.setSun sceneLight (ednToObject (get loadedLights 2)))
		(addPointLights (lazy-seq (get loadedLights 3)) sceneLight 0)
		(addSpotLights (lazy-seq (get loadedLights 4)) sceneLight 0)
	))

(defn loadFile [filename world]
	"Reinitializes the world and loads items, cameras and lights contained in the given file."
		(let [loadedVector (read-string (slurp filename))]
		(.init world)
		(loadItems (get loadedVector 0) world)
		(loadGroups (get loadedVector 1) world)
		(loadCameras (get loadedVector 2) world)
		(loadLights (get loadedVector 3) world)
	))
