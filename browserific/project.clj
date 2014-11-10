(defproject browserific "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2199"]]
  :plugins [[lein-cljsbuild "1.0.3"]
            [com.cemerick/clojurescript.test "0.3.0"]]
  :cljsbuild {
              :builds [
                      {:id "dev"
                       :source-paths ["dev" "src"]
                       :compiler {:optimizations :simple
                                  :source-map true}}
                      {:id "release"
                       :source-paths ["src"]
                       :compiler {:optimizations :advanced}}]})
