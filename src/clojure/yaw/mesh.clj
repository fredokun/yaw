(ns yaw.mesh)

(defn- anchor-fun
  "Given an anchor vertex and a scale vector,
  gives the function that would map
  a vertex placed in a unit cube centered at the origin
  to the vertex scaled by the scale and
  translated by the opposite of the origin"
  [p s]
  (partial mapv (fn [p s v] (- (* s v) p))
           p s))

(defn box-geometry
  "Generate a box mesh.

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)]

    {:vertices {:a (v [1 1 1])
                :b (v [1 -1 1])
                :c (v [-1 -1 1])
                :d (v [-1 1 1])
                :e (v [1 1 -1])
                :f (v [1 -1 -1])
                :g (v [-1 -1 -1])
                :h (v [-1 1 -1])}
     :tris [{:n [0 0 1]
             :v [:a :c :b]}
            {:n [0 0 1]
             :v [:a :d :c]}
            {:n [0 0 -1]
             :v [:e :f :h]}
            {:n [0 0 -1]
             :v [:f :g :h]}
            {:n [0 1 0]
             :v [:a :e :h]}
            {:n [0 1 0]
             :v [:a :h :d]}
            {:n [0 -1 0]
             :v [:b :c :f]}
            {:n [0 -1 0]
             :v [:f :c :g]}
            {:n [1 0 0]
             :v [:a :f :e]}
            {:n [1 0 0]
             :v [:a :b :f]}
            {:n [-1 0 0]
             :v [:d :h :c]}
            {:n [-1 0 0]
             :v [:c :h :g]}]}))

(defn pyramid-geometry
  "Generate a squared-based pyramid mesh

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        n (Math/cos (/ Math/PI 4))]

    {:vertices {:o (v [0 0 0])
                :s (v [0 0 1])
                :a (v [1 1 0])
                :b (v [1 -1 0])
                :c (v [-1 -1 0])
                :d (v [-1 1 0])}
     :tris [{:n [0 0 -1]
             :v [:o :a :b]}
            {:n [0 0 -1]
             :v [:o :b :c]}
            {:n [0 0 -1]
             :v [:o :c :d]}
            {:n [0 0 -1]
             :v [:o :d :a]}
            {:n (v [n 0 n])
             :v [:s :b :a]}
            {:n (v [0 (- n) n])
             :v [:s :c :b]}
            {:n (v [(- n) 0 n])
             :v [:s :d :c]}
            {:n (v [0 n n])
             :v [:s :a :d]}]}))

