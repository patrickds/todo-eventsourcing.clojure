FROM clojure:lein-2.8.1-alpine AS build
WORKDIR /app
COPY . /app
RUN lein deps
RUN lein uberjar

FROM openjdk:8-jre-alpine AS production
WORKDIR /app
COPY --from=build /app/target/todoes.jar /app
ENTRYPOINT ["java", "-jar", "todoes.jar"]
