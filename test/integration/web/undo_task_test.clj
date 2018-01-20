(ns web.undo-task-test
  (:require [midje.sweet :refer :all]
            [web.test-support :refer :all]))

(def base-url "http://localhost:3000")

(defn create-task [description]
  (let [url (str base-url "/create-task")]
    (-> (http-post-json-body url {:description description})
        response-as-uuid)))

(defn do-task [task-id]
  (-> (str base-url "/undo-task/" task-id)
      (http-post)
      response-as-uuid))

(defn undo-task [task-id]
  (http-post (str base-url "/undo-task/" task-id)))

(defn list-tasks []
  (http-get-json-body (str base-url "/list-tasks")))

(background (before :contents (start-server!)))

(fact "It makes active a completed task"
      (let [task-id (create-task "Buy milk")
            _ (do-task task-id)
            response (undo-task task-id)
            undone-task-id (response-as-uuid response)
            all-tasks (list-tasks)
            task (first all-tasks)]
        (:status response) => 200
        task-id => undone-task-id
        (:description task) => "Buy milk"
        (:status task) => "active"
        (count all-tasks) => 1))

(fact "It returns error when task doesn't exist"
      (let [response (undo-task "ffaf779c-3b45-42a7-99b9-f293dbcef5ca")]
        (:status response) => 404
        (:body response) => "Task with id ffaf779c-3b45-42a7-99b9-f293dbcef5ca not found"))

(background (after :contents (stop-server!)))
