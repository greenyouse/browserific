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
           :features #{class (keyword plat)}
           :inner-transforms []
           :outer-transforms []}})

(defn classify-platform
  "Tags a platform with: b, d, m, or a special tag
  (for browser, desktop, or mobile)."
  [plat]
  (#(cond
     (u/browsers %) [:b %]
     (#{"windows32" "windows64"} %) [:d :windows %]
     (#{"linux32" "linux64"} %) [:d :linux %]
     (#{"osx32" "osx64"} %) [:d :osx %]
     (u/mobile  %) [:m %])
   plat))

(defn write-chenex-builds
  "Creates a builds/chenex-builds.clj for chenex to parse with and a
  builds/chenex-repl.clj for the REPL. Also reads config.edn to only
  build relevant platform configs."
  [& plat]
  (let [custom (classify-platform (first plat))
        c-set (set (mapv keyword custom))
        all-platforms (mapv #(classify-platform %) u/platforms)
        builds (mapv #(apply write-build %)  all-platforms)]
    (do (io/make-parents "builds/chenex-builds.clj")
        (spit "builds/chenex-build.clj" (with-out-str (pprint builds)))
        (if plat (spit "builds/chenex-repl.clj" (with-out-str (pprint c-set)))))))
