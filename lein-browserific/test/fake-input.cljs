(ns some.file)

(defn stuff []
  (println "do stuff"))

[!+ [m d]
 (defn logger [e]
   (js/console.log e))]

;; doesn't work because the feature expression gets read :(
