(ns yaw.ratom
  (:import (clojure.lang Atom
                         IRef
                         IAtom
                         IDeref
                         IFn
                         ISeq)))

(defrecord RAtom [^Atom atom]
  IRef
  ;;(addWatch ^IRef [this ^Object key ^IFn callback]
  (addWatch [this key callback]
    (add-watch atom ::reaction (fn [key ref old-state new-state]
                                 (callback this (::controller new-state) (::dep-comps new-state) (::value old-state) (::value new-state)))))

  IAtom
  (swap [this ^IFn f]
    (swap! atom (fn [m]
                  (update m ::value f))))

  (swap [this ^IFn f ^Object arg]
    (swap! atom (fn [m]
                  (assoc m ::value (f (::value m)) arg))))

  (swap [this ^IFn f ^Object arg1 ^Object arg2]
    (swap! atom (fn [m]
                  (assoc m ::value (f (::value m) arg1 arg2)))))

  (swap [this ^IFn f ^Object x ^Object y ^ISeq args]
    (swap! atom (fn [m]
                  (assoc m ::value (apply f (::value m) x y args)))))

  IDeref
  (deref [this]
    (::value (deref atom))))

(defmethod print-method RAtom [v ^java.io.Writer w]
  (.write w (str "#reagent-like.ratom/Ratom[" (:atom v) "]")))


(defn make-ratom [controller reaction-handler init-val]
  (let [at (atom {::controller controller
                  ::dep-comps #{}
                  ::value init-val})
        rat (->RAtom at)]
    (add-watch rat ::reaction reaction-handler)
    rat))

(defn depend-component! [ratom component]
  ;;(println "[depend-component] ratom=" ratom ", component=" component)
  (swap! (:atom ratom) (fn [state]
                              ;; (println "state =" state)
                              (update state ::dep-comps #(conj % component)))))

(defn reactive-atom? [v]
  (instance? RAtom v))
