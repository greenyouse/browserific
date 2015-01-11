(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2657"]
                 [weasel "0.4.2"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [com.greenyouse/pldb-cache "0.1.0"]
                 [reagent "0.5.0-alpha"]
                 [figwheel "0.1.5-SNAPSHOT"] ;update?

  :plugins [[lein-cljsbuild "1.0.4"]
            [com.cemerick/clojurescript.test "0.3.1"]
            [lein-browserific "0.1.0-SNAPSHOT"] ;update
            [lein-auto "0.1.1"]
            [lein-figwheel "0.1.5-SNAPSHOT"] ;update
            [com.greenyouse/chenex "0.1.0"]]

  :chenex {:builds ~(-> "builds/chenex-build.clj" slurp read-string)}

  :cljsbuild ~(-> "builds/browserific-build.clj" slurp read-string))
