(ns core.commands.edit-task-test
  (:require [midje.sweet :refer :all]
            [failjure.core :as f]
            [core.commands.edit-task :refer :all]))

(def uuid "1")
(defn clock-now [] "2018-01-13T14:46:07.519")

(def task {:id uuid
           :description "Buy 6 eggs"
           :status :active})

(def state-with-task (list task))
(def empty-state '())

(fact "It returns a task edited event when the task exists"
      (edit-task-command
       clock-now
       state-with-task
       uuid
       "I'm a edited task") => {:type :task-edited
                                :created-at (clock-now)
                                :task/id uuid
                                :task/description "I'm a edited task"})

(facts "When the edited task id doesn't exists"
       (let [result (edit-task-command
                     clock-now
                     empty-state
                     uuid
                     "I'm a edited task")]
         (fact "It returns a failure"
               result => f/failed?)
         (fact "It retuns a \"Task not found\" messagel"
               (f/message result) => "Task with id 1 not found")))
