(ns unit.core.commands.create-task-test
  (:require [midje.sweet :refer :all]
            [core.commands.create-task :refer :all]))

(def uuid "1")
(defn clock-now [] "2018-01-13T14:46:07.519")

(fact "It retuns a task created event"
      (create-task-command clock-now uuid "I'm a task") => {:type :task-created
                                                            :created-at (clock-now)
                                                            :task/id uuid
                                                            :task/description "I'm a task"
                                                            :task/status :active})
