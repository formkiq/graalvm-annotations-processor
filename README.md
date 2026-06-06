# GraalVM Annotations Processor

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Annotation processor for [FormKiQ's GraalVM Annotations](https://github.com/formkiq/graalvm-annotations). The processor scans supported annotations at compile time and generates a GraalVM native-image reflection configuration file.

The generated file is written to:

```text
$buildDir/classes/java/main/META-INF/native-image/<package>/reflect-config.json
```

## Requirements

Version 1.6.0 and later requires Java 17 or newer to run the annotation processor.

Use version 1.5.x if your build must run the annotation processor on Java 11.

## Installation

Add the annotations library and annotation processor to your Gradle build:

```gradle
dependencies {
    implementation 'com.formkiq:graalvm-annotations:1.2.0'
    annotationProcessor 'com.formkiq:graalvm-annotations-processor:1.6.0'
}
```

For Gradle Kotlin DSL:

```kotlin
dependencies {
    implementation("com.formkiq:graalvm-annotations:1.2.0")
    annotationProcessor("com.formkiq:graalvm-annotations-processor:1.6.0")
}
```

## Usage

Annotate a class or record with `@Reflectable`:

```java
import com.formkiq.graalvm.annotations.Reflectable;

@Reflectable
public record Event(String name) {
}
```

The processor generates a `reflect-config.json` entry similar to:

```json
[
  {
    "name": "sample.Event",
    "allDeclaredConstructors": true,
    "allPublicConstructors": true,
    "allDeclaredMethods": true,
    "allDeclaredFields": true,
    "allPublicFields": true,
    "allPublicMethods": true
  }
]
```

You can override the generated reflection flags:

```java
@Reflectable(
    allDeclaredConstructors = true,
    allPublicConstructors = false,
    allDeclaredMethods = true,
    allDeclaredFields = true)
public final class Event {
}
```

## Java Records

Version 1.6.0 and later automatically sets `allDeclaredConstructors` to `true` for Java records annotated with `@Reflectable` or referenced through `@ReflectableClass`.

This is required by libraries such as Gson 2.13, which use the canonical record constructor through `Class.getDeclaredConstructor(...)` when deserializing records in GraalVM native images.

## Build and Test

Run the test suite with Java 17 or newer:

```bash
./gradlew test
```

Publish locally for testing with downstream projects:

```bash
./gradlew publishToMavenLocal
```

## Samples

See [Samples](https://github.com/formkiq/graalvm-annotations-processor/tree/master/samples) for examples on how to use the GraalVM annotations and processor.

## License

This project is licensed under the Apache License 2.0.
