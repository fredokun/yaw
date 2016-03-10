#!/usr/bin/env boot

(set-env! :repositories #(conj % ["mavencentral" {:url "https://repo.maven.apache.org/maven2"}])
	:dependencies '[[org.joml/joml "1.7.1"] [org.lwjgl/lwjgl "3.0.0b"] [org.lwjgl/lwjgl-platform "3.0.0b" :classifier "natives-linux"]]
	:resource-paths #{"src"})


(deftask build
  "Build YAW Project."
  []
  (comp 
	(pom :project 'YAW-project :version "0.1.0")  
	(javac "./src/testArchitecture/*.java")
	(javac "./src/gameEngine/*.java")
	(javac "./src/gameEngine/light/*.java")
	(jar :file "YAW.jar")
	(install)))


