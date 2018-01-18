(ns usecase.delete-task
  (:require [failjure.core :as f]
            [core.event-store :refer :all]
            [core.commands.delete-task :refer :all]
            [usecase.list-all-tasks :as list-all-tasks]))

(defn execute! [store clock-now id]
  (let [state (list-all-tasks/execute store)]
    (f/ok->> (delete-task-command clock-now state id)
             (save-event! store))))
