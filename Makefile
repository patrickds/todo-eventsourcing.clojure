.PHONY: install
install:
	lein deps

.PHONY: run
run:
	lein run -m web.server/-main

.PHONY: run.watch
run.watch:
	lein clean
	lein with-profile dev ring server

.PHONY: test
test:
	lein midje

.PHONY: test.watch
test.watch:
	lein midje :autotest

.PHONY: coverage
coverage:
	lein cloverage --runner :midje --codecov

.PHONY: lint
lint:
	lein eastwood

.PHONY: format
format:
	lein cljfmt fix

.PHONY: analyze
analyze:
	lein kibit

.PHONY: pre.commit
pre.commit:
	make format && make lint && make analyze
