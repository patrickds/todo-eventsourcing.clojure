(ns core.commands.failures
  (:require [failjure.core :as f]))

(defn task-not-found [id]
  (->> (str id)
       (format "Task with id %s not found")
       (f/fail)))
