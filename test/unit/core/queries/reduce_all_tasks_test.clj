(ns core.queries.reduce-all-tasks-test
  (:require [midje.sweet :refer :all]
            [core.queries.reduce-all-tasks :refer :all]))

(fact "Should return empty list for empty events"
      (reduce-all-tasks '()) => empty?)

(fact "Should return empty list for unkown event"
      (reduce-all-tasks '({:type :unknown})) => empty?)

(fact "Should return a task for task-created event"
      (reduce-all-tasks '({:type :task-created
                           :task/id "1"
                           :task/description "Buy milk"
                           :task/status :active})) => '({:id "1"
                                                         :description "Buy milk"
                                                         :status :active}))
