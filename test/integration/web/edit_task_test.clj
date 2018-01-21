(ns web.edit-task-test
  (:require [midje.sweet :refer :all]
            [web.test-support :refer :all]))

(def base-url "http://localhost:3000")

(defn create-task [description]
  (let [url (str base-url "/create-task")]
    (-> (http-post-json-body url {:description description})
        :body
        java.util.UUID/fromString)))

(defn edit-task [task-id description]
  (http-post-json-body (str base-url "/edit-task/" task-id) {:description description}))

(defn list-tasks []
  (http-get-json-body (str base-url "/list-tasks")))

(background (before :contents (start-server!)))

(fact "It completes a task"
      (let [task-id (create-task "Buy milk")
            response (edit-task task-id "Buy eggs")
            edited-task-id (response-as-uuid response)
            all-tasks (list-tasks)
            task (first all-tasks)]
        (:status response) => 200
        task-id => edited-task-id
        (:description task) => "Buy eggs"
        (:status task) => "active"
        (count all-tasks) => 1))

(fact "It returns error when task doesn't exist"
      (let [response (edit-task "ceaf775c-3b45-42a7-99b9-f293dbcef5ca" "Buy eggs")]
        (:status response) => 404
        (:body response) => "Task with id ceaf775c-3b45-42a7-99b9-f293dbcef5ca not found"))

(background (after :contents (stop-server!)))
