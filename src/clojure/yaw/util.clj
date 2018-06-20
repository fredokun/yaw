(ns yaw.util)


(defn explode
  "Explodes a vector [x y z] tuple into (:x x :y y :z z)"
  [[x y z]]
  (list :x x :y y :z z))


(defn -?
  "Takes two nilable numbers,"
  [a b]
  (if (nil? a)
    (if (nil? b)
      nil
      (- b))
    (if (nil? b)
      a
      (- a b))))



(def color-kw
  {:black [0 0 0]
   :red [1 0 0]
   :blue [0 0 1]
   :green [0 1 0]
   :yellow [1 1 0]
   :cyan [0 1 1]
   :magenta [1 0 1]
   :white [1 1 1]})

