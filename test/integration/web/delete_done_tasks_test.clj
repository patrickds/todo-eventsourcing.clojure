(ns integration.web.delete-done-tasks-test
  (:require [midje.sweet :refer :all]
            [integration.web.test-support :refer :all]))

(defn create-task [description]
  (let [url (str base-url "/create-task")]
    (-> (http-post-json-body url {:description description})
        :body
        java.util.UUID/fromString)))

(defn do-task [task-id]
  (http-post (str base-url "/do-task/" task-id)))

(defn delete-done-tasks []
  (http-post (str base-url "/delete-done-tasks")))

(defn list-tasks []
  (http-get-json-body (str base-url "/list-tasks")))

(against-background
 [(before :contents (start-server!))
  (after :contents (stop-server!))]

 (fact "It deletes done tasks"
       (let [task1-id (create-task "Buy milk")
             task2-id (create-task "Buy eggs")
             _ (do-task task1-id)
             response (delete-done-tasks)
             all-tasks (list-tasks)
             task (first all-tasks)]
         (:status response) => 200
         (count all-tasks) => 1
         task => {:id (str task2-id)
                  :description "Buy eggs"
                  :status "active"})))
