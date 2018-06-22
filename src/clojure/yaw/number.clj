(ns yaw.number
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
(defn one
  "Generate a squared-based pyramid mesh

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        vect (anchor-fun [0 0 0] scale)]

    {:vertices {
                 :a (v [0 2 5])
                 :b (v [0 3 5])
                 :c (v [0 3 0])
                 :d (v [0 2 0])



                 :a1 (v [1 2 5])
                 :b1 (v [1 3 5])
                 :c1 (v [1 3 0])
                 :d1 (v [1 2 0])}



    :tris [{:n (vect [0 0 1])
             :v [:a :b1 :a1]}
           {:n (vect [0 0 1])
             :v [:a :b1 :b]}


            {:n (vect [0 0 -1])
             :v [:c :d1 :d]}
            {:n (vect [0 0 -1])
             :v [:c :d1 :c1]}

            {:n (vect [0 1 0])
             :v [:b :c1 :c]}
            {:n (vect [0 1 0])
             :v [:b :c1 :b1]}

            {:n (vect [0 -1 0])
             :v [:d :a1 :a]}
            {:n (vect [0 -1 0])
             :v [:d :a1 :d1]}

           {:n (vect [1 0 0])
             :v [:a1 :c1 :b1]}
           {:n (vect [1 0 0])
             :v [:a1 :c1 :d1]}

           {:n (vect [-1 0 0])
             :v [:a :c :b]}
           {:n (vect [-1 0 0])
             :v [:a :c :d]}

            ]}))

(defn two
  "Generate a squared-based pyramid mesh

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        vect (anchor-fun [0 0 0] scale)]

    {:vertices {
         :a (v [0 0 5])
         :b (v [0 3 5])
         :c (v [0 3 2])
         :d (v [0 1 2])
         :e (v [0 1 1])
         :f (v [0 3 1])
         :g (v [0 3 0])
         :h (v [0 0 0])
         :i (v [0 0 3])
         :j (v [0 2 3])
         :k (v [0 2 4])
         :l (v [0 0 4])

         :a1 (v [1 0 5])
         :b1 (v [1 3 5])
         :c1 (v [1 3 2])
         :d1 (v [1 1 2])
         :e1 (v [1 1 1])
         :f1 (v [1 3 1])
         :g1 (v [1 3 0])
         :h1 (v [1 0 0])
         :i1 (v [1 0 3])
         :j1 (v [1 2 3])
         :k1 (v [1 2 4])
         :l1 (v [1 0 4])}



     :tris [{:n (vect [0 0 1])
             :v [:a :b1 :a1]}
            {:n (vect [0 0 1])
             :v [:a :b1 :b]}
            {:n (vect [0 0 1])
             :v [:i :j1 :i1]}
            {:n (vect [0 0 1])
             :v [:i :j1 :j]}
            {:n (vect [0 0 1])
             :v [:e :f1 :e1]}
            {:n (vect [0 0 1])
             :v [:e :f1 :f]}

            {:n (vect [0 0 -1])
             :v [:l :k1 :k]}
            {:n (vect [0 0 -1])
             :v [:l :k1 :l]}
            {:n (vect [0 0 -1])
             :v [:c :d1 :d]}
            {:n (vect [0 0 -1])
             :v [:c :d1 :c1]}
            {:n (vect [0 0 -1])
             :v [:h :g1 :g]}
            {:n (vect [0 0 -1])
             :v [:h :g1 :h1]}



            {:n (vect [0 1 0])
             :v [:b :c1 :c]}
            {:n (vect [0 1 0])
             :v [:b :c1 :b1]}
            {:n (vect [0 1 0])
             :v [:f :g1 :g]}
            {:n (vect [0 1 0])
             :v [:f :g1 :f1]}


            {:n (vect [0 -1 0])
             :v [:h :i1 :i]}
            {:n (vect [0 -1 0])
             :v [:h :i1 :h1]}
            {:n (vect [0 -1 0])
             :v [:l :a1 :a]}
            {:n (vect [0 -1 0])
             :v [:l :a1 :l1]}

            {:n (vect [1 0 0])
             :v [:a1 :b1 :l1]}
            {:n (vect [1 0 0])
             :v [:b1 :k1 :l1]}
            {:n (vect [1 0 0])
             :v [:b1 :c1 :k1]}
            {:n (vect [1 0 0])
             :v [:c1 :j1 :k1]}
            {:n (vect [1 0 0])
             :v [:c1 :i1 :j1]}
            {:n (vect [1 0 0])
             :v [:c1 :d1 :i1]}
            {:n (vect [1 0 0])
             :v [:d1 :e1 :i1]}
            {:n (vect [1 0 0])
             :v [:e1 :h1 :i1]}
            {:n (vect [1 0 0])
             :v [:e1 :f1 :h1]}
            {:n (vect [1 0 0])
             :v [:f1 :g1 :h1]}

            {:n (vect [0 0 0])
             :v [:a :b :l]}
            {:n (vect [0 0 0])
             :v [:b :k :l]}
            {:n (vect [0 0 0])
             :v [:b :c :k]}
            {:n (vect [0 0 0])
             :v [:c :j :k]}
            {:n (vect [0 0 0])
             :v [:c :i :j]}
            {:n (vect [0 0 0])
             :v [:c :d :i]}
            {:n (vect [0 0 0])
             :v [:d :e :i]}
            {:n (vect [0 0 0])
             :v [:e :h :i]}
            {:n (vect [0 0 0])
             :v [:e :f :h]}
            {:n (vect [0 0 0])
             :v [:f :g :h]}

            ]}))
(defn five
  "Generate a squared-based pyramid mesh

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let  [[xa ya za] anchor [xs ys zs ] scale]
    (two (:keys [[xa (+ 3 ya) za] [xs (- ys) zs]]) )))
(defn three
  "Generate a squared-based pyramid mesh

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        vect (anchor-fun [0 0 0] scale)]

    {:vertices {
                 :a (v [0 0 5])
                 :b (v [0 3 5])
                 :c (v [0 3 0])
                 :d (v [0 0 0])
                 :e (v [0 1 0])
                 :f (v [0 2 1])
                 :g (v [0 2 2])
                 :h (v [0 1 2])
                 :i (v [0 1 3])
                 :j (v [0 2 3])
                 :k (v [0 2 4])
                 :l (v [0 0 4])



                 :a1 (v [1 0 5])
                 :b1 (v [1 3 5])
                 :c1 (v [1 3 0])
                 :d1 (v [1 0 0])
                 :e1 (v [1 1 0])
                 :f1 (v [1 2 1])
                 :g1 (v [1 2 2])
                 :h1 (v [1 1 2])
                 :i1 (v [1 1 3])
                 :j1 (v [1 2 3])
                 :k1 (v [1 2 4])
                 :l1 (v [1 0 4])}



     :tris [{:n (vect [0 0 1])
             :v [:a :b1 :a1]}
            {:n (vect [0 0 1])
             :v [:a :b1 :b]}
            {:n (vect [0 0 1])
             :v [:i :j1 :i1]}
            {:n (vect [0 0 1])
             :v [:i :j1 :j]}
            {:n (vect [0 0 1])
             :v [:e :f1 :e1]}
            {:n (vect [0 0 1])
             :v [:e :f1 :f]}

            {:n (vect [0 0 -1])
             :v [:l :k1 :k]}
            {:n (vect [0 0 -1])
             :v [:l :k1 :l]}
            {:n (vect [0 0 -1])
             :v [:c :d1 :d]}
            {:n (vect [0 0 -1])
             :v [:c :d1 :c1]}
            {:n (vect [0 0 -1])
             :v [:h :g1 :g]}
            {:n (vect [0 0 -1])
             :v [:h :g1 :h1]}



            {:n (vect [0 1 0])
             :v [:b :c1 :c]}
            {:n (vect [0 1 0])
             :v [:b :c1 :b1]}


            {:n (vect [0 -1 0])
             :v [:d :e1 :e]}
            {:n (vect [0 -1 0])
             :v [:d :e1 :d1]}
            {:n (vect [0 -1 0])
             :v [:f :g1 :g]}
            {:n (vect [0 -1 0])
             :v [:f :g1 :f1]}
            {:n (vect [0 -1 0])
             :v [:h :i1 :i]}
            {:n (vect [0 -1 0])
             :v [:h :i1 :h1]}
            {:n (vect [0 -1 0])
             :v [:j :k1 :k]}
            {:n (vect [0 -1 0])
             :v [:j :k1 :j1]}
            {:n (vect [0 -1 0])
             :v [:a :l1 :l]}
            {:n (vect [0 -1 0])
             :v [:a :l1 :a1]}

            {:n (vect [1 0 0])
             :v [:a1 :b1 :l1]}
            {:n (vect [1 0 0])
             :v [:b1 :k1 :l1]}
            {:n (vect [1 0 0])
             :v [:b1 :c1 :k1]}
            {:n (vect [1 0 0])
             :v [:c1 :j1 :k1]}
            {:n (vect [1 0 0])
             :v [:c1 :g1 :j1]}
            {:n (vect [1 0 0])
             :v [:c1 :f1 :g1]}
            {:n (vect [1 0 0])
             :v [:c1 :e1 :f1]}
            {:n (vect [1 0 0])
             :v [:c1 :d1 :e1]}
            {:n (vect [1 0 0])
             :v [:g1 :h1 :i1]}
            {:n (vect [1 0 0])
             :v [:g1 :i1 :j1]}

            {:n (vect [-1 0 0])
             :v [:a :b :l1]}
            {:n (vect [-1 0 0])
             :v [:b :k :l1]}
            {:n (vect [-1 0 0])
             :v [:b :c :k1]}
            {:n (vect [-1 0 0])
             :v [:c :j :k1]}
            {:n (vect [-1 0 0])
             :v [:c :g :j1]}
            {:n (vect [-1 0 0])
             :v [:c :f :g1]}
            {:n (vect [-1 0 0])
             :v [:c :e :f1]}
            {:n (vect [-1 0 0])
             :v [:c :d :e1]}
            {:n (vect [-1 0 0])
             :v [:g :h :i1]}
            {:n (vect [-1 0 0])
             :v [:g :i :j1]}

            ]}))
(defn four
  "Generate a squared-based pyramid mesh

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        vect (anchor-fun [0 0 0] scale)]

    {:vertices {
                 :a (v [0 0 5])
                 :b (v [0 1 5])
                 :c (v [0 1 3])
                 :d (v [0 2 3])
                 :e (v [0 2 5])
                 :f (v [0 3 5])
                 :g (v [0 3 0])
                 :h (v [0 2 0])
                 :i (v [0 2 2])
                 :j (v [0 0 2])



                 :a1 (v [1 0 5])
                 :b1 (v [1 1 5])
                 :c1 (v [1 1 3])
                 :d1 (v [1 2 3])
                 :e1 (v [1 2 5])
                 :f1 (v [1 3 5])
                 :g1 (v [1 3 0])
                 :h1 (v [1 2 0])
                 :i1 (v [1 2 2])
                 :j1 (v [1 0 2])}



     :tris [{:n (vect [0 0 1])
             :v [:a :b1 :a1]}
            {:n (vect [0 0 1])
             :v [:a :b1 :b]}
            {:n (vect [0 0 1])
             :v [:c :d1 :c1]}
            {:n (vect [0 0 1])
             :v [:c :d1 :d]}
            {:n (vect [0 0 1])
             :v [:e :f1 :e1]}
            {:n (vect [0 0 1])
             :v [:e :f1 :f]}



            {:n (vect [0 0 -1])
             :v [:h :g1 :g]}
            {:n (vect [0 0 -1])
             :v [:h :g1 :h1]}
            {:n (vect [0 0 -1])
             :v [:i :j1 :j]}
            {:n (vect [0 0 -1])
             :v [:i :j1 :i1]}

            {:n (vect [0 1 0])
             :v [:b :c1 :c]}
            {:n (vect [0 1 0])
             :v [:b :c1 :b1]}
            {:n (vect [0 1 0])
             :v [:f :g1 :g]}
            {:n (vect [0 1 0])
             :v [:f :g1 :f1]}



            {:n (vect [0 -1 0])
             :v [:d :e1 :e]}
            {:n (vect [0 -1 0])
             :v [:d :e1 :d1]}
            {:n (vect [0 -1 0])
             :v [:i :h1 :h]}
            {:n (vect [0 -1 0])
             :v [:i :h1 :i1]}
            {:n (vect [0 -1 0])
             :v [:j :a1 :a]}
            {:n (vect [0 -1 0])
             :v [:j :a1 :j1]}


            {:n (vect [1 0 0])
             :v [:a1 :b1 :c1]}
            {:n (vect [1 0 0])
             :v [:a1 :c1 :j1]}
            {:n (vect [1 0 0])
             :v [:c1 :i1 :j1]}
            {:n (vect [1 0 0])
             :v [:c1 :d1 :i1]}
            {:n (vect [1 0 0])
             :v [:e1 :f1 :g1]}
            {:n (vect [1 0 0])
             :v [:e1 :g1 :h1]}

            {:n (vect [-1 0 0])
             :v [:a :b :c]}
            {:n (vect [-1 0 0])
             :v [:a :c :j]}
            {:n (vect [-1 0 0])
             :v [:c :i :j]}
            {:n (vect [-1 0 0])
             :v [:c :d :i]}
            {:n (vect [-1 0 0])
             :v [:e :f :g]}
            {:n (vect [-1 0 0])
             :v [:e :g :h]}



            ]}))


(defn six
  "Generate a squared-based pyramid mesh

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        vect (anchor-fun [0 0 0] scale)]

    {:vertices {
                 :a (v [0 0 5])
                 :b (v [0 3 5])
                 :c (v [0 3 4])
                 :d (v [0 1 4])
                 :e (v [0 1 3])
                 :f (v [0 3 3])
                 :g (v [0 3 0])
                 :h (v [0 0 0])


                 :i (v [0 1 1])
                 :j (v [0 1 2])
                 :k (v [0 2 2])
                 :l (v [0 2 1])


                 :a1 (v [1 0 5])
                 :b1 (v [1 3 5])
                 :c1 (v [1 3 4])
                 :d1 (v [1 1 4])
                 :e1 (v [1 1 3])
                 :f1 (v [1 3 3])
                 :g1 (v [1 3 0])
                 :h1 (v [1 0 0])


                 :i1 (v [1 1 1])
                 :j1 (v [1 1 2])
                 :k1 (v [1 2 2])
                 :l1 (v [1 2 1])
                 }



     :tris [{:n (vect [0 0 1])
             :v [:a :b1 :a1]}
            {:n (vect [0 0 1])
             :v [:a :b1 :b]}
            {:n (vect [0 0 1])
             :v [:e :f1 :e1]}
            {:n (vect [0 0 1])
             :v [:e :f1 :f]}
            {:n (vect [0 0 1])
             :v [:i :l1 :i1]}
            {:n (vect [0 0 1])
             :v [:i :l1 :l]}


            {:n (vect [0 0 -1])
             :v [:d :c1 :c]}
            {:n (vect [0 0 -1])
             :v [:d :c1 :d1]}
            {:n (vect [0 0 -1])
             :v [:h :g1 :g]}
            {:n (vect [0 0 -1])
             :v [:h :g1 :h1]}
            {:n (vect [0 0 -1])
             :v [:j :k1 :k]}
            {:n (vect [0 0 -1])
             :v [:j :k1 :j1]}



            {:n (vect [0 1 0])
             :v [:b :c1 :c]}
            {:n (vect [0 1 0])
             :v [:b :c1 :b1]}
            {:n (vect [0 1 0])
             :v [:d :e1 :e]}
            {:n (vect [0 1 0])
             :v [:d :e1 :d1]}
            {:n (vect [0 1 0])
             :v [:f :g1 :g]}
            {:n (vect [0 1 0])
             :v [:f :g1 :f1]}
            {:n (vect [0 1 0])
             :v [:j :i1 :i]}
            {:n (vect [0 1 0])
             :v [:j :i1 :j1]}



            {:n (vect [0 -1 0])
             :v [:k :l1 :l]}
            {:n (vect [0 -1 0])
             :v [:k :l1 :l1]}
            {:n (vect [0 -1 0])
             :v [:a :h1 :h]}
            {:n (vect [0 -1 0])
             :v [:a :h1 :a1]}


            {:n (vect [1 0 0])
             :v [:a1 :b1 :c1]}
            {:n (vect [1 0 0])
             :v [:a1 :c1 :d1]}
            {:n (vect [1 0 0])
             :v [:a1 :d1 :e1]}
            {:n (vect [1 0 0])
             :v [:a1 :e1 :h1]}
            {:n (vect [1 0 0])
             :v [:e1 :h1 :j1]}
            {:n (vect [1 0 0])
             :v [:e1 :f1 :j1]}
            {:n (vect [1 0 0])
             :v [:f1 :j1 :k1]}
            {:n (vect [1 0 0])
             :v [:f1 :g1 :k1]}
            {:n (vect [1 0 0])
             :v [:g1 :l1 :k1]}
            {:n (vect [1 0 0])
             :v [:g1 :h1 :l1]}
            {:n (vect [1 0 0])
             :v [:h1 :i1 :l1]}

            {:n (vect [-1  0 0])
             :v [:a  :b  :c ]}
            {:n (vect [-1  0 0])
             :v [:a  :c  :d ]}
            {:n (vect [-1  0 0])
             :v [:a  :d  :e ]}
            {:n (vect [-1  0 0])
             :v [:a  :e  :h ]}
            {:n (vect [-1  0 0])
             :v [:e  :h  :j ]}
            {:n (vect [-1  0 0])
             :v [:e  :f  :j ]}
            {:n (vect [-1  0 0])
             :v [:f  :j  :k ]}
            {:n (vect [-1  0 0])
             :v [:f  :g  :k ]}
            {:n (vect [-1  0 0])
             :v [:g  :l  :k ]}
            {:n (vect [-1  0 0])
             :v [:g  :h  :l ]}
            {:n (vect [-1  0 0])
             :v [:h  :i  :l ]}



            ]}))
(defn nine
  "Generate a squared-based pyramid mesh

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [[xa ya za] anchor [xs ys zs ] scale]
    (two (:keys [xa (+ 3 ya) (+ 5 za)] [xs (- ys) (- zs)]) )))

(defn seven
  "Generate a squared-based pyramid mesh

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        vect (anchor-fun [0 0 0] scale)]

    {:vertices {
                 :a (v [0 0 5])
                 :b (v [0 3 5])
                 :c (v [0 3 0])
                 :d (v [0 2 0])
                 :e (v [0 2 4])
                 :f (v [0 0 4])


                 :a1 (v [0 0 5])
                 :b1 (v [0 3 5])
                 :c1 (v [0 3 0])
                 :d1 (v [0 2 0])
                 :e1 (v [0 2 4])
                 :f1 (v [0 0 4])}



     :tris [{:n (vect [0 0 1])
             :v [:a :b1 :a1]}
            {:n (vect [0 0 1])
             :v [:a :b1 :b]}


            {:n (vect [0 0 -1])
             :v [:c :d1 :d]}
            {:n (vect [0 0 -1])
             :v [:c :d1 :c1]}
            {:n (vect [0 0 -1])
             :v [:e :f1 :f]}
            {:n (vect [0 0 -1])
             :v [:e :f1 :e1]}

            {:n (vect [0 1 0])
             :v [:b :c1 :c]}
            {:n (vect [0 1 0])
             :v [:b :c1 :b1]}

            {:n (vect [0 -1 0])
             :v [:d :e1 :e]}
            {:n (vect [0 -1 0])
             :v [:d :e1 :d1]}
            {:n (vect [0 -1 0])
             :v [:f :a1 :a]}
            {:n (vect [0 -1 0])
             :v [:f :a1 :a1]}

            {:n (vect [1 0 0])
             :v [:a1 :e1 :f1]}
            {:n (vect [1 0 0])
             :v [:a1 :e1 :b1]}
            {:n (vect [1 0 0])
             :v [:b1 :c1 :e1]}
            {:n (vect [1 0 0])
             :v [:c1 :d1 :e1]}

            {:n (vect [-1 0 0])
             :v [:a  :e  :f ]}
            {:n (vect [-1 0 0])
             :v [:a  :e  :b ]}
            {:n (vect [-1 0 0])
             :v [:b  :c  :e ]}
            {:n (vect [-1 0 0])
             :v [:c  :d  :e ]}

            ]}))


(defn eight
  "Generate a squared-based pyramid mesh

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        vect (anchor-fun [0 0 0] scale)]

    {:vertices {
                 :a (v [0 0 5])
                 :b (v [0 3 5])
                 :c (v [0 3 0])
                 :d (v [0 0 0])

                 :e (v [0 1 1])
                 :f (v [0 1 2])
                 :g (v [0 2 2])
                 :h (v [0 2 1])

                 :i (v [0 3 1])
                 :j (v [0 3 2])
                 :k (v [0 4 2])
                 :l (v [0 4 1])


                 :a1 (v [0 0 5])
                 :b1 (v [0 3 5])
                 :c1 (v [0 3 0])
                 :d1 (v [0 0 0])

                 :e1 (v [0 1 1])
                 :f1 (v [0 1 2])
                 :g1 (v [0 2 2])
                 :h1 (v [0 2 1])

                 :i1 (v [0 3 1])
                 :j1 (v [0 3 2])
                 :k1 (v [0 4 2])
                 :l1 (v [0 4 1])
                 }



     :tris [{:n (vect [0 0 1])
             :v [:a :b1 :a1]}
            {:n (vect [0 0 1])
             :v [:a :b1 :b]}
            {:n (vect [0 0 1])
             :v [:e :h1 :e1]}
            {:n (vect [0 0 1])
             :v [:e :h1 :h]}
            {:n (vect [0 0 1])
             :v [:i :l1 :i1]}
            {:n (vect [0 0 1])
             :v [:i :l1 :l]}


            {:n (vect [0 0 -1])
             :v [:d :c1 :c]}
            {:n (vect [0 0 -1])
             :v [:d :c1 :d1]}
            {:n (vect [0 0 -1])
             :v [:f :g1 :g]}
            {:n (vect [0 0 -1])
             :v [:f :g1 :f1]}
            {:n (vect [0 0 -1])
             :v [:j :k1 :k]}
            {:n (vect [0 0 -1])
             :v [:j :k1 :j1]}



            {:n (vect [0 1 0])
             :v [:b :c1 :c]}
            {:n (vect [0 1 0])
             :v [:b :c1 :b1]}
            {:n (vect [0 1 0])
             :v [:f :e1 :e]}
            {:n (vect [0 1 0])
             :v [:f :e1 :f1]}
            {:n (vect [0 1 0])
             :v [:j :i1 :i]}
            {:n (vect [0 1 0])
             :v [:j :i1 :j1]}

            {:n (vect [0 -1 0])
             :v [:a :d1 :d]}
            {:n (vect [0 -1 0])
             :v [:a :d1 :a1]}
            {:n (vect [0 -1 0])
             :v [:k :l1 :l]}
            {:n (vect [0 -1 0])
             :v [:k :l1 :k1]}
            {:n (vect [0 -1 0])
             :v [:g :h1 :h]}
            {:n (vect [0 -1 0])
             :v [:g :h1 :g1]}



            {:n (vect [1 0 0])
             :v [:a1 :b1 :j1]}
            {:n (vect [1 0 0])
             :v [:b1 :j1 :k1]}
            {:n (vect [1 0 0])
             :v [:b1 :k1 :l1]}
            {:n (vect [1 0 0])
             :v [:b1 :c1 :l1]}
            {:n (vect [1 0 0])
             :v [:c1 :g1 :l1]}
            {:n (vect [1 0 0])
             :v [:c1 :g1 :h1]}
            {:n (vect [1 0 0])
             :v [:c1 :d1 :h1]}
            {:n (vect [1 0 0])
             :v [:d1 :e1 :h1]}
            {:n (vect [1 0 0])
             :v [:d1 :e1 :f1]}
            {:n (vect [1 0 0])
             :v [:d1 :f1 :i1]}
            {:n (vect [1 0 0])
             :v [:d1 :i1 :a1]}
            {:n (vect [1 0 0])
             :v [:a1 :i1 :j1]}

            {:n (vect [-1 0 0])
             :v [:a :b :j]}
            {:n (vect [-1 0 0])
             :v [:b :j :k]}
            {:n (vect [-1 0 0])
             :v [:b :k :l]}
            {:n (vect [-1 0 0])
             :v [:b :c :l]}
            {:n (vect [-1 0 0])
             :v [:c :g :l]}
            {:n (vect [-1 0 0])
             :v [:c :g :h]}
            {:n (vect [-1 0 0])
             :v [:c :d :h]}
            {:n (vect [-1 0 0])
             :v [:d :e :h]}
            {:n (vect [-1 0 0])
             :v [:d :e :f]}
            {:n (vect [-1 0 0])
             :v [:d :f :i]}
            {:n (vect [-1 0 0])
             :v [:d :i :a]}
            {:n (vect [-1 0 0])
             :v [:a :i :j]}



            ]}))

(defn zero
  "Generate a squared-based pyramid mesh

  Create a low-level geometry object from a high-level simple keyword map."
  [& {:keys [anchor scale]
      :or {anchor [0 0 0]
           scale [1 1 1]}}]
  (let [v (anchor-fun anchor scale)
        vect (anchor-fun [0 0 0] scale)]

    {:vertices {
                 :a (v [0 0 5])
                 :b (v [0 3 5])
                 :c (v [0 3 0])
                 :d (v [0 0 0])

                 :e (v [0 1 1])
                 :f (v [0 1 4])
                 :g (v [0 2 4])
                 :h (v [0 2 1])


                 :a1 (v [0 0 5])
                 :b1 (v [0 3 5])
                 :c1 (v [0 3 0])
                 :d1 (v [0 0 0])

                 :e1 (v [0 1 1])
                 :f1 (v [0 1 4])
                 :g1 (v [0 2 4])
                 :h1 (v [0 2 1])
                 }



     :tris [{:n (vect [0 0 1])
             :v [:a :b1 :a1]}
            {:n (vect [0 0 1])
             :v [:a :b1 :b]}
            {:n (vect [0 0 1])
             :v [:e :h1 :e1]}
            {:n (vect [0 0 1])
             :v [:e :h1 :h]}


            {:n (vect [0 0 -1])
             :v [:d :c1 :c]}
            {:n (vect [0 0 -1])
             :v [:d :c1 :d1]}
            {:n (vect [0 0 -1])
             :v [:f :g1 :g]}
            {:n (vect [0 0 -1])
             :v [:f :g1 :f1]}



            {:n (vect [0 1 0])
             :v [:b :c1 :c]}
            {:n (vect [0 1 0])
             :v [:b :c1 :b1]}
            {:n (vect [0 1 0])
             :v [:f :e1 :e]}
            {:n (vect [0 1 0])
             :v [:f :e1 :f1]}

            {:n (vect [0 -1 0])
             :v [:a :d1 :d]}
            {:n (vect [0 -1 0])
             :v [:a :d1 :a1]}
            {:n (vect [0 -1 0])
             :v [:g :h1 :h]}
            {:n (vect [0 -1 0])
             :v [:g :h1 :g1]}


            {:n (vect [1 0 0])
             :v [:a1 :e1 :f1]}
            {:n (vect [1 0 0])
             :v [:a1 :f1 :g1]}
            {:n (vect [1 0 0])
             :v [:a1 :b1 :g1]}
            {:n (vect [1 0 0])
             :v [:b1 :c1 :g1]}
            {:n (vect [1 0 0])
             :v [:c1 :g1 :h1]}
            {:n (vect [1 0 0])
             :v [:c1 :e1 :h1]}
            {:n (vect [1 0 0])
             :v [:c1 :d1 :e1]}
            {:n (vect [1 0 0])
             :v [:a1 :d1 :e1]}

            {:n (vect [-1 0 0])
             :v [:a  :e  :f ]}
            {:n (vect [-1 0 0])
             :v [:a  :f  :g ]}
            {:n (vect [-1 0 0])
             :v [:a  :b  :g ]}
            {:n (vect [-1 0 0])
             :v [:b  :c  :g ]}
            {:n (vect [-1 0 0])
             :v [:c  :g  :h ]}
            {:n (vect [-1 0 0])
             :v [:c  :e  :h ]}
            {:n (vect [-1 0 0])
             :v [:c  :d  :e ]}
            {:n (vect [-1 0 0])
             :v [:a  :d  :e ]}



            ]}))


(defn register-meshes!
  [univ]
  (w/register-mesh! univ :number/one (one))
  (w/register-mesh! univ :number/two (two))
  (w/register-mesh! univ :number/three (three))
  (w/register-mesh! univ :number/four (four))
  (w/register-mesh! univ :number/five (five))
  (w/register-mesh! univ :number/six (six))
  (w/register-mesh! univ :number/seven (seven))
  (w/register-mesh! univ :number/eight (eight))
  (w/register-mesh! univ :number/nine (nine))
  (w/register-mesh! univ :number/zero (zero)))
