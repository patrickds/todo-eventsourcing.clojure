(ns unit.usecase.do-task-test
  (:require [midje.sweet :refer :all]
            [failjure.core :as f]
            [core.clock :as clock]
            [core.event-store :refer :all]
            [event-store.in-memory :refer :all]
            [usecase.create-task :as create-task]
            [usecase.do-task :as do-task]
            [usecase.list-all-tasks :as list-all-tasks]))

(def store (->InMemoryStore (atom '())))
(defn clock-0 [] (clock/clock-now))
(defn clock-1 [] (-> (clock-0) (.plusMinutes 5)))

(defn reset-store [] (reset! (:state store) '()))
(background (after :facts (reset-store)))

(facts "When doing an already created task"
       (let [task-id (create-task/execute! store clock-0 "Buy milk")
             task-done-id (do-task/execute! store clock-1 task-id)
             task (first (list-all-tasks/execute store))]
         (fact "It returns the same id"
               task-done-id => task-id)
         (fact "Task should have status :completed"
               (:status task) => :completed)))

(facts "When doing an unkown task"
       (let [result (do-task/execute! store clock-0 "unkown id")
             events (load-events store)]
         (fact "It should fail with proper message"
               result => f/failed?)
         (fact "It should not add events into the store"
               (count events) => 0)))
