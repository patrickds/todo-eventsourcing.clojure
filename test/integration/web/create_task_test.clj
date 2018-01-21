(ns integration.web.create-task-test
  (:require [midje.sweet :refer :all]
            [integration.web.test-support :refer :all]))

(defn create-task [description]
  (let [url (str base-url "/create-task")]
    (http-post-json-body url {:description description})))

(defn list-tasks []
  (http-get-json-body (str base-url "/list-tasks")))

(against-background
 [(before :contents (start-server!))
  (after :contents (stop-server!))]

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
          (count all-tasks) => 1))))
