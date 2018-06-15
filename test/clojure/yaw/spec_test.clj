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

(t/deftest light-spec
  (t/testing "Ambient"
    (t/testing "Conforming"
      (t/is (= {:tag :ambient :params {:color [:rgb [0 0 0]]}}
               (s/conform :scene/ambient-light [:ambient {:color [0 0 0]}]))
            "Minimal ambient light")
      (t/is (= {:tag :ambient :params {:color [:kw :blue]}}
               (s/conform :scene/ambient-light [:ambient {:color :blue}]))
            "Keyword color")
      (t/is (= {:tag :ambient :params {:color [:kw :red] :i 0}}
               (s/conform :scene/ambient-light [:ambient {:color :red :i 0}]))
            "Intensity"))
    (t/testing "Failing"
      (t/is (s/invalid? (s/conform :scene/ambient-light [:ambient]))
            "Omit all parameters")
      (t/is (s/invalid? (s/conform :scene/ambient-light [:ambient {:i 0}]))
            "Color is required")
      (t/is (s/invalid? (s/conform :scene/ambient-light [:ambient :test/ambient {:color :red}]))
            "Ambient is unique")
      (t/is (s/invalid? (s/conform :scene/ambient-light [:ambient {:color [0 0 0] :i -3}]))
            "Intensity must be positive")))

  (t/testing "Sun"
    (t/testing "Conforming"
      (t/is (= {:tag :sun :params {:color [:rgb [0 0 0]] :dir [0 0 0]}}
               (s/conform :scene/sun-light [:sun {:color [0 0 0] :dir [0 0 0]}]))
            "Minimal working sunlight")
      (t/is (= {:tag :sun :params {:color [:rgb [0 0 0]] :dir [0 0 0] :i 0}}
               (s/conform :scene/sun-light [:sun {:color [0 0 0] :dir [0 0 0] :i 0}]))
            "Intensity"))
    (t/testing "Failing"
      (t/is (s/invalid? (s/conform :scene/sun-light [:sun {:color [0 0 0]}]))
            "Direction is required")
      (t/is (s/invalid? (s/conform :scene/sun-light [:sun :test/sun {:color [0 0 0] :dir [0 0 0]}]))
            "Sun is unique")))

  (t/testing "Spotlight"
    (t/testing "Conforming"
      (t/is (= {:tag :spot :id-kw :test/spot :params {:color [:rgb [0 0 0]] :dir [0 0 0] :pos [0 0 0]}}
               (s/conform :scene/spot-light [:spot :test/spot {:color [0 0 0] :dir [0 0 0] :pos [0 0 0]}]))
            "Minimal working spot")

      ;; (t/is (= {:tag :spot :id-kw :test/spot :params {:color [:kw :blue] :dir [:item :test/item] :pos [0 0 0]}}
      ;;          (s/conform :scene/spot-light [:spot :test/spot {:color :blue :dir :test/item :pos [0 0 0]}]))
      ;;       "Item target")
      ;; TODO ? Discuss.

      (t/is (= {:tag :spot :id-kw :test/spot :params {:color [:kw :red] :dir [0 0 0] :pos [0 0 0] :i 0}}
               (s/conform :scene/spot-light [:spot :test/spot {:color :red :dir [0 0 0] :pos [0 0 0] :i 0}]))
            "Intensity"))
    (t/testing "Failing"
      (t/is (s/invalid? (s/conform :scene/spot-light [:spot {:color :red :dir [0 0 0] :pos [0 0 0]}]))
            "Omit id keyword")
      (t/is (s/invalid? (s/conform :scene/spot-light [:sun :test/spot {:color :red :dir [0 0 0] :pos [0 0 0]}]))
            "Wrong tag")
      (t/is (s/invalid? (s/conform :scene/spot-light [:spot :test/spot {:color :red :dir [0 0 0]}]))
            "Position is required")))

  (t/testing "Pointlight"
    (t/testing "Conforming"
      (t/is (= {:tag :light :id-kw :test/point :params {:color [:kw :yellow] :pos [0 0 0]}}
               (s/conform :scene/point-light [:light :test/point {:color :yellow :pos [0 0 0]}]))
            "Minimal working point")
      (t/is (= {:tag :light :id-kw :test/point :params {:color [:kw :yellow] :pos [0 0 0] :i 0}}
               (s/conform :scene/point-light [:light :test/point {:color :yellow :pos [0 0 0] :i 0}]))
            "Intensity"))
    (t/testing "Failing"
      (t/is (s/invalid? (s/conform :scene/point-light [:light {:color :yellow :pos [0 0 0]}]))
            "Omit id keyword")
      (t/is (s/invalid? (s/conform :scene/point-light [:light :test/point {:color :yellow}]))
            "Position is required")))

  (t/testing "Lights"
    (t/is (= [:sun {:tag :sun, :params {:dir [0 0 -1], :color [:kw :blue]}}]
           (s/conform :scene/light [:sun {:dir [0 0 -1] :color :blue}]))
          "Sun")
    (t/is (= [:point {:tag :light, :id-kw :test/point, :params {:color [:kw :red], :pos [0 0 0], :i 0.3}}]
             (s/conform :scene/light [:light :test/point {:color :red :pos [0 0 0] :i 0.3}]))
          "Point")
    (t/is (= [:spot {:tag :spot :id-kw :test/spot :params {:color [:kw :red] :pos [0 0 0] :dir [0 0 0]}}]
             (s/conform :scene/light [:spot :test/spot {:color :red :pos [0 0 0] :dir [0 0 0]}]))
          "Spot")
    (t/is (= [:ambient {:tag :ambient :params {:color [:kw :blue]}}]
             (s/conform :scene/light [:ambient {:color :blue}])))))

(t/deftest group-spec
  (t/testing "Conforming"
    (t/is (= {:tag :group :id-kw :test/group :params {:pos [0 0 0]}
              :items [{:tag :item :id-kw :test/item :params {:mesh :mesh/box :pos [0 0 0] :color [:kw :red]}}]}
             (s/conform :scene/group [:group :test/group {:pos [0 0 0]}
                                      [:item :test/item {:mesh :mesh/box :pos [0 0 0] :color :red}]]))
          "Minimal working group")
    (t/is (= {:tag :group :id-kw :test/group :params {:pos [0 0 0] :rot [0 0 0]}
              :items [{:tag :item :id-kw :test/item :params {:mesh :mesh/box :pos [0 0 0] :color [:kw :red]}}]}
             (s/conform :scene/group [:group :test/group {:pos [0 0 0] :rot [0 0 0]}
                                      [:item :test/item {:mesh :mesh/box :pos [0 0 0] :color :red}]]))
          "Rotation")
    (t/is (= {:tag :group :id-kw :test/group :params {:pos [0 0 0] :scale [0 0 0]}
              :items [{:tag :item :id-kw :test/item :params {:mesh :mesh/box :pos [0 0 0] :color [:kw :red]}}]}
             (s/conform :scene/group [:group :test/group {:pos [0 0 0] :scale [0 0 0]}
                                      [:item :test/item {:mesh :mesh/box :pos [0 0 0] :color :red}]]))
          "Scale"))
  (t/testing "Failing"
    (t/is (s/invalid? (s/conform :scene/group [:group {:pos [0 0 0]} [:item :test/item {:mesh :mesh/box :pos [0 0 0]}]]))
          "Omit id keyword")
    (t/is (s/invalid? (s/conform :scene/group [:group :test/group {:pos [0 0 0]}]))
          "At least one item")
    (t/is (s/invalid? (s/conform :scene/group [:group :test/parent-group {:pos [0 0 0]}
                                             [:group :test/child-group {:pos [0 0 0]}
                                              [:item :test/nested-item {:mesh :mesh/box :pos [0 0 0]}]]]))
          "No nested groups")
    (t/is (s/invalid? (s/conform :scene/group [:group :test/group [:item :test/item {:mesh :mesh/box :pos [0 0 0]}]]))
          "Position is required")))

(t/deftest scene-object-spec
  (t/is (= [:camera {:tag :camera, :id-kw :test/cam, :params {:pos [0 2 -5], :target [:vec [0 0 0]]}}]
           (s/conform :scene/object [:camera :test/cam {:pos [0 2 -5] :target [0 0 0]}]))
        "Camera object")
  (t/is (= [:light [:point {:tag :light, :id-kw :test/light, :params {:pos [0 0 1], :color [:kw :yellow], :i 0.3}}]]
           (s/conform :scene/object [:light :test/light {:pos [0 0 1] :color :yellow :i 0.3}]))
        "Point light object")
  (t/is (= [:light [:ambient {:tag :ambient, :params {:color [:kw :yellow]}}]]
           (s/conform :scene/object [:ambient {:color :yellow}]))
        "Point light object")
  (t/is (= [:item {:tag :item, :id-kw :test/item, :params {:pos [0 0 0], :mesh :mesh/box}}]
           (s/conform :scene/object [:item :test/item {:pos [0 0 0] :mesh :mesh/box}]))
        "Item object"))
