(ns core.queries.reduce-all-tasks
  (:require [core.queries.reduce-task-created :refer :all]))

(defn- tasks-reducer [acc current]
  (condp = (:type current)
    :task-created (reduce-task-created acc current)
    acc))

(defn reduce-all-tasks [events]
  (->> events
       (reduce tasks-reducer {})
       (map second)))
