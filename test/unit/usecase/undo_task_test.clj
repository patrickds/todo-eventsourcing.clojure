(ns usecase.undo-task-test
  (:require [midje.sweet :refer :all]
            [core.event-store :refer :all]
            [event-store.in-memory :refer :all]
            [usecase.create-task :as create-task]
            [usecase.do-task :as do-task]
            [usecase.undo-task :as undo-task]
            [usecase.list-all-tasks :as list-all-tasks]))

(def store (->InMemoryStore (atom '())))

(defn reset-store [] (reset! (:state store) '()))
(background (after :facts (reset-store)))

(defn do-and-delay [f & params]
  (let [result (apply f params)]
    (Thread/sleep 1)
    result))

(facts "When undoing a completed task"
       (let [task-id (do-and-delay create-task/execute! store "Buy milk")
             done-id (do-and-delay do-task/execute! store task-id)
             undone-id (undo-task/execute! store task-id)
             task (first (list-all-tasks/execute store))]
         (fact "Ids are preserved"
               task-id => undone-id)
         (fact "It makes tasks :active again"
               (:status task) => :active)))

(facts "When undoing a unkown task"
       (let [result (undo-task/execute! store "unkown-id")
             events (load-events store)]
         (fact "Should not add events to the store"
               (count events) => 0)))
