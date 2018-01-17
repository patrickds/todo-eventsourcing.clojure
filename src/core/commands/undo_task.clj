(ns core.commands.undo-task
  (:require [core.predicates :refer :all]
            [core.commands.failures :refer :all]))

(defn- undo-task-event [clock-now id] {:type :task-undone
                                       :created-at (clock-now)
                                       :task/id id
                                       :task/status :active})

(defn undo-task-command [clock-now state id]
  (if (task-exist? state id)
    (undo-task-event clock-now id)
    (task-not-found id)))
