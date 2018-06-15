(ns clojure.yaw.spec-test
  (:require [clojure.spec.alpha :as s]
            [yaw.spec :as ys]
            [clojure.test :as t]))

(t/deftest camera-specs
  (t/testing "Conforming"
    (t/is (= {:tag :camera :id-kw :test/cam :params {:pos [0 0 0] :target [:vec [0 0 0]]}}
             (s/conform :scene/camera [:camera :test/cam {:pos [0 0 0] :target [0 0 0]}]))
          "Vector target")
    (t/is (= {:tag :camera :id-kw :test/cam :params {:pos [0 0 0] :target [:item :test/target]}}
             (s/conform :scene/camera [:camera :test/cam {:pos [0 0 0] :target :test/target}]))
          "Item target")
    (t/is (= {:tag :camera :id-kw :test/cam :params {:pos [0 0 0] :target [:vec [0 0 0]] :fov 90}}
             (s/conform :scene/camera [:camera :test/cam {:pos [0 0 0] :target [0 0 0] :fov 90}]))
          "Field of View"))
  (t/testing "Failing"
    (t/is (s/invalid? (s/conform :scene/camera [:camera {:pos [0 0 0] :target [0 0 0]}]))
          "Omit id keyword")
    (t/is (s/invalid? (s/conform :scene/camera [:item :test/cam {:pos [0 0 0] :target [0 0 0]}]))
          "Wrong tag kw")
    (t/is (s/invalid? (s/conform :scene/camera [:camera :test/cam]))
          "Omit all parameters")
    (t/is (s/invalid? (s/conform :scene/camera [:camera :test/cam {:target [0 0 0]}]))
          "Position is required")
    (t/is (s/invalid? (s/conform :scene/camera [:camera :test/cam {:pos [0 0 0]}]))
          "Target is required")))
