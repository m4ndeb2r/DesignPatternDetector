# DesignPatternDetector

## Current version
The current version only works as a command line application. 
It analyses exports from ArgoUML (\*.xmi) and attempts to find 
design patterns, specied in templates file (\*.xml).

**Example**

`$ java -jar ./target/DesignPatternDetector.jar -x input.xmi -t templates.xml -n 1
`

## Working on ...
We are currently working on:
* unittests
* documentation
* refactoring
* a user-friendly GUI
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
* ... (work in progress)


## Other future work ...
Besides detecting patterns in UML, we foresee:
* detection of patterns in Java-code

# Tooling
Build tool: Maven 3.3.9 (Bundled with IntelliJ)

Compiler: Java 1.8
