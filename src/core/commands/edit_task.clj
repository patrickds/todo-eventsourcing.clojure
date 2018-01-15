(ns core.commands.edit-task
  (:require [core.commands.failures :refer :all]))

(defn- task-exists? [id state]
  (some #(= (:id %) id) state))

(defn- task-edited-event [created-at id description]
  {:type :task-edited
   :created-at created-at
   :task/id id
   :task/description description})

(defn edit-task-command [clock-now state id description]
  (if (task-exists? id state)
    (task-edited-event (clock-now) id description)
    (task-not-found id)))
