(defproject lein-browserific "0.1.2-SNAPSHOT"
  :description "A Leiningen build tool for unified app development"
  :url "https://github.com/greenyouse/browserific/tree/master/lein-browserific"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [cheshire "5.3.1"]
                 [com.greenyouse/chenex "0.2.2"]
                 [com.greenyouse/deepfns "0.1.2"]
                 [com.greenyouse/plugin-helpers "0.1.6"]
                 [compojure "1.4.0"]
                 [fogus/ring-edn "0.3.0"]
                 [me.raynes/fs "1.4.6"]
                 [ring "1.4.0"]
                 ;; cljs
                 [org.clojure/clojurescript "0.0-3119"]
                 [cljsjs/react "0.12.2-5"]
                 [reagent "0.5.0-alpha"]]

  :eval-in-leiningen true

  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [weasel "0.6.0"]]
                   :plugins [[lein-cljsbuild "1.0.5"]
                             [lein-ring "0.9.1"]]}}

  :ring {:handler browserific.config.server/app}

  :browserific {:config "test/test-config.edn" ; "test/whole-config.edn"
                :source-paths "test/fake-src"}

  :source-paths ["src/clj"]

  :cljsbuild {
              :builds [{:id "dev"
                        :source-paths ["dev" "src/cljs"]
                        :compiler {:output-to "resources/public/js/client.js"
                                   :output-dir "resources/public/js/out"
                                   :optimizations :none
                                   :source-map true
                                   :asset-path "js/out"
                                   :main browserific.config.core}}
                       {:id "release"
                        :source-paths ["src/cljs"]
                        :compiler {:output-to "resources/public/js/client.js"
                                   :output-dir "resources/public/js/release"
                                   :optimizations :advanced
                                   :source-map "resources/public/js/client.map.js"
                                   :main browserific.config.core
                                   :pretty-print false}}]})
