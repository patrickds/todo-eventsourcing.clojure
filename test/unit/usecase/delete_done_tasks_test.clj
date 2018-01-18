(ns usecase.delete-done-tasks-test
  (:require [midje.sweet :refer :all]
            [event-store.in-memory :refer :all]
            [core.clock :as clock]
            [usecase.create-task :as create-task]
            [usecase.do-task :as do-task]
            [usecase.delete-done-tasks :as delete-done-tasks]
            [usecase.list-all-tasks :as list-all-tasks]))

(def store (->InMemoryStore (atom '())))

(defn reset-store [] (reset! (:state store) '()))
(background (after :facts (reset-store)))

(defn clock-0 [] (clock/clock-now))
(defn clock-1 [] (-> (clock-0) (.plusMinutes 5)))
(defn clock-2 [] (-> (clock-1) (.plusMinutes 5)))
(defn clock-3 [] (-> (clock-2) (.plusMinutes 5)))
(defn clock-4 [] (-> (clock-3) (.plusMinutes 5)))
(defn clock-5 [] (-> (clock-4) (.plusMinutes 5)))

(fact "It delete all done tasks"
      (let [task-id1 (create-task/execute! store clock-0 "task 1")
            _        (do-task/execute! store clock-1 task-id1)
            task-id2 (create-task/execute! store clock-2 "task 2")
            task-id3 (create-task/execute! store clock-3 "task 3")
            _        (do-task/execute! store clock-4 task-id3)
            _        (delete-done-tasks/execute! store clock-5)
            all-tasks (list-all-tasks/execute store)]
        (count all-tasks) => 1
        (first all-tasks) => {:id task-id2
                              :description "task 2"
                              :status :active}))
