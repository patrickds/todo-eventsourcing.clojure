(ns usecase.create-task-test
  (:require [midje.sweet :refer :all]
            [core.clock :as clock]
            [core.event-store :refer :all]
            [event-store.in-memory :refer :all]
            [usecase.create-task :as create-task]))

(def store (->InMemoryStore (atom '())))

(def create-task!
  (partial create-task/execute! store clock/clock-now))

(defn reset-store [] (swap! (:state store) empty))

(background (after :facts (reset-store)))

(facts "When creating a task"
       (fact "It retuns the task uuid"
             (create-task! "This is a task") => clojure.core/uuid?))
