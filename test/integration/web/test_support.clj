(ns integration.web.test-support
  (:require [midje.sweet :refer :all]
            [ring.adapter.jetty :as jetty]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [web.server :as web-server]))

(def server (atom nil))

(defn start-server! []
  (reset! (:state web-server/store) '())
  (swap! server
         (fn [_]
           (web-server/-main :port 5000 :block-thread? :false))))

(defn stop-server! []
  (when @server
    (.stop @server)))

(defn parse-json [string]
  (json/parse-string string true))

(defn http-get [url]
  (try
    (client/get url)
    (catch Exception e (ex-data e))))

(defn http-post
  ([url] (http-post url {}))
  ([url body] (try
                (client/post url body)
                (catch Exception e (ex-data e)))))

(defn http-get-json-body [url]
  (-> (http-get url) :body parse-json))

(defn http-post-json-body [url body]
  (http-post url {:body (json/generate-string body)
                  :content-type :json}))

(defn response-as-uuid [response]
  (-> response :body java.util.UUID/fromString))

(def base-url "http://localhost:5000")
