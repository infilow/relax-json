# relax-json

A simple jackson wrapper to simplify the interaction between java and scala.

## Jackson for only Java

```
<dependency>
    <groupId>com.infilos</groupId>
    <artifactId>relax-json-java</artifactId>
    <version>LATEST</version>
</dependency>
```

## Jackson for Java with Scala support

> Current supported Scala binary version is 2.12.

```
<dependency>
    <groupId>com.infilos</groupId>
    <artifactId>relax-json-scala</artifactId>
    <version>LATEST</version>
</dependency>
```

Then you can register shared module between java and scala:

```
JsonMappers.register(yourCustomModule)
```

## Jackson version

Version is defined as `2.11.0-0`, `2.11.0` is the jackson release version, `-0` means this tookit's version.

## Contributions

### Version

1. Increase build version: `bash version.sh -b`
2. Chanbe jackson release version: `bash version.sh 2.11.0`

### Release

- Snapshot: `mvn clean deploy`
- Release: `mvn clean package source:jar gpg:sign install:install deploy:deploy`
