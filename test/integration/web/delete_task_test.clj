(ns integration.web.delete-task-test
  (:require [midje.sweet :refer :all]
            [integration.web.test-support :refer :all]))

(def base-url "http://localhost:3000")

(defn create-task [description]
  (let [url (str base-url "/create-task")]
    (-> (http-post-json-body url {:description description})
        :body
        java.util.UUID/fromString)))

(defn delete-task [task-id]
  (http-post (str base-url "/delete-task/" task-id)))

(defn list-tasks []
  (http-get-json-body (str base-url "/list-tasks")))

(background (before :contents (start-server!)))

(fact "It deletes a task"
      (let [task1-id (create-task "Buy milk")
            task2-id (create-task "Buy eggs")
            response (delete-task task1-id)
            all-tasks (list-tasks)
            task (first all-tasks)]
        (:status response) => 200
        (count all-tasks) => 1
        task => {:id (str task2-id)
                 :description "Buy eggs"
                 :status "active"}))

(fact "It returns error when task doesn't exist"
      (let [response (delete-task "ceaf775c-3b45-42a7-99b9-f293dbcef5ca")]
        (:status response) => 404
        (:body response) => "Task with id ceaf775c-3b45-42a7-99b9-f293dbcef5ca not found"))

(background (after :contents (stop-server!)))
