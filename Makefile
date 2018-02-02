docker-run = docker-compose run --rm --service-ports app

.PHONY: install
install:
	$(docker-run) lein deps

.PHONY: run
run:
	docker-compose up

.PHONY: run.watch
run.watch:
	$(docker-run) lein with-profile dev ring server-headless

.PHONY: test
test:
	$(docker-run) lein midje

.PHONY: test.watch
test.watch:
	$(docker-run) lein midje :autotest

.PHONY: coverage
coverage:
	$(docker-run) lein cloverage --runner :midje --codecov

.PHONY: lint
lint:
	$(docker-run) lein eastwood

.PHONY: format
format:
	$(docker-run) lein cljfmt fix

.PHONY: analyze
analyze:
	$(docker-run) lein kibit

.PHONY: pre.commit
pre.commit:
	make format && make lint && make analyze
