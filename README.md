# Alkun
Alkun is a Java HTTP Server and was created because of a lot of frustration about the spring boot project.

Spring is a great project but faces performance and usability issues. The issues come from a complex code structure that has grown historically. Because of a lot of nested objects and functions the performance is not so much great and the spring code itself is hard to extend and to develop, even for the project maintainers like you can see here: https://github.com/spring-projects/spring-boot/issues/5834

Software needs to be easy, simple and most important maintainable! Alkun does only what it is supposed to be, it translates http commands and make them accessable through a java api. Not less and not more.

## Usage
```xml
<dependencies>
    <dependency>
        <groupId>com.rudolfschmidt</groupId>
        <artifactId>alkun</artifactId>
        <version>3.1.0</version>
    </dependency>
</dependencies>
```

NOTE: I know that the code documentation is still in progress. I will write more documentation in future. I know that documentation is a part of code usability.
