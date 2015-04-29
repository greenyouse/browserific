(ns browserific.cordova.core)

(defn- tag-ns
  [k]
  (str "browserific.cordova.core/" k))

(defn- key->str
  [k]
  (-> k str (subs 1)))

(def ^:private localize
  "Tags each cordova lifecycle fn"
  (comp
    symbol
    tag-ns
    key->str))

;; make a macro to make this macro too (for general usage in personal lib)
(defmacro events
  "Registers all of Cordova's lifecycle events.

  * opts -- A map with keys that match the browserific fn names with corresponding
  callback fns as the values. A :ready fn must be provided."
  [opts]
  (let [file# (:file (meta &form))
        line# (:line (meta &form))
        col# (:column (meta &form))
        bad-value# (fn [v#]
                     (throw (Error. (str "Browserific Error: invalid value " v# ", all values must be a function, at " file# ":" line# ":" col#))))
        lcycle# #{:ready :pause :resume :back :menu :search :start-call
                  :end-call :vol-down :vol-up}
        bad-key# (fn [k#]
                   (throw (Error. (str "Browserific Error: invalid key " k# ", use the browserific lifecycle functions " lcycle# ", at " file# ":" line# ":" col#))))]
    `(do ~@(map (fn [[k# v#]]
                  (if (fn? (eval v#))
                    (if (lcycle# k#)
                      `(~(localize k#) ~v#)
                      (bad-key# k#))
                    (bad-value# v#)))
             opts))))

(comment
  (macroexpand '(events {:ready #(identity "set go") :bad (fn [] (identity "hi"))}))
  (macroexpand '(events {:ready (fn [] (identity "set go")) :vol-up (fn [] (identity "hi"))})))
