#Yet Another World


## Requirements

Boot : [Building Tool for Clojure] http://boot-clj.com/  
Java : 1.8 +

## How to start

Go to the root of the project where the file build.boot is located  
Launch the REPL of boot with: "boot repl"  
Initialize the project inside the REPL: (boot (build))  
Import the Clojure functions: (use 'yaw.world)  
Initialize the world: (def universe(start-yaw))  

Now you can access the world with: (:world @universe)

## Example

Initialisation
>boot repl  
>boot.user=> (boot (build))  
>boot.user=> (use 'yaw.world)  
>boot.user=> (def universe(start-yaw))  
>boot.user=> (def world (:world @universe))  


Create a Block and move it in front of the Camera
>(def cube(createBlock world 1 1 1 1 1 1 1))  
>(translate cube 0 0 -2)

Add a green SpotLight
>(def sl(addSpotLight world 0 1 0 0 0 0 1 0 0.5 0 0 0 -1 10 0))

Change the ambient light (Darker)
>(setAmbientLight world 1.0 1.0 1.0 0.5)

Change the color of the SpotLight to red
>(setColor sl 1 0 0)

Set the sun light to blue and rotate the cube to see the differences
>(rotate cube 25 0 0)  
>(setSunLight world 0 0 1 1 0 -1 1)


---
# How to use the Clojure Functions

## Functions that can be used by Camera / Item / Light / Group

##### Translate (object x y z)
>boot.user=> (translate object 10.0 0.0 1.0)

##### Get / Set the Position (object x y z)
>boot.user=> (getPosition object)  
>boot.user=> (setPosition object 0.0 0.0 0.0)

## Functions that can be used by Camera / Item / Group

##### Rotate (object x y z)
>boot.user=> (rotate object 3.0 0.0 0.0)

----
## Item

#####Creating the Ground of the world: (world red green blue width length)
>boot.user=> (def ground(createGround world 1 1 1 400 400))

#####Creating a Block: (world red green blue xLength yLength zLength scale)
>boot.user=> (def item (createBlock (:world @universe) 1.0 0.0 0.0 2.0 2.0 2.0 1.0))

#####Creating a HalfBlock: 
>boot.user=> (def item (createHalfBlock (:world @universe) 1.0 0.0 0.0 2.0 2.0 2.0 1.0))

#####Creating a Pyramid:
>boot.user=> (def item (createPyramid (:world @universe) 1.0 0.0 0.0 2.0 2.0 2.0 1.0))

#####Creating a Tetrahedron: (world r g b scale)
>boot.user=> (def item (createTetrahedron (:world @universe) 1.0 0.0 0.0 1.0))

#####Creating an Octahedron: (world r g b scale)
>boot.user=> (def item (createOctahedron (:world @universe) 1.0 0.0 0.0 1.0))

#####Clone: (world item)
>boot.user=> (clone world item)

#####Remove an item: (world item)
>boot.user=> (removeItem (:world @universe) item)
      
#####Get a vector with all the items:
>boot.user=> (getItemsList (:world @universe))

#####Move: (item x y z)
>boot.user=> (translate item 1.0 2.0 0.0)

#####Rotate: (item x y z)
>boot.user=> (rotate item 2.0 1.0 1.0)

#####Set the scale: (item scale)
>boot.user=> (setScale item 0.5)

#####Get the scale: (item)
>boot.user=> (getScale item)

#####Set the reflectance: (item refl)
>boot.user=> (setReflectance item 0.9)

#####Get the reflectance: (item)
>boot.user=> (getReflectance item)

#####Set the color: (item r g b)
>boot.user=> (setColor item 1.0 0.0 0.0)

#####Set the rotation: (item x y z)
>boot.user=> (setRotation item 2.0 1.0 1.0)

#####Get the rotation: (item)
>boot.user=> (getRotation item)

#####Revolve around a point: (item pointX pointY pointZ degX degY degZ)
>boot.user=> (revolveAround item 1.0 2.0 2.0 1.0 1.0 1.0)

#####Repel: (item pointX pointY pointZ distance)
>boot.user=> (repelBy 0.0 0.0 0.0 10.0)

#####Get the color: (item)
>boot.user=> (getColor item)

#####Change the position: (item x y z)
>boot.user=> (setPosition item 0.0 10.0 0.0)

#####Change the color: (item r g b) (0 to 1 for the color)
>boot.user=> (setColor item 1.0 0.0 0.0)

----
## Item Group

##### Get all the item groups
>boot.user=> (getGroupsList world)

##### Remove an item group
>boot.user=> (removeGroup world group)

##### Create a group
>boot.user=> (createGroup world)

##### Add an item to a group
>boot.user=> (addItem group item)

##### Remove an item from a group
>boot.user=> (removeFromGroup group item)

##### Separate the items (group distance)
>boot.user=> (separate group 5.0)

##### Scale all the items of the group
>boot.user=> (multScale group 2.0)

##### Get the items of a group
>boot.user=> (groupItems group)

----
## Camera

##### Create a Camera
>boot.user=> (createCamera world)

##### Add a Camera
>boot.user=> (addCamera world camera)

##### Set the live camera
>boot.user=> (setLiveCamera world camera)

##### Get Camera number X
>boot.user=> (getCamera world 1)

##### Get the live camera
>boot.user=> (getLiveCamera world)

##### Remove Camera number X
>boot.user=> (removeCameraNumber world 1)

##### Remove Camera
>boot.user=> (removeCamera world camera)

##### Set the orientation of the Camera (camera x y z)
>boot.user=> (setOrientation camera 1.0 0.0 0.0)

##### Get the orientation of the Camera (camera)
>boot.user=> (getOrientation camera)
----
## Light

##### DirectionalLight
Set the directional light: (world r g b intensity x y z)
>boot.user=> (setSunLight (:world @universe) 1.0 1.0 1.0 1.0 0.0 0.0 0.0)

##### AmbientLight
Set the AmbientLight: (world r g b intensity)
>boot.user=> (setAmbientLight (:world @universe) 1.0 1.0 1.0 0.8)

##### Add PointLight [Maximum of Five]
(world r g b x y z intensity constantAtt linearAtt quadraticAtt number)
>boot.user=>(addPointLight (:world @universe) 1.0 1.0 1.0 0.0 0.0 0.0 0.5)

##### Add SpotLight [Maximum of Five]
(world r g b x y z  intensity constantA linearAtt quadraticAtt xcone ycone zcone cutoffAngle number)
>boot.user=> (addSpotLight (:world @universe) 1.0 1.0 1.0 0.0 0.0 0.0 0.8 1.0 1.0 1.0 1.0 1.0 1.0 1.0 0)

### Manipulation of the lights (Point & Spot)

#####Get the List of the SpotLight & PointLight
>boot.user=> (getPointLightList world)  
>boot.user=> (getSpotLightList world)

#####Get / Set the intensity of the light [0.0 -> 1.0]
>boot.user=> (setIntensity light 0.8)  
>boot.user=> (getIntensity light)

#####Get / set the Attenuation of the light
>boot.user=> (setConstantAtt light 0.5)  
>boot.user=> (getConstantAtt light)

>boot.user=> (setLinearAtt light 0.5)  
>boot.user=> (getLinearAtt light)

>boot.user=> (setQuadraticAtt light 0.5)  
>boot.user=> (getQuadraticAtt light)

##### Get / Set Directional Cone & Angle of the Spot light
>boot.user=> (setConedir light 1.0 2.0 3.0)  
>boot.user=> (getConedir light)

>boot.user=> (setCutoffAngle light 0.8)  
>boot.user=> (getCutoffAngle light)

##### Get / Set the Specular Power of all the lights
>boot.user=> (setSpecularPower world 32)  
>boot.user=> (getSpecularPower world)

----
##Skybox

##### Set the Skybox (world width length height red green blue)
>boot.user=> (setSkybox world 500 500 500 1 0 0)

#### Remove the Skybox
>boot.user=> (removeSkybox world)

----
## Callback

##### The function has to implement IFn
>boot.user=> (defn toto [] clojure.lang.IFn (createBlock (:world @universe) 1.0 0.0 0.0 2.0 2.0 2.0 1.0))

##### Add Callback
(world keyString function)
>boot.user=> (registerCallback (:world @universe) "t" toto)

##### Remove all Callbacks associated with a key
(world keyString)
>boot.user=> (clearCallback (:world @universe) keyString)

##### Remove a function associated with a key
(world keyString function)
>boot.user=> (clearFunctionOfKey (:world @universe) keyString function)

----
## Save & Load

#### Save the World (filename world)
>boot.user=> (saveFile "yaw/save/u.edn" world)

#### Load the World (filename world)
 ! WARNING !  
This function removes the current world to load the file's content into a new one.  
This function returns the new universe so you need to keep it (def universe (...)).  
If you used (def world(:world @universe)), you need to do it again after the loadFile,
because otherwise, it will lead to the previous world (and cause an error if you try to load a new world with it).

> boot.user=> (def universe(loadFile "yaw/save/allfeatures.edn" (:world @universe)))




