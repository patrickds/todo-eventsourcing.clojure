(ns event-store.in-memory
  (:require [core.event-store :refer :all]))

(defrecord InMemoryStore [state]
  EventStore
  (load-events [store] @(:state store))
  (save-event! [store event] (swap! (:state store) conj event)))
