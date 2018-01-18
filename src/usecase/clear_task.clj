(ns usecase.clear-task
  (:require [failjure.core :as f]
            [core.event-store :refer :all]
            [core.commands.clear-task :refer :all]
            [usecase.list-all-tasks :as list-all-tasks]))

(defn execute! [store clock-now id]
  (let [state (list-all-tasks/execute store)]
    (f/ok->> (clear-task-command clock-now state id)
             (save-event! store))))
