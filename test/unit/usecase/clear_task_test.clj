(ns usecase.clear-task-test
  (:require [midje.sweet :refer :all]
            [event-store.in-memory :refer :all]
            [core.clock :as clock]
            [usecase.create-task :as create-task]
            [usecase.clear-task :as clear-task]
            [usecase.list-all-tasks :as list-all-tasks]))

(def store (->InMemoryStore (atom '())))
(defn clock-0 [] (clock/clock-now))
(defn clock-1 [] (-> (clock-0) (.plusMinutes 5)))

(defn reset-store [] (reset! (:state store) '()))
(background (after :facts (reset-store)))

(fact "It clears and existing task"
      (let [task-id (create-task/execute! store clock-0 "Buy milk")
            _ (clear-task/execute! store clock-1 task-id)
            all-tasks (list-all-tasks/execute store)]
        (count all-tasks) => 0))
