(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]
                 [weasel "0.4.2"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [om "0.8.0-alpha2"]
                 [figwheel "0.1.5-SNAPSHOT"]{{#sablono}}
                 [sablono "0.2.22"]{{/sablono}}{{^sablono}}{{/sablono}}{{#kioo}}
                 [kioo "0.4.0"]{{/kioo}}{{^kioo}}{{/kioo}}{{#secretary}}
                 [secretary "1.2.1"]{{/secretary}}{{#secretary}}{{/secretary}}]

  :plugins [[lein-cljsbuild "1.0.3"]
            [com.cemerick/clojurescript.test "0.3.1"]
            [lein-browserific "0.1.0-SNAPSHOT"]
            [lein-auto "0.1.1"]
            [lein-figwheel "0.1.5-SNAPSHOT"]]

  ;; build-ids: draft, browser-dev, mobile-dev, desktop-dev, release
  ;; erase unused platforms below to speed up lein-cljsbuild compilation
  :cljsbuild {
              :builds [
                       ;;;;;;;;;;;;;;;;;;;;;; Draft Mode
                       {:id "draft"
                        :source-paths ["dev" ""] ;choose a target platform here
                        :compiler {:output-to "resources/public/js/app.js"}
                        :output-dir "resources/public/js/out"
                        :source-map "resources/public/js/out.js.map"
                        :optimizations :none
                        :preamble ["react/react.min.js"]
                        :externs ["react/externs/react.js"]}

                       ;;;;;;;;;;;;;;;;;;;;;; Platform Development Mode
                       ;; Chrome
                       {:id "browser-dev"
                        :source-paths ["intermediate/chrome/background"]
                        :compiler {:output-to "resources/extension/chrome/src/background.js"
                                   :optimizations :simple}}
                       {:id "browser-dev"
                        :source-paths ["dev" "intermediate/chrome/content"]
                        :compiler {:output-to "resources/extension/chrome/src/content.js"
                                   :optimizations :simple}}

                       ;; Firefox
                       {:id "browser-dev"
                        :source-paths ["intermediate/firefox/background"]
                        :compiler {
                                   :output-to "resources/extension/firefox/lib/background.js"
                                   :optimizations :simple}}
                       {:id "browser-dev"
                        :source-paths ["dev" "intermediate/firefox/content"]
                        :compiler {:output-to "resources/extension/firefox/data/content.js"
                                   :optimizations :simple}}

                       ;; Opera
                       {:id "browser-dev"
                        :source-paths ["intermediate/opera/background"]
                        :compiler {:output-to "resources/extension/opera/src/background.js"
                                   :optimizations :simple}}
                       {:id "browser-dev"
                        :source-paths ["dev" "intermediate/opera/content"]
                        :compiler {:output-to "resources/extension/opera/src/content.js"
                                   :optimizations :simple}}

                       ;; Safari
                       {:id "browser-dev"
                        :source-paths ["intermediate/safari/background"]
                        :compiler {:output-to "resources/extension/safari/src/background.js"
                                   :optimizations :simple}}
                       {:id "browser-dev"
                        :source-paths ["dev" "intermediate/safari/content"]
                        :compiler {:output-to "resources/extension/safari/src/content.js"
                                   :optimizations :simple}}

                       ;; Amazon-Fire
                       {:id "mobile-dev"
                        :source-paths["dev" "intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/amazon-fireos/www/js/app.js"
                                   :output-dir "resources/mobile/{{sanitized}}/platforms/amazon-fireos/www/js/out"
                                   :sourc-map "resources/mobile/{{sanitized}}/platforms/amazon-fireos/www/js/out.js.map"
                                   :optimizations :none
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Android
                       {:id "mobile-dev"
                        :source-paths["dev" "intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/android/www/js/app.js"
                                   :output-dir "resources/mobile/{{sanitized}}/platforms/android/www/js/out"
                                   :sourc-map "resources/mobile/{{sanitized}}/platforms/android/www/js/out.js.map"
                                   :optimizations :none
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Blackberry
                       {:id "mobile-dev"
                        :source-paths["dev" "intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/blackberry/www/js/app.js"
                                   :output-dir "resources/mobile/{{sanitized}}/platforms/blackberry/www/js/out"
                                   :sourc-map "resources/mobile/{{sanitized}}/platforms/blackberry/www/js/out.js.map"
                                   :optimizations :none
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Firefox-OS
                       {:id "mobile-dev"
                        :source-paths["dev" "intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/firefox-os/www/js/app.js"
                                   :output-dir "resources/mobile/{{sanitized}}/platforms/firefox-os/www/js/out"
                                   :sourc-map "resources/mobile/{{sanitized}}/platforms/firefox-os/www/js/out.js.map"
                                   :optimizations :none
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; iOS
                       {:id "mobile-dev"
                        :source-paths["dev" "intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/ios/www/js/app.js"
                                   :output-dir "resources/mobile/{{sanitized}}/platforms/ios/www/js/out"
                                   :sourc-map "resources/mobile/{{sanitized}}/platforms/ios/www/js/out.js.map"
                                   :optimizations :none
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Ubuntu
                       {:id "mobile-dev"
                        :source-paths["dev" "intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/ubuntu/www/js/app.js"
                                   :output-dir "resources/mobile/{{sanitized}}/platforms/ubuntu/www/js/out"
                                   :sourc-map "resources/mobile/{{sanitized}}/platforms/ubuntu/www/js/out.js.map"
                                   :optimizations :none
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}


                       ;; Windows Phone 7
                       {:id "mobile-dev"
                        :source-paths["dev" "intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/wp7/www/js/app.js"
                                   :output-dir "resources/mobile/{{sanitized}}/platforms/wp7/www/js/out"
                                   :sourc-map "resources/mobile/{{sanitized}}/platforms/wp7/www/js/out.js.map"
                                   :optimizations :none
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Windows Phone 8
                       {:id "mobile-dev"
                        :source-paths["dev" "intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/wp8/www/js/app.js"
                                   :output-dir "resources/mobile/{{sanitized}}/platforms/wp8/www/js/out"
                                   :sourc-map "resources/mobile/{{sanitized}}/platforms/wp8/www/js/out.js.map"
                                   :optimizations :none
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Tizen
                       {:id "mobile-dev"
                        :source-paths["dev" "intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/tizen/www/js/app.js"
                                   :output-dir "resources/mobile/{{sanitized}}/platforms/tizen/www/js/out"
                                   :sourc-map "resources/mobile/{{sanitized}}/platforms/tizen/www/js/out.js.map"
                                   :optimizations :none
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; WebOS
                       {:id "mobile-dev"
                        :source-paths["dev" "intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/webos/www/js/app.js"
                                   :output-dir "resources/mobile/{{sanitized}}/platforms/webos/www/js/out"
                                   :sourc-map "resources/mobile/{{sanitized}}/platforms/webos/www/js/out.js.map"
                                   :optimizations :none
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Linux32
                       {:id "desktop-dev"
                        :source-paths["dev" "intermediate/linux32"]
                        :compiler {:output-to "resources/desktop/deploy/linux32/js/app.js"
                                   :output-dir "resources/desktop/deploy/linux32/js/out"
                                   :source-map "resources/desktop/deploy/linux32/js/out.js.map"
                                   :optimizations :none
                                   :target :nodejs
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Linux64
                       {:id "desktop-dev"
                        :source-paths["dev" "intermediate/linux64"]
                        :compiler {:output-to "resources/desktop/deploy/linux64/js/app.js"
                                   :output-dir "resources/desktop/deploy/linux64/js/out"
                                   :source-map "resources/desktop/deploy/linux64/js/out.js.map"
                                   :optimizations :none
                                   :target :nodejs
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; OSX32
                       {:id "desktop-dev"
                        :source-paths["dev" "intermediate/osx32"]
                        :compiler {:output-to "resources/desktop/deploy/osx32/js/app.js"
                                   :output-dir "resources/desktop/deploy/osx32/js/out"
                                   :source-map "resources/desktop/deploy/osx32/js/out.js.map"
                                   :optimizations :none
                                   :target :nodejs
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; OSX64
                       {:id "desktop-dev"
                        :source-paths["dev" "intermediate/osx64"]
                        :compiler {:output-to "resources/desktop/deploy/osx64/js/app.js"
                                   :output-dir "resources/desktop/deploy/osx64/js/out"
                                   :source-map "resources/desktop/deploy/osx64/js/out.js.map"
                                   :optimizations :none
                                   :target :nodejs
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Windows
                       {:id "desktop-dev"
                        :source-paths["dev" "intermediate/windows"]
                        :compiler {:output-to "resources/desktop/deploy/windows/js/app.js"
                                   :output-dir "resources/desktop/deploy/windows/js/out"
                                   :source-map "resources/desktop/deploy/windows/js/out.js.map"
                                   :optimizations :none
                                   :target :nodejs
                                   :preamble ["react/react.min.js"]
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

                       ;; Amazon-Fire
                       {:id "release"
                        :source-paths["intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/amazon-fireos/www/js/app.js"
                                   :optimizations :advanced
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Android
                       {:id "release"
                        :source-paths["intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/android/www/js/app.js"
                                   :optimizations :advanced
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Blackberry
                       {:id "release"
                        :source-paths["intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/blackberry/www/js/app.js"
                                   :optimizations :advanced
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Firefox-OS
                       {:id "release"
                        :source-paths["intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/firefox-os/www/js/app.js"
                                   :optimizations :advanced
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; iOS
                       {:id "release"
                        :source-paths["intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/ios/www/js/app.js"
                                   :optimizations :advanced
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Ubuntu
                       {:id "release"
                        :source-paths["intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/ubuntu/www/js/app.js"
                                   :optimizations :advanced
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Windows Phone 7
                       {:id "release"
                        :source-paths["intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/wp7/www/js/app.js"
                                   :optimizations :advanced
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Windows Phone 8
                       {:id "release"
                        :source-paths["intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/wp8/www/js/app.js"
                                   :optimizations :advanced
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Tizen
                       {:id "release"
                        :source-paths["intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/tizen/www/js/app.js"
                                   :optimizations :advanced
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; WebOS
                       {:id "release"
                        :source-paths["intermediate/mobile"]
                        :compiler {:output-to "resources/mobile/{{sanitized}}/platforms/webos/www/js/app.js"
                                   :optimizations :advanced
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Linux32
                       {:id "release"
                        :source-paths ["intermediate/linux32"]
                        :compiler {:output-to "resources/desktop/deploy/linux32/js/app.js"
                                   :source-map "resources/desktop/deploy/linux32/js/client.js.map"
                                   :optimizations :advanced
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Linux64
                       {:id "release"
                        :source-paths ["intermediate/linux64"]
                        :compiler {:output-to "resources/desktop/deploy/linux64/js/app.js"
                                   :source-map "resources/desktop/deploy/linux64/js/client.js.map"
                                   :optimizations :advanced
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; OSX
                       {:id "release"
                        :source-paths ["intermediate/osx32"]
                        :compiler {:output-to "resources/desktop/deploy/osx32/js/app.js"
                                   :source-map "resources/desktop/deploy/osx32/js/client.js.map"
                                   :optimizations :advanced
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; OSX64
                       {:id "release"
                        :source-paths ["intermediate/osx64"]
                        :compiler {:output-to "resources/desktop/deploy/osx64/js/app.js"
                                   :source-map "resources/desktop/deploy/osx64/js/client.js.map"
                                   :optimizations :advanced
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       ;; Windows
                       {:id "release"
                        :source-paths ["intermediate/windows"]
                        :compiler {:output-to "resources/desktop/deploy/windows/js/app.js"
                                   :source-map "resources/desktop/deploy/windows/js/client.js.map"
                                   :optimizations :advanced
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}]})
