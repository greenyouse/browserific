(ns browserific.builds.browserific
  "Writes the browserific builds"
  (:require [browserific.helpers.utils :as u]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]))

;; FIXME: watch out! the browser extensions may need a web_accessible_resource (aka permissions)
;; in manifest.json (or whatever) pointing to the sourcemap file
;; http://stackoverflow.com/questions/15097945/do-source-maps-work-for-chrome-extensions
;; https://developer.chrome.com/extensions/manifest/web_accessible_resources
(defn- build-template
  "A general template for lein-cljsbuild profiles"
  [env platform]
  (fn [id profile type]
    (let [root (str "resources/" env "/" platform "/js/" )
          to  (str root id ".js")
          main (cond
                 (= "app" id) (symbol (str u/project-name ".core"))
                 (= "content" id) (symbol (str u/project-name ".content.core"))
                 (= "background" id) (symbol (str u/project-name ".background.core")))
          src (if (not= "app" id) (str "intermediate/" platform "/" id)
                  (str "intermediate/" platform))
          asset (str "js/" env)
          assoc-build (fn [coll]
                        (reduce (fn [acc [k v]]
                                  (assoc-in acc k v))
                          {} coll))]
      (case type
        "dev" (assoc-build
                [[[:compiler :optimizations] :none]
                 [[:compiler :main] main]
                 [[:compiler :output-to] to]
                 [[:compiler :output-dir] (str root profile "-" id)]
                 [[:compiler :asset-path] asset]
                 [[:compiler :source-map] true]
                 [[:source-paths] ["dev" src]]
                 [[:id] (str profile "-dev")]])

        "test" (assoc-build
                 [[[:compiler :optimizations] :none]
                  [[:compiler :main] main]
                  [[:compiler :output-dir] (str root profile "-" id "-test")]
                  [[:compiler :source-map] true]
                  [[:source-paths] [src (str"test/" platform)]]
                  [[:id] (str profile "-test")]])

        "release" (assoc-build
                    [[[:compiler :optimizations] :advanced]
                     [[:compiler :main] main]
                     [[:compiler :output-to] to]
                     [[:compiler :output-dir] (str root profile "-" id "-release")]
                     [[:compiler :source-map] (str to ".map")]
                     [[:source-paths] [src]]
                     [[:id] profile]])))))

;; env := 'extension' | 'desktop' | 'mobile'
;; platform := [vendor-id]
;; id := 'app' | 'background' | 'content'
;; profile := platform | 'all' | env
;; type := 'dev' | 'test' | 'release'
(defn- write-platform
  "This creates a correct build configuration for each platform"
  [platform multi]
  (let [id (if multi ["background" "content"] ["app"])
        build (fn [env]
                (let [b (build-template env platform)]
                  (for [i id
                        p [platform "all" env]
                        t ["dev" "test" "release"]]
                    (b i p t))))]
    (cond
      (u/mobile platform) (build "mobile")
      (u/desktop platform) (build "desktop")
      (u/browsers platform) (build "extension"))))

(defn write-browserific-builds
  "Creates a builds/builds.clj file full of all the cljsbuild
  configurations. Also reads config.edn to only build relevant
  platform configs."
  [{:keys [draft multi]}]
  (println (str draft " -- " multi))
  (let [custom-build (if multi
                       [{:id "draft-background"
                         :source-paths ["dev" (str "intermediate/" draft "/background")]
                         :compiler {:output-to "resources/public/js/background.js"
                                    :output-dir "resources/public/js/background"
                                    :source-map true
                                    :optimizations :none
                                    :asset-path "js/background"
                                    :main (symbol (str u/project-name ".background.core"))}}
                        {:id "draft-content"
                         :source-paths ["dev" (str "intermediate/" draft "/content")]
                         :compiler {:output-to "resources/public/js/content.js"
                                    :output-dir "resources/public/js/content"
                                    :source-map true
                                    :optimizations :none
                                    :asset-path "js/content"
                                    :main (symbol (str u/project-name ".content.core"))}}]
                       {:id "draft"
                        :source-paths ["dev" (str "intermediate/" draft)]
                        :compiler {:output-to "resources/public/js/app.js"
                                   :output-dir "resources/public/js/out"
                                   :source-map true
                                   :optimizations :none
                                   :asset-path "js/out"
                                   :main (symbol (str u/project-name ".core"))}})
        builds {:builds (reduce #(into % (if (multi %2)
                                           (write-platform %2 true)
                                           (write-platform %2 false)))
                          (if draft [custom-build] [])
                          u/platforms)}]
    (do (io/make-parents "builds/browserific-build.clj")
        (spit "builds/browserific-build.clj" (with-out-str (pprint builds))))))
