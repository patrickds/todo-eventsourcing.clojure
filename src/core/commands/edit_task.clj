(ns core.commands.edit-task
  (:require [failjure.core :as f]))

(defn- task-exists? [id state]
  (some #(= (:id %) id) state))

(defn- task-edited-event [created-at id description]
  {:type :task-edited
   :created-at created-at
   :task/id id
   :task/description description})

(defn- build-fail [id]
  (->> (str id)
       (format "Task with id %s not found")
       (f/fail)))

(defn edit-task-command [clock-now state id description]
  (if (task-exists? id state)
    (task-edited-event (clock-now) id description)
    (build-fail id)))
