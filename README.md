# Data Collector Test App

A CDI portable extension demo that collects discovered bean classes at boot time
and exposes them via a JAX-RS endpoint.

## Architecture

The application consists of:

- **`DataCollectorExtension`** -- a CDI portable extension that observes
  `ProcessAnnotatedType` events during container bootstrap. It records every
  non-annotation, non-enum class whose package starts with `org.apache.deltaspike`
  or `org.os890`, and programmatically registers a singleton `DataHolder` bean
  containing the collected class list.
- **`DataHolder`** -- a simple POJO that stores the list of discovered classes.
- **`FoundDataResource`** -- a JAX-RS / EJB resource mapped to `/info/found` that
  renders the collected data as plain text.
- **`App`** -- the JAX-RS `Application` subclass that sets the base path to `/info`.

## Requirements

- **Java 25+**
- **Maven 3.6.3+**
- A Jakarta EE 10+ compatible application server for deployment (e.g. TomEE, WildFly)

## Build

```bash
mvn clean verify
```

## Deployment

Deploy the resulting WAR file to a Jakarta EE application server and open:

```
http://localhost:8080/data-collector-test-app-2.0.0/info/found/
```

## Testing

Tests use the [Dynamic CDI Test Bean Addon](https://github.com/os890/dynamic-cdi-test-bean-addon)
with OpenWebBeans SE to boot a CDI container in tests without an application server.

```bash
mvn clean test
```

## Quality Plugins

| Plugin      | Purpose                                      |
|-------------|----------------------------------------------|
| Compiler    | `-Xlint:all`, fail on warnings               |
| Enforcer    | Java 25+, Maven 3.6.3+, dependency convergence, banned javax.* |
| Checkstyle  | Code style validation (no star imports, braces, whitespace) |
| RAT         | Apache 2.0 license header verification       |
| Surefire    | Test execution                               |
| JaCoCo      | Code coverage reporting                      |
| Javadoc     | API documentation generation                 |

## License

This project is licensed under the [Apache License, Version 2.0](LICENSE).
