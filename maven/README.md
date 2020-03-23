# Apache Maven

## Introduction

See [this document](MavenIntroduction.md) for an introduction to Maven and POMs.

## Exercises

In the following exercises, we will see how to set up a project with Maven, step by step.

1. Create a new directory called `super-project`.  
**Solution:** `mkdir super-project`

2. Go to [this page](https://thorntail.io/generator/) and generate two new *Thorntail* projects called `sub-project-1` and `sub-project-2` with the group id `ch.unige`. You don't need to specify any dependencies for them. Once you've downloaded the two generated archives, unzip them and copy the resulting folders in `super-project`.  
**Solution:** See the [sub-project-1](https://github.com/PInfo-2020/Exercises/tree/master/maven/super-project/sub-project-1) and [sub-project-2](https://github.com/PInfo-2020/Exercises/tree/master/maven/super-project/sub-project-2) folders.

3. Open the `HelloWorldEndpoint.java` files that were automatically generated in `sub-project-1` and `sub-project-2` (their paths, relative to the projects roots, is `src/main/java/ch/unige/subproject*/rest`, where the `*` must be replaced by the project's number). In the two files, replace the path `"/hello"` (in `@Path("/hello")`) by `"/hello1"` in `sub-project-1` and ``"/hello2"`` in `sub-project-2`, and replace `"Hello from Thorntail!"` by `"Hello from Subproject 1!"` and `"Hello from Subproject 2!"`.  
**Solution:** See the `HelloWorldEndpoint.java` files in [sub-project-1](https://github.com/PInfo-2020/Exercises/blob/master/maven/super-project/sub-project-1/src/main/java/ch/unige/subproject1/rest/HelloWorldEndpoint.java) and [sub-project-2](https://github.com/PInfo-2020/Exercises/blob/master/maven/super-project/sub-project-2/src/main/java/ch/unige/subproject2/rest/HelloWorldEndpoint.java).

4. Create a new `pom.xml` file at the root of the `super-project` directory (see [this document](MavenIntroduction.md) to learn about POMs and what they should contain to be valid). In the POM:

    1. Set the project's `<groupId>` to `ch.unige`, the `<artifactId>` to `super-project`, the `<version>` to `0.1.0-SNAPSHOT`, the `<name>` to `Super Project` and the `<packaging>` to `pom`.

    2. Define two modules for `sub-project-1` and `sub-project-2`.

    3. Add the following properties:
        ```xml
        <properties>
            <maven.compiler.source>1.8</maven.compiler.source>
		    <maven.compiler.target>1.8</maven.compiler.target>
		    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>

            <java-ee-api.version>8.0.1</java-ee-api.version>
            <version.thorntail>2.6.0.Final</version.thorntail>

            <failOnMissingWebXml>false</failOnMissingWebXml>
        </properties>
        ```
    
    4. In a `dependencyManagement` element, fix the dependency for:
        ```xml
        <groupId>io.thorntail</groupId>
        <artifactId>bom</artifactId>
        ```
        to version `2.6.0.Final`.
    
    5. In a `build` element, define a `pluginManagement` element to fix the following plugin versions:
        
        - ```xml
          <groupId>org.apache.maven.plugins</groupId>
	      <artifactId>maven-compiler-plugin</artifactId>
          ```
          to version `3.8.1`, and use the `maven.compiler.source` and `maven  compiler.target` properties defined above as the `source` and `target`  values in the `configuration` element of the plugin.

        - ```xml
            <groupId>io.thorntail</groupId>
			<artifactId>thorntail-maven-plugin</artifactId>
          ```
          to the version defined in `properties` for `version.thorntail`.
        
        - ```xml
          <groupId>io.fabric8</groupId>
		  <artifactId>fabric8-maven-plugin</artifactId>
          ```
          to version `4.4.0`.
    
    6. Still in the `build` element, use the two first plugins in `plugins` to tell Maven to compile the sources and package the result in WARs with Thorntail:
        ```xml
        <groupId>org.apache.maven.plugins</groupId>
	    <artifactId>maven-compiler-plugin</artifactId>
        ``` 
        and
        ```xml
        <groupId>io.thorntail</groupId>
	    <artifactId>thorntail-maven-plugin</artifactId>
        ```
        For `thorntail`, you will need to define an `execution` with the goal `package`.  
**Solution:** See this [pom.xml](https://github.com/PInfo-2020/Exercises/blob/master/maven/super-project/pom.xml).

5. Open the `pom.xml` files in `sub-project-1` and `sub-project-2`. Change their `version` element to `0.1.0-SNAPSHOT`. Set their parent to the `super-project` POM.  
**Solution:** See the `pom.xml` files in [sub-project-1](https://github.com/PInfo-2020/Exercises/blob/master/maven/super-project/sub-project-1/pom.xml) and [sub-project-2](https://github.com/PInfo-2020/Exercises/blob/master/maven/super-project/sub-project-2/pom.xml).

6. If you now run `mvn clean install` at the root of the `super-project`, the two modules and the project should build. Congratulations, you've managed to set up a Java project composed of two Thorntail modules with Maven.

7. As mentioned in [this document](MavenIntroduction.md), the `fabric8-maven-plugin` can be used to build Docker images with Maven. To set it up, open the `pom.xml` of `super-project` and define a new `profile` with the `id` `package-docker-image`. In this profile, use the `fabric8-maven-plugin` to build Docker images for the two modules of the project. You can basically just reuse the profiles from the [microservices example](https://github.com/hostettler/microservices/blob/master/pom.xml) exactly as is for this step. Be careful however to properly define the properties used by the profiles, in particular `skip-docker-build`. You will need to set `skip-docker-build` to `true` in the `super-project` POM, because you do not want to build a Docker image for it, only for the modules it contains. This also means that the POMs of the modules will need to set this property to `false` to override the value defined in the project's Super POM.  
**Solution:** See the `<profiles>` section in the [Super POM](https://github.com/PInfo-2020/Exercises/blob/master/maven/super-project/pom.xml).

8. Once you've set up the `package-docker-image` profile in your Super POM, you can run `mvn install -Ppackage-docker-image` from the command line. This will package the two modules in `super-project` into Docker images. You can then use these images like any other. For example, if you run `docker run -p 10080:8080 --name=subproject1 unige/sub-project-1` from the command line, this will launch a Docker container with the image you just created with Fabric 8 for the `sub-project-1` module. If you go to `localhost:10080/hello1`, you will get the message `Hello from Subproject 1!`. Congrats, you've built valid Docker image with Maven for your project's modules !
