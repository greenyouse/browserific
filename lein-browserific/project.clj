(defproject lein-browserific "0.1.0-alpha"
  :description "A Leiningen build tool for unified app development"
  :url "https://github.com/greenyouse/browserific/lein-browserific"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [cheshire "5.3.1"]
                 [instaparse "1.3.4"]
                 [me.raynes/fs "1.4.6"]
                 [ring "1.3.2"]
                 [fogus/ring-edn "0.2.0"]
                 [compojure "1.2.1"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [com.greenyouse/chenex "0.1.1"]
                 ;; cljs
                 [org.clojure/clojurescript "0.0-2850"] ;2371
                 [reagent "0.5.0-alpha"]
                 [cljsjs/react "0.12.2-5"]]

  :eval-in-leiningen true

  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [weasel "0.6.0"]]
                   :plugins [[lein-cljsbuild "1.0.4"]
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
                                   :main browserific.config.core}}]})
