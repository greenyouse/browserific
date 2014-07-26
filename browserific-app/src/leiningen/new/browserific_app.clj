(ns leiningen.new.browserific-app
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))


(def render (renderer "browserific-app"))

(defn browserific-app
  [name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (main/info "Cooking up a fresh browserific project...")
    (->files data
             [".gitignore" (render "gitignore" data)]
             ["project.clj" (render "project.clj" data)]
             ["src/config.edn" (render "config.edn" data)]
             ["src/{{sanitized}}/background/background.cljs"
              (render "background.cljs" data)]
             ["src/{{sanitized}}/content/content.cljs"
              (render "content.cljs" data)]
             ["test/test.clj" (render "test.cljs" data)]
             ["dev/brepl.cljs" (render "brepl.cljs" data)]
             "resources")))
