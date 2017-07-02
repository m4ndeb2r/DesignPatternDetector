# DesignPatternDetector

## Original version
The original version was a command line application. It was developed
as a prototype by E. van Doorn. it analyses exports from ArgoUML (\*.xmi) 
and attempts to find design patterns, specied in templates file (\*.xml).
The input files were provided to the application as command line arguments,
along with a third argument, the maximum number of allowed missing edges 
(-n flag).

**Example**

`$ java -jar ./target/patterndetectionArgouml.jar -x input.xmi -t templates.xml -n 1
`

With -n 0 the application is very strict when detecting patterns. With higher values
it is less strict, and accepts n missing edges in detected patterns. Typical values 
for -n are 0 (strict) or 1 (not so strict).

## Current version
The current version is a JavaFX application with a graphic userinterface. 
Just like the original prototype, it analyses exports from ArgoUML (\*.xmi) 
and attempts to find design patterns, specied in templates file (\*.xml).
All arguments can be entered in the GUI. Pattern detection is currently no
different from the original prototype, fundamentally.

**Example**

`$ java -jar ./target/DesignPatternDetector.jar
`

(Note that the name of the application has changed from patterndetectionArgouml to DesignPatternDedector.)

**Using the application (the happy flow)**

After starting the application choose *File* > *New project* from the menu. A new project opens with the default name 
[New Project]. To run an analysis, two input parameters have to be provided: (1) select an input (an export from
ArgoUML, an `*.xmi` file), and (2) select a desing pattern templates file (`*.xml`). After that, hit the analyse button, 
and the application will attempt to detect design patterns defined in templates file (2) in the in input file (1).

Save the project via *File > Save* or *File > Save as*. Saved projects can be re-opened via *File > Open*.

For info, warning and error messages, see the log files that are also new in the new application. Older log files
are stored as *.gz files in subdirectories named after the date of creation.

## Working on ...
We are currently working on:
* implementing a new matching method
* the new matching method requires a new parsing method
* extension of recognized patterns (made possible by the new matching method)
* unit- and integrationtests (on-going)
* documentation
* updating the GUI to work with the new matcher/parsers
* perfection of the user-friendly GUI

## Other future work ...
Besides detecting patterns in UML, we foresee:
* detection of patterns in Java-code

# Tooling
Build tool: Maven 3.3.9 (Bundled with IntelliJ)

Compiler: jdk1.8.0_121

JavaFX SceneBuilder 2.0
