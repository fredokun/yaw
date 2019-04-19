(ns yaw.world
  (:import (yaw.engine.meshs MeshBuilder)
           (yaw.engine.items Item)
           (yaw.engine World
                       InputCallback)
           (yaw.engine.light AmbientLight DirectionalLight PointLight SpotLight)
           (yaw.engine.camera Camera))
  (:require [yaw.mesh]))
  ;;(gen-class)

(def empty-item-map
  {:cameras {}
   :lights {:ambient nil :sun nil :points {} :spots {}}
   :groups {}
   :items {}})

;;UTILS------------------------------------------------------------------
(defn flat-map "flatten 'map'" [m]
  (flatten (conj (vals m))))

(defn start-universe!
  "Start an empty yaw universe."
  [& {:keys [width height x y vsync]
      :or   {x      0
             y      0
             width  800
             height 600
             vsync true}}]
  (let [world (World. x y width height vsync)
        thread (future world)]
    (.start (Thread. world))
    (atom {:world world :thread thread
           :meshes {:mesh/box (yaw.mesh/box-geometry)
                    :mesh/cone (yaw.mesh/cone-geometry)
                    :mesh/pyramid (yaw.mesh/pyramid-geometry)}
           :data empty-item-map
           :items {}
           :components {}})))

(defn register-mesh!
  "Given a universe, a keyword id, and mesh data, associates the id to the data in the universe atom"
  [univ id mesh]
  (swap! univ assoc-in [:meshes id] mesh))

;;CALLBACKS---------------------------------------------------------------

(defn register-input-callback! 
  "Register the input callback for low-level keyboard management."
  [world callback]
  (let [cb (reify InputCallback
             (sendKey [this key scancode action mode]
               ;; (println "key event! key=" key " scancode=" scancode "action=" action "mode=" mode)
               (callback key scancode action mode)))]
    ;; (println "[input callback] cb=" cb "world=" world)
    (.registerInputCallback world cb)))


;; TODO
;; (defn unregister-input-callback!
;;   "Unregister the current input callback (if any)"
;;   [world]
;;   )

;;Since we completely destroy the old architecture we will migrate the basic method to this module
;;README ONLY USE WORLD IT is A FACADE, no DIRECT USE OF MANAGEMENT/BUILDER TOOLS

;; MeshOld Functions------------------------------------------------
;; TODO: delete this when `create-simple-mesh` is stable (and we can handle texture)

(defn create-mesh!
  "Create an item in the `world` with the  specified id, position, mesh"
  [world & {:keys [vertices text-coord normals faces weight rgb texture-name]
            :or   {texture-name ""
                   rgb          [1 1 1]
                   weight       1
                   vertices     {:v0 [-1 1 1] :v1 [-1 -1 1] :v2 [1 -1 1] :v3 [1 1 1]
                                 :v4 [-1 1 -1] :v5 [1 1 -1] :v6 [-1 -1 -1] :v7 [1 -1 -1]}
                   normals      {:front  [0 0 1 0 0 1 0 0 1 0 0 1]
                                 :top    [0 1 0 0 1 0 0 1 0 0 1 0]
                                 :back   [0 0 -1 0 0 -1 0 0 -1 0 0 -1]
                                 :bottom [0 -1 0 0 -1 0 0 -1 0 0 -1 0]
                                 :left   [-1 0 0 -1 0 0 -1 0 0 -1 0 0]
                                 :right  [1 0 0 1 0 0 1 0 0 1 0 0]}
                   faces        {:front  [0 1 3 3 1 2]
                                 :top    [4 0 3 5 4 3]
                                 :back   [7 6 4 7 4 5]
                                 :bottom [2 1 6 2 6 7]
                                 :left   [6 1 0 6 0 4]
                                 :right  [3 2 7 5 3 7]}
                   text-coord   {:front  [0 0 0 0.5 0.5 0.5 0.5 0]
                                 :back   [0 0 0.5 0 0 0.5 0.5 0.5]
                                 :top    [0 0.5 0.5 0.5 0 1 0.5 1]
                                 :right  [0 0 0 0.5]
                                 :left   [0.5 0 0.5 0.5]
                                 :bottom [0.5 0 1 0 0.5 0.5 1 0.5]}}}]

  (.createMesh world
               (float-array (flat-map vertices))
               (float-array (flat-map text-coord))
               (float-array (flat-map normals))
               (int-array (flat-map faces))
               (int weight) (float-array rgb) texture-name))

(defn create-simple-mesh!
  "Create an item in the `world` from the specified mesh object"
  [world & {:keys [geometry rgb]
            :or {geometry (yaw.mesh/box-geometry)
                 rgb [0 0 1]}}]
  (let [{:keys [vertices tris]} geometry
        vidx (zipmap (keys vertices) (range (count vertices)))
        coords (float-array (mapcat second vertices))
        normals (float-array (mapcat (fn [{n :n v :v}] (concat n n n)) tris))
        indices (int-array (mapcat (fn [{n :n v :v}] (map #(% vidx) v)) tris))]
    (.createMesh world coords normals indices (float-array rgb))))

;; Items Functions------------------------------------------------
(defn create-item!
  "Create an item in the `world` with the
  specified id, position, mesh"
  [world id & {:keys [position scale mesh]
               :or   {position [0 0 2] 
                      scale    1
                      mesh     (create-mesh! world)}}]         ;;error here
  (.createItemObject world id (position 0) (position 1) (position 2) scale mesh))

  
(defn remove-item!
  "Remove the specified `item` from the `world`"
  [world item]
  (.removeItem world item))

(defn set-item-color!
  "Replaces the material of the item with the specified color"
  [item [r g b]]
  (.setColor item r g b))

(defn create-block!
  "Create a rectangular block in the `world` with the
  specified id, position, color"
  [world & {:keys [id position scale color texture]
            :or   {texture  ""
                   color    [0 0 1]
                   scale    1
                   position [0 0 -2]
                   id (str (gensym "block-"))}}]
  (create-item! world id
                :position position
                :scale scale
                :mesh (create-simple-mesh! world :rgb color)))

(defn create-pyramid!
  "Create a rectangular block in the `world` with the
  specified id, position, color"
  [world & {:keys [id position scale color texture]
            :or   {texture  ""
                   color    [0 0 1]
                   scale    1
                   position [0 0 -2]
                   id (str (gensym "pyra-"))}}]
  (create-item! world id
                :position position
                :scale scale
                :mesh (create-simple-mesh! world :rgb color :geometry (yaw.mesh/pyramid-geometry))))

;;CAMERA MANAGEMENT------------------------------------------------
(defn cameras "Retrieve the list of cameras of the world" [world] (.getCamerasList world))

(defn camera "Retrieve the main camera of the world" [world] (.getCamera world))

(defn clear-cameras! "Remove all the cameras from the `world`" [world] (.emptyListCamera world))

(defn add-camera!
  "Add a camera to the `world`"
  ([world idx camera]
   (.addCamera world idx camera))
  ([world camera]
   (.add (cameras world) camera)))

(defn set-camera!
  "Set the current camera of the `world`"
  [world camera]
  (.setCamera world camera))

(defn create-camera!
  "Create a camera with the given parameters"
  [{:keys [fov near far pos target]
    :or {fov 60 near 0.1 far 1000.0 pos [5 5 5] target [0 0 0]}}]
  (println fov near far pos target)
  (let [[px py pz] pos
        [ox oy oz] target]
    (Camera. (float (Math/toRadians fov)) (float near) (float far)
             (float px) (float py) (float pz)
             (float ox) (float oy) (float oz))))

(defn set-camera-target!
  "Sets the target of a camera"
  [camera [x y z]]
  (.setOrientation camera x y z))

(defn set-camera-fov!
  "Sets the fov of a camera"
  [camera fov]
  (.setFieldOfView camera (Math/toRadians fov)))

;;LIGHT------------------------------------------------------------
(defn lights "Retrieve the lighting settings of the world scene" [world] (.getSceneLight world))

(defn create-ambient-light!
  [{:keys [color i]
    :or {color [1 1 1]
         i 0.3}}]
  (let [[r g b] color]
    (AmbientLight. r g b i)))

(defn create-sun-light!
  [{:keys [color i dir]
    :or {color [1 1 1]
         i 0.6
         dir [-1 -1 -1]}}]
  (let [[r g b] color
        [dx dy dz] dir]
    (DirectionalLight. r g b i dx dy dz)))

(defn create-point-light!
  [{{:keys [const lin quad] :or {const 0.3 lin 0.5 quad 0.9}} :att
    :keys [color i pos]
    :or {color [1 1 1]
         i 1
         pos [0 0 0]}}]
  (let [[r g b] color
        [px py pz] pos]
    (PointLight. r g b px py pz i const lin quad)))

(defn create-spot-light!
  [ {{:keys [const lin quad] :or {const 0.3 lin 0.5 quad 0.9}} :att
     :keys [color i position direction angle]
     :or {color [1 1 1]
          i 1
          position [0 0 0]
          direction [0 0 -1]
          angle 30}}]
  (let [[r g b] color [px py pz] position [dx dy dz] direction]
    (SpotLight. r g b px py pz i const lin quad dx dy dz angle)))

(defn set-ambient-light!
  "Set the ambient light of the world"
  [world l]
    (.setAmbient (lights world) l))

(defn set-sun!
  "Set the sun of the world"
  [world l]
    (.setSun (lights world) l))

(defn set-point-light!
  "Set the `n`th pointlight with the given `color`, `position`, `itensity`, and attenuation factors"
  [world n l]
    (.setPointLight (lights world) l n))

(defn set-spot-light!
  "Set the `n`th spotlight with the given `color`, `intensity`, `position`, `direction` and attenuation factors"
  [world n l]
    (.setSpotLight (lights world) l n))

;;COLLISIONS------------------------------------------------------

(defn create-bouding-box!
  "Create a boundingbox in the `world` with the
  specified id, position, length, scale"
  [world & {:keys [id position length scale]
            :or   {id       "can't read the doc..."
                   position [0 0 -2]
                   length   [1 1 1]
                   scale    1}}]

  (.createBoundingBox world id (float-array position) scale (float-array length)))
(defn add-bounding-box!
  "Add the specified 'bounding box' to the specified 'item'"
  [item bounding-box]
  (.setBoundingBox item bounding-box))
(defn check-collision!
  "Check if 2 items are in collision in the `world` with the
  specified items"
  [world item1 item2]
  (.isInCollision world item1 item2))

;;SKYBOX MANAGEMENT---------------------------------------------------
(defn skybox "Retrieve the skybox of the world" [world] (.getSkybox world))

(defn set-skybox!
  "Create a flat-colored skybox for the `world` with the
  specified scale and color"
  [world & {[w l h] :scale
            [r g b] :color
            :or {w 1000 l 1000 h 1000
                 r 0 g 0 b 0}}]
  (.setSkybox world w l h r g b))

(defn clear-skybox!
  "Remove the current skybox from the `world`"
  [world]
  (.removeSkybox world))

;;GROUP MANAGEMENT---------------------------------------------------------
(defn groups "Retrieve the groups of the `world`" [world] (into [] (.getItemGroupArrayList world)))

(defn new-group!
  "Get a new group created in the `world`"
  [world]
  (.createGroup world))

(defn remove-group!
  "Remove the specified group from the `world`"
  [world group]
  (.removeGroup world group))

;; Item/camera Manipulation ------------------------------------------------
(defn rotate! [item & {:keys [x y z]
                       :or   {x 0
                              y 0
                              z 0}}]
  (.rotateXYZ item x y z))

(defn translate! [item & {:keys [x y z]
                          :or   {x 0
                                 y 0
                                 z 0}}]
  (.translate item x y z))


