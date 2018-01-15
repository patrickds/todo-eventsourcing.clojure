(ns core.queries.reduce-done-tasks-test
  (:require [midje.sweet :refer :all]
            [core.queries.reduce-all-tasks :refer :all]))

(fact "It returns empty list for task done without task created"
      (reduce-all-tasks '({:type :task-done
                           :created-at #inst "2017-03-01"
                           :task/id "1"
                           :task/status :completed})) => '())

(fact "It returns task done for task created and task done"
      (reduce-all-tasks '({:type :task-created
                           :created-at #inst "2017-03-01"
                           :task/id "1"
                           :task/description "Buy milk"
                           :task/status :active}
                          {:type :task-done
                           :task/id "1"
                           :task/status :completed})) => '({:id "1"
                                                            :description "Buy milk"
                                                            :status :completed}))

(fact "It maintains the state for invalid task done events"
      (reduce-all-tasks '({:type :task-created
                           :created-at #inst "2017-03-01"
                           :task/id "1"
                           :task/description "Buy milk"
                           :task/status :active}
                          {:type :task-done
                           :task/id "1234"
                           :task/status :completed})) => '({:id "1"
                                                            :description "Buy milk"
                                                            :status :active}))
