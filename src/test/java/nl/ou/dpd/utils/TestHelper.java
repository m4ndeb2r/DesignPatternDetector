package nl.ou.dpd.utils;

import nl.ou.dpd.domain.*;

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
        dps.add(createIteratorPattern());
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
        abstractFactory.add(new DesignPatternEdge("Client", "AbstractFactory", EdgeType.ASSOCIATION_DIRECTED));
        abstractFactory.add(new DesignPatternEdge("Client", "AbstractProductA", EdgeType.ASSOCIATION_DIRECTED));
        abstractFactory.add(new DesignPatternEdge("Client", "AbstractProductB", EdgeType.ASSOCIATION_DIRECTED));
        abstractFactory.add(new DesignPatternEdge("ConcreteFact1", "AbstractFactory", EdgeType.INHERITANCE));
        abstractFactory.add(new DesignPatternEdge("ConcreteFact2", "AbstractFactory", EdgeType.INHERITANCE));
        abstractFactory.add(new DesignPatternEdge("ConcreteFact1", "ProductA1", EdgeType.DEPENDENCY));
        abstractFactory.add(new DesignPatternEdge("ConcreteFact2", "ProductA2", EdgeType.DEPENDENCY));
        abstractFactory.add(new DesignPatternEdge("ConcreteFact1", "ProductB1", EdgeType.DEPENDENCY));
        abstractFactory.add(new DesignPatternEdge("ConcreteFact2", "ProductB2", EdgeType.DEPENDENCY));
        abstractFactory.add(new DesignPatternEdge("ProductA1", "AbstractProductA", EdgeType.INHERITANCE));
        abstractFactory.add(new DesignPatternEdge("ProductA2", "AbstractProductA", EdgeType.INHERITANCE));
        abstractFactory.add(new DesignPatternEdge("ProductB1", "AbstractProductB", EdgeType.INHERITANCE));
        abstractFactory.add(new DesignPatternEdge("ProductB2", "AbstractProductB", EdgeType.INHERITANCE));
        return abstractFactory;
    }

    /**
     * Creates an Adapter design pattern.
     *
     * @return a {@link DesignPattern} representing an Adapter pattern.
     */
    public static DesignPattern createAdapterPattern() {
        final DesignPattern adapter = new DesignPattern("Adapter");
        adapter.add(new DesignPatternEdge("Client", "Target", EdgeType.ASSOCIATION_DIRECTED));
        adapter.add(new DesignPatternEdge("Adapter", "Target", EdgeType.INHERITANCE));
        adapter.add(new DesignPatternEdge("Adapter", "Adaptee", EdgeType.ASSOCIATION_DIRECTED));
        return adapter;
    }

    /**
     * Creates a Builder design pattern.
     *
     * @return a {@link DesignPattern} representing a Builder pattern.
     */
    public static DesignPattern createBuilderPattern() {
        final DesignPattern builder = new DesignPattern("Builder");
        builder.add(new DesignPatternEdge("Builder", "Director", EdgeType.AGGREGATE));
        builder.add(new DesignPatternEdge("ConcreteBuilder", "Builder", EdgeType.INHERITANCE));
        builder.add(new DesignPatternEdge("ConcreteBuilder", "Product", EdgeType.DEPENDENCY));
        return builder;
    }

    /**
     * Creates a ChainOfResponsibility design pattern.
     *
     * @return a {@link DesignPattern} representing a ChainOfResponsibility pattern.
     */
    public static DesignPattern createChainOfResponsibilityPattern() {
        final DesignPattern chainOfResponsibility = new DesignPattern("ChainOfResponsibility");
        chainOfResponsibility.add(new DesignPatternEdge("ConcreteHandler", "Handler", EdgeType.INHERITANCE_MULTI));
        chainOfResponsibility.add(new DesignPatternEdge("Handler", "Handler", EdgeType.ASSOCIATION_DIRECTED));
        chainOfResponsibility.add(new DesignPatternEdge("Client", "Handler", EdgeType.ASSOCIATION_DIRECTED));
        return chainOfResponsibility;
    }

    /**
     * Creates a Command design pattern.
     *
     * @return a {@link DesignPattern} representing a Command pattern.
     */
    public static DesignPattern createCommandPattern() {
        final DesignPattern command = new DesignPattern("Command");
        command.add(new DesignPatternEdge("Client", "Receiver", EdgeType.ASSOCIATION_DIRECTED));
        command.add(new DesignPatternEdge("Client", "ConcreteCommand", EdgeType.DEPENDENCY));
        command.add(new DesignPatternEdge("ConcreteCommand", "Receiver", EdgeType.ASSOCIATION_DIRECTED));
        command.add(new DesignPatternEdge("ConcreteCommand", "Command", EdgeType.INHERITANCE));
        command.add(new DesignPatternEdge("Command", "Invoker", EdgeType.AGGREGATE));
        return command;
    }

    /**
     * Creates a Composite design pattern.
     *
     * @return a {@link DesignPattern} representing a Composite pattern.
     */
    public static DesignPattern createCompositePattern() {
        final DesignPattern composite = new DesignPattern("Composite");
        composite.add(new DesignPatternEdge("Client", "Component", EdgeType.ASSOCIATION_DIRECTED));
        composite.add(new DesignPatternEdge("Leaf", "Component", EdgeType.INHERITANCE));
        composite.add(new DesignPatternEdge("Composite", "Component", EdgeType.INHERITANCE));
        composite.add(new DesignPatternEdge("Component", "Composite", EdgeType.AGGREGATE));
        return composite;
    }

    /**
     * Creates a Decorator design pattern.
     *
     * @return a {@link DesignPattern} representing a Decorator pattern.
     */
    public static DesignPattern createDecoratorPattern() {
        final DesignPattern decorator = new DesignPattern("Decorator");
        decorator.add(new DesignPatternEdge("ConcreteComponent", "Component", EdgeType.INHERITANCE));
        decorator.add(new DesignPatternEdge("Decorator", "Component", EdgeType.INHERITANCE));
        decorator.add(new DesignPatternEdge("Component", "Decorator", EdgeType.AGGREGATE));
        decorator.add(new DesignPatternEdge("ConcreteDecorator", "Decorator", EdgeType.INHERITANCE_MULTI));
        return decorator;
    }

    /**
     * Creates a FactoryMethod design pattern.
     *
     * @return a {@link DesignPattern} representing a FactoryMethod pattern.
     */
    public static DesignPattern createFactoryMethodPattern() {
        final DesignPattern factoryMethod = new DesignPattern("Factory Method");
        factoryMethod.add(new DesignPatternEdge("Product", "ConcreteProduct", EdgeType.INHERITANCE));
        factoryMethod.add(new DesignPatternEdge("ConcreteCreator", "Product", EdgeType.DEPENDENCY));
        factoryMethod.add(new DesignPatternEdge("ConcreteCreator", "Creator", EdgeType.INHERITANCE));
        return factoryMethod;
    }

    /**
     * Creates a flyweight pattern. Checked with the UML diagram in [GoF].
     *
     * @return a {@link DesignPattern} representing a flyweight pattern.
     */
    public static DesignPattern createFlyweightPattern() {
        final DesignPattern flyweight = new DesignPattern("Flyweight");
        flyweight.add(new DesignPatternEdge("Client", "FlyweightFactory", EdgeType.ASSOCIATION_DIRECTED));
        flyweight.add(new DesignPatternEdge("Client", "ConcreteFlyweight", EdgeType.ASSOCIATION_DIRECTED));
        flyweight.add(new DesignPatternEdge("Client", "UnsharedConcreteFlyweight", EdgeType.ASSOCIATION_DIRECTED));
        flyweight.add(new DesignPatternEdge("Flyweight", "FlyweightFactory", EdgeType.AGGREGATE));
        flyweight.add(new DesignPatternEdge("ConcreteFlyweight", "Flyweight", EdgeType.INHERITANCE));
        flyweight.add(new DesignPatternEdge("UnsharedConcreteFlyweight", "Flyweight", EdgeType.INHERITANCE));
        return flyweight;
    }

    /**
     * Creates an Iterator design pattern.
     *
     * @return a {@link DesignPattern} representing an Iterator pattern.
     */

    public static DesignPattern createIteratorPattern() {
        final DesignPattern iterator = new DesignPattern("Iterator");
        iterator.add(new DesignPatternEdge("ConcreteAggregate", "Aggregate", EdgeType.INHERITANCE));
        iterator.add(new DesignPatternEdge("Client", "Aggregate", EdgeType.ASSOCIATION_DIRECTED));
        iterator.add(new DesignPatternEdge("Client", "Iterator", EdgeType.ASSOCIATION_DIRECTED));
        iterator.add(new DesignPatternEdge("ConcreteIterator", "Iterator", EdgeType.INHERITANCE));
        iterator.add(new DesignPatternEdge("ConcreteIterator", "ConcreteAggregate", EdgeType.ASSOCIATION_DIRECTED));
        iterator.add(new DesignPatternEdge("ConcreteAggregate", "ConcreteIterator", EdgeType.DEPENDENCY));
        return iterator;
    }

    /**
     * Creates a Mediator design pattern.
     *
     * @return a {@link DesignPattern} representing a Mediator pattern.
     */

    public static DesignPattern createMediatorPattern() {
        final DesignPattern mediator = new DesignPattern("Mediator");
        mediator.add(new DesignPatternEdge("ConcreteMediator", "Mediator", EdgeType.INHERITANCE));
        mediator.add(new DesignPatternEdge("Colleague", "Mediator", EdgeType.ASSOCIATION_DIRECTED));
        mediator.add(new DesignPatternEdge("ConcreteColleague1", "Colleague", EdgeType.INHERITANCE));
        mediator.add(new DesignPatternEdge("ConcreteColleague2", "Colleague", EdgeType.INHERITANCE));
        mediator.add(new DesignPatternEdge("ConcreteMediator", "ConcreteColleague1", EdgeType.ASSOCIATION_DIRECTED));
        mediator.add(new DesignPatternEdge("ConcreteMediator", "ConcreteColleague2", EdgeType.ASSOCIATION_DIRECTED));
        return mediator;
    }

    /**
     * Creates a Memento design pattern.
     *
     * @return a {@link DesignPattern} representing a Memento pattern.
     */

    public static DesignPattern createMementoPattern() {
        final DesignPattern memento = new DesignPattern("Memento");
        memento.add(new DesignPatternEdge("Memento", "Caretaker", EdgeType.AGGREGATE));
        memento.add(new DesignPatternEdge("Originator", "Memento", EdgeType.DEPENDENCY));
        return memento;
    }

    /**
     * Creates an Observer design pattern.
     *
     * @return a {@link DesignPattern} representing an Observer pattern.
     */
    public static DesignPattern createObserverPattern() {
        final DesignPattern observer = new DesignPattern("Observer");
        observer.add(new DesignPatternEdge("ConcreteSubject", "Subject", EdgeType.INHERITANCE));
        observer.add(new DesignPatternEdge("Subject", "Observer", EdgeType.AGGREGATE));
        observer.add(new DesignPatternEdge("ConcreteObserver", "Observer", EdgeType.INHERITANCE));
        observer.add(new DesignPatternEdge("ConcreteObserver", "ConcreteSubject", EdgeType.ASSOCIATION_DIRECTED));
        return observer;
    }

    /**
     * Creates a Proxy design pattern.
     *
     * @return a {@link DesignPattern} representing a Proxy pattern.
     */
    public static DesignPattern createProxyPattern() {
        final DesignPattern proxy = new DesignPattern("Proxy");
        proxy.add(new DesignPatternEdge("Client", "Subject", EdgeType.ASSOCIATION_DIRECTED));
        proxy.add(new DesignPatternEdge("Proxy", "Subject", EdgeType.INHERITANCE));
        proxy.add(new DesignPatternEdge("RealSubject", "Subject", EdgeType.INHERITANCE));
        proxy.add(new DesignPatternEdge("Proxy", "RealSubject", EdgeType.ASSOCIATION_DIRECTED));
        return proxy;
    }

    /**
     * Creates a State/Strategy design pattern.
     *
     * @return a {@link DesignPattern} representing a State/Strategy pattern.
     */
    public static DesignPattern createStateStrategyPattern() {
        final DesignPattern strategy = new DesignPattern("State - Strategy");
        strategy.add(new DesignPatternEdge("Strategy", "Context", EdgeType.AGGREGATE));
        strategy.add(new DesignPatternEdge("ConcreteStrategy", "Strategy", EdgeType.INHERITANCE_MULTI));
        return strategy;
    }

    /**
     * Creates a bridge pattern. Checked with the UML diagram in [GoF].
     *
     * @return a {@link DesignPattern} representing a bridge pattern.
     */
    public static DesignPattern createBridgePattern() {
        final DesignPattern bridge = new DesignPattern("Bridge");
        bridge.add(new DesignPatternEdge("Client", "Abstraction", EdgeType.ASSOCIATION));
        bridge.add(new DesignPatternEdge("Implementor", "Abstraction", EdgeType.AGGREGATE));
        bridge.add(new DesignPatternEdge("ConcreteImplementorA", "Implementor", EdgeType.INHERITANCE));
        bridge.add(new DesignPatternEdge("ConcreteImplementorB", "Implementor", EdgeType.INHERITANCE));
        bridge.add(new DesignPatternEdge("RefinedAbstraction", "Abstraction", EdgeType.INHERITANCE));
        return bridge;
    }

    /**
     * Creates a prototype pattern.
     *
     * @return a {@link DesignPattern} representing a protoype pattern.
     */
    public static DesignPattern createPrototypePattern() {
        final DesignPattern prototype = new DesignPattern("Prototype");
        prototype.add(new DesignPatternEdge("P", "Q", EdgeType.ASSOCIATION_DIRECTED));
        prototype.add(new DesignPatternEdge("R", "Q", EdgeType.INHERITANCE));
        return prototype;
    }

    /**
     * Creates a "system under consideration" containing quite a number of patterns that the application should be able
     * to detect.
     *
     * @return a {@link DesignPattern} representing the design of a "system under consideration".
     */
    public static SystemUnderConsideration createComplexSystemUnderConsideration() {
        final SystemUnderConsideration system = new SystemUnderConsideration();

        // Bridge
        system.add(new SystemUnderConsiderationEdge("Client", "Ab", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("ConcAb1", "Ab", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("ConcAb2", "Ab", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("ConcAb2", "F_Factory", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("Impl", "Ab", EdgeType.AGGREGATE));
        system.add(new SystemUnderConsiderationEdge("F_Factory", "Impl", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("P_Subject", "Impl", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("ConcImpl3", "Impl", EdgeType.INHERITANCE));

        // Factory Method
        system.add(new SystemUnderConsiderationEdge("F_ConcreteFactory", "F_Factory", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("F_ConcreteFactory", "F_Product", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("F_Product", "F_ProdInterface", EdgeType.INHERITANCE));

        // Proxy
        system.add(new SystemUnderConsiderationEdge("P_Proxy", "P_Subject", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("P_RealSubject", "P_Subject", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("P_Proxy", "P_RealSubject", EdgeType.ASSOCIATION));

        // Decorator
        system.add(new SystemUnderConsiderationEdge("DecInterface", "ConcImpl3", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("DecBasis", "DecInterface", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("DecInterface", "DecWrapper", EdgeType.COMPOSITE));
        system.add(new SystemUnderConsiderationEdge("DecOption1", "DecWrapper", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("DecOption2", "DecWrapper", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("DecWrapper", "DecInterface", EdgeType.INHERITANCE));

        // Memento
        system.add(new SystemUnderConsiderationEdge("Client", "Maintainer", EdgeType.DEPENDENCY));
        system.add(new SystemUnderConsiderationEdge("Status", "Maintainer", EdgeType.AGGREGATE));

        // Adapter
        system.add(new SystemUnderConsiderationEdge("Client", "T_Interface", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("Aanpasser", "T_Interface", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("Aanpasser", "NietPassend", EdgeType.ASSOCIATION));

        // Composite
        system.add(new SystemUnderConsiderationEdge("Leaflet", "T_Interface", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("Union", "T_Interface", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("T_Interface", "Union", EdgeType.AGGREGATE));

        // Mediator
        system.add(new SystemUnderConsiderationEdge("DecInterface", "Med", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("ConcMed", "Med", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("ConcMed", "DecBasis", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("ConcMed", "DecWrapper", EdgeType.ASSOCIATION));

        // Command
        system.add(new SystemUnderConsiderationEdge("Maintainer", "Opdracht", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("Client", "Ontvanger", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("Opdracht", "Aanroeper", EdgeType.AGGREGATE));
        system.add(new SystemUnderConsiderationEdge("Maintainer", "Ontvanger", EdgeType.ASSOCIATION));

        // Iterator
        system.add(new SystemUnderConsiderationEdge("User", "DecInterface", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("User", "Med", EdgeType.ASSOCIATION));

        // ChainOfResponsibility
        system.add(new SystemUnderConsiderationEdge("Aanroeper", "Behandelaar", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("ConcreteBehandelaar_1", "Behandelaar", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("Behandelaar", "Behandelaar", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("ConcreteBehandelaar_2", "Behandelaar", EdgeType.INHERITANCE));

        // AbstractFactory
        system.add(new SystemUnderConsiderationEdge("DecOption1", "AbstrFact", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("ConcrFact1", "AbstrFact", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("ConcrFact2", "AbstrFact", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("DecOption1", "AbstrProA", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("DecOption1", "AbstrProB", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("Pro1A", "AbstrProA", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("Pro2A", "AbstrProA", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("Pro1B", "AbstrProB", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("Pro2B", "AbstrProB", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("ConcrFact1", "Pro1A", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("ConcrFact2", "Pro2A", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("ConcrFact1", "Pro1B", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("ConcrFact2", "Pro2B", EdgeType.ASSOCIATION));

        // Flyweight
        system.add(new SystemUnderConsiderationEdge("FW_Cl", "FlywFact", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("FW_Cl", "ConcFlyw", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("FW_Cl", "UnshConcFlyw", EdgeType.ASSOCIATION));
        system.add(new SystemUnderConsiderationEdge("Flyw", "NietPassend", EdgeType.AGGREGATE));
        system.add(new SystemUnderConsiderationEdge("ConcFlyw", "Flyw", EdgeType.INHERITANCE));
        system.add(new SystemUnderConsiderationEdge("UnshConcFlyw", "Flyw", EdgeType.INHERITANCE));

        return system;
    }

}
