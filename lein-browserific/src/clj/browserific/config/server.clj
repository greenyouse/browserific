(ns browserific.config.server
    (:require [compojure.handler :as handler]
              [compojure.route :as route]
              [compojure.core :refer [GET POST defroutes]]
              [ring.util.response :as resp]
              [ring.middleware.edn :refer [wrap-edn-params]]
              [browserific.config.file :as f]
              [clojure.java.io :as io]))

(def config-state
  "To update the config.edn file onChange"
  (atom {}))

(def om-state
  "The schema for OM to load when it starts up"
  (atom {}))

(defn edn-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/edn"}
   :body (pr-str data)})

;; Should we eliminate the extra :config map stuff?
(defn init []
  (reset! config-state (read-string (slurp "resources/public/config.edn")))
  (reset! om-state f/test-data)
  (edn-response @om-state))

(defn send-config []
  (edn-response @om-state))

;; TODO: figure out what to do with the msg here...
;; TODO: write this to disk
(defn save-config [{:keys [k v]}]
  (swap! config-state #(assoc-in % [k] v))
  (spit "resources/public/test-data.edn" @om-state))

(defroutes app-routes
  (GET "/" [] (resp/redirect "/index.html"))
  (GET "/init" [] (init))
  (GET "/config" [] (send-config))
  (POST "/config" {params :edn-params} (save-config params))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> #'app-routes
      (handler/api)
      wrap-edn-params))
