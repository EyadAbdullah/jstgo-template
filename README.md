# JSTGO Template Engine

![GitHub](https://img.shields.io/github/license/EyadAbdullah/jstgo-template)
![publish package](https://github.com/EyadAbdullah/jstgo-template/actions/workflows/publish-java-gradle.yml/badge.svg?branch=main)
![build gradle](https://github.com/EyadAbdullah/jstgo-template/actions/workflows/gradle.yml/badge.svg)
![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/net.jstgo.repo/template-engine?server=https%3A%2F%2Fs01.oss.sonatype.org)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/net.jstgo.repo/template-engine?server=https%3A%2F%2Fs01.oss.sonatype.org)

[![](https://jitpack.io/v/EyadAbdullah/jstgo-template.svg)](https://jitpack.io/#EyadAbdullah/jstgo-template)

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
repositories {
  maven { url "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2//" }
  maven { url "https://s01.oss.sonatype.org/content/repositories/snapshots/" }
}

dependencies {
  implementation 'net.jstgo.repo:template-engine:0.1.0-SNAPSHOT'
}
```

### Maven

Configure the following in your **pom.xml**

```xml
  <dependencies>
    <dependency>
      <groupId>net.jstgo.repo</groupId>
      <artifactId>template-engine</artifactId>
      <version>0.1.0-SNAPSHOT</version>
    </dependency>
  </dependencies>
  
  <repositories>
    <repository>
      <id>oss.sonatype.org-snapshot</id>
      <url>https://s01.oss.sonatype.org/content/repositories/snapshots/</url>
      <releases><enabled>false</enabled></releases>
      <snapshots><enabled>true</enabled></snapshots>
    </repository>
    <repository>
      <id>oss.sonatype.org</id>
      <url>https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/</url>
      <releases><enabled>true</enabled></releases>
      <snapshots><enabled>false</enabled></snapshots>
    </repository>
  </repositories>
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
