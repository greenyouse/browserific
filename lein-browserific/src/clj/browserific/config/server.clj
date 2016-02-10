(ns browserific.config.server
  "This server is for the config.edn GUI. It loads the state
  of the config.edn file and saves a snapshot of the state
  each time a configuration option gets edited."
  (:require [browserific.helpers.utils :as u]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [compojure.core :refer [GET PUT DELETE defroutes]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.edn :refer [wrap-edn-params]]
            [ring.util.response :as resp]))

(defn- edn-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/edn"}
   :body (pr-str data)})

(defn- init
  "Loads the config file on startup"
  []
  (edn-response {:coll (u/get-config)}))

(defn- update-config
  "Saves a copy of the current state back to the config.edn file"
  [{:keys [value]}]
  (spit u/config-file (with-out-str (pprint value)))
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
  (jetty/run-jetty #'app {:port 4242 :host "localhost"}))
