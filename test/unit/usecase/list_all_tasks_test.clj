(ns usecase.list-all-tasks-test
  (:require [midje.sweet :refer :all]
            [core.event-store :refer :all]
            [event-store.in-memory :refer :all]
            [usecase.create-task :as create-task]
            [usecase.list-all-tasks :as list-all-tasks]))

(def store (->InMemoryStore (atom '())))
(defn reset-store [] (swap! (:state store) empty))

(background (after :facts (reset-store)))

(defn find-task-by-id [all-tasks id]
  (->> all-tasks
       (filter #(= id (:id %)))
       first))

(facts "When no tasks created"
       (fact "It should retrieve an empty list"
             (list-all-tasks/execute store) => '()))

(facts "When two tasks created"
       (let [first-id (create-task/execute! store "Learn to play the guitar")
             second-id (create-task/execute! store "Buy eggs")
             all-tasks (list-all-tasks/execute store)
             first-task (find-task-by-id all-tasks first-id)
             second-task (find-task-by-id all-tasks second-id)]

         (fact "It should retrieve two tasks"
               (count all-tasks) => 2)

         (fact "It retrieves first task info correctly"
               first-task => {:id first-id
                              :description "Learn to play the guitar"
                              :status :active})

         (fact "It retrieves second task info correctly"
               second-task => {:id second-id
                               :description "Buy eggs"
                               :status :active})))
