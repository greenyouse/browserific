(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2199"] ; update before relase :)
                 ;FIXME: Add the SDK wrapper here after it's done
                 ]

  :plugins [[lein-cljsbuild "1.0.3"]
            [com.cemerick/clojurescript.test "0.3.0"]
            [lein-pdo "0.1.1"]
            ;FIXME: Add the lein plugin here after it's done
            ]

  :aliases {"dev" ["pdo" "cljsbuild" "auto" "dev," "browserific" "auto"]
            "release" ["pdo" "cljsbuild" "auto" "release," "browserific" "auto"]}

  :cljsbuild {
              :builds [
                       ;; Chrome
                       {:id "dev"
                        :source-paths ["src/{{sanitized}}/chrome/background"]
                        :compiler {:output-to "resources/chrome/src/background.js"
                                   :optimizations :simple}}
                       {:id "dev"
                        :source-paths ["dev" "src/{{sanitized}}/chrome/content"]
                        :compiler {:output-to "resources/chrome/src/content.js"
                                   :optimizations :simple}}

                       ;; Firefox
                       {:id "dev"
                        :source-paths ["src/{{sanitized}}/firefox/background"]
                         :compiler {
                                    :output-to "resources/firefox/lib/background.js"
                                    :optimizations :simple}}
                        {:id "dev"
                         :source-paths ["dev" "src/{{sanitized}}/firefox/content"]
                         :compiler {:output-to "resources/firefox/data/content.js"
                                    :optimizations :simple}}

                       ;; Opera
                       {:id "dev"
                        :source-paths ["src/{{sanitized}}/opera/background"]
                        :compiler {:output-to "resources/opera/src/background.js"
                                   :optimizations :simple}}
                       {:id "dev"
                        :source-paths ["dev" "src/{{sanitized}}/opera/content"]
                        :compiler {:output-to "resources/opera/src/content.js"
                                   :optimizations :simple}}

                       ;; Safari
                       {:id "dev"
                        :source-paths ["src/{{sanitized}}/safari/background"]
                        :compiler {:output-to "resources/safari/src/background.js"
                                   :optimizations :simple}}
                       {:id "dev"
                        :source-paths ["dev" "src/{{sanitized}}/safari/background"]
                        :compiler {:output-to "resources/safari/src/content.js"
                                   :optimizations :simple}}


                       ;;;;;;;;;;;;;;;;;;;;;; Production Mode
                       ;; Chrome
                       {:id "release"
                        :source-paths ["src/{{sanitized}}/chrome/background"]
                        :compiler {:output-to "resources/chrome/src/background.js"
                                   :optimizations :advanced}}
                       {:id "release"
                        :source-paths ["src/{{sanitized}}/chrome/content"]
                        :compiler {:output-to "resources/chrome/src/content.js"
                                   :optimizations :advanced}}

                       ;; Firefox
                       {:id "release"
                        :source-paths ["src/{{sanitized}}/firefox/background"]
                        :compiler {
                                   :output-to "resources/firefox/lib/background.js"
                                   :optimizations :advanced}}
                       {:id "release"
                        :source-paths ["src/{{sanitized}}/firefox/content"]
                        :compiler {:output-to "resources/firefox/data/content.js"
                                   :optimizations :advanced}}

                       ;; Opera
                       {:id "release"
                        :source-paths ["src/{{sanitized}}/opera/background"]
                        :compiler {:output-to "resources/opera/src/background.js"
                                   :optimizations :advanced}}
                       {:id "release"
                        :source-paths ["src/{{sanitized}}/opera/content"]
                        :compiler {:output-to "resources/opera/src/content.js"
                                   :optimizations :advanced}}

                       ;; Safari
                       {:id "release"
                        :source-paths ["src/{{sanitized}}/safari/background"]
                        :compiler {:output-to "resources/safari/src/background.js"
                                   :optimizations :advanced}}
                       {:id "release"
                        :source-paths ["src/{{sanitized}}/safari/background"]
                        :compiler {:output-to "resources/safari/src/content.js"
                                   :optimizations :advanced}}]})
