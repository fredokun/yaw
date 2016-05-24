#Yet Another World


## Requirements

Boot : [Building Tool for Clojure] http://boot-clj.com/  
Java : 1.8 +

## How to start

Go to the root of the project where the file build.boot is located  
Launch the REPL of boot with : "boot repl"  
Initialize the project inside the REPL : (boot (build))  
Import the clojure functions : (use 'yaw.world)  
Initialize the world : (def universe(start-yaw))  

Now you can access the world with : (:world @universe)

---
# How to use the Clojure Functions

## Functions that can be used by Camera / Item / Light / Group

### Translate (object x y z)
>boot.user=> (translate object 10.0 0.0 1.0)

### Get / Set the Position (object x y z)
>boot.user=> (getPosition object)
>boot.user=> (setPosition object 0.0 0.0 0.0)

## Functions that can be used by Camera / Item / Group

### Rotate (object x y z)
>boot.user=> (rotate object 3.0 0.0 0.0)

----
## Item

###Creating  Block : (world r g b xL yL zL scale)

#### Parameters : world red green blue XLength YLength ZLength Scale
>boot.user=> (def item (createBlock (:world @universe) 1.0 0.0 0.0 2.0 2.0 2.0 1.0))

###Creating  HalfBlock : 
>boot.user=> (def item (createHalfBlock (:world @universe) 1.0 0.0 0.0 2.0 2.0 2.0 1.0))

###Creating  Pyramid :
>boot.user=> (def item (createPyramid (:world @universe) 1.0 0.0 0.0 2.0 2.0 2.0 1.0))

###Creating  Tetraedre :
>boot.user=> (def item (createTetraedreReg (:world @universe) 1.0 0.0 0.0 2.0 2.0 2.0 1.0))

###Creating Octaedre  :
>boot.user=> (def item (createOctaedre (:world @universe) 1.0 0.0 0.0 2.0 2.0 2.0 1.0))

###Clone : (item)
>boot.user=> (clone world item)

###Remove Item : (world item)
>boot.user=> (removeItem (:world @universe) item)
      
###Get a vector with all the items :
>boot.user=> (getListItems (:world @universe))

###Move : (item x y z)
>boot.user=> (translate item 1.0 2.0 0.0)

###Rotate : (item x y z)
>boot.user=> (rotate item 2.0 1.0 1.0)

###Set the Scale (item scale)
>boot.user=> (setScale item 0.5)

###Get the Scale (item)
>boot.user=> (getScale item)

###Set the reflectance (item refl)
>boot.user=> (setReflectance item 0.9)

###Get the reflectance (item)
>boot.user=> (getReflectance item)

###Set the Color (item r g b)
>boot.user=> (setColor item 1.0 0.0 0.0)

###Set the Rotation (item x y z)
>boot.user=> (setRotation item 2.0 1.0 1.0)

###Get the Rotation (item)
>boot.user=> (getRotation item)

###Revolve around a point (item pointX pointY pointZ degX degY degZ)
>boot.user=> (revolveAround item 1.0 2.0 2.0 1.0 1.0 1.0)

###Repel (item pointX pointY pointZ distance)
>boot.user=> (repelBy 0.0 0.0 0.0 10.0)

###Get the Color (item)
>boot.user=> (getColor item)

###Change the position : (item x y z)
>boot.user=> (setPosition item 0.0 10.0 0.0)

###Change the color : (item r g b) (0 to 1 for the color)
>boot.user=> (setColor item 1.0 0.0 0.0)

----
## Group of Item

----
## Camera

----
## Light

### Directional Light
Set the directional light : (world r g b intensity x y z)
>boot.user=> (setSunLight (:world @universe) 1.0 1.0 1.0 1.0 0.0 0.0 0.0)

### Ambiant Light
Set the AmbiantLight : (world r g b intensity)
>boot.user=> (setAmbiantLight (:world @universe) 1.0 1.0 1.0 0.8)

### Add Point Light [Maximum of Five]
(world r g b x y z intensity constantAtt linearAtt quadraticAtt number)
>boot.user=>(addPointLight (:world @universe) 1.0 1.0 1.0 0.0 0.0 0.0 0.5)

### Add SpotLight [Maximum of Five]

(world r g b x y z  intensity constantA linearAtt quadraticAtt xcone ycone zcone cutoffAngle number)
>boot.user=> (addSpotLight (:world @universe) 1.0 1.0 1.0 0.0 0.0 0.0 0.8 1.0 1.0 1.0 1.0 1.0 1.0 1.0 0)

### Manipulation of the lights (Point & Spot)

###Get the List of the SpotLight & PointLight
>boot.user=> (getPointLightList world)
>boot.user=> (getSpotLightList world)

###Get / Set the intensity of the light [0.0 -> 1.0]
>boot.user=> (setIntensity light 0.8)
>boot.user=> (getIntensity light)

###Get / set the Attenuation of the light
>boot.user=> (setConstantAtt light 0.5)
>boot.user=> (getConstantAtt light)

>boot.user=> (setLinearAtt light 0.5)
>boot.user=> (getLinearAtt light)

>boot.user=> (setQuadraticAtt light 0.5)
>boot.user=> (getQuadraticAtt light)

### Get / Set Directional Cone & Angle of the Spot light
>boot.user=> (setConedir light 1.0 2.0 3.0)
>boot.user=> (getConedir light)

>boot.user=> (setCutoffAngle light 0.8)
>boot.user=> (getCutoffAngle light)

### Get / Set the Specular Power of all the lights
>boot.user=> (setSpecularPower world 32)
>boot.user=> (getSpecularPower world)

----
## Callback

### The function has to implement IFn

>boot.user=> (defn toto [] clojure.lang.IFn (createBlock (:world @universe) 1.0 0.0 0.0 2.0 2.0 2.0 1.0))

### Add Callback
(world keyString function)
>boot.user=> (registerCallback (:world @universe) "t" toto)

### Remove all Callbacks associated with a key
(world keyString)
>boot.user=> (.clearCallback (.getCallback world) keyString)

----
## Save & Load




