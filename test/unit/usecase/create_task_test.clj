(ns usecase.create-task-test
  (:require [midje.sweet :refer :all]
            [usecase.create-task :as create-task]))

(fact "It returns the input text"
      (create-task/execute "This is a task") => "This is a task")
