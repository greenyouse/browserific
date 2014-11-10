(ns browserific.config.server
  "This server is for the config.edn GUI. It loads the state
  of the config.edn file and saves a snapshot of the state
  each time configuration option gets edited."
  (:require [browserific.config.file :as f]
            [browserific.helpers.utils :as u]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer [GET PUT DELETE defroutes]]
            [ring.util.response :as resp]
            [ring.middleware.edn :refer [wrap-edn-params]]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]))

(def ^:private config-state
  "To update the config.edn file onChange"
  (atom {}))

(def ^:private om-state
  "The schema for Om to load when it starts up"
  (atom {}))

(defn- edn-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/edn"}
   :body (pr-str data)})

(defn- init []
  (reset! config-state  (-> u/config-file slurp read-string))
  (reset! om-state f/test-data)
  (edn-response {:url "/config" :coll @om-state}))

(defn- send-om-state []
  (edn-response @om-state))

(defn- update-config
  "Saves a copy of the current state back to the config.edn file"
  [{:keys [path value] :as params}]
  (swap! om-state #(assoc-in % path value))
  (let [item-path (into [] (butlast path))
        getter-path (get-in @om-state (conj item-path :getter))]
    (swap! config-state #(assoc-in % getter-path value)))
  (spit u/config-file @config-state)
  (edn-response {:status :ok}))

(defroutes ^:private app-routes
  (GET "/" [] (resp/redirect "/index.html"))
  (GET "/init" [] (init))
  (PUT "/config" {params :edn-params} (update-config params))
  (DELETE "/config" {params :edn-params} (update-config params))
  (route/resources "/")
  (route/not-found "Page not found"))

(def ^:private app
  (-> app-routes
      (handler/api)
      wrap-edn-params))

(defn config-server
  "Start a server for the config page" []
  (jetty/run-jetty #'app {:port 50000 :host "localhost"}))
