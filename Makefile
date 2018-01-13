.PHONY: install
install:
	lein deps

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
analize:
	lein kibit

.PHONY: pre.commit
pre.commit:
	make format && make lint && make analyze
