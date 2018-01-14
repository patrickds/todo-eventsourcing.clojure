(ns usecase.edit-task
  (:require [core.event-store :refer :all]))

(defn clock-now [] (java.time.LocalDateTime/now))

(defn edit-task-command [id description]
  {:type :task-edited
   :created-at (clock-now)
   :task/id id
   :task/description description})

(defn execute! [store id description]
  (do
    (->> (edit-task-command id description)
         (save-event! store))
    id))
