(defproject lein-browserific "0.1.0-SNAPSHOT"
  :description "Pre-processor to convert browserific code to API code"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [cheshire "5.3.1"]
                 [instaparse "1.3.4"]
                 [me.raynes/fs "1.4.6"]
                 [ring "1.3.1"]
                 [fogus/ring-edn "0.2.0"]
                 [compojure "1.2.1"]
                 [com.greenyouse/chenex "0.1.0"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 ;; cljs
                 [org.clojure/clojurescript "0.0-2371"]
                 [sablono "0.2.22"]
                 [om "0.8.0-alpha1"]
                 [om-sync "0.1.1"]]


  :eval-in-leiningen true

  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]]
                   :plugins [[lein-cljsbuild "1.0.3"]
                             [lein-ring "0.8.7"]]}}

  :ring {:handler browserific.config.server/app}

  :browserific {:config "test/test-config.edn"
                :source-paths "test/fake-src"}

  :source-paths ["src/clj"]

  :cljsbuild {
              :builds [{:id "dev"
                        :source-paths ["dev" "src/cljs"]
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
