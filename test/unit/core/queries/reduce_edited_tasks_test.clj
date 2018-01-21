(ns unit.core.queries.reduce-edited-tasks-test
  (:require [midje.sweet :refer :all]
            [core.queries.reduce-all-tasks :refer :all]))

(fact "Should return a empty list for task-edited without task-created"
      (reduce-all-tasks '({:type :task-edited
                           :task/id "1"
                           :task/description "Buy eggs"
                           :task/status :active})) => '())

(fact "Should return a edited task for task-created and task-edited events"
      (reduce-all-tasks '({:type :task-created
                           :task/id "1"
                           :task/description "Buy milk"
                           :task/status :active}
                          {:type :task-edited
                           :task/id "1"
                           :task/description "Buy eggs"
                           :task/status :active})) => '({:id "1"
                                                         :description "Buy eggs"
                                                         :status :active}))

(fact "Should maintain state if invalid task-edited event"
      (reduce-all-tasks '({:type :task-created
                           :task/id "1"
                           :task/description "First task created"
                           :task/status :active}
                          {:type :task-edited
                           :task/id "1234"
                           :task/description "I'm a edit event without a created event"
                           :task/status :active}
                          {:type :task-created
                           :task/id "2"
                           :task/description "Second task created"
                           :task/status :active}
                          {:type :task-edited
                           :task/id "2"
                           :task/description "Second task edited"
                           :task/status :active})) => '({:id "1"
                                                         :description "First task created"
                                                         :status :active}
                                                        {:id "2"
                                                         :description "Second task edited"
                                                         :status :active}))
