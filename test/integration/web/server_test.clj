(ns web.server-test
  (:require [midje.sweet :refer :all]
            [ring.adapter.jetty :as jetty]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [web.server :as server]))

(defn start-server []
  (server/-main :port 3000 :block-thread? :false))

(defn stop-server [server] (.stop server))

(defn parse-json [string]
  (json/parse-string string true))

(defn http-get [url]
  (-> url
      client/get
      :body
      parse-json))

(defn http-post [url body]
  (let [json-body (json/generate-string body)]
    (client/post url {:body json-body
                      :content-type :json})))

(defn create-task [base-url description]
  (let [url (str base-url "/create-task")]
    (-> (http-post url {:description description})
        :body
        java.util.UUID/fromString)))

(defn list-tasks [base-url]
  (http-get (str base-url "/list-tasks")))

(facts
 (let [server (start-server)
       url "http://localhost:3000"]

   (fact "It creates a task"
         (let [response (create-task url "Buy milk")
               all-tasks (list-tasks url)]
           response => clojure.core/uuid?
           (count all-tasks) => 1
           (:description (first all-tasks)) => "Buy milk"))

   (stop-server server)))
