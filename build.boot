#!/usr/bin/env boot

(set-env!
 :repositories
 #(conj % ["mavencentral" {:url "https://repo.maven.apache.org/maven2"}])
 :dependencies '[[org.joml/joml "1.7.1"]
                 [org.lwjgl/lwjgl "3.0.0b"]
                 [org.lwjgl/lwjgl-platform "3.0.0b" :classifier "natives-linux"]
                 [org.lwjgl/lwjgl-platform "3.0.0b" :classifier "natives-osx"]
                 [org.lwjgl/lwjgl-platform "3.0.0b" :classifier "natives-windows"]
                 [org.clojure/clojure "1.8.0"]]
 :source-paths #{"src/"})


(deftask build
  "Build YAW Project."
  []
  (comp 
	(pom :project 'yaw :version "0.1.0")
	(javac "./yaw/java/src/*")
	(aot :all)
;;	(jar :file "YAW.jar")
;;	(install)
	))

