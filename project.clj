(require 'leiningen.core.eval)

;; per-os jvm-opts code cribbed from Overtone
(def JVM-OPTS
  {:common   []
   :macosx   ["-XstartOnFirstThread" "-Djava.awt.headless=true"]
   :linux    []
   :windows  []})

(defn jvm-opts
  "Return a complete vector of jvm-opts for the current os."
  [] (let [os (leiningen.core.eval/get-os)]
       (vec (set (concat (get JVM-OPTS :common)
                         (get JVM-OPTS os))))))

(defproject embla3d "0.1.0"
  :description "A simple 3D programming world."
  :url "https://github.com/fredokun/embla3D"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [hello_lwjgl/lwjgl   "3.0.0"]
                 [cider/cider-nrepl "0.11.0"]
                 [org.joml/joml "1.8.4"]]
  :min-lein-version "2.1.0"
  :jvm-opts ^:replace ~(jvm-opts)
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]
  :test-paths ["test" "src/test"])

