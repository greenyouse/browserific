(ns browserific.builds.chenex
  "Writes the chenex build configurations"
  (:require [browserific.helpers.utils :as u]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]))

(defn- to-set [{:keys [class plat]}]
  "helper fn to convert the platform into a set"
  (-> (into #{} class) (conj (keyword plat))))

(defn- write-build
  "Writes the chenex build for each platform"
  [{:keys [class plat] :as opts}]
  {:source-paths ["src"]
   :output-path (str "intermediate/" plat)
   :rules {:filetype "cljs"
           :features (to-set opts)
           :inner-transforms []
           :outer-transforms []}})

(defn- classify-platform
  "Tags a platform with: b, d, m, or a special tag
  (for browser, desktop, or mobile)."
  [plat]
  (#(cond
     (u/browsers %) {:class [:b :windows] :plat %}
     (#{"windows32" "windows64"} %) {:class [:d :windows] :plat %}
     (#{"linux32" "linux64"} %) {:class [:d :linux] :plat %}
     (#{"osx32" "osx64"} %) {:class [:d :osx] :plat %}
     (u/mobile  %) {:class [:m] :plat %})
   plat))

(defn write-chenex-builds
  "Creates a builds/chenex-builds.clj for chenex to parse with and a
  builds/chenex-repl.clj for the REPL. Also reads config.edn to only
  build relevant platform configs."
  [& plat]
  (let [custom (classify-platform (first plat))
        c-set (to-set custom)
        all-platforms (mapv #(classify-platform %) u/platforms)
        builds (mapv write-build all-platforms)]
    (do (io/make-parents "builds/chenex-builds.clj")
        (spit "builds/chenex-build.clj" (with-out-str (pprint builds)))
        (if plat (spit "builds/chenex-repl.clj" (with-out-str (pprint c-set)))))))
