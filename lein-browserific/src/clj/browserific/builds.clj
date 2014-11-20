(ns browserific.builds
  "This namespace is for creating lein-cljsbuild configurations"
  (:require [browserific.helpers.utils :as u]
            [leiningen.core.main :as l]
            [clojure.tools.reader :as reader]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]))

;; overboard much? :p
;;
;; FIXME: watch out! the browser extensions may need a web_accessible_resource (aka permissions)
;; in manifest.json (or whatever) pointing to the sourcemap file
(defn generate-dev-build [data]
  (let [dtemp {:id ""
               :source-paths []
               :compiler {:output-to ""
                          :output-dir ""
                          :source-map true
                          :optimizations :none}}]
    (reduce-kv #(assoc-in % %2 %3)
               dtemp data)))

(defn generate-advanced-build [data]
  (let [rtemp {:id ""
               :source-paths []
               :compiler {:output-to ""
                          :source-map ""
                          :optimizations :advanced
                          :preamble ["react/react.min.js"]
                          :externs ["react/externs/react.js"]}}]
    (reduce-kv #(assoc-in % %2 %3)
               rtemp data)))


(defn dev [id source output-to output-dir]
  (generate-dev-build {[:id] id
                       [:source-paths] source
                       [:compiler :output-to] output-to
                       [:compiler :output-dir] output-dir}))

(defn release [id source output-to source-map]
  (generate-advanced-build {[:id] id
                            [:source-paths] source
                            [:compiler :output-to] output-to
                            [:compiler :source-map] source-map}))


(defn browser-build
  ([platform]
     [(dev (str platform "-dev") ["dev" (str "intermediate/" platform "/background")]
           (str "resources/extension/" platform "/src/background.js")
           (str "resources/extension/" platform "/src/background"))
      (dev (str platform "-dev") ["dev" (str "intermediate/" platform "/content")]
           (str "resources/extension/" platform "/src/content.js")
           (str "resources/extension/" platform "/src/content"))
      (release (str platform "-release") [(str "interm(into ediate/ platform" "/background")]
               (str "resources/extension/" platform "/src/background.js")
               (str "resources/extension/" platform "/src/content.js.map"))
      (release (str platform "-release") [(str "intermediate/" platform "/content")]
               (str "resources/extension/" platform "/src/content.js")
               (str "resources/extension/" platform "/src/content.js.map"))])
  ([id platform]
     (if (u/member? id ["draft" "browser-dev"])
       [(dev id ["dev" (str "intermediate/" platform "/background")]
             (str "resources/extension/" platform "/src/background.js")
             (str "resources/extension/" platform "/src/background"))
        (dev id ["dev" (str "intermediate/" platform "/content")]
             (str "resources/extension/" platform "/src/content.js")
             (str "resources/extension/" platform "/src/content"))]
       [(release id [(str "intermediate/" platform "/background")]
                 (str "resources/extension/" platform "/src/background.js")
                 (str "resources/extension/" platform "/src/content.js.map"))
        (release id [(str "intermediate/" platform "/content")]
                 (str "resources/extension/" platform "/src/content.js")
                 (str "resources/extension/" platform "/src/content.js.map"))])))

(defn mobile-build
  ([platform]
     [(dev (str platform "-dev") [(str "intermediate/" platform)] ;FIXME: add "dev" once websocket tools work on actual devices
           (str "resources/mobile/" u/project-name "/platforms/" platform "/www/js/app.js")
           (str "resources/mobile/" u/project-name "/platforms/" platform "/www/js/out"))
      (release (str platform "-release")
               [(str "intermediate/" platform)]
               (str "resources/mobile/" u/project-name "/platforms/" platform "/www/js/app.js")
               (str "resources/mobile/" u/project-name "/platforms/" platform "/www/js/app.js.map"))])
  ([id platform]
     (if (u/member? id ["draft" "mobile-dev"])
       (dev id [(str "intermediate/" platform)] ;FIXME: add "dev" once websocket tools work on actual devices
              (str "resources/mobile/" u/project-name "/platforms/" platform "/www/js/app.js")
              (str "resources/mobile/" u/project-name "/platforms/" platform "/www/js/out"))
       (release id [(str "intermediate/" platform)]
                  (str "resources/mobile/" u/project-name "/platforms/" platform "/www/js/app.js")
                  (str "resources/mobile/" u/project-name "/platforms/" platform "/www/js/app.js.map")))))

(defn desktop-build
  ([platform]
     [(dev (str platform "-dev")
           ["dev" (str "intermediate/" platform)]
           (str "resources/desktop/deploy/" platform "/js/app.js")
           (str "resources/desktop/deploy/" platform "/js/out"))
      (release (str platform "-release") [(str "intermediate/" platform)]
               (str "resources/desktop/deploy/" platform "/js/app.js")
               (str "resources/desktop/deploy/" platform "/js/app.js.map"))])
  ([id platform]
     (if (u/member? id ["draft" "desktop-dev"])
       (dev id ["dev" (str "intermediate/" platform)]
              (str "resources/desktop/deploy/" platform "/js/app.js")
              (str "resources/desktop/deploy/" platform "/js/out"))
       (release id [(str "intermediate/" platform)]
                  (str "resources/desktop/deploy/" platform "/js/app.js")
                  (str "resources/desktop/deploy/" platform "/js/app.js.map")))))
;; Above fns work for single platfroms but not for lists of platforms

(defn generate-builds
  "Creates a builds.clj file full of all the cljsbuild configurations.
  Also reads config.edn to only build relevant platform configs."
  [& plat]
  (letfn [(specific [build plat]
            (reduce #(into % (build %2))
                    [] plat))
          (generic [build id plat]
            (for [p plat]
              (build id p)))
          (browser-generic [build id plat]
            (reduce #(into % (build id %2))
                    [] plat))]
    (let [platform (first plat)
          {:keys [browsers mobile desktop]} u/systems
          custom-build (cond
                        (u/member? platform browsers) (browser-build "draft" platform)
                        (u/member? platform mobile) (mobile-build "draft" platform)
                        (u/member? platform desktop) (desktop-build "draft" platform))
          builds {:builds (-> (if platform [custom-build] []) ; cutom build for rapid devopment

                              (into (specific browser-build browsers))
                              (into (specific mobile-build mobile))
                              (into (specific desktop-build desktop))

                              (into (browser-generic browser-build "browser-dev" browsers))
                              (into (browser-generic browser-build "browser-release" browsers))

                              (into (generic mobile-build "mobile-dev" mobile))
                              (into (generic mobile-build "mobile-release" mobile))

                              (into (generic desktop-build "desktop-dev" desktop))
                              (into (generic desktop-build "desktop-release" desktop)))}]
      (spit "builds.clj" (with-out-str (pprint builds))))))
