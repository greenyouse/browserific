(defproject browserific "0.1.2-alpha4"
  :description "A library for JS framework interop"
  :url "https://github.com/greenyouse/browserific/tree/master/browserific"
  :license {:name "EPL 1.0"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-3196"]
                 [com.greenyouse/chenex "0.2.1"]]
  :plugins [[lein-cljsbuild "1.0.5"]
            [com.cemerick/clojurescript.test "0.3.1"]]

  :profiles {:dev {:dependencies [[weasel "0.6.0"]]}}

  :cljsbuild {
              :builds [{:id "dev"
                        :source-paths ["dev" "src"]
                        :compiler {:output-to "js/browserific.js"
                                   :output-dir "js/out"
                                   :optimizations :none
                                   :source-map true
                                   :main browserific.core}}
                       {:id "release"
                        :source-paths ["src"]
                        :compiler {:optimizations :advanced
                                   :output-to "js/browserific.js"
                                   :output-dir "js/release"
                                   :source-map "js/browserific.js.map"
                                   :main browserific.core}}]})
