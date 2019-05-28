(ns yaw.keyboard
  (:refer-clojure :exclude [key]))

(defn action [action]
  (case action
    :pressed 1
    :released 0
    :repeat 2))

(defn key [keyval]
  (case keyval
    :up-arrow 265
    :down-arrow 264
    :z 87
    :s 83
    (throw (ex-info "No such key (GLFW constant)" {:keyval keyval}))))
