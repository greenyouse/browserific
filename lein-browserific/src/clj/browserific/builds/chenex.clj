(ns browserific.builds.chenex
  "Writes the chenex build configurations"
  (:require [browserific.helpers.utils :as u]
            [clojure.java.io :as io]
            [deepfns.core :refer [<=>]]
            [deepfns.transitive :as t]
            [plugin-helpers.core :as p]))

(defn- classify-platform
  "Tags a platform with: b, d, m, or a special tag
  (for browser, desktop, or mobile)."
  [plat]
  (let [plat-k (keyword plat)]
    (cond
      (u/all-browsers plat) #{:b plat-k}
      (#{"windows32" "windows64"} plat) #{:d :windows plat-k}
      (#{"linux32" "linux64"} plat) #{:d :linux plat-k}
      (#{"osx32" "osx64"} plat) #{:d :osx plat-k}
      (u/all-mobile plat) #{:m plat-k})))

(def build>
  "Writes the chenex build for each platform"
  (<=> {:source-paths ["src"]
        :output-path (t/str> "target/intermediate/" :platform)
        :rules {:filetype "cljs"
                :features (t/=> :platform classify-platform)
                :inner-transforms (constantly [])}}))

(defn write-chenex-builds
  "Inserts chenex builds and chenex repl settings into the project.clj."
  [& plat]
  (let [repl (-> plat first classify-platform)
        builds (mapv #(build> {:platform %}) u/platforms)]
    (do (p/assoc-in-project [:chenex :builds] builds)
        (when plat (p/assoc-in-project [:chenex :repl] repl)))))
