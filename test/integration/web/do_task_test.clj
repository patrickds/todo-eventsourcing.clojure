(ns web.do-task-test
  (:require [midje.sweet :refer :all]
            [web.test-support :refer :all]))

(def base-url "http://localhost:3000")

(defn create-task [description]
  (let [url (str base-url "/create-task")]
    (-> (http-post-json-body url {:description description})
        :body
        java.util.UUID/fromString)))

(defn do-task [task-id]
  (http-post (str base-url "/do-task/" task-id)))

(defn list-tasks []
  (http-get-json-body (str base-url "/list-tasks")))

(background (before :contents (start-server!)))

(fact "It completes a task"
      (let [task-id (create-task "Buy milk")
            response (do-task task-id)
            done-task-id (response-as-uuid response)
            all-tasks (list-tasks)
            task (first all-tasks)]
        (:status response) => 200
        task-id => done-task-id
        (:description task) => "Buy milk"
        (:status task) => "completed"
        (count all-tasks) => 1))

(fact "It returns error when task doesn't exist"
      (let [response (do-task "ceaf775c-3b45-42a7-99b9-f293dbcef5ca")]
        (:status response) => 404
        (:body response) => "Task with id ceaf775c-3b45-42a7-99b9-f293dbcef5ca not found"))

(background (after :contents (stop-server!)))
