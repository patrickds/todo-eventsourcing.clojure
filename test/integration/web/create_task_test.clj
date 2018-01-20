(ns web.create-task-test
  (:require [midje.sweet :refer :all]
            [web.test-support :refer :all]))

(def base-url "http://localhost:3000")

(defn create-task [description]
  (let [url (str base-url "/create-task")]
    (http-post-json-body url {:description description})))

(defn list-tasks []
  (http-get-json-body (str base-url "/list-tasks")))

(background (before :contents (start-server!)))

(facts
 (fact "It creates a task"
       (let [response (create-task "Buy milk")
             task-id (-> response :body java.util.UUID/fromString)
             all-tasks (list-tasks)
             task (first all-tasks)]
         (:status response) => 200
         task-id => clojure.core/uuid?
         (:description task) => "Buy milk"
         (:status task) => "active"
         (count all-tasks) => 1)))

(background (after :contents (stop-server!)))
