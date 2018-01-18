(ns core.queries.reduce-all-tasks
  (:require [core.queries.reduce-task-created :refer :all]
            [core.queries.reduce-task-edited :refer :all]
            [core.queries.reduce-task-done :refer :all]
            [core.queries.reduce-task-undone :refer :all]
            [core.queries.reduce-task-deleted :refer :all]))

(defn- tasks-reducer [acc current]
  (condp = (:type current)
    :task-created (reduce-task-created acc current)
    :task-edited (reduce-task-edited acc current)
    :task-done (reduce-task-done acc current)
    :task-undone (reduce-task-undone acc current)
    :task-deleted (reduce-task-deleted acc current)
    acc))

(defn reduce-all-tasks [events]
  (->> events
       (reduce tasks-reducer {})
       (map second)))
