(ns unit.core.commands.undo-task-test
  (:require [midje.sweet :refer :all]
            [failjure.core :as f]
            [core.commands.undo-task :refer :all]))

(def uuid "1")
(defn clock-now [] "2018-01-13T14:46:07.519")

(def valid-state (list {:id uuid :description "Buy milk" :status :completed}))
(def invalid-state '())

(fact "When state contains task it should return a task-undone event"
      (undo-task-command clock-now valid-state uuid) => {:type :task-undone
                                                         :created-at (clock-now)
                                                         :task/id uuid
                                                         :task/status :active})

(fact "When state doesnt contain task it should fail"
      (let [result (undo-task-command clock-now invalid-state uuid)]
        result => f/failed?
        (f/message result) => "Task with id 1 not found"))
