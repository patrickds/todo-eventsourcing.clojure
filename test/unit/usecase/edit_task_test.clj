(ns usecase.edit-task-test
  (:require [midje.sweet :refer :all]
            [failjure.core :as f]
            [event-store.in-memory :refer :all]
            [core.event-store :refer :all]
            [usecase.create-task :as create-task]
            [usecase.edit-task :as edit-task]
            [usecase.list-all-tasks :as list-all-tasks]))

(def store (->InMemoryStore (atom '())))
(defn reset-store [] (swap! (:state store) empty))

(background (after :facts (reset-store)))

(defn find-task-by-id [all-tasks id]
  (->> all-tasks
       (filter #(= id (:id %)))
       first))

(defn create-task-and-delay!
  "Create a task and delay 1 ms to prevent dates crashing that causes unordered events (not proud of this).
   Should I inject the clock ? :thinking_face:"
  [store description]
  (let [task-id (create-task/execute! store description)]
    (Thread/sleep 1)
    task-id))

(facts "When creating one task and editing it"
       (let [task-id (create-task-and-delay! store "Buy 6 eggs")
             edited-task-id (edit-task/execute! store task-id "Buy 12 eggs")
             all-tasks (list-all-tasks/execute store)
             task (find-task-by-id all-tasks task-id)]
         (fact "Original task and edited should have the same id"
               task-id => edited-task-id)
         (fact "Should have only one task"
               (count all-tasks) => 1)
         (fact "Task should have edited description"
               (:description task) => "Buy 12 eggs")))

(facts "When editing a task that doesn't existe"
       (let [result (edit-task/execute! store "unknown-id" "Buy 12 eggs")]
         (fact "It should fail with proper message"
               result => f/failed?
               (f/message result) => "Task with id unknown-id not found")
         (fact "Should not add events into store"
               (count (load-events store)) => 0)))
