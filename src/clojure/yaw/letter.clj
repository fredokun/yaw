(ns yaw.letter
  (:require [yaw.world :as w]))

(defn- anchor-fun
  "Given an anchor vertex and a scale vector,
  gives the function that would map
  a vertex placed in a unit cube centered at the origin
  to the vertex scaled by the scale and
  translated by the opposite of the origin"
  [p s]
  (partial mapv (fn [p s v] (- (* s v) p))
           p s))

(defn sides [v orientation names]

  (reduce (fn [res [nameS1  nameS2]]
            (let [k1 (keyword nameS1) k1b (keyword (str nameS1 1))  k2 (keyword nameS2) k2b (keyword (str nameS2 1))]
              (conj res
                    {:n (v orientation)
                     :v [k1 k2b k2]}

                    {:n (v orientation)
                     :v [k1 k2b k1b]}))) [] names))
(defn face [v names]

  (reduce (fn [res [nameS1  nameS2 nameS3]]
            (let [k1 (keyword nameS1) k1b (keyword (str nameS1 1))  k2 (keyword nameS2) k2b (keyword (str nameS2 1)) k3 (keyword nameS3) k3b (keyword (str nameS3 1))]
              (conj res
                    {:n (v [1 0 0])
                     :v [k1 k2 k3]}

                    {:n (v  [-1 0 0])
                     :v [k1b k2b k3b]}))) [] names))
(defn- point [v key  y z]
  (conj  {(keyword key) (v [0 y z])} {(keyword (str key 1)) (v [1 y z])}))
(defn- anchor-fun2
  [v]
  (partial point v))
(defn- anchor-fun3
  [v]
  (partial sides v))
(defn- anchor-fun4
  [v]
  (partial face v))

(defn letter-A
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 3 5])
                (p "c" [0 3 0])
                (p "d" [0 2 0])
                (p "e" [0 2 2])
                (p "f" [0 1 2])
                (p "g" [0 1 0])
                (p "h" [0 0 0])

                (p "i" [0 1 4])
                (p "j" [0 2 4])
                (p "k" [0 2 3])
                (p "l" [0 1 3]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["l" "k"]])

                     (s [0 0 -1]
                        [["c" "d"]
                         ["e" "f"]
                         ["g" "h"]
                         ["i" "j"]])

                     (s [0 1 0]
                        [["b" "c"]
                         ["i" "l"]
                         ["f" "g"]])

                     (s [0 -1 0]
                        [["a" "h"]
                         ["j" "k"]
                         ["e" "d"]])

                     (f [["a" "b" "i"]
                         ["a" "i" "l"]
                         ["a" "h" "l"]

                         ["b" "i" "j"]
                         ["b" "j" "k"]
                         ["b" "c" "k"]

                         ["c" "d" "e"]
                         ["c" "e" "k"]

                         ["e" "f" "l"]
                         ["e" "k" "l"]

                         ["f" "g" "h"]
                         ["f" "h" "l"]])))}))

(defn letter-B
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 2 5])
                (p "c" [0 3 4])
                (p "d" [0 3 3])
                (p "e" [0 3 2])
                (p "f" [0 3 1])
                (p "g" [0 2 0])
                (p "h" [0 0 0])

                (p "i" [0 1 2])
                (p "j" [0 2 2])
                (p "k" [0 2 1])
                (p "l" [0 1 1])

                (p "m" [0 1 4])
                (p "n" [0 2 4])
                (p "o" [0 2 3])
                (p "p" [0 1 3]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["l" "k"]
                         ["o" "p"]])

                     (s [0 0 -1]
                        [["m" "n"]
                         ["g" "h"]
                         ["i" "j"]])

                     (s [0 1 0]
                        [["d" "c"]
                         ["e" "f"]
                         ["i" "l"]
                         ["m" "p"]])

                     (s [0 -1 0]
                        [["a" "h"]
                         ["j" "k"]
                         ["n" "o"]])
                     (s [0 n n]
                        [["b" "c"]
                         ["o" "e"]])
                     (s [0  n (- n)]
                        [["d" "j"]
                         ["f" "g"]])
                     (f [["a" "b" "m"]
                         ["a" "m" "p"]
                         ["a" "h" "p"]

                         ["c" "b" "m"]
                         ["c" "n" "o"]
                         ["c" "d" "o"]

                         ["d" "o" "j"]

                         ["e" "o" "j"]

                         ["f" "e" "j"]
                         ["f" "e" "k"]
                         ["f" "g" "k"]

                         ["o" "j" "i"]
                         ["o" "p" "i"]

                         ["h" "g" "k"]
                         ["h" "k" "l"]
                         ["h" "i" "l"]
                         ["h" "i" "p"]])))}))
(defn letter-C
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 1 5])
                (p "b" [0 3 5])
                (p "c" [0 3 4])
                (p "d" [0 2 4])
                (p "e" [0 1 3])
                (p "f" [0 1 2])
                (p "g" [0 2 1])
                (p "h" [0 3 1])
                (p "i" [0 3 0])
                (p "j" [0 1 0])
                (p "k" [0 0 1])
                (p "l" [0 0 4]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["g" "h"]])

                     (s [0 0 -1]
                        [["c" "d"]
                         ["j" "i"]])

                     (s [0 1 0]
                        [["b" "c"]
                         ["e" "f"]
                         ["h" "i"]])

                     (s [0 -1 0]
                        [["k" "l"]])
                     (s [0 n n]
                        [["f" "g"]])
                     (s [0 n (- n)]
                        [["e" "d"]])
                     (s [0 (- n) n]
                        [["a" "l"]])
                     (s [0 (- n) (- n)]
                        [["k" "j"]])
                     (f [["a" "b" "c"]
                         ["a" "c" "d"]
                         ["a" "d" "e"]
                         ["a" "e" "l"]

                         ["e" "k" "l"]
                         ["e" "f" "k"] ["j" "f" "k"]
                         ["j" "g" "f"]
                         ["j" "g" "h"]
                         ["j" "h" "i"]])))}))
(defn letter-D
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 2 5])
                (p "c" [0 3 4])
                (p "d" [0 3 1])
                (p "e" [0 2 0])
                (p "f" [0 0 0])

                (p "g" [0 1 4])
                (p "h" [0 2 4])
                (p "i" [0 2 1])
                (p "j" [0 1 1]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["j" "i"]])

                     (s [0 0 -1]
                        [["e" "f"]
                         ["g" "h"]])

                     (s [0 1 0]
                        [["c" "d"]
                         ["g" "j"]])

                     (s [0 -1 0]
                        [["a" "f"]
                         ["h" "i"]])
                     (s [0 n n]
                        [["b" "c"]])
                     (s [0 n (- n)]
                        [["e" "d"]])
                     (f [["a" "b" "h"]
                         ["a" "h" "g"]
                         ["a" "g" "j"]
                         ["a" "j" "f"]

                         ["c" "b" "h"]
                         ["c" "h" "i"]
                         ["c" "d" "i"]

                         ["e" "d" "i"]
                         ["e" "i" "j"]
                         ["e" "f" "j"]])))}))
(defn letter-E
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 3 5])
                (p "c" [0 3 4])
                (p "d" [0 1 4])
                (p "e" [0 1 3])
                (p "f" [0 2 3])
                (p "g" [0 2 2])
                (p "h" [0 1 2])
                (p "i" [0 1 1])
                (p "j" [0 3 1])
                (p "k" [0 3 0])
                (p "l" [0 0 0]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["e" "f"]
                         ["i" "j"]])

                     (s [0 0 -1]
                        [["d" "c"]
                         ["h" "g"]
                         ["l" "k"]])

                     (s [0 1 0]
                        [["b" "c"]
                         ["d" "e"]
                         ["f" "g"]
                         ["h" "i"]
                         ["j" "k"]])

                     (s [0 -1 0]
                        [["a" "l"]])

                     (f [["a" "b" "c"]
                         ["a" "c" "d"]
                         ["a" "d" "e"]
                         ["a" "e" "l"]

                         ["e" "f" "g"]
                         ["e" "g" "h"]
                         ["e" "h" "l"]

                         ["l" "h" "i"]
                         ["l" "i" "j"]
                         ["l" "j" "k"]])))}))
(defn letter-F
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 3 5])
                (p "c" [0 3 4])
                (p "d" [0 1 4])
                (p "e" [0 1 3])
                (p "f" [0 2 3])
                (p "g" [0 2 2])
                (p "h" [0 1 2])
                (p "i" [0 1 0])
                (p "j" [0 0 0]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["e" "f"]])

                     (s [0 0 -1]
                        [["d" "c"]
                         ["h" "g"]
                         ["j" "i"]])

                     (s [0 1 0]
                        [["b" "c"]
                         ["d" "e"]
                         ["f" "g"]
                         ["h" "i"]])

                     (s [0 -1 0]
                        [["a" "j"]])

                     (f [["a" "b" "c"]
                         ["a" "c" "d"]
                         ["a" "d" "e"]
                         ["a" "e" "j"]

                         ["e" "f" "g"]
                         ["e" "g" "h"]
                         ["e" "h" "j"]

                         ["j" "h" "i"]])))}))
(defn letter-G
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 1 5])
                (p "b" [0 2 5])
                (p "c" [0 3 4])
                (p "d" [0 3 3])
                (p "e" [0 2 3])
                (p "f" [0 2 4])
                (p "g" [0 1 4])
                (p "h" [0 1 1])
                (p "i" [0 2 1])
                (p "j" [0 2 2])
                (p "k" [0 3 2])
                (p "l" [0 3 1])
                (p "m" [0 2 0])
                (p "n" [0 1 0])
                (p "o" [0 0 1])
                (p "o" [0 0 4]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["h" "i"]
                         ["j" "k"]])

                     (s [0 0 -1]
                        [["f" "g"]
                         ["m" "n"]])

                     (s [0 1 0]
                        [["c" "d"]
                         ["g" "h"]
                         ["k" "l"]])

                     (s [0 -1 0]
                        [["f" "e"]
                         ["p" "o"]])
                     (s [0 n n]
                        [["b" "c"]])
                     (s [0 n (- n)]
                        [["l" "m"]])
                     (s [0 (- n) n]
                        [["a" "p"]
                         ["k" "i"]])
                     (s [0 (- n) (- n)]
                        [["l" "j"]
                         ["n" "o"]])
                     (f [["a" "b" "f"]
                         ["a" "f" "g"]
                         ["a" "g" "p"]

                         ["c" "b" "f"]
                         ["c" "d" "e"]
                         ["c" "e" "f"]

                         ["o" "g" "p"]
                         ["o" "g" "h"]
                         ["o" "h" "n"]

                         ["m" "h" "n"]
                         ["m" "i" "h"]
                         ["m" "i" "l"]

                         ["k" "j" "l"]
                         ["k" "l" "i"]])))}))
(defn letter-H
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 1 5])
                (p "c" [0 1 3])
                (p "d" [0 2 3])
                (p "e" [0 2 5])
                (p "f" [0 3 5])
                (p "g" [0 3 0])
                (p "h" [0 2 0])
                (p "i" [0 2 2])
                (p "j" [0 1 2])
                (p "k" [0 1 0])
                (p "l" [0 0 0]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["c" "d"]
                         ["e" "f"]])

                     (s [0 0 -1]
                        [["g" "h"]
                         ["j" "i"]
                         ["k" "l"]])

                     (s [0 1 0]
                        [["b" "c"]
                         ["f" "g"]
                         ["j" "k"]])

                     (s [0 -1 0]
                        [["a" "l"]
                         ["d" "e"]
                         ["h" "i"]])
                     (f [["a" "b" "k"]
                         ["a" "k" "l"]

                         ["c" "d" "i"]
                         ["c" "i" "j"]

                         ["e" "f" "g"]
                         ["e" "g" "h"]])))}))
(defn letter-I
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 3 5])
                (p "c" [0 3 4])
                (p "d" [0 2 4])
                (p "e" [0 2 1])
                (p "f" [0 3 1])
                (p "g" [0 3 0])
                (p "h" [0 0 0])
                (p "i" [0 0 1])
                (p "j" [0 1 1])
                (p "k" [0 1 4])
                (p "l" [0 0 4]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["e" "f"]
                         ["i" "j"]])

                     (s [0 0 -1]
                        [["c" "d"]
                         ["g" "h"]
                         ["k" "l"]])

                     (s [0 1 0]
                        [["b" "c"]
                         ["d" "e"]
                         ["f" "g"]])

                     (s [0 -1 0]
                        [["a" "l"]
                         ["k" "j"]
                         ["h" "i"]])

                     (f [["a" "b" "c"]
                         ["a" "c" "l"]

                         ["k" "d" "e"]
                         ["k" "e" "j"]

                         ["i" "f" "g"]
                         ["i" "h" "g"]])))}))
(defn letter-J
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 3 5])
                (p "c" [0 3 4])
                (p "d" [0 2 4])
                (p "e" [0 2 0])
                (p "f" [0 0 0])
                (p "g" [0 0 2])
                (p "h" [0 1 1])
                (p "i" [0 1 4])
                (p "j" [0 0 4]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]])

                     (s [0 0 -1]
                        [["c" "d"]
                         ["f" "e"]
                         ["i" "j"]])

                     (s [0 1 0]
                        [["b" "c"]
                         ["d" "e"]])

                     (s [0 -1 0]
                        [["a" "j"]
                         ["f" "g"]
                         ["h" "i"]])
                     (s [0 n n]
                        [["g" "h"]])
                     (f [["a" "b" "c"]
                         ["a" "c" "j"]

                         ["i" "d" "e"]
                         ["i" "e" "h"]

                         ["f" "g" "h"]
                         ["f" "h" "e"]])))}))

(defn letter-K
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 1 5])
                (p "c" [0 1 3])
                (p "d" [0 3 5])
                (p "e" [0 3 4])
                (p "f" [0 3 1])
                (p "g" [0 3 0])
                (p "h" [0 1 2])
                (p "i" [0 1 0])
                (p "j" [0 0 0]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]])

                     (s [0 0 -1]
                        [["i" "j"]])

                     (s [0 1 0]
                        [["b" "c"]
                         ["d" "e"]
                         ["f" "g"]
                         ["h" "u"]])

                     (s [0 -1 0]
                        [["a" "j"]])
                     (s [0 n n]
                        [["c" "f"]])
                     (s [0 n (- n)]
                        [["h" "e"]])
                     (s [0 (- n) n]
                        [["c" "d"]])
                     (s [0 (- n) (- n)]
                        [["h" "g"]])
                     (f [["a" "b" "c"]
                         ["a" "c" "h"]
                         ["a" "h" "j"]

                         ["j" "h" "i"]

                         ["e" "d" "c"]
                         ["e" "h" "c"] ["f" "g" "h"]
                         ["f" "c" "h"]])))}))
(defn letter-L
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 1 5])
                (p "c" [0 1 1])
                (p "d" [0 3 1])
                (p "e" [0 3 0])
                (p "f" [0 0 0]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["c" "d"]])

                     (s [0 0 -1]
                        [["f" "e"]])

                     (s [0 1 0]
                        [["c" "b"]
                         ["d" "e"]])

                     (s [0 -1 0]
                        [["a" "f"]])
                     (f [["a" "d" "c"]

                         ["f" "c" "a"]
                         ["f" "c" "d"]
                         ["f" "d" "e"]])))}))
(defn letter-M
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 1 4])
                (p "c" [0 2 4])
                (p "d" [0 3 5])
                (p "e" [0 3 0])
                (p "f" [0 2 0])
                (p "g" [0 2 3])
                (p "h" [0 1 3])
                (p "i" [0 1 0])
                (p "j" [0 0 0]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["b" "c"]])

                     (s [0 0 -1]
                        [["e" "f"]
                         ["g" "h"]
                         ["i" "j"]])

                     (s [0 1 0]
                        [["d" "e"]
                         ["h" "i"]])

                     (s [0 -1 0]
                        [["f" "g"]
                         ["a" "j"]])
                     (s [0 n n]
                        [["a" "b"]])

                     (s [0 (- n) n]
                        [["c" "d"]])
                     (f [["j" "a" "b"]
                         ["j" "b" "h"]
                         ["j" "h" "i"]

                         ["b" "c" "g"]
                         ["b" "g" "h"]

                         ["e" "c" "d"]
                         ["e" "c" "g"]
                         ["e" "f" "g"]])))}))
(defn letter-N
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 2 3])
                (p "c" [0 2 5])
                (p "d" [0 3 5])
                (p "e" [0 3 0])
                (p "f" [0 1 2])
                (p "g" [0 1 0])
                (p "h" [0 0 0]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["c" "d"]])

                     (s [0 0 -1]
                        [["g" "h"]])

                     (s [0 1 0]
                        [["d" "e"]])

                     (s [0 -1 0]
                        [["a" "h"]])
                     (s [0 n n]
                        [["a" "b"]])
                     (s [0 (- n) (- n)]
                        [["e" "f"]])
                     (f [["a" "b" "f"]
                         ["a" "f" "g"]
                         ["a" "g" "h"]

                         ["e" "d" "c"]
                         ["e" "c" "b"]
                         ["e" "b" "f"]])))}))
(defn letter-O
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 1 5])
                (p "b" [0 2 5])
                (p "c" [0 3 4])
                (p "d" [0 3 1])
                (p "e" [0 2 0])
                (p "f" [0 1 0])
                (p "k" [0 0 1])
                (p "l" [0 0 4])

                (p "g" [0 1 4])
                (p "h" [0 2 4])
                (p "i" [0 2 1])
                (p "j" [0 1 1]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["j" "i"]])

                     (s [0 0 -1]
                        [["e" "f"]
                         ["g" "h"]])

                     (s [0 1 0]
                        [["c" "d"]
                         ["g" "j"]])

                     (s [0 -1 0]
                        [["k" "l"]
                         ["h" "i"]])
                     (s [0 n n]
                        [["b" "c"]])
                     (s [0 n (- n)]
                        [["e" "d"]])
                     (s [0 (- n) n]
                        [["a" "l"]])
                     (s [0 (- n) (- n)]
                        [["f" "k"]])
                     (f [["a" "b" "h"]
                         ["a" "h" "g"]
                         ["a" "g" "l"]

                         ["k" "l" "g"]
                         ["k" "g" "j"]
                         ["k" "j" "f"]

                         ["e" "i" "j"]
                         ["e" "j" "f"]
                         ["e" "d" "i"]

                         ["h" "b" "c"]
                         ["h" "c" "d"]
                         ["h" "d" "i"]])))}))
(defn letter-P
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 2 5])
                (p "c" [0 3 4])
                (p "d" [0 3 3])
                (p "e" [0 2 2])
                (p "f" [0 1 2])
                (p "g" [0 1 0])
                (p "h" [0 0 0])
                (p "i" [0 1 4])
                (p "j" [0 2 4])
                (p "k" [0 2 3])
                (p "l" [0 1 3]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["l" "k"]])

                     (s [0 0 -1]
                        [["e" "f"]
                         ["g" "h"]
                         ["i" "j"]])

                     (s [0 1 0]
                        [["d" "c"]
                         ["g" "f"]
                         ["i" "l"]])

                     (s [0 -1 0]
                        [["a" "h"]
                         ["j" "k"]])
                     (s [0 n n]
                        [["b" "c"]])
                     (s [0  n (- n)]
                        [["d" "e"]])
                     (f [["a" "b" "j"]
                         ["a" "j" "i"]
                         ["a" "i" "l"]
                         ["a" "l" "h"]

                         ["c" "b" "j"]
                         ["c" "j" "k"]
                         ["c" "e" "k"]
                         ["c" "d" "e"]

                         ["l" "g" "h"]
                         ["l" "f" "e"]
                         ["l" "f" "k"]])))}))
(defn letter-Q
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 1 5])
                (p "b" [0 2 5])
                (p "c" [0 3 4])
                (p "d" [0 3 2])
                (p "e" [0 3 1])
                (p "f" [0 3 0])
                (p "g" [0 2 0])
                (p "h" [0 1 0])
                (p "i" [0 0 1])
                (p "j" [0 0 4])

                (p "k" [0 1 4])
                (p "l" [0 2 4])
                (p "m" [0 2 1])
                (p "n" [0 1 1])

                (p "r" [0 2 2]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["m" "n"]])

                     (s [0 0 -1]
                        [["f" "g"]
                         ["k" "l"]])

                     (s [0 1 0]
                        [["c" "d"]
                         ["e" "f"]
                         ["k" "n"]])

                     (s [0 -1 0]
                        [["i" "j"]
                         ["l" "m"]])
                     (s [0 n n]
                        [["r" "e"]
                         ["b" "c"]])
                     (s [0 n (- n)]
                        [["d" "m"]
                         ["m" "h"]])
                     (s [0 (- n) n]
                        [["a" "j"]])
                     (s [0 (- n) (- n)]
                        [["h" "i"]
                         ["n" "g"]])
                     (f [["a" "b" "l"]
                         ["a" "l" "k"]
                         ["a" "k" "j"]

                         ["k" "j" "i"]
                         ["k" "i" "n"]

                         ["c" "b" "l"]
                         ["c" "r" "l"]
                         ["c" "d" "r"]

                         ["m" "r" "d"]
                         ["m" "r" "e"]
                         ["m" "e" "f"]
                         ["m" "n" "h"]
                         ["m" "n" "g"]
                         ["m" "g" "f"]])))}))

(defn letter-R
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 1 5])
                (p "b" [0 2 5])
                (p "c" [0 3 4])
                (p "d" [0 3 3])
                (p "e" [0 2 2])
                (p "f" [0 3 1])
                (p "g" [0 3 0])
                (p "h" [0 1 2])
                (p "i" [0 1 0])
                (p "j" [0 0 0])
                (p "k" [0 0 4])

                (p "l" [0 1 4])
                (p "m" [0 2 4])
                (p "n" [0 2 3])
                (p "o" [0 1 3]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["o" "n"]])

                     (s [0 0 -1]
                        [["l" "m"]
                         ["j" "i"]])

                     (s [0 1 0]
                        [["c" "d"]
                         ["f" "g"]
                         ["h" "i"]
                         ["l" "o"]])

                     (s [0 -1 0]
                        [["j" "k"]
                         ["m" "n"]])
                     (s [0 n n]
                        [["e" "f"]
                         ["b" "c"]])
                     (s [0 n (- n)]
                        [["e" "d"]])
                     (s [0 (- n) n]
                        [["a" "k"]])
                     (s [0 (- n) (- n)]
                        [["h" "g"]])
                     (f [["a" "b" "m"]
                         ["a" "m" "l"]
                         ["a" "l" "k"]

                         ["j" "k" "l"]
                         ["j" "l" "o"]
                         ["j" "o" "h"]
                         ["j" "h" "i"]

                         ["h" "o" "n"]
                         ["h" "n" "e"]
                         ["h" "e" "f"]
                         ["h" "f" "g"]

                         ["m" "b" "c"]

                         ["n" "m" "c"]
                         ["n" "c" "d"]
                         ["n" "d" "e"]])))}))
(defn letter-S
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 1 5])
                (p "b" [0 3 5])
                (p "c" [0 2 4])
                (p "d" [0 1 4])
                (p "e" [0 1 3])
                (p "f" [0 2 3])
                (p "g" [0 3 2])
                (p "h" [0 3 1])
                (p "i" [0 2 0])
                (p "j" [0 0 0])
                (p "k" [0 1 1])
                (p "l" [0 2 1])
                (p "m" [0 2 2])
                (p "n" [0 1 2])
                (p "o" [0 0 3])
                (p "p" [0 0 4]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["e" "f"]
                         ["k" "l"]])

                     (s [0 0 -1]
                        [["c" "d"]
                         ["o" "m"]
                         ["j" "i"]])

                     (s [0 1 0]
                        [["d" "e"]
                         ["g" "h"]])

                     (s [0 -1 0]
                        [["m" "l"]
                         ["p" "q"]])
                     (s [0 n n]
                        [["f" "g"]])
                     (s [0 n (- n)]
                        [["b" "c"]
                         ["h" "i"]])
                     (s [0 (- n) n]
                        [["a" "q"]
                         ["j" "k"]])
                     (s [0 (- n) (- n)]
                        [["o" "p"]])
                     (f [["a" "b" "c"]
                         ["a" "c" "d"]
                         ["a" "d" "q"]

                         ["p" "q" "d"]
                         ["p" "d" "e"]
                         ["p" "e" "o"]

                         ["m" "o" "e"]
                         ["m" "e" "f"]
                         ["m" "f" "g"]

                         ["l" "m" "g"]
                         ["l" "g" "h"]
                         ["l" "h" "i"]

                         ["k" "l" "i"]
                         ["k" "i" "j"]])))}))
(defn letter-T
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 3 5])
                (p "c" [0 3 4])
                (p "d" [0 2 4])
                (p "e" [0 2 0])
                (p "f" [0 1 0])
                (p "g" [0 1 4])
                (p "h" [0 0 4]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]])

                     (s [0 0 -1]
                        [["c" "d"]
                         ["e" "f"]
                         ["g" "h"]])

                     (s [0 1 0]
                        [["b" "c"]
                         ["d" "e"]])

                     (s [0 -1 0]
                        [["a" "h"]
                         ["f" "g"]])
                     (f [["a" "b" "c"]
                         ["a" "c" "h"]

                         ["g" "d" "e"]
                         ["g" "e" "f"]])))}))
(defn letter-U
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 1 5])
                (p "c" [0 2 4])
                (p "d" [0 1 4])
                (p "e" [0 2 5])
                (p "f" [0 3 5])
                (p "g" [0 3 1])
                (p "h" [0 2 0])
                (p "i" [0 1 0])
                (p "j" [0 0 1]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["c" "d"]
                         ["e" "f"]])

                     (s [0 0 -1]
                        [["i" "h"]])

                     (s [0 1 0]
                        [["f" "g"]
                         ["b" "c"]])

                     (s [0 -1 0]
                        [["d" "f"]
                         ["j" "a"]])

                     (s [0 n (- n)]
                        [["g" "h"]])
                     (s [0 (- n) (- n)]
                        [["i" "j"]])
                     (f [["a" "b" "c"]
                         ["a" "c" "j"]

                         ["e" "g" "f"]
                         ["e" "g" "d"]

                         ["c" "d" "h"]
                         ["c" "h" "i"]

                         ["j" "c" "i"]

                         ["d" "g" "h"]])))}))
(defn letter-V
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        cos (Math/cos (/ Math/PI 16))
        sin (Math/sin (/ Math/PI 16))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 1 5])
                (p "c" [0 1 4])
                (p "d" [0 2 4])
                (p "e" [0 2 5])
                (p "f" [0 3 5])
                (p "g" [0 3 4])
                (p "h" [0 2 0])
                (p "i" [0 1 0])
                (p "j" [0 0 4]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["e" "f"]])

                     (s [0 0 -1]
                        [["i" "h"]])

                     (s [0 1 0]
                        [["b" "c"]
                         ["f" "g"]])

                     (s [0 -1 0]
                        [["e" "d"]
                         ["a" "j"]])

                     (s [0 cos sin]
                        [["c" "h"]])
                     (s [0 cos (- sin)]
                        [["g" "h"]])
                     (s [0 (- cos) sin]
                        [["d" "i"]])
                     (s [0 (- cos) (- sin)]
                        [["i" "j"]])

                     (f [["a" "b" "c"]
                         ["a" "c" "j"]

                         ["e" "f" "g"]
                         ["e" "g" "d"]

                         ["i" "j" "c"]
                         ["i" "c" "h"]
                         ["i" "h" "g"]
                         ["i" "g" "d"]])))}))
(defn letter-W
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))
        cos (Math/cos (/ Math/PI 16))
        sin (Math/sin (/ Math/PI 16))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 1 5])
                (p "c" [0 1 2])
                (p "d" [0 1 3])
                (p "e" [0 2 3])
                (p "f" [0 2 2])
                (p "g" [0 2 5])
                (p "h" [0 3 5])
                (p "i" [0 3 1])
                (p "j" [0 3 0])
                (p "k" [0 2 1])
                (p "l" [0 1 1])
                (p "m" [0 0 0])
                (p "n" [0 0 1]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["d" "e"]
                         ["g" "h"]])

                     (s [0 1 0]
                        [["e" "f"]
                         ["h" "i"]
                         ["j" "i"]])

                     (s [0 -1 0]
                        [["d" "c"]
                         ["a" "n"]
                         ["m" "n"]])

                     (s [0 n n]
                        [["f" "i"]])
                     (s [0 (- n) n]
                        [["c" "n"]])
                     (s [0 n (- n)]
                        [["m" "l"]
                         ["f" "l"]])
                     (s [0 (- n) (- n)]
                        [["c" "k"]
                         ["j" "k"]])
                     (s [0 cos sin]
                        [["a" "l"]])
                     (s [0 cos (- sin)]
                        [["b" "n"]])
                     (s [0 (- cos) sin]
                        [["k" "h"]])
                     (s [0 (- cos) (- sin)]
                        [["g" "i"]])

                     (f [["a" "b" "n"]
                         ["a" "l" "n"]

                         ["m" "n" "l"]

                         ["e" "f" "g"]
                         ["e" "g" "d"]

                         ["c" "n" "l"]
                         ["c" "l" "f"]
                         ["c" "d" "e"]
                         ["c" "e" "f"]
                         ["c" "f" "k"]

                         ["i" "g" "h"]
                         ["i" "h" "k"]
                         ["i" "k" "f"]
                         ["i" "k" "j"]])))}))

(defn letter-X
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        cos (Math/cos (/ Math/PI 16))
        sin (Math/sin (/ Math/PI 16))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 1 5])
                (p "c" [0 2 5])
                (p "d" [0 3 5])
                (p "e" [0 3 0])
                (p "f" [0 2 0])
                (p "g" [0 1 0])
                (p "h" [0 0 0])

                (p "i" [0 1 4])
                (p "j" [0 2 4])
                (p "k" [0 2 1])
                (p "l" [0 1 1])

                (p "m" [0 1 3])
                (p "n" [0 2 3])
                (p "o" [0 2 2])
                (p "p" [0 1 2]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["c" "d"]])

                     (s [0 0 -1]
                        [["e" "f"]
                         ["g" "h"]])

                     (s [0 1 0]
                        [["j" "k"]])

                     (s [0 -1 0]
                        [["i" "l"]])

                     (s [0 cos sin]
                        [["b" "k"]
                         ["j" "e"]])
                     (s [0 cos (- sin)]
                        [["j" "g"]
                         ["d" "k"]])
                     (s [0 (- cos) sin]
                        [["c" "l"]
                         ["i" "h"]])
                     (s [0 (- cos) (- sin)]
                        [["a" "l"]
                         ["i" "f"]])

                     (f [["a" "b" "i"]

                         ["h" "l" "g"]

                         ["c" "d" "j"]

                         ["e" "f" "k"]

                         ["a" "i" "l"]
                         ["b" "i" "l"]

                         ["d" "j" "k"]
                         ["e" "j" "k"]

                         ["i" "o" "p"]
                         ["j" "o" "p"]

                         ["l" "m" "n"]
                         ["k" "m" "n"]

                         ["b" "k" "i"]
                         ["f" "k" "i"]

                         ["c" "j" "l"]
                         ["g" "j" "l"]])))}))
(defn letter-Y
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 1 5])
                (p "c" [0 1 3])
                (p "d" [0 2 3])
                (p "e" [0 3 4])
                (p "f" [0 3 5])
                (p "g" [0 3 3])
                (p "h" [0 2 2])
                (p "i" [0 2 0])
                (p "j" [0 1 0])
                (p "k" [0 1 2])
                (p "l" [0 0 3]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["c" "d"]
                         ["e" "f"]])

                     (s [0 0 -1]
                        [["i" "j"]])

                     (s [0 1 0]
                        [["f" "g"]
                         ["b" "c"]
                         ["h" "i"]])

                     (s [0 -1 0]
                        [["d" "f"]
                         ["j" "k"]
                         ["l" "a"]])

                     (s [0 n (- n)]
                        [["g" "h"]])
                     (s [0 (- n) (- n)]
                        [["k" "l"]])
                     (f [["a" "b" "c"]
                         ["a" "c" "l"]

                         ["e" "g" "f"]
                         ["e" "g" "d"]

                         ["c" "d" "i"]
                         ["c" "i" "j"]

                         ["l" "c" "k"]

                         ["d" "g" "h"]])))}))
(defn letter-Z
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale offset]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        p (anchor-fun2 v)
        s (anchor-fun3 v)
        f (anchor-fun4 (anchor-fun [0 0 0] scale))
        n (Math/cos (/ Math/PI 4))]

    {:vertices (conj
                (p "a" [0 0 5])
                (p "b" [0 3 5])
                (p "c" [0 3 4])
                (p "d" [0 1 1])
                (p "e" [0 3 1])
                (p "f" [0 3 0])
                (p "g" [0 0 0])
                (p "h" [0 0 1])
                (p "i" [0 2 4])
                (p "j" [0 0 4]))
     :tris (into [] (concat
                     (s [0 0 1]
                        [["a" "b"]
                         ["d" "e"]])

                     (s [0 0 -1]
                        [["f" "g"]
                         ["i" "j"]])

                     (s [0 1 0]
                        [["e" "f"]
                         ["b" "c"]])

                     (s [0 -1 0]
                        [["g" "h"]
                         ["a" "j"]])

                     (s [0 n (- n)]
                        [["c" "d"]])
                     (s [0 (- n) n]
                        [["h" "i"]])
                     (f [["i" "b" "a"]
                         ["i" "a" "j"]
                         ["i" "b" "c"]

                         ["d" "h" "g"]
                         ["d" "g" "f"]
                         ["d" "f" "e"]

                         ["c" "i" "h"]
                         ["c" "h" "d"]])))}))


(defn register-meshes!
  [univ]
  (w/register-mesh! univ :letter/a (letter-A))
  (w/register-mesh! univ :letter/b (letter-B))
  (w/register-mesh! univ :letter/c (letter-C))
  (w/register-mesh! univ :letter/d (letter-D))
  (w/register-mesh! univ :letter/e (letter-E))
  (w/register-mesh! univ :letter/f (letter-F))
  (w/register-mesh! univ :letter/g (letter-G))
  (w/register-mesh! univ :letter/h (letter-H))
  (w/register-mesh! univ :letter/i (letter-I))
  (w/register-mesh! univ :letter/j (letter-J))
  (w/register-mesh! univ :letter/k (letter-K))
  (w/register-mesh! univ :letter/l (letter-L))
  (w/register-mesh! univ :letter/m (letter-M))
  (w/register-mesh! univ :letter/n (letter-N))
  (w/register-mesh! univ :letter/o (letter-O))
  (w/register-mesh! univ :letter/p (letter-P))
  (w/register-mesh! univ :letter/q (letter-Q))
  (w/register-mesh! univ :letter/r (letter-R))
  (w/register-mesh! univ :letter/s (letter-S))
  (w/register-mesh! univ :letter/t (letter-T))
  (w/register-mesh! univ :letter/u (letter-U))
  (w/register-mesh! univ :letter/v (letter-V))
  (w/register-mesh! univ :letter/w (letter-W))
  (w/register-mesh! univ :letter/x (letter-X))
  (w/register-mesh! univ :letter/y (letter-Y))
  (w/register-mesh! univ :letter/z (letter-Z)))
