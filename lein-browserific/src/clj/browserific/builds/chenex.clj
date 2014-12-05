(ns browserific.builds.chenex
  "Writes the chenex build configurations"
  (:require [browserific.helpers.utils :as u]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]))


(defn write-build
  "Writes the chenex build for each platform"
  [class plat]
  {:source-paths ["src"]
   :output-path (str "intermediate/" plat)
   :rules {:filetype "cljs"
           :features #{class plat}
           :inner-transforms []
           :outer-transforms []}})

(defn repl-build
  "Creates the repl file for chenex"
  [class plat]
  {:filetype "cljs"
   :features #{class plat}
   :inner-transforms []
   :outer-transforms []})

(defn classify-platform
  "Tags a platform with a meta tag of: b, d, or m
  (for browser, desktop, or mobile)."
  [plat]
  (#(cond
     (u/browsers %) ["b" %]
     (u/desktop %) ["d" %]
     (u/mobile  %) ["m" %])
   plat))

(defn write-chenex-builds
  "Creates a builds/chenex-builds.clj for chenex to parse with and a
  builds/chenex-repl.clj for the REPL. Also reads config.edn to only
  build relevant platform configs."
  [& plat]
  (let [custom (classify-platform (first plat))
        repl-file (apply repl-build custom)
        all-platforms (mapv #(classify-platform %) u/platforms)
        builds (mapv #(apply write-build %)  all-platforms)]
    (do (io/make-parents "builds/chenex-builds.clj")
        (spit "builds/chenex-build.clj" (with-out-str (pprint builds)))
        (if custom (spit "builds/chenex-repl.clj" (with-out-str (pprint repl-file)))))))
