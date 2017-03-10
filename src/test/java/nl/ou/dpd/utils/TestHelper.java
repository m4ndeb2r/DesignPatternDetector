package nl.ou.dpd.utils;

import nl.ou.dpd.domain.Clazz;
import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.Edge;
import nl.ou.dpd.domain.EdgeType;
import nl.ou.dpd.domain.SystemUnderConsideration;

import java.util.ArrayList;

/**
 * A utility class for unittest support.
 *
 * @author Martin de Boer
 */
public class TestHelper {

    /**
     * Creates a "template" containing 17 GoF design patterns represented by an {@link ArrayList} of
     * {@link DesignPattern} instances.
     *
     * @return an {@link ArrayList} containing 17 GoF design patterns.
     */
    public static ArrayList<DesignPattern> createDesignPatternsTemplates() {
        final ArrayList<DesignPattern> dps = new ArrayList<>();
        dps.add(createBridgePattern());
        dps.add(createStateStrategyPattern());
        dps.add(createMediatorPattern());
        dps.add(createMementoPattern());
        dps.add(createObserverPattern());
        dps.add(createProxyPattern());
        dps.add(createCommandPattern());
        dps.add(createCompositePattern());
        dps.add(createDecoratorPattern());
        dps.add(createFactoryMethodPattern());
        dps.add(createInterpreterPattern());
        dps.add(createChainOfResponsibilityPattern());
        dps.add(createAbstractFactoryPattern());
        dps.add(createAdapterPattern());
        dps.add(createBuilderPattern());
        dps.add(createFlyweightPattern());
        dps.add(createIteratorPattern());
        return dps;
    }

    /**
     * Creates an AbstractFactory design pattern.
     *
     * @return a {@link DesignPattern} representing an AbstractFactory pattern.
     */
    public static DesignPattern createAbstractFactoryPattern() {
        final DesignPattern abstractFactory = new DesignPattern("Abstract Factory");
        abstractFactory.add(createEdge("Client", "AbstractFactory", EdgeType.ASSOCIATION_DIRECTED));
        abstractFactory.add(createEdge("Client", "AbstractProductA", EdgeType.ASSOCIATION_DIRECTED));
        abstractFactory.add(createEdge("Client", "AbstractProductB", EdgeType.ASSOCIATION_DIRECTED));
        abstractFactory.add(createEdge("ConcreteFact1", "AbstractFactory", EdgeType.INHERITANCE));
        abstractFactory.add(createEdge("ConcreteFact2", "AbstractFactory", EdgeType.INHERITANCE));
        abstractFactory.add(createEdge("ConcreteFact1", "ProductA1", EdgeType.DEPENDENCY));
        abstractFactory.add(createEdge("ConcreteFact2", "ProductA2", EdgeType.DEPENDENCY));
        abstractFactory.add(createEdge("ConcreteFact1", "ProductB1", EdgeType.DEPENDENCY));
        abstractFactory.add(createEdge("ConcreteFact2", "ProductB2", EdgeType.DEPENDENCY));
        abstractFactory.add(createEdge("ProductA1", "AbstractProductA", EdgeType.INHERITANCE));
        abstractFactory.add(createEdge("ProductA2", "AbstractProductA", EdgeType.INHERITANCE));
        abstractFactory.add(createEdge("ProductB1", "AbstractProductB", EdgeType.INHERITANCE));
        abstractFactory.add(createEdge("ProductB2", "AbstractProductB", EdgeType.INHERITANCE));
        return abstractFactory;
    }

    /**
     * Creates an Adapter design pattern.
     *
     * @return a {@link DesignPattern} representing an Adapter pattern.
     */
    public static DesignPattern createAdapterPattern() {
        final DesignPattern adapter = new DesignPattern("Adapter");
        adapter.add(createEdge("Client", "Target", EdgeType.ASSOCIATION_DIRECTED));
        adapter.add(createEdge("Adapter", "Target", EdgeType.INHERITANCE));
        adapter.add(createEdge("Adapter", "Adaptee", EdgeType.ASSOCIATION_DIRECTED));
        return adapter;
    }

    /**
     * Creates a Builder design pattern.
     *
     * @return a {@link DesignPattern} representing a Builder pattern.
     */
    public static DesignPattern createBuilderPattern() {
        final DesignPattern builder = new DesignPattern("Builder");
        builder.add(createEdge("Builder", "Director", EdgeType.AGGREGATE));
        builder.add(createEdge("ConcreteBuilder", "Builder", EdgeType.INHERITANCE));
        builder.add(createEdge("ConcreteBuilder", "Product", EdgeType.DEPENDENCY));
        return builder;
    }

    /**
     * Creates a ChainOfResponsibility design pattern.
     *
     * @return a {@link DesignPattern} representing a ChainOfResponsibility pattern.
     */
    public static DesignPattern createChainOfResponsibilityPattern() {
        final DesignPattern chainOfResponsibility = new DesignPattern("ChainOfResponsibility");
        chainOfResponsibility.add(createEdge("ConcreteHandler", "Handler", EdgeType.INHERITANCE_MULTI));
        chainOfResponsibility.add(createEdge("Handler", "Handler", EdgeType.ASSOCIATION_DIRECTED));
        chainOfResponsibility.add(createEdge("Client", "Handler", EdgeType.ASSOCIATION_DIRECTED));
        return chainOfResponsibility;
    }

    /**
     * Creates a Command design pattern.
     *
     * @return a {@link DesignPattern} representing a Command pattern.
     */
    public static DesignPattern createCommandPattern() {
        final DesignPattern command = new DesignPattern("Command");
        command.add(createEdge("Client", "Receiver", EdgeType.ASSOCIATION_DIRECTED));
        command.add(createEdge("Client", "ConcreteCommand", EdgeType.DEPENDENCY));
        command.add(createEdge("ConcreteCommand", "Receiver", EdgeType.ASSOCIATION_DIRECTED));
        command.add(createEdge("ConcreteCommand", "Command", EdgeType.INHERITANCE));
        command.add(createEdge("Command", "Invoker", EdgeType.AGGREGATE));
        return command;
    }

    /**
     * Creates a Composite design pattern.
     *
     * @return a {@link DesignPattern} representing a Composite pattern.
     */
    public static DesignPattern createCompositePattern() {
        final DesignPattern composite = new DesignPattern("Composite");
        composite.add(createEdge("Client", "Component", EdgeType.ASSOCIATION_DIRECTED));
        composite.add(createEdge("Leaf", "Component", EdgeType.INHERITANCE));
        composite.add(createEdge("Composite", "Component", EdgeType.INHERITANCE));
        composite.add(createEdge("Component", "Composite", EdgeType.AGGREGATE));
        return composite;
    }

    /**
     * Creates a Decorator design pattern.
     *
     * @return a {@link DesignPattern} representing a Decorator pattern.
     */
    public static DesignPattern createDecoratorPattern() {
        final DesignPattern decorator = new DesignPattern("Decorator");
        decorator.add(createEdge("ConcreteComponent", "Component", EdgeType.INHERITANCE));
        decorator.add(createEdge("Decorator", "Component", EdgeType.INHERITANCE));
        decorator.add(createEdge("Component", "Decorator", EdgeType.AGGREGATE));
        decorator.add(createEdge("ConcreteDecorator", "Decorator", EdgeType.INHERITANCE_MULTI));
        return decorator;
    }

    /**
     * Creates a FactoryMethod design pattern.
     *
     * @return a {@link DesignPattern} representing a FactoryMethod pattern.
     */
    public static DesignPattern createFactoryMethodPattern() {
        final DesignPattern factoryMethod = new DesignPattern("Factory Method");
        factoryMethod.add(createEdge("ConcreteProduct", "Product", EdgeType.INHERITANCE));
        factoryMethod.add(createEdge("ConcreteCreator", "ConcreteProduct", EdgeType.DEPENDENCY));
        factoryMethod.add(createEdge("ConcreteCreator", "Creator", EdgeType.INHERITANCE));
        return factoryMethod;
    }

    /**
     * Creates a flyweight pattern. Checked with the UML diagram in [GoF].
     *
     * @return a {@link DesignPattern} representing a flyweight pattern.
     */
    public static DesignPattern createFlyweightPattern() {
        final DesignPattern flyweight = new DesignPattern("Flyweight");
        flyweight.add(createEdge("Client", "FlyweightFactory", EdgeType.ASSOCIATION_DIRECTED));
        flyweight.add(createEdge("Client", "ConcreteFlyweight", EdgeType.ASSOCIATION_DIRECTED));
        flyweight.add(createEdge("Client", "UnsharedConcreteFlyweight", EdgeType.ASSOCIATION_DIRECTED));
        flyweight.add(createEdge("Flyweight", "FlyweightFactory", EdgeType.AGGREGATE));
        flyweight.add(createEdge("ConcreteFlyweight", "Flyweight", EdgeType.INHERITANCE));
        flyweight.add(createEdge("UnsharedConcreteFlyweight", "Flyweight", EdgeType.INHERITANCE));
        return flyweight;
    }

    /**
     * Creates an Iterator design pattern.
     *
     * @return a {@link DesignPattern} representing an Iterator pattern.
     */

    public static DesignPattern createIteratorPattern() {
        final DesignPattern iterator = new DesignPattern("Iterator");
        iterator.add(createEdge("ConcreteAggregate", "Aggregate", EdgeType.INHERITANCE));
        iterator.add(createEdge("Client", "Aggregate", EdgeType.ASSOCIATION_DIRECTED));
        iterator.add(createEdge("Client", "Iterator", EdgeType.ASSOCIATION_DIRECTED));
        iterator.add(createEdge("ConcreteIterator", "Iterator", EdgeType.INHERITANCE));
        iterator.add(createEdge("ConcreteIterator", "ConcreteAggregate", EdgeType.ASSOCIATION_DIRECTED));
        iterator.add(createEdge("ConcreteAggregate", "ConcreteIterator", EdgeType.DEPENDENCY));
        return iterator;
    }

    /**
     * Creates an Interpreter design pattern.
     *
     * @return a {@link DesignPattern} representing an Interpreter pattern.
     */
    public static DesignPattern createInterpreterPattern() {
        final DesignPattern interpreter = new DesignPattern("Interpreter");
        interpreter.add(createEdge("Client", "AbstractExpression", EdgeType.ASSOCIATION_DIRECTED));
        interpreter.add(createEdge("Client", "Context", EdgeType.ASSOCIATION_DIRECTED));
        interpreter.add(createEdge("TerminalExpression", "AbstractExpression", EdgeType.INHERITANCE));
        interpreter.add(createEdge("NonTerminalExpression", "AbstractExpression", EdgeType.INHERITANCE));
        interpreter.add(createEdge("AbstractExpression", "NonTerminalExpression", EdgeType.AGGREGATE));
        return interpreter;
    }

    /**
     * Creates a Mediator design pattern.
     *
     * @return a {@link DesignPattern} representing a Mediator pattern.
     */

    public static DesignPattern createMediatorPattern() {
        final DesignPattern mediator = new DesignPattern("Mediator");
        mediator.add(createEdge("ConcreteMediator", "Mediator", EdgeType.INHERITANCE));
        mediator.add(createEdge("Colleague", "Mediator", EdgeType.ASSOCIATION_DIRECTED));
        mediator.add(createEdge("ConcreteColleague1", "Colleague", EdgeType.INHERITANCE));
        mediator.add(createEdge("ConcreteColleague2", "Colleague", EdgeType.INHERITANCE));
        mediator.add(createEdge("ConcreteMediator", "ConcreteColleague1", EdgeType.ASSOCIATION_DIRECTED));
        mediator.add(createEdge("ConcreteMediator", "ConcreteColleague2", EdgeType.ASSOCIATION_DIRECTED));
        return mediator;
    }

    /**
     * Creates a Memento design pattern.
     *
     * @return a {@link DesignPattern} representing a Memento pattern.
     */

    public static DesignPattern createMementoPattern() {
        final DesignPattern memento = new DesignPattern("Memento");
        memento.add(createEdge("Memento", "Caretaker", EdgeType.AGGREGATE));
        memento.add(createEdge("Originator", "Memento", EdgeType.DEPENDENCY));
        return memento;
    }

    /**
     * Creates an Observer design pattern.
     *
     * @return a {@link DesignPattern} representing an Observer pattern.
     */
    public static DesignPattern createObserverPattern() {
        final DesignPattern observer = new DesignPattern("Observer");
        observer.add(createEdge("ConcreteSubject", "Subject", EdgeType.INHERITANCE));
        observer.add(createEdge("Subject", "Observer", EdgeType.ASSOCIATION_DIRECTED));
        observer.add(createEdge("ConcreteObserver", "Observer", EdgeType.INHERITANCE));
        observer.add(createEdge("ConcreteObserver", "ConcreteSubject", EdgeType.ASSOCIATION_DIRECTED));
        return observer;
    }

    /**
     * Creates a Proxy design pattern.
     *
     * @return a {@link DesignPattern} representing a Proxy pattern.
     */
    public static DesignPattern createProxyPattern() {
        final DesignPattern proxy = new DesignPattern("Proxy");
        proxy.add(createEdge("Client", "Subject", EdgeType.ASSOCIATION_DIRECTED));
        proxy.add(createEdge("Proxy", "Subject", EdgeType.INHERITANCE));
        proxy.add(createEdge("RealSubject", "Subject", EdgeType.INHERITANCE));
        proxy.add(createEdge("Proxy", "RealSubject", EdgeType.ASSOCIATION_DIRECTED));
        return proxy;
    }

    /**
     * Creates a State/Strategy design pattern.
     *
     * @return a {@link DesignPattern} representing a State/Strategy pattern.
     */
    public static DesignPattern createStateStrategyPattern() {
        final DesignPattern strategy = new DesignPattern("State - Strategy");
        strategy.add(createEdge("Strategy", "Context", EdgeType.AGGREGATE));
        strategy.add(createEdge("ConcreteStrategy", "Strategy", EdgeType.INHERITANCE_MULTI));
        return strategy;
    }

    /**
     * Creates a bridge pattern. Checked with the UML diagram in [GoF].
     *
     * @return a {@link DesignPattern} representing a bridge pattern.
     */
    public static DesignPattern createBridgePattern() {
        final DesignPattern bridge = new DesignPattern("Bridge");
        bridge.add(createEdge("Client", "Abstraction", EdgeType.ASSOCIATION_DIRECTED));
        bridge.add(createEdge("RefinedAbstraction", "Abstraction", EdgeType.INHERITANCE_MULTI));
        bridge.add(createEdge("Implementor", "Abstraction", EdgeType.AGGREGATE));
        bridge.add(createEdge("ConcreteImplementor", "Implementor", EdgeType.INHERITANCE_MULTI));
        return bridge;
    }

    /**
     * Creates a prototype pattern.
     *
     * @return a {@link DesignPattern} representing a protoype pattern.
     */
    public static DesignPattern createPrototypePattern() {
        final DesignPattern prototype = new DesignPattern("Prototype");
        prototype.add(createEdge("Client", "Prototype", EdgeType.ASSOCIATION_DIRECTED));
        prototype.add(createEdge("ConcretePrototype", "Prototype", EdgeType.INHERITANCE_MULTI));
        return prototype;
    }

    /**
     * Creates a "system under consideration" containing quite a number of patterns that the application should be able
     * to detect. This system is depicted Figure 4.5 in E.M. van Doorns article on page 29 (4.3 - Experiments).
     *
     * @return a {@link DesignPattern} representing the design of a "system under consideration".
     */
    public static SystemUnderConsideration createComplexSystemUnderConsideration() {
        final SystemUnderConsideration system = new SystemUnderConsideration();

        // Bridge
        system.add(createEdge("Client", "Ab", EdgeType.ASSOCIATION));
        system.add(createEdge("ConcAb1", "Ab", EdgeType.INHERITANCE));
        system.add(createEdge("ConcAb2", "Ab", EdgeType.INHERITANCE));
        system.add(createEdge("ConcAb2", "F_Factory", EdgeType.ASSOCIATION));
        system.add(createEdge("Impl", "Ab", EdgeType.AGGREGATE));
        system.add(createEdge("F_Factory", "Impl", EdgeType.INHERITANCE));
        system.add(createEdge("P_Subject", "Impl", EdgeType.INHERITANCE));
        system.add(createEdge("ConcImpl3", "Impl", EdgeType.INHERITANCE));

        // Factory Method
        system.add(createEdge("F_ConcreteFactory", "F_Factory", EdgeType.INHERITANCE));
        system.add(createEdge("F_ConcreteFactory", "F_Product", EdgeType.ASSOCIATION));
        system.add(createEdge("F_Product", "F_ProdInterface", EdgeType.INHERITANCE));

        // Proxy
        system.add(createEdge("P_Proxy", "P_Subject", EdgeType.INHERITANCE));
        system.add(createEdge("P_RealSubject", "P_Subject", EdgeType.INHERITANCE));
        system.add(createEdge("P_Proxy", "P_RealSubject", EdgeType.ASSOCIATION));

        // Decorator
        system.add(createEdge("DecInterface", "ConcImpl3", EdgeType.ASSOCIATION));
        system.add(createEdge("DecBasis", "DecInterface", EdgeType.INHERITANCE));
        system.add(createEdge("DecInterface", "DecWrapper", EdgeType.COMPOSITE));
        system.add(createEdge("DecOption1", "DecWrapper", EdgeType.INHERITANCE));
        system.add(createEdge("DecOption2", "DecWrapper", EdgeType.INHERITANCE));
        system.add(createEdge("DecWrapper", "DecInterface", EdgeType.INHERITANCE));

        // Memento
        system.add(createEdge("Client", "Maintainer", EdgeType.DEPENDENCY));
        system.add(createEdge("Status", "Maintainer", EdgeType.AGGREGATE));

        // Adapter
        system.add(createEdge("Client", "T_Interface", EdgeType.ASSOCIATION));
        system.add(createEdge("Aanpasser", "T_Interface", EdgeType.INHERITANCE));
        system.add(createEdge("Aanpasser", "NietPassend", EdgeType.ASSOCIATION));

        // Composite
        system.add(createEdge("Leaflet", "T_Interface", EdgeType.INHERITANCE));
        system.add(createEdge("Union", "T_Interface", EdgeType.INHERITANCE));
        system.add(createEdge("T_Interface", "Union", EdgeType.AGGREGATE));

        // Mediator
        system.add(createEdge("DecInterface", "Med", EdgeType.ASSOCIATION));
        system.add(createEdge("ConcMed", "Med", EdgeType.INHERITANCE));
        system.add(createEdge("ConcMed", "DecBasis", EdgeType.ASSOCIATION));
        system.add(createEdge("ConcMed", "DecWrapper", EdgeType.ASSOCIATION));

        // Command
        system.add(createEdge("Maintainer", "Opdracht", EdgeType.INHERITANCE));
        system.add(createEdge("Client", "Ontvanger", EdgeType.ASSOCIATION));
        system.add(createEdge("Opdracht", "Aanroeper", EdgeType.AGGREGATE));
        system.add(createEdge("Maintainer", "Ontvanger", EdgeType.ASSOCIATION));

        // Iterator
        system.add(createEdge("User", "DecInterface", EdgeType.ASSOCIATION));
        system.add(createEdge("User", "Med", EdgeType.ASSOCIATION));

        // ChainOfResponsibility
        system.add(createEdge("Aanroeper", "Behandelaar", EdgeType.ASSOCIATION));
        system.add(createEdge("ConcreteBehandelaar_1", "Behandelaar", EdgeType.INHERITANCE));
        system.add(createEdge("Behandelaar", "Behandelaar", EdgeType.ASSOCIATION));
        system.add(createEdge("ConcreteBehandelaar_2", "Behandelaar", EdgeType.INHERITANCE));

        // AbstractFactory
        system.add(createEdge("DecOption1", "AbstrFact", EdgeType.ASSOCIATION));
        system.add(createEdge("ConcrFact1", "AbstrFact", EdgeType.INHERITANCE));
        system.add(createEdge("ConcrFact2", "AbstrFact", EdgeType.INHERITANCE));
        system.add(createEdge("DecOption1", "AbstrProA", EdgeType.ASSOCIATION));
        system.add(createEdge("DecOption1", "AbstrProB", EdgeType.ASSOCIATION));
        system.add(createEdge("Pro1A", "AbstrProA", EdgeType.INHERITANCE));
        system.add(createEdge("Pro2A", "AbstrProA", EdgeType.INHERITANCE));
        system.add(createEdge("Pro1B", "AbstrProB", EdgeType.INHERITANCE));
        system.add(createEdge("Pro2B", "AbstrProB", EdgeType.INHERITANCE));
        system.add(createEdge("ConcrFact1", "Pro1A", EdgeType.ASSOCIATION));
        system.add(createEdge("ConcrFact2", "Pro2A", EdgeType.ASSOCIATION));
        system.add(createEdge("ConcrFact1", "Pro1B", EdgeType.ASSOCIATION));
        system.add(createEdge("ConcrFact2", "Pro2B", EdgeType.ASSOCIATION));

        // Flyweight
        system.add(createEdge("FW_Cl", "FlywFact", EdgeType.ASSOCIATION));
        system.add(createEdge("FW_Cl", "ConcFlyw", EdgeType.ASSOCIATION));
        system.add(createEdge("FW_Cl", "UnshConcFlyw", EdgeType.ASSOCIATION));
        system.add(createEdge("Flyw", "NietPassend", EdgeType.AGGREGATE));
        system.add(createEdge("ConcFlyw", "Flyw", EdgeType.INHERITANCE));
        system.add(createEdge("UnshConcFlyw", "Flyw", EdgeType.INHERITANCE));

        return system;
    }

    public static Edge createEdge(String classname1, String classname2, EdgeType edgeType) {
        final Clazz cl1 = new Clazz(classname1);
        final Clazz cl2 = new Clazz(classname2);
        return new Edge(cl1, cl2, edgeType);

    }
}
