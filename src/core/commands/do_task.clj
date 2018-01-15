(ns core.commands.do-task
  (:require [core.commands.failures :refer :all]
            [core.predicates :refer :all]))

(defn- do-task-event [clock-now id]
  {:type :task-done
   :created-at (clock-now)
   :task/id id
   :task/status :completed})

(defn do-task-command [clock-now state id]
  (if (task-exist? state id)
    (do-task-event clock-now id)
    (task-not-found id)))
