(ns usecase.undo-task
  (:require [core.event-store :refer :all]
            [core.clock :refer :all]
            [core.commands.undo-task :refer :all]))

(defn execute! [store id]
  (->> id
       (undo-task-command clock-now)
       (save-event! store))
  id)
