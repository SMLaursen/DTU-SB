# DTU-SB (Synthetic Biology)

This repository contains an Eclipse-project that can be imported in Eclipse out-of-the-box. It also contains a build-file to be 
used with Ant. Invoke `ant` without any arguments to see the available targets.

# Features

* Algorithm: Gillespie (direct)
* Input: Supports SBML up to Level 3 Version 1 (lib: JSBML v0.8).
* Output: GUI graph
* Output: CSV format

# Building and developing

The repository can be imported to Eclipse, thereby the build tools of Eclipse can be facilitated. 
Alternatively use your favorite editor and use the Ant target `build`, `test` and `run` 
(see [`build.xml`](https://bitbucket.org/jboysen/dtu-sb/src/master/build.xml?at=master)).

# Documentation

The documentation is hosted at Github Pages [http://github.com/jboysen/dtu-sb-docs](http://github.com/jboysen/dtu-sb-docs) and 
and can be accessed at [http://jboysen.github.io/dtu-sb-docs](http://jboysen.github.io/dtu-sb-docs).

# How to use

For many simulations the JVM maximum heap size has to be increased, this can be done by giving the argument `-Xmx2g` where `2g` 
is 2GB maximum heap size.  

##CLI

The Ant target `jar` will create an executable JAR `DTU-SB.jar` file that can be used if the dependencies listed below is on the classpath.
If not the Ant target `jarBundle` will create an executable JAR file `DTU-SB.bundle.jar` that works out of the box with the command:

	$ java -jar DTU-SB.bundle.jar 
	
### Example

The example in `/example` is a simulation of a negative feedback device. Run the example with the command (from the root, i.e. from `/framework`):

    $ java -Xmx2g -jar DTU-SB.bundle.jar -prop example/neg_feedback.properties -d
    
The simulation parameters can be found in the file [`neg_feedback.properties`](example/neg_feedback.properties).

##API

See the unit tests of the [parser](test/test/parser), [compiler](test/test/compiler) and [simulator](test/test/simulator) 
to understand how the API can be used.

# Dependencies

* [Apache Commons CLI 1.2](http://commons.apache.org/proper/commons-cli/)
* [LOGBack 1.0.13](http://logback.qos.ch/)

Input and simulation:

* [JSBML 0.8 (with dependencies)](http://sbml.org/Software/JSBML)
* [exp4j 0.3.11](http://www.objecthunter.net/exp4j/)

Output:

* [JFreeChart 1.0.16](http://www.jfree.org/jfreechart/)
* [JCommon 1.0.20](http://www.jfree.org/jcommon/)

Testing: 

* [JUnit 4.11 (only for testing)](https://github.com/junit-team/junit/wiki/Download-and-Install)
* [Hamcrest 1.3 (only for testing)](https://github.com/junit-team/junit/wiki/Download-and-Install)