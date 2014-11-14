(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]
                 [weasel "0.4.2"]
                 [om "0.8.0-alpha2"]{{#sablono}}
                 [sablono "0.2.22"]{{/sablono}}{{^sablono}}{{/sablono}}{{#kioo}}
                 [kioo "0.4.0"]{{/kioo}}{{^kioo}}{{/kioo}}{{#secretary}}
                 [secretary "1.2.1"]{{/secretary}}{{#secretary}}{{/secretary}}]

  :plugins [[lein-cljsbuild "1.0.3"]
            [com.cemerick/clojurescript.test "0.3.1"]
            [lein-pdo "0.1.1"]
            [lein-browserific "0.1.0-SNAPSHOT"]
            [lein-auto "0.1.1"]]

  ;; FIXME: get rid of pdo?
  :aliases {"dev" ["pdo" "cljsbuild" "auto" "dev," "browserific" "auto"]
            "release" ["pdo" "cljsbuild" "auto" "release," "browserific" "auto"]}


  ;; erase unused platforms below to speed up lein-cljsbuild compilation
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
                        :source-paths["dev" "intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/www/js/app.js"
                                   :output-dir "resources/mobile/{{sanitized}}/www/js/out"
                                   :optimizations :none
                                   :source-map true
                                   :externs ["react/externs/react.js"]}}

                       ;; Linux32
                       {:id "dev"
                        :source-paths["dev" "intermediate/linux32"]
                        :compiler {:output-to "resources/desktop/deploy/linux32/js/app.js"
                                   :output-dir "resources/desktop/deploy/linux32/js/out"
                                   :optimizations :none
                                   :target :nodejs
                                   :source-map true
                                   :externs ["react/externs/react.js"]}}

                       ;; Linux64
                       {:id "dev"
                        :source-paths["dev" "intermediate/linux64"]
                        :compiler {:output-to "resources/desktop/deploy/linux64/js/app.js"
                                   :output-dir "resources/desktop/deploy/linux64/js/out"
                                   :optimizations :none
                                   :target :nodejs
                                   :source-map true
                                   :externs ["react/externs/react.js"]}}

                       ;; OSX32
                       {:id "dev"
                        :source-paths["dev" "intermediate/osx32"]
                        :compiler {:output-to "resources/desktop/deploy/osx32/js/app.js"
                                   :output-dir "resources/desktop/deploy/osx32/js/out"
                                   :optimizations :none
                                   :target :nodejs
                                   :source-map true
                                   :externs ["react/externs/react.js"]}}

                       ;; OSX64
                       {:id "dev"
                        :source-paths["dev" "intermediate/osx64"]
                        :compiler {:output-to "resources/desktop/deploy/osx64/js/app.js"
                                   :output-dir "resources/desktop/deploy/osx64/js/out"
                                   :optimizations :none
                                   :target :nodejs
                                   :source-map true
                                   :externs ["react/externs/react.js"]}}

                       ;; Windows
                       {:id "dev"
                        :source-paths["dev" "intermediate/windows"]
                        :compiler {:output-to "resources/desktop/deploy/windows/js/app.js"
                                   :output-dir "resources/desktop/deploy/windows/js/out"
                                   :optimizations :none
                                   :target :nodejs
                                   :source-map true
                                   :externs ["react/externs/react.js"]}}


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
                        :source-paths["intermediate/mobile/background"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/www/background.js"
                                   :optimizations :advanced}}
                       {:id "release"
                        :source-paths ["intermediate/mobile/content"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/www/content.js"
                                   :optimizations :advanced}}

                       ;; Linux32
                       {:id "release"
                        :source-paths ["intermediate/linux32"]
                        :compiler {:output-to "resources/desktop/deploy/linux32/app.js"
                                   :source-map "resources/desktop/deploy/linux32/js/client.js.map"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Linux64
                       {:id "release"
                        :source-paths ["intermediate/linux64"]
                        :compiler {:output-to "resources/desktop/deploy/linux64/app.js"
                                   :source-map "resources/desktop/deploy/linux64/js/client.js.map"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; OSX
                       {:id "release"
                        :source-paths ["intermediate/osx32"]
                        :compiler {:output-to "resources/desktop/deploy/osx32/app.js"
                                   :source-map "resources/desktop/deploy/osx32/js/client.js.map"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; OSX64
                       {:id "release"
                        :source-paths ["intermediate/osx64"]
                        :compiler {:output-to "resources/desktop/deploy/osx64/app.js"
                                   :source-map "resources/desktop/deploy/osx64/js/client.js.map"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Windows
                       {:id "release"
                        :source-paths ["intermediate/windows"]
                        :compiler {:output-to "resources/desktop/deploy/windows/app.js"
                                   :source-map "resources/desktop/deploy/windows/js/client.js.map"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}]})
