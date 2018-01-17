(ns core.queries.reduce-undone-tasks-test
  (:require [midje.sweet :refer :all]
            [core.queries.reduce-all-tasks :refer :all]))

(fact "it produces empty list when undone task without created task"
      (reduce-all-tasks '({:type :task-undone
                           :created-at #inst "2017-12-02"
                           :task/id "1"
                           :task/status :active})) => '())

(fact "It produces a undone task"
      (reduce-all-tasks '({:type :task-created
                           :created-at #inst "2017-12-01"
                           :task/id "1"
                           :task/description "Buy milk"
                           :task/status :active}
                          {:type :task-done
                           :created-at #inst "2017-12-02"
                           :task/id "1"
                           :task/status :completed}
                          {:type :task-undone
                           :created-at #inst "2017-12-03"
                           :task/id "1"
                           :task/status :active})) => '({:id "1"
                                                         :description "Buy milk"
                                                         :status :active}))

(fact "It maintains the status when invalid undone task event"
      (reduce-all-tasks '({:type :task-created
                           :created-at #inst "2017-12-01"
                           :task/id "1"
                           :task/description "Buy milk"
                           :task/status :active}
                          {:type :task-undone
                           :created-at #inst "2017-12-03"
                           :task/id "2"
                           :task/status :active})) => '({:id "1"
                                                         :description "Buy milk"
                                                         :status :active}))
