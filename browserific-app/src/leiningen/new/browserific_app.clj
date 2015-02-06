(ns leiningen.new.browserific-app
  (:require [leiningen.new.templates :refer [renderer name-to-path year ->files]]
            [leiningen.core.main :as main]))


;; TODO: make an option for multi but have the default be single
(def render (renderer "browserific-app"))

(defn browserific-app
  "A template for browserific projects"
  [name & args]
  (main/info "\033[33mCooking up a fresh browserific project...\n\033[0m")
  (let [data {:name name
              :sanitized (name-to-path name)
              :year (year)}]
    (->files data
      [".gitignore" (render "gitignore" data)]
      ["project.clj" (render "project.clj" data)]
      ["README.md" (render "project-README.md" data)]
      ["LICENSE" (render "LICENSE" data)]
      ["src/config.edn" (render "config.edn" data)]
      ["src/{{sanitized}}/background/background.cljx" (render "background.cljx" data)]
      ["src/{{sanitized}}/content/content.cljx" (render "content.cljx" data)]
      ["test/test.clj" (render "test.cljs" data)]
      ["dev/brepl.cljs" (render "brepl.cljs" data)]
      ;; public
      ["resources/public/index.html" (render "index.html" data)]
      ["resources/public/index.css" (render "index.css" data)]
      ["resources/public/js/react-0.12.2.js" (render "react-0.12.2.js" data)]
      "resources/public/css"

      ;; mobile
      ["resources/mobile/{{sanitized}}/hooks/README.md" (render "cordova-README.md" data)]
      ["resources/mobile/{{sanitized}}/www/index.html" (render "mobile-index.html" data)]
      ["resources/mobile/{{sanitized}}/www/js/react-0.12.2.js" (render "react-0.12.2.js" data)]
      ["resources/mobile/{{sanitized}}/www/css/index.css" (render "index.css" data)]
      "resources/mobile/{{sanitized}}/www/img"
      "resources/mobile/{{sanitized}}/platforms"
      "resources/mobile/{{sanitized}}/plugins")))
