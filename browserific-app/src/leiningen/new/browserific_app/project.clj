(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]
                 [weasel "0.4.2"]]

  :plugins [[lein-cljsbuild "1.0.3"]
            [com.cemerick/clojurescript.test "0.3.1"]
            [lein-pdo "0.1.1"]
            [lein-browserific "0.1.0-SNAPSHOT"]]

  :aliases {"dev" ["pdo" "cljsbuild" "auto" "dev," "browserific" "auto"]
            "release" ["pdo" "cljsbuild" "auto" "release," "browserific" "auto"]}

  :cljsbuild {
              :builds [
                       ;; Chrome
                       {:id "dev"
                        :source-paths ["intermediate/chrome/background"]
                        :compiler {:output-to "resources/extension/chrome/src/background.js"
                                   :optimizations :simple}}
                       {:id "dev"
                        :source-paths ["dev" "intermediate/chrome/content"]
                        :compiler {:output-to "resources/extension/chrome/src/content.js"
                                   :optimizations :simple}}

                       ;; Firefox
                       {:id "dev"
                        :source-paths ["intermediate/firefox/background"]
                         :compiler {
                                    :output-to "resources/extension/firefox/lib/background.js"
                                    :optimizations :simple}}
                        {:id "dev"
                         :source-paths ["dev" "intermediate/firefox/content"]
                         :compiler {:output-to "resources/extension/firefox/data/content.js"
                                    :optimizations :simple}}

                       ;; Opera
                       {:id "dev"
                        :source-paths ["intermediate/opera/background"]
                        :compiler {:output-to "resources/extension/opera/src/background.js"
                                   :optimizations :simple}}
                       {:id "dev"
                        :source-paths ["dev" "intermediate/opera/content"]
                        :compiler {:output-to "resources/extension/opera/src/content.js"
                                   :optimizations :simple}}

                       ;; Safari
                       {:id "dev"
                        :source-paths ["intermediate/safari/background"]
                        :compiler {:output-to "resources/extension/safari/src/background.js"
                                   :optimizations :simple}}
                       {:id "dev"
                        :source-paths ["dev" "intermediate/safari/content"]
                        :compiler {:output-to "resources/extension/safari/src/content.js"
                                   :optimizations :simple}}

                       ;; Mobile
                       {:id "dev"
                        :source ["intermediate/mobile/background"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/www/background.js"
                                   :optimizations :simple}}
                       {:id "dev"
                        :source  ["dev" "intermediate/mobile/content"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/www/content.js"
                                   :optimizations :simple}}

                       ;; Linux
                       {:id "dev"
                        :source ["intermediate/linux/background"]
                        :compiler {:output-to "resources/desktop/deploy/linux/background.js"
                                   :optimizations :simple}}
                       {:id "dev"
                        :source  ["dev" "intermediate/linux/content"]
                        :compiler {:output-to "resources/desktop/deploy/linux/content.js"
                                   :optimizations :simple}}

                       ;; OSX
                       {:id "dev"
                        :source ["intermediate/osx/background"]
                        :compiler {:output-to "resources/desktop/deploy/osx/background.js"
                                   :optimizations :simple}}
                       {:id "dev"
                        :source  ["dev" "intermediate/osx/content"]
                        :compiler {:output-to "resources/desktop/deploy/osx/content.js"
                                   :optimizations :simple}}

                       ;; Windows
                       {:id "dev"
                        :source ["intermediate/windows/background"]
                        :compiler {:output-to "resources/desktop/deploy/windows/background.js"
                                   :optimizations :simple}}
                       {:id "dev"
                        :source  ["dev" "intermediate/windows/content"]
                        :compiler {:output-to "resources/desktop/deploy/windows/content.js"
                                   :optimizations :simple}}


                       ;;;;;;;;;;;;;;;;;;;;;; Production Mode
                       ;; Chrome
                       {:id "release"
                        :source-paths ["intermediate/chrome/background"]
                        :compiler {:output-to "resources/extension/chrome/src/background.js"
                                   :optimizations :advanced}}
                       {:id "release"
                        :source-paths ["intermediate/chrome/content"]
                        :compiler {:output-to "resources/extension/chrome/src/content.js"
                                   :optimizations :advanced}}

                       ;; Firefox
                       {:id "release"
                        :source-paths ["intermediate/firefox/background"]
                        :compiler {
                                   :output-to "resources/extension/firefox/lib/background.js"
                                   :optimizations :advanced}}
                       {:id "release"
                        :source-paths ["intermediate/firefox/content"]
                        :compiler {:output-to "resources/extension/firefox/data/content.js"
                                   :optimizations :advanced}}

                       ;; Opera
                       {:id "release"
                        :source-paths ["intermediate/opera/background"]
                        :compiler {:output-to "resources/extension/opera/src/background.js"
                                   :optimizations :advanced}}
                       {:id "release"
                        :source-paths ["intermediate/opera/content"]
                        :compiler {:output-to "resources/extension/opera/src/content.js"
                                   :optimizations :advanced}}

                       ;; Safari
                       {:id "release"
                        :source-paths ["intermediate/safari/background"]
                        :compiler {:output-to "resources/extension/safari/src/background.js"
                                   :optimizations :advanced}}
                       {:id "release"
                        :source-paths ["intermediate/safari/content"]
                        :compiler {:output-to "resources/extension/safari/src/content.js"
                                   :optimizations :advanced}}

                       ;; Mobile
                       {:id "release"
                        :source ["intermediate/mobile/background"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/www/background.js"
                                   :optimizations :advanced}}
                       {:id "release"
                        :source  ["intermediate/mobile/content"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/www/content.js"
                                   :optimizations :advanced}}

                       ;; Linux
                       {:id "release"
                        :source ["intermediate/linux/background"]
                        :compiler {:output-to "resources/desktop/deploy/linux/background.js"
                                   :optimizations :advanced}}
                       {:id "release"
                        :source  ["intermediate/linux/content"]
                        :compiler {:output-to "resources/desktop/deploy/linux/content.js"
                                   :optimizations :advanced}}

                       ;; OSX
                       {:id "release"
                        :source ["intermediate/osx/background"]
                        :compiler {:output-to "resources/desktop/deploy/osx/background.js"
                                   :optimizations :advanced}}
                       {:id "release"
                        :source  ["intermediate/osx/content"]
                        :compiler {:output-to "resources/desktop/deploy/osx/content.js"
                                   :optimizations :advanced}}

                       ;; Windows
                       {:id "release"
                        :source ["intermediate/windows/background"]
                        :compiler {:output-to "resources/desktop/deploy/windows/background.js"
                                   :optimizations :advanced}}
                       {:id "release"
                        :source  ["intermediate/windows/content"]
                        :compiler {:output-to "resources/desktop/deploy/windows/content.js"
                                   :optimizations :advanced}}

                       ]})
