(ns usecase.create-task
  (:require [core.uuid-generator :refer :all]
            [core.event-store :refer :all]
            [core.commands.create-task :refer :all]))

(defn execute! [store clock-now description]
  (let [uuid (generate-uuid)]
    (->> description
         (create-task-command clock-now uuid)
         (save-event! store))
    uuid))
