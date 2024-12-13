# Number Converter API

## Introduction

This is a web API to convert a value into another format, for example, an arabic number to the appropriate roman number.

### Swagger UI

You can find a Swagger UI endpoint at `http://{{host}}/swagger-ui` 

## Local development

### Monitoring

There is a prepared `compose.yml` to use with Docker locally.

* A Prometheus instance can be accessed at `http://localhost:8085/`
* A Grafana instance with a JVM metrics dashboard can be accessed at `http://localhost:8086/d/jvm-actuator-service/java-virtual-machine`

## Motivation

* Getting back into pure Java after some years of using Kotlin
* Getting more practice with Maven again, because I haven't used it for a long time
* Using Spring WebFlux that I never used before, just out of curiosity how it would behave compared to Spring Web MVC (wouldn't do that in a productive project though ;-)) 
