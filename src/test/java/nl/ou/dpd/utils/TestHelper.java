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
        abstractFactory.add(createDesignPatternEdge("Client", "AbstractFactory", EdgeType.ASSOCIATION_DIRECTED));
        abstractFactory.add(createDesignPatternEdge("Client", "AbstractProductA", EdgeType.ASSOCIATION_DIRECTED));
        abstractFactory.add(createDesignPatternEdge("Client", "AbstractProductB", EdgeType.ASSOCIATION_DIRECTED));
        abstractFactory.add(createDesignPatternEdge("ConcreteFact1", "AbstractFactory", EdgeType.INHERITANCE));
        abstractFactory.add(createDesignPatternEdge("ConcreteFact2", "AbstractFactory", EdgeType.INHERITANCE));
        abstractFactory.add(createDesignPatternEdge("ConcreteFact1", "ProductA1", EdgeType.DEPENDENCY));
        abstractFactory.add(createDesignPatternEdge("ConcreteFact2", "ProductA2", EdgeType.DEPENDENCY));
        abstractFactory.add(createDesignPatternEdge("ConcreteFact1", "ProductB1", EdgeType.DEPENDENCY));
        abstractFactory.add(createDesignPatternEdge("ConcreteFact2", "ProductB2", EdgeType.DEPENDENCY));
        abstractFactory.add(createDesignPatternEdge("ProductA1", "AbstractProductA", EdgeType.INHERITANCE));
        abstractFactory.add(createDesignPatternEdge("ProductA2", "AbstractProductA", EdgeType.INHERITANCE));
        abstractFactory.add(createDesignPatternEdge("ProductB1", "AbstractProductB", EdgeType.INHERITANCE));
        abstractFactory.add(createDesignPatternEdge("ProductB2", "AbstractProductB", EdgeType.INHERITANCE));
        return abstractFactory;
    }

    /**
     * Creates an Adapter design pattern.
     *
     * @return a {@link DesignPattern} representing an Adapter pattern.
     */
    public static DesignPattern createAdapterPattern() {
        final DesignPattern adapter = new DesignPattern("Adapter");
        adapter.add(createDesignPatternEdge("Client", "Target", EdgeType.ASSOCIATION_DIRECTED));
        adapter.add(createDesignPatternEdge("Adapter", "Target", EdgeType.INHERITANCE));
        adapter.add(createDesignPatternEdge("Adapter", "Adaptee", EdgeType.ASSOCIATION_DIRECTED));
        return adapter;
    }

    /**
     * Creates a Builder design pattern.
     *
     * @return a {@link DesignPattern} representing a Builder pattern.
     */
    public static DesignPattern createBuilderPattern() {
        final DesignPattern builder = new DesignPattern("Builder");
        builder.add(createDesignPatternEdge("Builder", "Director", EdgeType.AGGREGATE));
        builder.add(createDesignPatternEdge("ConcreteBuilder", "Builder", EdgeType.INHERITANCE));
        builder.add(createDesignPatternEdge("ConcreteBuilder", "Product", EdgeType.DEPENDENCY));
        return builder;
    }

    /**
     * Creates a ChainOfResponsibility design pattern.
     *
     * @return a {@link DesignPattern} representing a ChainOfResponsibility pattern.
     */
    public static DesignPattern createChainOfResponsibilityPattern() {
        final DesignPattern chainOfResponsibility = new DesignPattern("ChainOfResponsibility");
        chainOfResponsibility.add(createDesignPatternEdge("ConcreteHandler", "Handler", EdgeType.INHERITANCE_MULTI));
        chainOfResponsibility.add(createDesignPatternEdge("Handler", "Handler", EdgeType.ASSOCIATION_DIRECTED));
        chainOfResponsibility.add(createDesignPatternEdge("Client", "Handler", EdgeType.ASSOCIATION_DIRECTED));
        return chainOfResponsibility;
    }

    /**
     * Creates a Command design pattern.
     *
     * @return a {@link DesignPattern} representing a Command pattern.
     */
    public static DesignPattern createCommandPattern() {
        final DesignPattern command = new DesignPattern("Command");
        command.add(createDesignPatternEdge("Client", "Receiver", EdgeType.ASSOCIATION_DIRECTED));
        command.add(createDesignPatternEdge("Client", "ConcreteCommand", EdgeType.DEPENDENCY));
        command.add(createDesignPatternEdge("ConcreteCommand", "Receiver", EdgeType.ASSOCIATION_DIRECTED));
        command.add(createDesignPatternEdge("ConcreteCommand", "Command", EdgeType.INHERITANCE));
        command.add(createDesignPatternEdge("Command", "Invoker", EdgeType.AGGREGATE));
        return command;
    }

    /**
     * Creates a Composite design pattern.
     *
     * @return a {@link DesignPattern} representing a Composite pattern.
     */
    public static DesignPattern createCompositePattern() {
        final DesignPattern composite = new DesignPattern("Composite");
        composite.add(createDesignPatternEdge("Client", "Component", EdgeType.ASSOCIATION_DIRECTED));
        composite.add(createDesignPatternEdge("Leaf", "Component", EdgeType.INHERITANCE));
        composite.add(createDesignPatternEdge("Composite", "Component", EdgeType.INHERITANCE));
        composite.add(createDesignPatternEdge("Component", "Composite", EdgeType.AGGREGATE));
        return composite;
    }

    /**
     * Creates a Decorator design pattern.
     *
     * @return a {@link DesignPattern} representing a Decorator pattern.
     */
    public static DesignPattern createDecoratorPattern() {
        final DesignPattern decorator = new DesignPattern("Decorator");
        decorator.add(createDesignPatternEdge("ConcreteComponent", "Component", EdgeType.INHERITANCE));
        decorator.add(createDesignPatternEdge("Decorator", "Component", EdgeType.INHERITANCE));
        decorator.add(createDesignPatternEdge("Component", "Decorator", EdgeType.AGGREGATE));
        decorator.add(createDesignPatternEdge("ConcreteDecorator", "Decorator", EdgeType.INHERITANCE_MULTI));
        return decorator;
    }

    /**
     * Creates a FactoryMethod design pattern.
     *
     * @return a {@link DesignPattern} representing a FactoryMethod pattern.
     */
    public static DesignPattern createFactoryMethodPattern() {
        final DesignPattern factoryMethod = new DesignPattern("Factory Method");
        factoryMethod.add(createDesignPatternEdge("Product", "ConcreteProduct", EdgeType.INHERITANCE));
        factoryMethod.add(createDesignPatternEdge("ConcreteCreator", "Product", EdgeType.DEPENDENCY));
        factoryMethod.add(createDesignPatternEdge("ConcreteCreator", "Creator", EdgeType.INHERITANCE));
        return factoryMethod;
    }

    /**
     * Creates a flyweight pattern. Checked with the UML diagram in [GoF].
     *
     * @return a {@link DesignPattern} representing a flyweight pattern.
     */
    public static DesignPattern createFlyweightPattern() {
        final DesignPattern flyweight = new DesignPattern("Flyweight");
        flyweight.add(createDesignPatternEdge("Client", "FlyweightFactory", EdgeType.ASSOCIATION_DIRECTED));
        flyweight.add(createDesignPatternEdge("Client", "ConcreteFlyweight", EdgeType.ASSOCIATION_DIRECTED));
        flyweight.add(createDesignPatternEdge("Client", "UnsharedConcreteFlyweight", EdgeType.ASSOCIATION_DIRECTED));
        flyweight.add(createDesignPatternEdge("Flyweight", "FlyweightFactory", EdgeType.AGGREGATE));
        flyweight.add(createDesignPatternEdge("ConcreteFlyweight", "Flyweight", EdgeType.INHERITANCE));
        flyweight.add(createDesignPatternEdge("UnsharedConcreteFlyweight", "Flyweight", EdgeType.INHERITANCE));
        return flyweight;
    }

    /**
     * Creates an Iterator design pattern.
     *
     * @return a {@link DesignPattern} representing an Iterator pattern.
     */

    public static DesignPattern createIteratorPattern() {
        final DesignPattern iterator = new DesignPattern("Iterator");
        iterator.add(createDesignPatternEdge("ConcreteAggregate", "Aggregate", EdgeType.INHERITANCE));
        iterator.add(createDesignPatternEdge("Client", "Aggregate", EdgeType.ASSOCIATION_DIRECTED));
        iterator.add(createDesignPatternEdge("Client", "Iterator", EdgeType.ASSOCIATION_DIRECTED));
        iterator.add(createDesignPatternEdge("ConcreteIterator", "Iterator", EdgeType.INHERITANCE));
        iterator.add(createDesignPatternEdge("ConcreteIterator", "ConcreteAggregate", EdgeType.ASSOCIATION_DIRECTED));
        iterator.add(createDesignPatternEdge("ConcreteAggregate", "ConcreteIterator", EdgeType.DEPENDENCY));
        return iterator;
    }

    /**
     * Creates a Mediator design pattern.
     *
     * @return a {@link DesignPattern} representing a Mediator pattern.
     */

    public static DesignPattern createMediatorPattern() {
        final DesignPattern mediator = new DesignPattern("Mediator");
        mediator.add(createDesignPatternEdge("ConcreteMediator", "Mediator", EdgeType.INHERITANCE));
        mediator.add(createDesignPatternEdge("Colleague", "Mediator", EdgeType.ASSOCIATION_DIRECTED));
        mediator.add(createDesignPatternEdge("ConcreteColleague1", "Colleague", EdgeType.INHERITANCE));
        mediator.add(createDesignPatternEdge("ConcreteColleague2", "Colleague", EdgeType.INHERITANCE));
        mediator.add(createDesignPatternEdge("ConcreteMediator", "ConcreteColleague1", EdgeType.ASSOCIATION_DIRECTED));
        mediator.add(createDesignPatternEdge("ConcreteMediator", "ConcreteColleague2", EdgeType.ASSOCIATION_DIRECTED));
        return mediator;
    }

    /**
     * Creates a Memento design pattern.
     *
     * @return a {@link DesignPattern} representing a Memento pattern.
     */

    public static DesignPattern createMementoPattern() {
        final DesignPattern memento = new DesignPattern("Memento");
        memento.add(createDesignPatternEdge("Memento", "Caretaker", EdgeType.AGGREGATE));
        memento.add(createDesignPatternEdge("Originator", "Memento", EdgeType.DEPENDENCY));
        return memento;
    }

    /**
     * Creates an Observer design pattern.
     *
     * @return a {@link DesignPattern} representing an Observer pattern.
     */
    public static DesignPattern createObserverPattern() {
        final DesignPattern observer = new DesignPattern("Observer");
        observer.add(createDesignPatternEdge("ConcreteSubject", "Subject", EdgeType.INHERITANCE));
        observer.add(createDesignPatternEdge("Subject", "Observer", EdgeType.AGGREGATE));
        observer.add(createDesignPatternEdge("ConcreteObserver", "Observer", EdgeType.INHERITANCE));
        observer.add(createDesignPatternEdge("ConcreteObserver", "ConcreteSubject", EdgeType.ASSOCIATION_DIRECTED));
        return observer;
    }

    /**
     * Creates a Proxy design pattern.
     *
     * @return a {@link DesignPattern} representing a Proxy pattern.
     */
    public static DesignPattern createProxyPattern() {
        final DesignPattern proxy = new DesignPattern("Proxy");
        proxy.add(createDesignPatternEdge("Client", "Subject", EdgeType.ASSOCIATION_DIRECTED));
        proxy.add(createDesignPatternEdge("Proxy", "Subject", EdgeType.INHERITANCE));
        proxy.add(createDesignPatternEdge("RealSubject", "Subject", EdgeType.INHERITANCE));
        proxy.add(createDesignPatternEdge("Proxy", "RealSubject", EdgeType.ASSOCIATION_DIRECTED));
        return proxy;
    }

    /**
     * Creates a State/Strategy design pattern.
     *
     * @return a {@link DesignPattern} representing a State/Strategy pattern.
     */
    public static DesignPattern createStateStrategyPattern() {
        final DesignPattern strategy = new DesignPattern("State - Strategy");
        strategy.add(createDesignPatternEdge("Strategy", "Context", EdgeType.AGGREGATE));
        strategy.add(createDesignPatternEdge("ConcreteStrategy", "Strategy", EdgeType.INHERITANCE_MULTI));
        return strategy;
    }

    /**
     * Creates a bridge pattern. Checked with the UML diagram in [GoF].
     *
     * @return a {@link DesignPattern} representing a bridge pattern.
     */
    public static DesignPattern createBridgePattern() {
        final DesignPattern bridge = new DesignPattern("Bridge");
        bridge.add(createDesignPatternEdge("Client", "Abstraction", EdgeType.ASSOCIATION));
        bridge.add(createDesignPatternEdge("Implementor", "Abstraction", EdgeType.AGGREGATE));
        bridge.add(createDesignPatternEdge("ConcreteImplementorA", "Implementor", EdgeType.INHERITANCE));
        bridge.add(createDesignPatternEdge("ConcreteImplementorB", "Implementor", EdgeType.INHERITANCE));
        bridge.add(createDesignPatternEdge("RefinedAbstraction", "Abstraction", EdgeType.INHERITANCE));
        return bridge;
    }

    /**
     * Creates a prototype pattern.
     *
     * @return a {@link DesignPattern} representing a protoype pattern.
     */
    public static DesignPattern createPrototypePattern() {
        final DesignPattern prototype = new DesignPattern("Prototype");
        prototype.add(createDesignPatternEdge("P", "Q", EdgeType.ASSOCIATION_DIRECTED));
        prototype.add(createDesignPatternEdge("R", "Q", EdgeType.INHERITANCE));
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
        system.add(createSystemUnderConsiderationEdge("Client", "Ab", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("ConcAb1", "Ab", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("ConcAb2", "Ab", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("ConcAb2", "F_Factory", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("Impl", "Ab", EdgeType.AGGREGATE));
        system.add(createSystemUnderConsiderationEdge("F_Factory", "Impl", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("P_Subject", "Impl", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("ConcImpl3", "Impl", EdgeType.INHERITANCE));

        // Factory Method
        system.add(createSystemUnderConsiderationEdge("F_ConcreteFactory", "F_Factory", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("F_ConcreteFactory", "F_Product", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("F_Product", "F_ProdInterface", EdgeType.INHERITANCE));

        // Proxy
        system.add(createSystemUnderConsiderationEdge("P_Proxy", "P_Subject", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("P_RealSubject", "P_Subject", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("P_Proxy", "P_RealSubject", EdgeType.ASSOCIATION));

        // Decorator
        system.add(createSystemUnderConsiderationEdge("DecInterface", "ConcImpl3", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("DecBasis", "DecInterface", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("DecInterface", "DecWrapper", EdgeType.COMPOSITE));
        system.add(createSystemUnderConsiderationEdge("DecOption1", "DecWrapper", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("DecOption2", "DecWrapper", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("DecWrapper", "DecInterface", EdgeType.INHERITANCE));

        // Memento
        system.add(createSystemUnderConsiderationEdge("Client", "Maintainer", EdgeType.DEPENDENCY));
        system.add(createSystemUnderConsiderationEdge("Status", "Maintainer", EdgeType.AGGREGATE));

        // Adapter
        system.add(createSystemUnderConsiderationEdge("Client", "T_Interface", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("Aanpasser", "T_Interface", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("Aanpasser", "NietPassend", EdgeType.ASSOCIATION));

        // Composite
        system.add(createSystemUnderConsiderationEdge("Leaflet", "T_Interface", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("Union", "T_Interface", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("T_Interface", "Union", EdgeType.AGGREGATE));

        // Mediator
        system.add(createSystemUnderConsiderationEdge("DecInterface", "Med", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("ConcMed", "Med", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("ConcMed", "DecBasis", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("ConcMed", "DecWrapper", EdgeType.ASSOCIATION));

        // Command
        system.add(createSystemUnderConsiderationEdge("Maintainer", "Opdracht", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("Client", "Ontvanger", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("Opdracht", "Aanroeper", EdgeType.AGGREGATE));
        system.add(createSystemUnderConsiderationEdge("Maintainer", "Ontvanger", EdgeType.ASSOCIATION));

        // Iterator
        system.add(createSystemUnderConsiderationEdge("User", "DecInterface", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("User", "Med", EdgeType.ASSOCIATION));

        // ChainOfResponsibility
        system.add(createSystemUnderConsiderationEdge("Aanroeper", "Behandelaar", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("ConcreteBehandelaar_1", "Behandelaar", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("Behandelaar", "Behandelaar", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("ConcreteBehandelaar_2", "Behandelaar", EdgeType.INHERITANCE));

        // AbstractFactory
        system.add(createSystemUnderConsiderationEdge("DecOption1", "AbstrFact", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("ConcrFact1", "AbstrFact", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("ConcrFact2", "AbstrFact", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("DecOption1", "AbstrProA", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("DecOption1", "AbstrProB", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("Pro1A", "AbstrProA", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("Pro2A", "AbstrProA", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("Pro1B", "AbstrProB", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("Pro2B", "AbstrProB", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("ConcrFact1", "Pro1A", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("ConcrFact2", "Pro2A", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("ConcrFact1", "Pro1B", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("ConcrFact2", "Pro2B", EdgeType.ASSOCIATION));

        // Flyweight
        system.add(createSystemUnderConsiderationEdge("FW_Cl", "FlywFact", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("FW_Cl", "ConcFlyw", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("FW_Cl", "UnshConcFlyw", EdgeType.ASSOCIATION));
        system.add(createSystemUnderConsiderationEdge("Flyw", "NietPassend", EdgeType.AGGREGATE));
        system.add(createSystemUnderConsiderationEdge("ConcFlyw", "Flyw", EdgeType.INHERITANCE));
        system.add(createSystemUnderConsiderationEdge("UnshConcFlyw", "Flyw", EdgeType.INHERITANCE));

        return system;
    }

    public static DesignPatternEdge createDesignPatternEdge(String classname1, String classname2, EdgeType edgeType) {
        final DesignPatternClass cl1 = new DesignPatternClass(classname1);
        final DesignPatternClass cl2 = new DesignPatternClass(classname2);
        return new DesignPatternEdge(cl1, cl2, edgeType);

    }

    public static SystemUnderConsiderationEdge createSystemUnderConsiderationEdge(String classname1, String classname2, EdgeType edgeType) {
        final SystemUnderConsiderationClass cl1 = new SystemUnderConsiderationClass(classname1);
        final SystemUnderConsiderationClass cl2 = new SystemUnderConsiderationClass(classname2);
        return new SystemUnderConsiderationEdge(cl1, cl2, edgeType);
    }

}
