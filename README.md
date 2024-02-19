# JSTGO Template Engine
![GitHub](https://img.shields.io/github/license/EyadAbdullah/jstgo-template)
![publish package](https://github.com/EyadAbdullah/jstgo-template/actions/workflows/publish-java-gradle.yml/badge.svg?branch=main)
![build gradle](https://github.com/EyadAbdullah/jstgo-template/actions/workflows/gradle.yml/badge.svg)


![![Maven Central (Releases)](https://img.shields.io/maven-central/v/net.jstgo.repo/template-engine)](https://mvnrepository.com/artifact/net.jstgo.repo/template-engine)


## Overview

The Template Engine Library is an open-source project that provides a versatile and customizable
template engine for various applications. This library allows developers to easily integrate
dynamic content generation into their projects by using templates with placeholders that are
replaced with actual data during runtime.

## Documentation

For detailed information on how to use the Template Engine Library, check out our full
documentation ( which still does not exist 😊).

## Implementation

To use Implement Jstgo Template Engine in your Java project, follow these simple steps:

### Gradle

Configure the following in your **build.gradle**

```groovy
dependencies {
  implementation 'net.jstgo.repo:template-engine:0.1.0'
}
```

### Maven

Configure the following in your **pom.xml**

```xml
  <dependencies>
    <dependency>
      <groupId>net.jstgo.repo</groupId>
      <artifactId>template-engine</artifactId>
      <version>0.1.0</version>
    </dependency>
  </dependencies>
```

## Documentation

Simple example of how to use. 

```java
  public class Main {
    public static void main(String[] args) {
      var engine = new TemplateEngine();
      var template = engine.processTemplate("5 + 6 = {{ 5 + 6 }}");
      System.out.println(template.getOutput());
    }
  }
```

## Contributing

We welcome contributions from the community! To get involved:

1. Fork the repository and create a new branch.
2. Make your enhancements or bug fixes.
3. Submit a pull request with a clear description of your changes.
4. Please read our [Contribution Guidelines](./docs/contribution-guidelines.md) for more details.

## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.
