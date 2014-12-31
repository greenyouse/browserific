(ns leiningen.new.browserific-app
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))


(def render (renderer "browserific-app"))

(defn member? [i coll]
  (some #(= i %) coll))

;; FIXME: may not need this part
(defn parse-opts [opts]
  (reduce (fn [acc i]
            (cond
             (re-find #":sablono" i) (conj acc :sablono)
             (re-find #":kioo" i) (conj acc :kioo)
             (re-find #":secretary" i) (conj acc :secretary)
             :else (main/abort "\033[31mError: " i " not supported.\nOptions are :sablono, :kioo, :secretary\n\n\033[0m")))
          [] opts))

(defn browserific-app
  "A template for browserific projects"
  [name & args]
  (main/info "\033[33mCooking up a fresh browserific project...\n\033[0m")
  (let [opts (parse-opts args)
        data {:name name
              :sanitized (name-to-path name)
              :sablono (member? :sablono opts)
              :kioo (member? :kioo opts)
              :secretary (member? :secretary opts)}]
    (->files data
             [".gitignore" (render "gitignore" data)]
             ["builds/chenex-build.clj" (render "chenex-build.clj" data)]
             ["builds/browserific-build.clj" (render "browserific-build.clj" data)]
             ["project.clj" (render "project.clj" data)]
             ["src/config.edn" (render "config.edn" data)]
             ["src/{{sanitized}}/background/background.cljx" (render "background.cljx" data)]
             ["src/{{sanitized}}/content/content.cljx" (render "content.cljx" data)]
             ["test/test.clj" (render "test.cljs" data)]
             ["dev/brepl.cljs" (render "brepl.cljs" data)]
             ;; public
             ["resources/public/index.html" (render "index.html" data)]
             ["resources/public/js/react-0.12.1.js" (render "react-0.12.1.js" data)]
             "resources/public/css"

             ;; mobile
             ["resources/mobile/{{sanitized}}/hooks/README.md" (render "README.md" data)]
             ["resources/mobile/{{sanitized}}/www/index.html" (render "mobile-index.html" data)]
             ["resources/mobile/{{sanitized}}/www/js/react-0.12.1.js" (render "react-0.12.1.js" data)]
             "resources/mobile/{{sanitized}}/www/css"
             "resources/mobile/{{sanitized}}/www/img"
             "resources/mobile/{{sanitized}}/platforms"
             "resources/mobile/{{sanitized}}/plugins")))
