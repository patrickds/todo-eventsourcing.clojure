(ns core.commands.undo-task-test
  (:require [midje.sweet :refer :all]
            [core.commands.undo-task :refer :all]))

(def uuid "1")
(defn clock-now [] "2018-01-13T14:46:07.519")

(fact "It should return a task-undone event"
      (undo-task-command clock-now uuid) => {:type :task-undone
                                             :created-at (clock-now)
                                             :task/id uuid
                                             :task/status :active})
