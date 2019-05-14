(ns yaw.keyboard
  (:refer-clojure :exclude [key]))

(defn key [keyval]
  (case keyval
      (:up-arrow ::up-arrow) 265
      (throw (ex-info "No such key (GLFW constant)" {:keyval keyval}))))
