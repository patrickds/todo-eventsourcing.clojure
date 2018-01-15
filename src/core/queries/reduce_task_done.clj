(ns core.queries.reduce-task-done)

(defn reduce-task-done [acc current]
  (let [task-id (:task/id current)
        task-status (:task/status current)]
    (assoc-in acc [task-id :status] task-status)))
