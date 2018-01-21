(ns unit.usecase.undo-task-test
  (:require [midje.sweet :refer :all]
            [core.clock :as clock]
            [core.event-store :refer :all]
            [event-store.in-memory :refer :all]
            [usecase.create-task :as create-task]
            [usecase.do-task :as do-task]
            [usecase.undo-task :as undo-task]
            [usecase.list-all-tasks :as list-all-tasks]))

(defn clock-0 [] (clock/clock-now))
(defn clock-1 [] (-> (clock-0) (.plusMinutes 5)))
(defn clock-2 [] (-> (clock-1) (.plusMinutes 5)))

(def store (->InMemoryStore (atom '())))

(defn reset-store [] (reset! (:state store) '()))
(background (after :facts (reset-store)))

(facts "When undoing a completed task"
       (let [task-id (create-task/execute! store clock-0 "Buy milk")
             done-id (do-task/execute! store clock-1 task-id)
             undone-id (undo-task/execute! store clock-2 task-id)
             task (first (list-all-tasks/execute store))]
         (fact "Ids are preserved"
               task-id => undone-id)
         (fact "It makes tasks :active again"
               (:status task) => :active)))

(facts "When undoing a unkown task"
       (let [result (undo-task/execute! store clock-0 "unkown-id")
             events (load-events store)]
         (fact "Should not add events to the store"
               (count events) => 0)))
