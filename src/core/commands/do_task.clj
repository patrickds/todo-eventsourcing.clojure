(ns core.commands.do-task
  (:require [failjure.core :as f]))

(defn- task-exists? [state id]
  (some #(= (:id %) id) state))

(defn- do-task-event [clock-now id]
  {:type :task-done
   :created-at (clock-now)
   :task/id id
   :task/status :completed})

(defn- build-fail [id]
  (->> (str id)
       (format "Task with id %s not found")
       (f/fail)))

(defn do-task-command [clock-now state id]
  (if (task-exists? state id)
    (do-task-event clock-now id)
    (build-fail id)))
