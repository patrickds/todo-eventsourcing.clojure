(ns core.event-store)

(defprotocol EventStore
  (load-events [store])
  (save-event! [store event]))
