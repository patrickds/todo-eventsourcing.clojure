(ns usecase.list-all-tasks
  (:require [core.event-store :refer :all]
            [core.queries.reduce-all-tasks :refer :all]))

(defn execute [store]
  (reduce-all-tasks (load-events store)))
