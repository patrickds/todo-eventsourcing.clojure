(ns web.server-test
  (:require [midje.sweet :refer :all]
            [ring.adapter.jetty :as jetty]
            [clj-http.client :as client]
            [web.server :as server]))

(defn start-server []
  (server/-main :port 3000 :block-thread? :false))

(defn stop-server [server] (.stop server))

(defn http-get [url]
  (-> url
      client/get
      :body))

(facts
 (let [server (start-server)
       url "http://localhost:3000"]

   (fact "It http gets"
         (http-get url) => "Hi pretty")

   (stop-server server)))
