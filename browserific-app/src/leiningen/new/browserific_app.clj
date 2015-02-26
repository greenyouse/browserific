(ns leiningen.new.browserific-app
  (:require [leiningen.new.templates :refer [renderer name-to-path year ->files]]
            [leiningen.core.main :as main]))


(def render (renderer "browserific-app"))

;; TODO: make desktop and extension folders too?
(defn browserific-app
  "A template for browserific projects"
  [name]
  (main/info "\033[33mCooking up a fresh browserific project...\n\033[0m")
  (let [data {:name name
              :sanitized (name-to-path name)
              :year (year)}]
    (->files data
      [".gitignore" (render "gitignore" data)]
      ["project.clj" (render "project.clj" data)]
      ["README.md" (render "project-README.md" data)]
      ["LICENSE" (render "LICENSE" data)]
      ["builds/browserific-build.clj" (render "browserific-build.clj" data)]
      ["builds/chenex-build.clj" (render "chenex-build.clj" data)]
      ["src/config.edn" (render "config.edn" data)]
      ["src/{{sanitized}}/core.cljx" (render "core.cljx" data)]
      ["test/core_test.clj" (render "test.cljs" data)]
      ["dev/brepl.cljs" (render "brepl.cljs" data)]

      ;; public
      ["resources/public/index.html" (render "index.html" data)]
      ["resources/public/css/index.css" (render "index.css" data)]

      ;; cordova
      ["resources/mobile/{{sanitized}}/hooks/README.md" (render "cordova-README.md" data)]
      ["resources/mobile/{{sanitized}}/www/index.html" (render "mobile-index.html" data)]
      ["resources/mobile/{{sanitized}}/www/css/index.css" (render "index.css" data)]
      "resources/mobile/{{sanitized}}/www/img"
      "resources/mobile/{{sanitized}}/platforms"
      "resources/mobile/{{sanitized}}/plugins"
      "resources/mobile/{{sanitized}}/merges")))
