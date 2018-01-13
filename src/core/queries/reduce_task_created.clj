(ns core.queries.reduce-task-created)

(defn reduce-task-created [acc current]
  (assoc acc (:task/id current) {:id (:task/id current)
                                 :description (:task/description current)
                                 :status (:task/status current)}))
