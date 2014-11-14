(ns leiningen.new.browserific-app
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))


(def render (renderer "browserific-app"))

(defn member? [i coll]
  (some #(= i %) coll))

(defn browserific-app
  ([name] (browserific-app name []))
  ([name opt-vec]
     (main/info "\033[33mCooking up a fresh browserific project...\n\033[0m")
     (let [opts (read-string opt-vec)
           data {:name name
                 :sanitized (name-to-path name)
                 :sablono (member? :sablono opts)
                 :kioo (member? :kioo opts)
                 :secretary (member? :secretary opts)}]
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
                "resources"))))
