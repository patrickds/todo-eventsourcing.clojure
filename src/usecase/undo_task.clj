(ns usecase.undo-task
  (:require [failjure.core :as f]
            [core.event-store :refer :all]
            [core.commands.undo-task :refer :all]
            [usecase.list-all-tasks :as list-all-tasks]))

(defn- undo-task [store clock-now state id]
  (f/ok->> (undo-task-command clock-now state id)
           (save-event! store)))

(defn execute! [store clock-now id]
  (let [state (list-all-tasks/execute store)
        result (undo-task store clock-now state id)]
    (if (f/failed? result)
      result
      id)))
