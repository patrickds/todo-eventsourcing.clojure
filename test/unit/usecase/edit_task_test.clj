(ns unit.usecase.edit-task-test
  (:require [midje.sweet :refer :all]
            [failjure.core :as f]
            [event-store.in-memory :refer :all]
            [core.clock :as clock]
            [core.event-store :refer :all]
            [usecase.create-task :as create-task]
            [usecase.edit-task :as edit-task]
            [usecase.list-all-tasks :as list-all-tasks]))

(defn clock-0 [] (clock/clock-now))
(defn clock-1 [] (-> (clock-0) (.plusMinutes 5)))

(def store (->InMemoryStore (atom '())))
(defn reset-store [] (swap! (:state store) empty))

(background (after :facts (reset-store)))

(defn find-task-by-id [all-tasks id]
  (->> all-tasks
       (filter #(= id (:id %)))
       first))

(facts "When creating one task and editing it"
       (let [task-id (create-task/execute! store clock-0 "Buy 6 eggs")
             edited-task-id (edit-task/execute! store clock-1 task-id "Buy 12 eggs")
             all-tasks (list-all-tasks/execute store)
             task (find-task-by-id all-tasks task-id)]
         (fact "Original task and edited should have the same id"
               task-id => edited-task-id)
         (fact "Should have only one task"
               (count all-tasks) => 1)
         (fact "Task should have edited description"
               (:description task) => "Buy 12 eggs")))

(facts "When editing a task that doesn't existe"
       (let [result (edit-task/execute! store clock-0 "unknown-id" "Buy 12 eggs")
             events (load-events store)]
         (fact "It should fail with proper message"
               result => f/failed?
               (f/message result) => "Task with id unknown-id not found")
         (fact "Should not add events into store"
               (count events) => 0)))
