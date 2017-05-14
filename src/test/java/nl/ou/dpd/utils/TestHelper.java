package nl.ou.dpd.utils;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.edge.Cardinality;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.domain.node.Interface;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.Visibility;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A utility class for unittest support.
 *
 * @author Martin de Boer
 */
public class TestHelper {

    /**
     * Creates an AbstractFactory design pattern.
     *
     * @return a {@link DesignPattern} representing an AbstractFactory pattern.
     */
    public static DesignPattern createAbstractFactoryPattern() {
        final DesignPattern abstractFactoryPattern = new DesignPattern("Abstract Factory");

        final Node client = createClazz("Client");

        final Node abstractFactory = createInterface("AbstractFactory");
        final Node abstractProductA = createInterface("AbstractProductA");
        final Node abstractProductB = createInterface("AbstractProductB");

        final Node concreteFact1 = createClazz("ConcreteFact1");
        final Node concreteFact2 = createClazz("ConcreteFact2");

        final Node productA1 = createClazz("ProductA1");
        final Node productA2 = createClazz("ProductA2");

        final Node productB1 = createClazz("ProductB1");
        final Node productB2 = createClazz("ProductB2");

        abstractFactoryPattern.add(new Edge(client, abstractFactory, EdgeType.ASSOCIATION_DIRECTED));
        abstractFactoryPattern.add(new Edge(client, abstractProductA, EdgeType.ASSOCIATION_DIRECTED));
        abstractFactoryPattern.add(new Edge(client, abstractProductB, EdgeType.ASSOCIATION_DIRECTED));
        abstractFactoryPattern.add(new Edge(concreteFact1, abstractFactory, EdgeType.INHERITANCE));
        abstractFactoryPattern.add(new Edge(concreteFact2, abstractFactory, EdgeType.INHERITANCE));
        abstractFactoryPattern.add(new Edge(concreteFact1, productA1, EdgeType.DEPENDENCY));
        abstractFactoryPattern.add(new Edge(concreteFact2, productA2, EdgeType.DEPENDENCY));
        abstractFactoryPattern.add(new Edge(concreteFact1, productB1, EdgeType.DEPENDENCY));
        abstractFactoryPattern.add(new Edge(concreteFact2, productB2, EdgeType.DEPENDENCY));
        abstractFactoryPattern.add(new Edge(productA1, abstractProductA, EdgeType.INHERITANCE));
        abstractFactoryPattern.add(new Edge(productA2, abstractProductA, EdgeType.INHERITANCE));
        abstractFactoryPattern.add(new Edge(productB1, abstractProductB, EdgeType.INHERITANCE));
        abstractFactoryPattern.add(new Edge(productB2, abstractProductB, EdgeType.INHERITANCE));

        return abstractFactoryPattern;
    }

    /**
     * Creates an Adapter design pattern.
     *
     * @return a {@link DesignPattern} representing an Adapter pattern.
     */
    public static DesignPattern createAdapterPattern() {
        final DesignPattern adapterPattern = new DesignPattern("Adapter");

        final Node client = createClazz("Client");
        final Node target = createInterface("Target");
        final Node adapter = createClazz("Adapter");
        final Node adaptee = createClazz("Adaptee");

        adapterPattern.add(new Edge(client, target, EdgeType.ASSOCIATION_DIRECTED));
        adapterPattern.add(new Edge(adapter, target, EdgeType.INHERITANCE));
        adapterPattern.add(new Edge(adapter, adaptee, EdgeType.ASSOCIATION_DIRECTED));

        return adapterPattern;
    }

    /**
     * Creates a Builder design pattern.
     *
     * @return a {@link DesignPattern} representing a Builder pattern.
     */
    public static DesignPattern createBuilderPattern() {
        final DesignPattern builderPattern = new DesignPattern("Builder");

        final Node builder = createInterface("Builder");
        final Node director = createClazz("Director");
        final Node concreteBuilder = createClazz("ConcreteBuilder");
        final Node product = createClazz("Product");

        builderPattern.add(new Edge(builder, director, EdgeType.AGGREGATE));
        builderPattern.add(new Edge(concreteBuilder, builder, EdgeType.INHERITANCE));
        builderPattern.add(new Edge(concreteBuilder, product, EdgeType.DEPENDENCY));

        return builderPattern;
    }

    /**
     * Creates a ChainOfResponsibility design pattern.
     *
     * @return a {@link DesignPattern} representing a ChainOfResponsibility pattern.
     */
    public static DesignPattern createChainOfResponsibilityPattern() {
        final DesignPattern chainOfResponsibility = new DesignPattern("ChainOfResponsibility");

        final Node concreteHandler = createClazz("ConcreteHandler");
        final Node handler = createAbstractClazz("Handler");
        final Node client = createClazz("Client");

        chainOfResponsibility.add(new Edge(concreteHandler, handler, EdgeType.INHERITANCE_MULTI));
        chainOfResponsibility.add(new Edge(handler, handler, EdgeType.ASSOCIATION_DIRECTED));
        chainOfResponsibility.add(new Edge(client, handler, EdgeType.ASSOCIATION_DIRECTED));

        return chainOfResponsibility;
    }

    /**
     * Creates a Command design pattern.
     *
     * @return a {@link DesignPattern} representing a Command pattern.
     */
    public static DesignPattern createCommandPattern() {
        final DesignPattern commandPattern = new DesignPattern("Command");

        final Node client = createClazz("Client");
        final Node receiver = createClazz("Receiver");
        final Node concreteCommand = createClazz("ConcreteCommand");
        final Node command = createInterface("Command");
        final Node invoker = createClazz("Invoker");

        commandPattern.add(new Edge(client, receiver, EdgeType.ASSOCIATION_DIRECTED));
        commandPattern.add(new Edge(client, concreteCommand, EdgeType.DEPENDENCY));
        commandPattern.add(new Edge(concreteCommand, receiver, EdgeType.ASSOCIATION_DIRECTED));
        commandPattern.add(new Edge(concreteCommand, command, EdgeType.INHERITANCE));
        commandPattern.add(new Edge(command, invoker, EdgeType.AGGREGATE));

        return commandPattern;
    }

    /**
     * Creates a Composite design pattern.
     *
     * @return a {@link DesignPattern} representing a Composite pattern.
     */
    public static DesignPattern createCompositePattern() {
        final DesignPattern compositePattern = new DesignPattern("Composite");

        final Node client = createClazz("Client");
        final Node component = createInterface("Component");
        final Node leaf = createClazz("Leaf");
        final Node composite = createClazz("Composite");

        compositePattern.add(new Edge(client, component, EdgeType.ASSOCIATION_DIRECTED));
        compositePattern.add(new Edge(leaf, component, EdgeType.INHERITANCE));
        compositePattern.add(new Edge(composite, component, EdgeType.INHERITANCE));
        compositePattern.add(new Edge(component, composite, EdgeType.AGGREGATE));

        return compositePattern;
    }

    /**
     * Creates a Decorator design pattern.
     *
     * @return a {@link DesignPattern} representing a Decorator pattern.
     */
    public static DesignPattern createDecoratorPattern() {
        final DesignPattern decoratorPattern = new DesignPattern("Decorator");

        final Node component = createInterface("Component");
        final Node concreteComponent = createClazz("ConcreteComponent");
        final Node decorator = createAbstractClazz("Decorator");
        final Node concreteDecorator = createClazz("ConcreteDecorator");

        decoratorPattern.add(new Edge(concreteComponent, component, EdgeType.INHERITANCE));
        decoratorPattern.add(new Edge(decorator, component, EdgeType.INHERITANCE));
        decoratorPattern.add(new Edge(component, decorator, EdgeType.AGGREGATE));
        decoratorPattern.add(new Edge(concreteDecorator, decorator, EdgeType.INHERITANCE_MULTI));

        return decoratorPattern;
    }

    /**
     * Creates a FactoryMethod design pattern.
     *
     * @return a {@link DesignPattern} representing a FactoryMethod pattern.
     */
    public static DesignPattern createFactoryMethodPattern() {
        final DesignPattern factoryMethod = new DesignPattern("Factory Method");

        final Node product = createInterface("Product");
        final Node concreteProduct = createClazz("ConcreteProduct");
        final Node creator = createInterface("Creator");
        final Node concreteCreator = createClazz("ConcreteCreator");

        factoryMethod.add(new Edge(concreteProduct, product, EdgeType.INHERITANCE));
        factoryMethod.add(new Edge(concreteCreator, concreteProduct, EdgeType.DEPENDENCY));
        factoryMethod.add(new Edge(concreteCreator, creator, EdgeType.INHERITANCE));

        return factoryMethod;
    }

    /**
     * Creates a flyweight pattern. Checked with the UML diagram in [GoF].
     *
     * @return a {@link DesignPattern} representing a flyweight pattern.
     */
    public static DesignPattern createFlyweightPattern() {
        final DesignPattern flyweightPattern = new DesignPattern("Flyweight");

        final Node client = createClazz("Client");
        final Node flyweightFactory = createClazz("FlyweightFactory");
        final Node concreteFlyweight = createClazz("ConcreteFlyweight");
        final Node unsharedConcreteFlyweight = createClazz("UnsharedConcreteFlyweight");
        final Node flyweight = createInterface("Flyweight");

        flyweightPattern.add(new Edge(client, flyweightFactory, EdgeType.ASSOCIATION_DIRECTED));
        flyweightPattern.add(new Edge(client, concreteFlyweight, EdgeType.ASSOCIATION_DIRECTED));
        flyweightPattern.add(new Edge(client, unsharedConcreteFlyweight, EdgeType.ASSOCIATION_DIRECTED));
        flyweightPattern.add(new Edge(flyweight, flyweightFactory, EdgeType.AGGREGATE));
        flyweightPattern.add(new Edge(concreteFlyweight, flyweight, EdgeType.INHERITANCE));
        flyweightPattern.add(new Edge(unsharedConcreteFlyweight, flyweight, EdgeType.INHERITANCE));

        return flyweightPattern;
    }

    /**
     * Creates an Iterator design pattern.
     *
     * @return a {@link DesignPattern} representing an Iterator pattern.
     */

    public static DesignPattern createIteratorPattern() {
        final DesignPattern iteratorPattern = new DesignPattern("Iterator");

        final Node client = createClazz("Client");

        final Node iterator = createInterface("Iterator");
        final Node concreteIterator = createClazz("ConcreteIterator");

        final Node aggregate = createInterface("Aggregate");
        final Node concreteAggregate = createClazz("ConcreteAggregate");

        iteratorPattern.add(new Edge(concreteAggregate, aggregate, EdgeType.INHERITANCE));
        iteratorPattern.add(new Edge(client, aggregate, EdgeType.ASSOCIATION_DIRECTED));
        iteratorPattern.add(new Edge(client, iterator, EdgeType.ASSOCIATION_DIRECTED));
        iteratorPattern.add(new Edge(concreteIterator, iterator, EdgeType.INHERITANCE));
        iteratorPattern.add(new Edge(concreteIterator, concreteAggregate, EdgeType.ASSOCIATION_DIRECTED));
        iteratorPattern.add(new Edge(concreteAggregate, concreteIterator, EdgeType.DEPENDENCY));

        return iteratorPattern;
    }

    /**
     * Creates an Interpreter design pattern.
     *
     * @return a {@link DesignPattern} representing an Interpreter pattern.
     */
    public static DesignPattern createInterpreterPattern() {
        final DesignPattern interpreter = new DesignPattern("Interpreter");

        final Node client = createClazz("Client");
        final Node context = createClazz("Context");

        final Node abstractExpression = createInterface("AbstractExpression");
        final Node terminalExpression = createClazz("TerminalExpression");
        final Node nonTerminalExpression = createClazz("NonTerminalExpression");

        interpreter.add(new Edge(client, abstractExpression, EdgeType.ASSOCIATION_DIRECTED));
        interpreter.add(new Edge(client, context, EdgeType.ASSOCIATION_DIRECTED));
        interpreter.add(new Edge(terminalExpression, abstractExpression, EdgeType.INHERITANCE));
        interpreter.add(new Edge(nonTerminalExpression, abstractExpression, EdgeType.INHERITANCE));
        interpreter.add(new Edge(abstractExpression, nonTerminalExpression, EdgeType.AGGREGATE));

        return interpreter;
    }

    /**
     * Creates a Mediator design pattern.
     *
     * @return a {@link DesignPattern} representing a Mediator pattern.
     */

    public static DesignPattern createMediatorPattern() {
        final DesignPattern mediatorPattern = new DesignPattern("Mediator");

        final Node mediator = createInterface("Mediator");
        final Node concreteMediator = createClazz("ConcreteMediator");

        final Node colleague = createAbstractClazz("Colleague");
        final Node concreteColleague1 = createClazz("ConcreteColleague1");
        final Node concreteColleague2 = createClazz("ConcreteColleague2");

        mediatorPattern.add(new Edge(concreteMediator, mediator, EdgeType.INHERITANCE));
        mediatorPattern.add(new Edge(colleague, mediator, EdgeType.ASSOCIATION_DIRECTED));
        mediatorPattern.add(new Edge(concreteColleague1, colleague, EdgeType.INHERITANCE));
        mediatorPattern.add(new Edge(concreteColleague2, colleague, EdgeType.INHERITANCE));
        mediatorPattern.add(new Edge(concreteMediator, concreteColleague1, EdgeType.ASSOCIATION_DIRECTED));
        mediatorPattern.add(new Edge(concreteMediator, concreteColleague2, EdgeType.ASSOCIATION_DIRECTED));

        return mediatorPattern;
    }

    /**
     * Creates a Memento design pattern.
     *
     * @return a {@link DesignPattern} representing a Memento pattern.
     */

    public static DesignPattern createMementoPattern() {
        final DesignPattern mementoPattern = new DesignPattern("Memento");

        final Node memento = createClazz("Memento");
        final Node caretaker = createClazz("Caretaker");
        final Node originator = createClazz("Originator");

        mementoPattern.add(new Edge(memento, caretaker, EdgeType.AGGREGATE));
        mementoPattern.add(new Edge(originator, memento, EdgeType.DEPENDENCY));

        return mementoPattern;
    }

    /**
     * Creates an Observer design pattern.
     *
     * @return a {@link DesignPattern} representing an Observer pattern.
     */
    public static DesignPattern createObserverPattern() {
        final DesignPattern observerPattern = new DesignPattern("Observer");

        final Node subject = createAbstractClazz("Subject");
        final Node concreteSubject = createClazz("ConcreteSubject");
        final Node observer = createInterface("Observer");
        final Node concreteObserver = createClazz("ConcreteObserver");

        observerPattern.add(new Edge(concreteSubject, subject, EdgeType.INHERITANCE));
        observerPattern.add(new Edge(subject, observer, EdgeType.ASSOCIATION_DIRECTED));
        observerPattern.add(new Edge(concreteObserver, observer, EdgeType.INHERITANCE));
        observerPattern.add(new Edge(concreteObserver, concreteSubject, EdgeType.ASSOCIATION_DIRECTED));

        return observerPattern;
    }

    /**
     * Creates a Proxy design pattern.
     *
     * @return a {@link DesignPattern} representing a Proxy pattern.
     */
    public static DesignPattern createProxyPattern() {
        final DesignPattern proxyPattern = new DesignPattern("Proxy");

        final Node client = createClazz("Client");
        final Node subject = createInterface("Subject");
        final Node realSubject = createClazz("RealSubject");
        final Node proxy = createClazz("Proxy");

        proxyPattern.add(new Edge(client, subject, EdgeType.ASSOCIATION_DIRECTED));
        proxyPattern.add(new Edge(proxy, subject, EdgeType.INHERITANCE));
        proxyPattern.add(new Edge(realSubject, subject, EdgeType.INHERITANCE));
        proxyPattern.add(new Edge(proxy, realSubject, EdgeType.ASSOCIATION_DIRECTED));

        return proxyPattern;
    }

    /**
     * Creates a State/Strategy design pattern.
     *
     * @return a {@link DesignPattern} representing a State/Strategy pattern.
     */
    public static DesignPattern createStateStrategyPattern() {
        final DesignPattern strategyPattern = new DesignPattern("State - Strategy");

        final Node context = createClazz("Context");

        final Node strategy = createInterface("Strategy");
        final Node concreteStrategy = createClazz("ConcreteStrategy");

        strategyPattern.add(new Edge(strategy, context, EdgeType.AGGREGATE));
        strategyPattern.add(new Edge(concreteStrategy, strategy, EdgeType.INHERITANCE_MULTI));

        return strategyPattern;
    }

    /**
     * Creates a bridge pattern. Checked with the UML diagram in [GoF].
     *
     * @return a {@link DesignPattern} representing a bridge pattern.
     */
    public static DesignPattern createBridgePattern() {
        final DesignPattern bridge = new DesignPattern("Bridge");

        final Node client = createClazz("Client");

        final Node abstraction = createAbstractClazz("Abstraction");
        final Node refinedAbstraction = createClazz("RefinedAbstraction");

        final Node implementor = createInterface("Implementor");
        final Node concreteImplementor = createClazz("ConcreteImplementor");

        bridge.add(new Edge(client, abstraction, EdgeType.ASSOCIATION_DIRECTED));
        bridge.add(new Edge(refinedAbstraction, abstraction, EdgeType.INHERITANCE_MULTI));
        bridge.add(new Edge(implementor, abstraction, EdgeType.AGGREGATE));
        bridge.add(new Edge(concreteImplementor, implementor, EdgeType.INHERITANCE_MULTI));

        return bridge;
    }

    /**
     * Creates a prototype pattern.
     *
     * @return a {@link DesignPattern} representing a protoype pattern.
     */
    public static DesignPattern createPrototypePattern() {
        final DesignPattern prototypePattern = new DesignPattern("Prototype");

        final Node client = createClazz("Client");

        final Node prototype = createInterface("Prototype");
        final Node concretePrototype = createClazz("ConcretePrototype");

        prototypePattern.add(new Edge(client, prototype, EdgeType.ASSOCIATION_DIRECTED));
        prototypePattern.add(new Edge(concretePrototype, prototype, EdgeType.INHERITANCE_MULTI));

        return prototypePattern;
    }

    /**
     * Creates an {@link Interface} for test purposes.
     *
     * @param name the name of the {@link Interface}
     * @return the created {@link Interface}
     */
    public static Interface createInterface(String name) {
        return new Interface(name, name);
    }

    /**
     * Creates a non-abstract {@link Clazz} for test purposes.
     *
     * @param name the name of the {@link Clazz}
     * @return the created {@link Clazz}
     */
    public static Clazz createAbstractClazz(String name) {
        return new Clazz(name, name, Visibility.PUBLIC, null, true);
    }

    /**
     * Creates an abstract {@link Clazz} for test purposes.
     *
     * @param name the name of the {@link Clazz}
     * @return the created {@link Clazz}
     */
    public static Clazz createClazz(String name) {
        return new Clazz(name, name, Visibility.PUBLIC, null, false);
    }

    /**
     * Generate a string representation of a system under consideration for visual checking.
     *
     * @param system the system under consideration
     * @return a string representation of {@code system}
     */
    public static String systemStructureToString(SystemUnderConsideration system) {
        final StringBuilder builder = new StringBuilder("");

        final String sysTitle = String.format("System name: %s; system id: %s", system.getName(), system.getId());
        final String line = String.join("", Collections.nCopies(sysTitle.length(), "-"));
        builder.append(String.format("%s\n", line));
        builder.append(String.format("%s\n", sysTitle));
        builder.append(String.format("%s\n", line));

        builder.append(String.format("Number of edges = %d\n", system.getEdges().size()));
        builder.append(String.format("Number of nodes = %d\n\n", countUniqueNodes(system.getEdges())));

        for (int i = 0; i < system.getEdges().size(); i++) {
            Edge edge = system.getEdges().get(i);
            Node nodeLeft = edge.getLeftNode();
            Node nodeRight = edge.getRightNode();

            builder.append(String.format("Edge %d: id = %s; name = %s\n", i, edge.getId(), edge.getName()));
            builder.append(String.format("%s - %s\n", nodeLeft.getName(), nodeRight.getName()));
            builder.append(String.format("RelationType: %s\n\n", edge.getRelationType()));

            builder.append(nodeToString(edge.getCardinalityLeft(), nodeLeft, "Left"));
            builder.append(nodeToString(edge.getCardinalityRight(), nodeRight, "Right"));

        }
        return builder.toString();
    }

    private static String nodeToString(Cardinality cardinality, Node node, String side) {
        StringBuilder builder = new StringBuilder("");
        builder.append(String.format("\t%s node: name = %s; id = %s\n", side, node.getName(), node.getId()));
        builder.append(String.format("\tClass = %s\n", node.getType()));
        if (cardinality != null) {
            builder.append(String.format("\tCardinality = %s\n", cardinality));
        } else {
            builder.append("\tCardinality not set\n");
        }
        builder.append(String.format("\tVisibility = %s\n", node.getVisibility()));
        builder.append(String.format("\tisAbstract = %s\n", node.isAbstract()));
        builder.append("\tAttributes:\n");
        for (Attribute a : node.getAttributes()) {
            builder.append(String.format("\tAttribute name = %s; id = %s\n", a.getName(), a.getId()));
            if (a.getType() != null) {
                builder.append(String.format("\t\tType = %s\n", a.getType().getName()));
            } else {
                builder.append("\tType not set\n");
            }
            if (a.getVisibility() != null) {
                builder.append(String.format("\t\tVisibility = %s\n", a.getVisibility()));
            } else {
                builder.append("\tVisibility not set\n");
            }
        }
        builder.append("\n");
        return builder.toString();
    }

    /**
     * Conuts all the unique nodes attached to the specified edges. Skips doubles.
     *
     * @param edges the edges to scan
     * @return the number of unique nodes found
     */
    public static int countUniqueNodes(List<Edge> edges) {
        Set<Node> nodeSet = new HashSet<>();
        edges.forEach(edge -> {
            nodeSet.add(edge.getLeftNode());
            nodeSet.add(edge.getRightNode());
        });
        return nodeSet.size();
    }

}
