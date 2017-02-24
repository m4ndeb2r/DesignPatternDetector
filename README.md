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

## Other future work ...
Besides detecting patterns in UML, we foresee:
* detection of patterns in Java-code

# Tooling
Build tool: Maven 3.3.9 (Bundled with IntelliJ)

Compiler: Java 1.8
