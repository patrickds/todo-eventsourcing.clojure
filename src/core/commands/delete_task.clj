(ns core.commands.delete-task
  (:require [core.predicates :refer :all]
            [core.commands.failures :refer :all]))

(defn- delete-task-event [clock-now id]
  {:type :task-deleted
   :created-at (clock-now)
   :task/id id})

(defn delete-task-command [clock-now state id]
  (if (task-exist? state id)
    (delete-task-event clock-now id)
    (task-not-found id)))
