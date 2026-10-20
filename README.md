# classgraph-playground

[![CI](https://github.com/sullis/classgraph-playground/actions/workflows/ci.yml/badge.svg)](https://github.com/sullis/classgraph-playground/actions/workflows/ci.yml)

A small sandbox for experimenting with [ClassGraph](https://github.com/classgraph/classgraph),
the JVM classpath and module scanning library. The interesting parts live in the
tests: each one exercises a piece of the ClassGraph API and prints what it finds.

The build has two Maven modules with the same tests, one per ClassGraph major version:

| Module | ClassGraph | Java |
| --- | --- | --- |
| `classgraph5` | 5.x | 21 |
| `classgraph6` | 6.x | 25 (ClassGraph 6 is compiled for Java 25) |

`classgraph5` always builds. `classgraph6` sits in a Maven profile that switches on
with JDK 25+, so the JDK you build with decides what builds: JDK 21 builds
`classgraph5` only, and JDK 25+ builds both. CI runs the build once on each JDK.
The build fails fast (via `maven-enforcer-plugin`) on JDKs older than 21.

# Release notes
- [ClassGraph 5.0.0](https://github.com/classgraph/classgraph/releases/tag/classgraph-5.0.0)
- [ClassGraph 6.0.0](https://github.com/classgraph/classgraph/releases/tag/classgraph-6.0.0)

# Upgrading from older ClassGraph versions
- [Porting ClassGraph 4.x code to ClassGraph 6.x](https://github.com/classgraph/classgraph/wiki/Porting-ClassGraph-4.x-code-to-ClassGraph-6.x).

## Requirements

- Java 21 for `classgraph5`, Java 25 for `classgraph6` (an [SDKMAN!](https://sdkman.io)
  `.sdkmanrc` is included at the root, pinned to `21.0.3-tem`; each module has its
  own `.sdkmanrc` too, so with `sdkman_auto_env=true`, `cd classgraph6` switches you to Java 25)
- Maven (3.9+ recommended; not enforced by the build)

## Build and test

```bash
mvn -ntp -B clean test        # on JDK 21 -> classgraph5, on JDK 25 -> classgraph5 + classgraph6
```

The tests print what the scans discovered to stdout, which appears in the
console and in `<module>/target/surefire-reports/`.

Run a single test class or method:

```bash
mvn -ntp test -Dtest=ClassgraphTest
mvn -ntp test -Dtest=ClassgraphTest#findResourceFile
```

## What the tests demonstrate

`ClassgraphTest` ([classgraph5](classgraph5/src/test/java/foobar/ClassgraphTest.java),
[classgraph6](classgraph6/src/test/java/foobar/ClassgraphTest.java)):

| Test | ClassGraph feature |
| --- | --- |
| `findClasses` | `enableClassInfo()` scanning, then walking `ScanResult.getAllClasses()` and printing each class's `SourceFile` attribute (`ClassInfo.getSourceFile()`) |
| `findResourceFile` | Resource scanning — locating `data.txt` via `ScanResult.getAllResources()` and inspecting its path and classpath element URI |

Both tests call `enableClasspath()` plus `disableJarScanning()` and
`disableNestedJarScanning()`, so the
scan is limited to this project's own directory-based classpath entries, which
keeps results small and deterministic.

## Project layout

```
pom.xml                                  parent POM: shared dependencies, plugins, JDK 25 profile
classgraph5/, classgraph6/               one module per ClassGraph major version, same layout:
  src/main/java/foobar/HelloApp.java     trivial class, something for the scanner to find
  src/main/resources/data.txt            trivial resource, ditto
  src/test/java/foobar/                  the ClassGraph experiments
  src/test/resources/logback-test.xml    test logging configuration
```

## Notes

- Surefire is configured with `forkCount=8` and `reuseForks=true`, so test classes
  can run in parallel across several JVMs. Forking is per class, so with a single
  test class only one JVM is used in practice.
- Dependabot ignores ClassGraph major-version bumps, so each module stays on its major.
- Dependency updates arrive via Dependabot and are auto-merged once CI passes
  (see `.github/workflows/`).

## License

[Apache License 2.0](LICENSE)
