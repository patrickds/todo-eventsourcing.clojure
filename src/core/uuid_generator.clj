(ns core.uuid-generator)

(defn generate-uuid [] (java.util.UUID/randomUUID))
