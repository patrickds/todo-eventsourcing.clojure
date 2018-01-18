(ns core.commands.clear-task
  (:require [core.predicates :refer :all]
            [core.commands.failures :refer :all]))

(defn- clear-task-event [clock-now id]
  {:type :task-cleared
   :created-at (clock-now)
   :task/id id})

(defn clear-task-command [clock-now state id]
  (if (task-exist? state id)
    (clear-task-event clock-now id)
    (task-not-found id)))
