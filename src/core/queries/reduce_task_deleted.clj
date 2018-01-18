(ns core.queries.reduce-task-deleted)

(defn reduce-task-deleted [acc current]
  (let [task-id (:task/id current)
        task-description (:task/description current)]
    (dissoc acc task-id)))
