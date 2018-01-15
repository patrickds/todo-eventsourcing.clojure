(ns core.commands.edit-task
  (:require [core.commands.failures :refer :all]
            [core.predicates :refer :all]))

(defn- task-edited-event [created-at id description]
  {:type :task-edited
   :created-at created-at
   :task/id id
   :task/description description})

(defn edit-task-command [clock-now state id description]
  (if (task-exist? state id)
    (task-edited-event (clock-now) id description)
    (task-not-found id)))
