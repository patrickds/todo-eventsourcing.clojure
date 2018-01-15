(ns core.commands.do-task-test
  (:require [midje.sweet :refer :all]
            [failjure.core :as f]
            [core.commands.do-task :refer :all]))

(def uuid "1")
(defn clock-now [] "2018-01-13T14:46:07.519")

(def task {:id uuid
           :description "Buy 6 eggs"
           :status :active})

(def state-with-task (list task))
(def state-without-task '())

(fact "It returns a task-done event when task exists"
      (do-task-command clock-now state-with-task uuid) => {:type :task-done
                                                           :created-at (clock-now)
                                                           :task/id uuid
                                                           :task/status :completed})

(fact "It fails when task does not exists"
      (let [result (do-task-command clock-now state-without-task uuid)]
        result => f/failed?
        (f/message result) => "Task with id 1 not found"))
