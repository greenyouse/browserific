(ns browserific.builds.browserific
  "Writes the browserific builds"
  (:require [browserific.helpers.utils :as u]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]))

;; TODO: update builds with new :main compiler option, update the template
;; HTML too after this is changed.
;; TODO: add a build that can target all of the builds at once, there is trouble
;; with duplicating output paths...

;; keeping it simple :)
;;
;; FIXME: watch out! the browser extensions may need a web_accessible_resource (aka permissions)
;; in manifest.json (or whatever) pointing to the sourcemap file
;; http://stackoverflow.com/questions/15097945/do-source-maps-work-for-chrome-extensions
;; https://developer.chrome.com/extensions/manifest/web_accessible_resources
(defn- write-platform
  "This creates a correct build configuration for each platform"
  [platform]
  (cond
   ;; browsers
   (u/browsers platform)
   ;; dev browser extensions
   [{:id "browser-dev"
     :source-paths ["dev" (str "intermediate/" platform "/" u/project-name "/background")]
     :compiler {:output-to (str "resources/extension/" platform "/src/background.js")
                :output-dir (str "resources/extension/" platform "/src/background")
                :source-map true
                :optimizations :none}}
    {:id "browser-dev"
     :source-paths ["dev" (str "intermediate/" platform "/" u/project-name "/content")]
     :compiler {:output-to (str "resources/extension/" platform "/src/content/content.js")
                :output-dir (str "resources/extension/" platform "/src/content")
                :source-map true
                :optimizations :none}}
    ;; release browser extensions
    {:id "browser-release"
     :source-paths [(str "intermediate/" platform "/" u/project-name "background")]
     :compiler {:output-to (str "resources/extension/" platform "/src/background.js")
                :source-map (str "resources/extension/" platform "/src/background.js.map")
                :optimizations :advanced
                :preamble ["react/react.min.js"]
                :externs ["react/externs/react.js"]}}
    {:id "browser-release"
     :source-paths [(str "intermediate/" platform "/" u/project-name "/content")]
     :compiler {:output-to (str "resources/extension/" platform "/src/content.js")
                :source-map (str "resources/extension/" platform "/src/content.js.map")
                :optimizations :advanced
                :preamble ["react/react.min.js"]
                :externs ["react/externs/react.js"]}}]

   ;; desktops
   (u/desktop platform)
   ;; dev desktop
   [{:id "desktop-dev"
     :source-paths ["dev" (str "intermediate/" platform)]
     :compiler {:output-to (str "resources/desktop/deploy/" platform "/js/app.js")
                :output-dir (str "resources/desktop/deploy/" platform "/js/out")
                :source-map true
                :optimizations :none}}
    ;; release desktop
    {:id "desktop-release"
     :source-paths ["dev" (str "intermediate/" platform)]
     :compiler {:output-to (str "resources/desktop/deploy/" platform "/js/app.js")
                :source-map (str "resources/desktop/deploy/" platform "/js/app.js.map")
                :optimizations :advanced
                :preamble ["react/react.min.js"]
                :externs ["react/externs/react.js"]}}]

   ;; mobile
   (u/mobile platform)
   ;; dev mobile
   [{:id "mobile-dev"
     :source-paths ["dev" (str "intermediate/" platform)]
     :compiler {:output-to (str "resources/mobile/" u/project-name "/platforms/" platform "/www/js/app.js")
                :output-dir (str "resources/mobile/" u/project-name "/platforms/" platform "/www/js/out")
                :source-map true
                :optimizations :none}}
    ;; release mobile
    {:id "mobile-release"
     :source-paths [(str "intermediate/" platform)]
     :compiler {:output-to (str "resources/mobile/" u/project-name "/platforms/" platform "/www/js/app.js")
                :source-map (str "resources/mobile/" u/project-name "/platforms/" platform "/www/js/app.js.map")
                :optimizations :advanced
                :preamble ["react/react.min.js"]
                :externs ["react/externs/react.js"]}}]))


(defn write-browserific-builds
  "Creates a builds/builds.clj file full of all the cljsbuild
  configurations. Also reads config.edn to only build relevant
  platform configs."
  [& plat]
  (let [platform (first plat)
        all-platforms u/platforms
        choosen-platform (if (#{"chrome" "firefox" "opera" "safari"} platform)
                           ["dev" (str "intermediate/" platform "/content")
                            (str "intermediate/" platform "/background")]
                           ["dev" (str "intermediate/" platform)])
        custom-build {:id "draft"
                      :source-paths choosen-platform
                      :compiler {:output-to "resources/public/js/app.js"
                                 :output-dir "resources/public/js/out"
                                 :source-map true
                                 :optimizations :none}}
        builds {:builds (reduce #(into % (write-platform %2))
                                (if platform [custom-build] [])
                                all-platforms)}]
    (do (io/make-parents "builds/browserific-build.clj")
        (spit "builds/browserific-build.clj" (with-out-str (pprint builds))))))
