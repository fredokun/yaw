#!/usr/bin/env boot

(set-env! :repositories #(conj % ["mavencentral" {:url "https://repo.maven.apache.org/maven2"}])
	:dependencies '[[org.joml/joml "1.7.1"] [org.lwjgl/lwjgl "3.0.0b"] [org.lwjgl/lwjgl-platform "3.0.0b" :classifier "natives-linux"]]
	:resource-paths #{"yaw/"})


(deftask build
  "Build YAW Project."
  []
  (comp 
	(pom :project 'yaw :version "0.1.0")  
	(javac "./yaw/java/src/gameEngine/*.java")
	(javac "./yaw/java/src/gameEngine/light/*.java")
	(aot :all)
;;	(jar :file "YAW.jar")
;;	(install)
	))


