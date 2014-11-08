(ns browserific.config.server
    (:require [compojure.handler :as handler]
              [compojure.route :as route]
              [compojure.core :refer [GET POST PUT DELETE defroutes]]
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

(defn init []
  (reset! config-state (read-string (slurp "resources/public/config.edn")))
  (reset! om-state f/test-data)
  (edn-response {:url "/config" :coll @om-state}))

(defn send-om-state []
  (edn-response @om-state))

(defn update-config [{:keys [path value] :as params}]
  (swap! om-state #(assoc-in % path value))
  (let [item-path (into [] (butlast path))
        getter-path (get-in @om-state (conj item-path :getter))]
    (swap! config-state #(assoc-in % getter-path value)))
  (spit "resources/public/config.edn" @config-state)
  (edn-response {:status :ok}))

(defroutes app-routes
  (GET "/" [] (resp/redirect "/index.html"))
  (GET "/init" [] (init))
  (PUT "/config" {params :edn-params} (update-config params))
  (DELETE "/config" {params :edn-params} (update-config params))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> app-routes
      (handler/api)
      wrap-edn-params))


(comment (GET "/config" [] (send-om-state))
         (POST "/config" {params :edn-params} (update-config params)))
