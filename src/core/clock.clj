(ns core.clock)

(defn clock-now [] (java.time.LocalDateTime/now))
