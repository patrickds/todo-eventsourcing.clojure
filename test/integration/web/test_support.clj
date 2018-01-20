(ns web.test-support
  (:require [midje.sweet :refer :all]
            [ring.adapter.jetty :as jetty]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [web.server :as web-server]))

(defonce server nil)

(defn start-server! []
  (alter-var-root
   #'server
   (fn [_]
     (web-server/-main :port 3000 :block-thread? :false))))

(defn stop-server! []
  (alter-var-root
   #'server
   (fn [server]
     (when server
       (.stop server)))))

(defn parse-json [string]
  (json/parse-string string true))

(defn http-get [url] (client/get url))
(defn http-post [url] (client/post url))

(defn http-get-json-body [url]
  (-> url
      http-get
      :body
      parse-json))

(defn http-post-json-body [url body]
  (let [json-body (json/generate-string body)]
    (client/post url {:body json-body
                      :content-type :json})))
