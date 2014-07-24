(defproject lein-browserific "0.1.0"
  :description "Pre-processor to convert browserific code to API code"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript ""]
                 [org.clojure/data.xml "0.0.7"]
                 [cheshire "5.3.1"]
                 [instaparse "1.3.2"]
                 [clojure-watch "0.1.9"]
                 [me.raynes/fs "1.4.4"]
                 [ring "1.3.0-RC1"]
                 [fogus/ring-edn "0.2.0"]
                 [compojure "1.1.8"]
                 [org.clojure/core.async "0.1.303.0-886421-alpha"]
                 ;; CLJS
                 [org.clojure/clojurescript "0.0-2197"]
                 [sablono "0.2.17"]
                 [om "0.5.3"]
                 [om-sync "0.1.1"]]


  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-ring "0.8.7"]
            [lein-pdo "0.1.1"]]

  :aliases {"dev" ["pdo" "cljsbuild" "auto" "dev," "ring" "server-headless"]}
  :eval-in-leiningen true

  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]]}}

  :ring {:handler leiningen.browserific.config.server/app}

  ;; TODO: delete the example :browserific when finished
  :browserific {:config "test/test-config.edn"
                :source-paths "test"}

  :source-paths ["src/clj"]

  :cljsbuild {
              :builds [{:id "dev"
                        :source-paths ["src/cljs"]
                        :compiler {
                                   :output-to "resources/public/js/client.js"
                                   :output-dir "resources/public/js/out"
                                   :optimizations :none
                                   :source-map true
                                   :externs ["react/externs/react.js"]}}
                       {:id "release"
                        :source-paths ["src/cljs"]
                        :compiler {
                                   :output-to "resources/public/js/client.js"
                                   :source-map "resources/public/js/client.js.map"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :output-wrapper false
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]
                                   :closure-warnings
                                   {:non-standard-jsdoc :off}}}]})
