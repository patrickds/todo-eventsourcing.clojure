(defproject todo-eventsourcing.clojure "0.1.0-SNAPSHOT"
  :description "A simple todo system using event sourcing techniques"
  :url "https://github.com/patrickds/todo-eventsourcing.clojure"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :test-paths ["test/unit"]
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :profiles {:dev {:dependencies [[midje "1.9.1"]]
                   :plugins [[lein-midje "3.2.1"]
                             [lein-cljfmt "0.5.7"]
                             [jonase/eastwood "0.2.5"]
                             [lein-cloverage "1.0.10"]
                             [lein-kibit "0.1.5"]]}})
