(ns browserific.config.server
  "This server is for the config.edn GUI. It loads the state
  of the config.edn file and saves a snapshot of the state
  each time configuration option gets edited."
  (:require [browserific.helpers.utils :as u]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer [GET PUT DELETE defroutes]]
            [ring.util.response :as resp]
            [ring.middleware.edn :refer [wrap-edn-params]]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]))

(defn- edn-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/edn"}
   :body (pr-str data)})

(defn- init
  "Loads the config file on startup"
  []
  (let [config (-> u/config-file slurp read-string)]
    (edn-response {:coll config})))

(defn- update-config
  "Saves a copy of the current state back to the config.edn file"
  [{:keys [value]}]
  (spit u/config-file value)
  (edn-response {:status :ok}))

(defroutes ^:private app-routes
  (GET "/" [] (resp/redirect "/index.html"))
  (GET "/init" [] (init))
  (PUT "/config" {params :edn-params} (update-config params))
  (route/resources "/")
  (route/not-found "Page not found"))

(def ^:private app
  (-> app-routes
      (handler/api)
      wrap-edn-params))

(defn config-server
  "Start a server for the config page" []
  (jetty/run-jetty #'app {:port 50000 :host "localhost"}))
