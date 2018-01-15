(ns core.commands.do-task
  (:require [core.commands.failures :refer :all]))

(defn- task-exists? [state id]
  (some #(= (:id %) id) state))

(defn- do-task-event [clock-now id]
  {:type :task-done
   :created-at (clock-now)
   :task/id id
   :task/status :completed})

(defn do-task-command [clock-now state id]
  (if (task-exists? state id)
    (do-task-event clock-now id)
    (task-not-found id)))
