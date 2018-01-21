(ns unit.usecase.delete-task-test
  (:require [midje.sweet :refer :all]
            [failjure.core :as f]
            [event-store.in-memory :refer :all]
            [core.clock :as clock]
            [usecase.create-task :as create-task]
            [usecase.delete-task :as delete-task]
            [usecase.list-all-tasks :as list-all-tasks]))

(def store (->InMemoryStore (atom '())))
(defn clock-0 [] (clock/clock-now))
(defn clock-1 [] (-> (clock-0) (.plusMinutes 5)))

(defn reset-store [] (reset! (:state store) '()))
(background (after :facts (reset-store)))

(fact "It deletes and existing task"
      (let [task-id (create-task/execute! store clock-0 "Buy milk")
            _ (delete-task/execute! store clock-1 task-id)
            all-tasks (list-all-tasks/execute store)]
        (count all-tasks) => 0))

(fact "It fails with a proper message when task doesn't exist"
      (let [result (delete-task/execute! store clock-1 "unkown-id")]
        result => f/failed?
        (f/message result) => "Task with id unkown-id not found"))
