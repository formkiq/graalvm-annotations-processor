# Gradle Graalvm Annotations Processor

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Gradle Annotations Processor for [FormKiQ's Graalvm Annotations](https://github.com/formkiq/graalvm-annotations). The annotations are used to generate a [Graalvm Reflection File](https://github.com/oracle/graal/blob/master/substratevm/Reflection.md).

The generated file can be found in ```$buildDir/classes/java/main/META-INF/graal/reflect.json```.

## Gradle Installation

Add the following to your build.gradle to enable processing [FormKiQ's Graalvm Annotations](https://github.com/formkiq/graalvm-annotations).

```
dependencies {
    annotationProcessor 'com.formkiq:graalvm-annotations-processor:1.0.0'
    <!-- Replace 1.0.0 with the version you want to use -->
}
```

## Samples
See [Samples](https://github.com/formkiq/graalvm-annotations-processor/tree/master/samples) for examples on how to use the Graalvm Annotations and Processor.
