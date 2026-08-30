# classgraph-playground

[![CI](https://github.com/sullis/classgraph-playground/actions/workflows/ci.yml/badge.svg)](https://github.com/sullis/classgraph-playground/actions/workflows/ci.yml)

A small sandbox for experimenting with [ClassGraph](https://github.com/classgraph/classgraph),
the JVM classpath and module scanning library. The interesting parts live in the
tests: each one exercises a piece of the ClassGraph API and prints what it finds.

## Requirements

- Java 21 (an [SDKMAN!](https://sdkman.io) `.sdkmanrc` is included, pinned to `21.0.3-tem`)
- Maven 3.9+

## Build and test

```bash
mvn -ntp -B clean test
```

Test output goes to the console, so use `-Dsurefire.useFile=false` or read
`target/surefire-reports/` to see what the scans discovered.

Run a single test class or method:

```bash
mvn -ntp test -Dtest=ClassgraphTest
mvn -ntp test -Dtest=ClassgraphTest#findResourceFile
```

## What the tests demonstrate

[`ClassgraphTest`](src/test/java/foobar/ClassgraphTest.java):

| Test | ClassGraph feature |
| --- | --- |
| `findClasses` | `enableClassInfo()` scanning, then walking `ScanResult.getAllClasses()` and reading each class's source file |
| `findResourceFile` | Resource scanning — locating `data.txt` via `ScanResult.getAllResources()` and inspecting its path and classpath element URI |

Both tests call `disableJarScanning()` and `disableNestedJarScanning()` so the
scan is limited to this project's own directory-based classpath entries, which
keeps results small and deterministic.

## Project layout

```
src/main/java/foobar/HelloApp.java   trivial class, something for the scanner to find
src/main/resources/data.txt          trivial resource, ditto
src/test/java/foobar/                the ClassGraph experiments
src/test/resources/logback-test.xml  test logging configuration
```

## Notes

- Surefire is configured with `forkCount=8` and `reuseForks=true`, so tests run
  in parallel across several JVMs.
- Dependency updates arrive via Dependabot and are auto-merged once CI passes
  (see `.github/workflows/`).

## License

[Apache License 2.0](LICENSE)
