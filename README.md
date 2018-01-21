<h1><p align="center">Todo Event Sourcing</p></h1>

<div align="center">
  <a href="https://travis-ci.org/patrickds/todo-eventsourcing.clojure">
    <img src="https://travis-ci.org/patrickds/todo-eventsourcing.clojure.svg?branch=master" />
  </a>
  <a href="https://codecov.io/gh/patrickds/todo-eventsourcing.clojure">
    <img src="https://codecov.io/gh/patrickds/todo-eventsourcing.clojure/branch/master/graph/badge.svg" />
  </a>
</div>

---

A basic todo system architected around event sourcing techniques :)

## Usage

```
$ make install        # install all dependencies
$ make run            # run server on :port 3000
$ make run.watch      # run server and reloads on changes
$ make test           # runs all tests
$ make test.watch     # runs tests in watch mode
$ make coverage       # runs code coverage
$ make lint           # runs eastwood linter
$ make format         # runs cljfmt code formatter
$ make analyze        # runs kibit static analysis
$ make pre.commit     # runs lint, format and analyze
```
