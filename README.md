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
for -n are 0 (strict)or 1 (not so strict).

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

## Working on ...
We are currently working on:
* unittests
* documentation
* refactoring
* perfection of the user-friendly GUI
* extension of recognized patterns

## Some known problems
During unittesting we tried to apply the examples given in the 
Gang of Four book "Design Patterns, De Nederlands Editie - Elementen 
van herbruikbare objectgeorienteerde software" in our unittests, but 
discoverd some of the examples were not properly detected, or had some
foreseeable problems, when applying larger system designs. These issues
concern the following design patterns:
* AbstractFactory
* Builder
* Prototype
* Flyweight
* Chain of responsibility
* Interpreter
* Mediator
* Observer


## Other future work ...
Besides detecting patterns in UML, we foresee:
* detection of patterns in Java-code

# Tooling
Build tool: Maven 3.3.9 (Bundled with IntelliJ)

Compiler: jdk1.8.0_121

JavaFX SceneBuilder 2.0 for Mac OSX
