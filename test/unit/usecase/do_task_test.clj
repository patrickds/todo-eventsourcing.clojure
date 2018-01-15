(ns usecase.do-task-test
  (:require [midje.sweet :refer :all]
            [failjure.core :as f]
            [core.event-store :refer :all]
            [event-store.in-memory :refer :all]
            [usecase.create-task :as create-task]
            [usecase.do-task :as do-task]
            [usecase.list-all-tasks :as list-all-tasks]))

(def store (->InMemoryStore (atom '())))

(defn reset-store [] (reset! (:state store) '()))
(background (after :facts (reset-store)))

(defn create-task-and-delay!
  [store description]
  (let [task-id (create-task/execute! store description)]
    (Thread/sleep 1)
    task-id))

(facts "When doing an already created task"
       (let [task-id (create-task-and-delay! store "Buy milk")
             task-done-id (do-task/execute! store task-id)
             task (first (list-all-tasks/execute store))]
         (fact "It returns the same id"
               task-done-id => task-id)
         (fact "Task should have status :completed"
               (:status task) => :completed)))

(facts "When doing an unkown task"
       (let [result (do-task/execute! store "unkown id")
             events (load-events store)]
         (fact "It should fail with proper message"
               result => f/failed?)
         (fact "It should not add events into the store"
               (count events) => 0)))
