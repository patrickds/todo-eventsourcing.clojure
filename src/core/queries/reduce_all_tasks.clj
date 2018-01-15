(ns core.queries.reduce-all-tasks
  (:require [core.queries.reduce-task-created :refer :all]
            [core.queries.reduce-task-edited :refer :all]
            [core.queries.reduce-task-done :refer :all]))

(defn- tasks-reducer [acc current]
  (condp = (:type current)
    :task-created (reduce-task-created acc current)
    :task-edited (reduce-task-edited acc current)
    :task-done (reduce-task-done acc current)
    acc))

(defn reduce-all-tasks [events]
  (->> events
       (reduce tasks-reducer {})
       (map second)))
