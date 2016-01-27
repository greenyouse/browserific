(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [browserific "0.1.2-alpha4"]
                 [com.greenyouse/chenex "0.2.1"]
                 [com.greenyouse/multco "0.1.2-beta"]
                 ;; multco can hadle core.logic, bacwn, or datascript dbs
                 [org.clojure/core.logic "0.8.10"]
                 ;;[fogus/bacwn "0.4.0"]
                 ;;[datascript "0.15.0"]
                 [reagent "0.5.0"]]

  :profiles {:default [:base :system :user :provided :dev :plugin.chenex/default]
             :dev {:dependencies [[com.cemerick/piggieback "0.2.1"]
                                  [figwheel "0.5.0-5"]
                                  [figwheel-sidecar "0.5.0-5"]
                                  [org.clojure/tools.nrepl "0.2.10"]]
                   :plugins [[cider/cider-nrepl "0.10.1"]
                             [com.greenyouse/chenex "0.2.1"]
                             [lein-auto "0.1.2"]
                             [lein-browserific "0.1.2-alpha4"]
                             [lein-cljsbuild "1.1.2"]
                             [lein-figwheel "0.5.0-5"]]
                   ;; Start a repl with M-x cider-jack-in and
                   ;; use these commands in the repl to get started:
                   ;; (use 'figwheel-sidecar.repl-api)
                   ;; (start-figwheel!)
                   ;; (cljs-repl)
                   :repl-options
                   {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}}

  :auto {:default {:paths ["src"]}}

  :figwheel {:http-server-root "public"
             :css-dirs ["resources/public/css"]}

  :browserific {}

  :chenex {:builds []}

  :cljsbuild {:builds []})
