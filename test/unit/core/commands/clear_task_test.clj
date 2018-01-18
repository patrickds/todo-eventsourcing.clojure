(ns core.commands.clear-task-test
  (:require [midje.sweet :refer :all]
            [failjure.core :as f]
            [core.commands.clear-task :refer :all]))

(def uuid "1")
(def valid-state (list {:id uuid :description "Buy milk" :status :active}))
(def invalid-state '())
(defn clock-now [] #inst "2017-12-01")

(fact "It return task cleared event when task exist"
      (clear-task-command clock-now valid-state uuid) => {:type :task-cleared
                                                          :created-at (clock-now)
                                                          :task/id uuid})

(fact "It faild when task doesn't exist"
      (let [result (clear-task-command clock-now invalid-state uuid)]
        result => f/failed?
        (f/message result) => "Task with id 1 not found"))
