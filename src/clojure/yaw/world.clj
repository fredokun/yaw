(ns yaw.world
  (:import (yaw.engine.meshs MeshBuilder)
           (yaw.engine World))
  (:require [yaw.mesh]))
  ;;(gen-class)


;;UTILS------------------------------------------------------------------
(defn flat-map "flatten 'map'" [m]
  (flatten (conj (vals m))))

(defn start-universe!
  "Start an empty yaw universe."
  [& {:keys [width height x y]
      :or   {x      0
             y      0
             width  800
             height 600}}]
  (let [world (World. x y width height)
        thread (future world)]
    (.start (Thread. world))
    (atom {:world world :thread thread})))

;;CALLBACKS---------------------------------------------------------------
(defn callmap "Retrieve the `world` callback map"
  [world] (.getCallback world))

(defn register-callback! "Add a callback to the `world` callmap"
  [world name f]
  (.registerCallback (callmap world) name f))

(defn clear-callback! "Remove the named callback from the `world` callmap"
  [world name]
  (.clearCallback (callmap world) name))

(defn clear-keystroke! "Remove the specified function from the callback corresponding to the specified key"
  [world key f]
  (.clearFunctionOfKey (callmap world) key f))

;;Since we completely destroy the old architecture we will migrate the basic method to this module
;;README ONLY USE WORLD IT is A FACADE, no DIRECT USE OF MANAGEMENT/BUILDER TOOLS

;; Mesh Functions------------------------------------------------
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
            :or {geometry (yaw.mesh/box-geometry {})
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
  [world & {:keys [id position scale mesh]
            :or   {id       "can't read the doc..."
                   position [0 0 -2]
                   scale    1
                   mesh     (create-mesh! world)}}]         ;;error here
  (.createItem world id (float-array position) scale mesh))

(defn remove-item!
  "Remove the specified `item` from the `world`"
  [world item]
  (.removeItem world item))

(defn create-block!
  "Create a rectangular block in the `world` with the
  specified id, position, color"
  [world & {:keys [id position scale color texture]
            :or   {texture  ""
                   color    [0 0 1]
                   scale    1
                   position [0 0 -2]
                   id       ""}}]
  (create-item! world :id id
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
                   id       ""}}]
  (create-item! world :id id
                :position position
                :scale scale
                :mesh (create-simple-mesh! world :rgb color :geometry (yaw.mesh/pyramid-geometry {}))))

;;CAMERA MANAGEMENT------------------------------------------------
(defn camera "Retrieve the main camera of the world" [world] (.getCamera world))

(defn clear-cameras! "Remove all the cameras from the `world`" [world] (.emptyListCamera world))

(defn add-camera!
  "Add a camera to the `world`"
  [world idx camera]
  (.addCamera world idx camera))

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
  (.rotate item x y z))
(defn translate! [item & {:keys [x y z]
                          :or   {x 0
                                 y 0
                                 z 0}}]
  (.translate item x y z))

(defn clone! [world item] (.clone world item))
