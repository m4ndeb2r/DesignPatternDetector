# DesignPatternDetector

## Original version
The original version was a command line application. It was developed as a prototype by E. van Doorn. It analyses 
exports from ArgoUML (`*.xmi`) and attempts to find design patterns, specied in a templates file (`*.xml`). The input 
files were provided to the application as command line arguments, along with a third argument, the maximum number of 
allowed missing edges (`-n` flag).

**Example**

`$ java -jar ./target/patterndetectionArgouml.jar -x input.xmi -t templates.xml -n 1
`

With `-n 0` the application is very strict when detecting patterns. With higher values it is less strict, and accepts 
n missing edges in detected patterns. Typical values for `-n` are 0 (strict) or 1 (not so strict).

## Current version
The current version is a JavaFX application with a graphic user interface. Just like the original prototype, it analyses 
exports from ArgoUML (`*.xmi`) and attempts to find design patterns, specied in templates file (`*.xml`). All arguments 
can be entered in the GUI. Since these are both files, they can be selected using a file browser. Also, the matching 
process is refactored, and is based on graphs, using the JGraphT library.

### How to build and automatically test the application
The application consists of two modules: dpd-application (the application) and dpd-integrations-test (the integration 
tests testing the dpd-application). 

To run the unittests of the application, in the dpd-application module directory, run:

`$ mvn clean test`

To run the integration tests, in the dpd-integration-test directory, run:

`$ mvn clean install`

or:

`$ mvn clean integration-test`

or:

`$ mvn clean test`

To run all tests, in the root directory of the project, run:

`$ mvn clean test`

To run all tests, build and package the application jar, in the root directory of the project, run:

`$ mvn clean install`

To run the unit tests (and skip the integration tests), build and package the application jar, in the dpd-application 
directory, run:

`$ mvn clean install`

### How to start the application
After building and packaging the application, go to the dpd-application directory of the project, and enter:

`$ java -jar ./target/DesignPatternDetector-<version>.jar`

**Example**

`$ java -jar ./target/DesignPatternDetector-0.0.7-SNAPSHOT.jar`

### Using the application (the happy flow)

After starting the application choose *File* > *New project* from the menu. A new project opens with the default name 
[New Project]. To run an analysis, two input parameters have to be provided: (1) select an input (an export from
ArgoUML, an `*.xmi` file), and (2) select a design pattern (templates) file (`*.xml`). After that, hit the analyse 
button, and the application will attempt to detect design patterns defined in patterns file (2) in the in input file 
containing the specs of your system design (1).

Save the project via *File > Save* or *File > Save as*. Saved projects can be re-opened via *File > Open*.

For info, warning and error messages, see the log files that are also new in the new application. Older log files
are stored as `*.gz` files in subdirectories named after the date of creation.

### Example files
Our example files can be used to run the application and to get an idea of its workings. All the example files 
(XMI-files as well as XML-files) are present in the dpd-integration-test module, in the `/src/test/resources/patterns` 
and `/src/test/resources/systems` directory. 

**Patterns file**

The `/src/test/resources/patterns/designpatterns_templates.xml` file contains the templates for all the design patterns
we have tested using the application.

**Systems files**

The `/src/test/resources/systems` directory contains a list of XMI-files that can be used as example files when running
the application. Some of them are also used in some of our integration tests. 

# Tooling
Build tool: Maven 3.3.9 (Bundled with IntelliJ)

Compiler: jdk1.8.0_121

IntelliJ IDEA 2017.x

JavaFX SceneBuilder 2.0

