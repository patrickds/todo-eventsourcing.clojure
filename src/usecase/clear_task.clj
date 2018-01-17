(ns usecase.clear-task
  (:require [core.event-store :refer :all]
            [core.commands.clear-task :refer :all]))

(defn execute! [store clock-now id]
  (->> (clear-task-command clock-now id)
       (save-event! store)))
