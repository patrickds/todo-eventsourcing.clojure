(ns web.server
  (:require [compojure.core :refer [routes]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [event-store.in-memory :refer :all]
            [web.routes :refer :all]
            [web.param-parser :refer [parse-params]])
  (:gen-class))

(defn make-store [] (->InMemoryStore (atom '())))

(defn handler [store] (routes (api-routes store)))

(defn make-server [store]
  (-> (handler store)
      (wrap-json-response)
      (wrap-json-body {:keywords? true})))

(def development-server
  "Global var used by lein ring for interactive development"
  (make-server (make-store)))

(defn -main [& args]
  (let [options (parse-params args)
        store (make-store)
        server (make-server store)]
    (jetty/run-jetty server {:port (:port options)
                             :join? (:block-thread? options)})))
