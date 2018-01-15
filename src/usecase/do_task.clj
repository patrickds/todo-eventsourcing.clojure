(ns usecase.do-task
  (:require [core.event-store :refer :all]
            [core.clock :refer :all]))

(defn- do-task-command [clock-now id]
  {:type :task-done
   :created-at (clock-now)
   :task/id id
   :task/status :completed})

(defn execute! [store id]
  (->> id
       (do-task-command clock-now)
       (save-event! store))
  id)
