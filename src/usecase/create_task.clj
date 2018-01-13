(ns usecase.create-task
  (:require [core.event-store :refer :all]
            [core.commands.create-task :refer :all]))

(defn generate-uuid [] (java.util.UUID/randomUUID))
(defn clock-now [] (java.time.LocalDateTime/now))

(defn execute! [store description]
  (let [uuid (generate-uuid)]
    (->> description
         (create-task-command clock-now uuid)
         (save-event! store))
    uuid))
