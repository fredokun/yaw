(ns embla3d.new-world
  (:import (embla3d.engine.meshs MeshBuilder)
           [embla3d.engine World]))
;;Since we completely destroy the old architecture we will migrate the basic method to this module
;;TODO/README ONLY USE WORLD IT is A FACADE, no DIRECT USE OF MANAGEMENT/BUILDER TOOLS


;; Items Functions------------------------------------------------
;;TODO create basic block item
(defn create-block!
  "Create a rectangular block in the `world` with the
  specified id, position, color"
  [world & {:keys [id position dimensions scale color texture]
            :or   {color      [0 0 1]
                   dimensions [1 1 1]
                   scale      1
                   position   [0 0 -2]
                   id         ""}}]
  )
;;TODO CHANGE ID PARAM
(defn create-item!
  "Create an item in the `world` with the
  specified id, position, mesh"
  [world & {:keys [id position scale mesh]
            :or   {id       "can't read the doc..."
                   position [0 0 -2]
                   scale    1
                   mesh     (create-block-mesh!)}}]

  (let [item (.createItem world id position scale mesh)]
    item))

;; Mesh Functions------------------------------------------------
;;TODO create basic meshes
(defn create-mesh! "" []

  )
(defn create-block-mesh! "" []

  )

;; Generic Generic------------------------------------------------
