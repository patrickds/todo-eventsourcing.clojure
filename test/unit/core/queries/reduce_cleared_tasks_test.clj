(ns core.queries.reduce-cleared-tasks-test
  (:require [midje.sweet :refer :all]
            [core.queries.reduce-all-tasks :refer :all]))

(fact "it produces empty list when undone task without created task"
      (reduce-all-tasks '({:type :task-cleared
                           :created-at #inst "2017-12-02"
                           :task/id "1"})) => '())

(fact "It empty list when clears a task"
      (reduce-all-tasks '({:type :task-created
                           :created-at #inst "2017-12-01"
                           :task/id "1"
                           :task/description "Buy milk"
                           :task/status :active}
                          {:type :task-cleared
                           :created-at #inst "2017-12-02"
                           :task/id "1"})) => '())

(fact "It clears only the right task"
      (reduce-all-tasks '({:type :task-created
                           :created-at #inst "2017-12-01"
                           :task/id "1"
                           :task/description "Buy milk"
                           :task/status :active}
                          {:type :task-created
                           :created-at #inst "2017-12-01"
                           :task/id "2"
                           :task/description "Buy eggs"
                           :task/status :active}
                          {:type :task-cleared
                           :created-at #inst "2017-12-02"
                           :task/id "1"})) => '({:id "2"
                                                 :description "Buy eggs"
                                                 :status :active}))

(fact "It maintains the status when invalid cleared task event"
      (reduce-all-tasks '({:type :task-created
                           :created-at #inst "2017-12-01"
                           :task/id "1"
                           :task/description "Buy milk"
                           :task/status :active}
                          {:type :task-cleared
                           :created-at #inst "2017-12-03"
                           :task/id "2"})) => '({:id "1"
                                                 :description "Buy milk"
                                                 :status :active}))
