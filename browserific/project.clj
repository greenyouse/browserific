(defproject browserific "0.1.0-SNAPSHOT"
  :description "A library to abstract app framework APIs"
  :url "https://github.com/greenyouse/browserific/browserific"
  :license {:name "BSD 2-Clause"
            :url "http://www.opensource.org/licenses/BSD-2-Clause"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2199"]]
  :plugins [[lein-cljsbuild "1.0.3"]
            [com.cemerick/clojurescript.test "0.3.0"]]
  :cljsbuild {
              :builds [{:id "dev"
                        :source-paths ["dev" "src"]
                        :compiler {:optimizations :simple
                                   :source-map true}}
                       {:id "release"
                        :source-paths ["src"]
                        :compiler {:optimizations :advanced}}]})
