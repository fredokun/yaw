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

(t/deftest item-specs
  (t/testing "Conforming"
    (t/is (= {:tag :item :id-kw :test/item :params {:mesh :mesh/box :pos [0 0 0]}}
             (s/conform :scene/item [:item :test/item {:mesh :mesh/box :pos [0 0 0]}]))
          "Minimal working item")
    (t/is (= {:tag :item :id-kw :test/item :params {:mesh :mesh/cone :pos [0 0 0]}}
             (s/conform :scene/item [:item :test/item {:mesh :mesh/cone :pos [0 0 0]}]))
          "Different mesh")
    (t/is (= {:tag :item :id-kw :test/item :params {:mesh :mesh/box :pos [0 0 0] :rot [0 0 0]}}
             (s/conform :scene/item [:item :test/item {:mesh :mesh/box :pos [0 0 0] :rot [0 0 0]}]))
          "Rotation")
    (t/is (= {:tag :item :id-kw :test/item :params {:mesh :mesh/box :pos [0 0 0] :color [:rgb [0 0 0]]}}
             (s/conform :scene/item [:item :test/item {:mesh :mesh/box :pos [0 0 0] :color [0 0 0]}]))
          "RGB color")
    (t/is (= {:tag :item :id-kw :test/item :params {:mesh :mesh/box :pos [0 0 0] :color [:kw :red]}}
             (s/conform :scene/item [:item :test/item {:mesh :mesh/box :pos [0 0 0] :color :red}]))
          "Keyword color"))
  (t/testing "Failing"
    (t/is (s/invalid? (s/conform :scene/item [:camera :test/cam {:pos [0 0 0] :target [0 0 0]}]))
          "Wrong type")
    (t/is (s/invalid? (s/conform :scene/item [:item {:pos [0 0 0] :mesh :mesh/box}]))
          "Omit id keyword")
    (t/is (s/invalid? (s/conform :scene/item [:item :test/item {:pos [0 0 0]}]))
          "Omit mesh")
    (t/is (s/invalid? (s/conform :scene/item [:item :test/item {:mesh :mesh/box}]))
          "Omit position")
    (t/is (s/invalid? (s/conform :scene/item [:item :test/item {:mesh :mesh/megalodon :pos [0 0 0]}]))
          "Invalid mesh")))
