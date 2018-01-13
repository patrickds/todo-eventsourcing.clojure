(ns core.commands.create-task
  (:require [clojure.core :refer :all]))

(defn create-task-command [clock-now uuid description]
  {:type :task-created
   :created-at (clock-now)
   :task/id uuid
   :task/description description
   :task/status :active})
