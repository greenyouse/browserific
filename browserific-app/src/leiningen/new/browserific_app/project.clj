(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2670"]
                 [weasel "0.5.0"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [com.greenyouse/pldb-cache "0.1.0"]
                 [reagent "0.5.0-alpha"]
                 [cljsjs/react "0.12.2-5"]
                 [figwheel "0.2.2-SNAPSHOT"]]

  :plugins [[lein-cljsbuild "1.0.4"]
            [com.cemerick/clojurescript.test "0.3.1"]
            [lein-browserific "0.1.0-alpha"]
            [lein-auto "0.1.1"]
            [lein-figwheel "0.2.2-SNAPSHOT"]]

  :browserific {}

  :chenex {:builds ~(-> "builds/chenex-build.clj" slurp read-string)}

  :cljsbuild ~(-> "builds/browserific-build.clj" slurp read-string))
