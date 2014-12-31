(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]
                 [weasel "0.4.2"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [reagent "0.5.0-alpha"]
                 [figwheel "0.1.5-SNAPSHOT"]{{#sablono}}
                 [sablono "0.2.22"]{{/sablono}}{{^sablono}}{{/sablono}}{{#kioo}}
                 [kioo "0.4.0"]{{/kioo}}{{^kioo}}{{/kioo}}{{#secretary}}
                 [secretary "1.2.1"]{{/secretary}}{{^secretary}}{{/secretary}}]

  :plugins [[lein-cljsbuild "1.0.3"]
            [com.cemerick/clojurescript.test "0.3.1"]
            [lein-browserific "0.1.0-SNAPSHOT"]
            [lein-auto "0.1.1"]
            [lein-figwheel "0.1.5-SNAPSHOT"]
            [com.greenyouse/chenex "0.1.0"]]

  :chenex {:builds ~(-> "builds/chenex-build.clj" slurp read-string)}

  :cljsbuild ~(-> "builds/browserific-build.clj" slurp read-string))
