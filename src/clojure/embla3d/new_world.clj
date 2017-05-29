(ns embla3d.new-world
  (:import (embla3d.engine.meshs MeshBuilder)
           [embla3d.engine World])
  (gen-class))
;;UTILS------------------------------------------------------------------
(defn flat-map "flatten 'map'" [m]
  (flatten (conj (vals m))))

(defn start-universe!
  "Start an empty embla3d universe."
  [& {:keys [width height x y]
      :or   {x      0
             y      0
             width  800
             height 600}}]
  (let [world (World. x y width height)
        thread (future world)]
    (.start (Thread. world))
    (atom {:world world :thread thread})))

;;Since we completely destroy the old architecture we will migrate the basic method to this module
;;TODO/README ONLY USE WORLD IT is A FACADE, no DIRECT USE OF MANAGEMENT/BUILDER TOOLS
;; Material Fucntions---------------------------------------------

;; Mesh Functions------------------------------------------------
;;TODO create basic meshes
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
                                 :bottom [0.5 0 1 0 0.5 0.5 1 0.5]}
                   }}]
  (.createMesh world
               (float-array (flat-map vertices))
               (float-array (flat-map text-coord))
               (float-array (flat-map normals))
               (int-array (flat-map faces))
               (int weight) (float-array rgb) texture-name))
;; Items Functions------------------------------------------------
;;TODO create basic block item
(defn create-item!
  "Create an item in the `world` with the
  specified id, position, mesh"
  [world & {:keys [id position scale mesh]
            :or   {id       "can't read the doc..."
                   position [0 0 -2]
                   scale    1
                   mesh     (create-mesh! world)}}]
  (.createItem world id (float-array position) scale mesh))

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
                :mesh (create-mesh! world :rgb color :texture-name texture)))

;;TODO NEED CAMERA MANAGEMENT

;; Collision
(defn create-boudingbox!
  "Create a boundingbox in the `world` with the
  specified id, position, length, scale"
  [world & {:keys [id position length scale]
            :or   {id       "can't read the doc..."
                   position [0 0 -2]
                   length   [1 1 1]
                   scale    1}}]

  (.createBoundingBox world id (float-array position) scale (float-array length)))
(defn check-collision!
  "Check if 2 items are in collision in the `world` with the
  specified items"
  [world item1 item2]
  (.isInCollision world item1 item2))

(defn test1 [world]
  (.test world (float-array [1 2 3])))
;; Generic Generic------------------------------------------------
