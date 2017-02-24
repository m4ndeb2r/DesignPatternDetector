package nl.ou.dpd.utils;

import nl.ou.dpd.fourtuples.EdgeType;
import nl.ou.dpd.fourtuples.FourTuple;
import nl.ou.dpd.fourtuples.FourTupleArray;

import java.util.ArrayList;

/**
 * A utility class for unittest support.
 *
 * @author Martin de Boer
 */
public class TestHelper {

    /**
     * Creates a "template" containing 17 GoF design patterns represented by an {@link ArrayList} of
     * {@link FourTupleArray} instances.
     *
     * @return an {@link ArrayList} containing 17 GoF design patterns.
     */
    public static ArrayList<FourTupleArray> createDesignPatternsTemplates() {
        final ArrayList<FourTupleArray> dps = new ArrayList<>();
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
     * @return a {@link FourTupleArray} representing an AbstractFactory pattern.
     */
    public static FourTupleArray createAbstractFactoryPattern() {
        final FourTupleArray abstractFactory = new FourTupleArray("Abstract Factory");
        abstractFactory.add(new FourTuple("Client", "AbstractFactory", EdgeType.ASSOCIATION_DIRECTED));
        abstractFactory.add(new FourTuple("Client", "AbstractProductA", EdgeType.ASSOCIATION_DIRECTED));
        abstractFactory.add(new FourTuple("Client", "AbstractProductB", EdgeType.ASSOCIATION_DIRECTED));
        abstractFactory.add(new FourTuple("ConcreteFact1", "AbstractFactory", EdgeType.INHERITANCE));
        abstractFactory.add(new FourTuple("ConcreteFact2", "AbstractFactory", EdgeType.INHERITANCE));
        abstractFactory.add(new FourTuple("ConcreteFact1", "ProductA1", EdgeType.DEPENDENCY));
        abstractFactory.add(new FourTuple("ConcreteFact2", "ProductA2", EdgeType.DEPENDENCY));
        abstractFactory.add(new FourTuple("ConcreteFact1", "ProductB1", EdgeType.DEPENDENCY));
        abstractFactory.add(new FourTuple("ConcreteFact2", "ProductB2", EdgeType.DEPENDENCY));
        abstractFactory.add(new FourTuple("ProductA1", "AbstractProductA", EdgeType.INHERITANCE));
        abstractFactory.add(new FourTuple("ProductA2", "AbstractProductA", EdgeType.INHERITANCE));
        abstractFactory.add(new FourTuple("ProductB1", "AbstractProductB", EdgeType.INHERITANCE));
        abstractFactory.add(new FourTuple("ProductB2", "AbstractProductB", EdgeType.INHERITANCE));
        return abstractFactory;
    }

    /**
     * Creates an Adapter design pattern.
     *
     * @return a {@link FourTupleArray} representing an Adapter pattern.
     */
    public static FourTupleArray createAdapterPattern() {
        final FourTupleArray adapter = new FourTupleArray("Adapter");
        adapter.add(new FourTuple("Client", "Target", EdgeType.ASSOCIATION_DIRECTED));
        adapter.add(new FourTuple("Adapter", "Target", EdgeType.INHERITANCE));
        adapter.add(new FourTuple("Adapter", "Adaptee", EdgeType.ASSOCIATION_DIRECTED));
        return adapter;
    }

    /**
     * Creates a Builder design pattern.
     *
     * @return a {@link FourTupleArray} representing a Builder pattern.
     */
    public static FourTupleArray createBuilderPattern() {
        final FourTupleArray builder = new FourTupleArray("Builder");
        builder.add(new FourTuple("Builder", "Director", EdgeType.AGGREGATE));
        builder.add(new FourTuple("ConcreteBuilder", "Builder", EdgeType.INHERITANCE));
        builder.add(new FourTuple("ConcreteBuilder", "Product", EdgeType.DEPENDENCY));
        return builder;
    }

    /**
     * Creates a ChainOfResponsibility design pattern.
     *
     * @return a {@link FourTupleArray} representing a ChainOfResponsibility pattern.
     */
    public static FourTupleArray createChainOfResponsibilityPattern() {
        final FourTupleArray chainOfResponsibility = new FourTupleArray("ChainOfResponsibility");
        chainOfResponsibility.add(new FourTuple("ConcreteHandler", "Handler", EdgeType.INHERITANCE_MULTI));
        chainOfResponsibility.add(new FourTuple("Handler", "Handler", EdgeType.ASSOCIATION_DIRECTED));
        chainOfResponsibility.add(new FourTuple("Client", "Handler", EdgeType.ASSOCIATION_DIRECTED));
        return chainOfResponsibility;
    }

    /**
     * Creates a Command design pattern.
     *
     * @return a {@link FourTupleArray} representing a Command pattern.
     */
    public static FourTupleArray createCommandPattern() {
        final FourTupleArray command = new FourTupleArray("Command");
        command.add(new FourTuple("Client", "Receiver", EdgeType.ASSOCIATION_DIRECTED));
        command.add(new FourTuple("Client", "ConcreteCommand", EdgeType.DEPENDENCY));
        command.add(new FourTuple("ConcreteCommand", "Receiver", EdgeType.ASSOCIATION_DIRECTED));
        command.add(new FourTuple("ConcreteCommand", "Command", EdgeType.INHERITANCE));
        command.add(new FourTuple("Command", "Invoker", EdgeType.AGGREGATE));
        return command;
    }

    /**
     * Creates a Composite design pattern.
     *
     * @return a {@link FourTupleArray} representing a Composite pattern.
     */
    public static FourTupleArray createCompositePattern() {
        final FourTupleArray composite = new FourTupleArray("Composite");
        composite.add(new FourTuple("Client", "Component", EdgeType.ASSOCIATION_DIRECTED));
        composite.add(new FourTuple("Leaf", "Component", EdgeType.INHERITANCE));
        composite.add(new FourTuple("Composite", "Component", EdgeType.INHERITANCE));
        composite.add(new FourTuple("Component", "Composite", EdgeType.AGGREGATE));
        return composite;
    }

    /**
     * Creates a Decorator design pattern.
     *
     * @return a {@link FourTupleArray} representing a Decorator pattern.
     */
    public static FourTupleArray createDecoratorPattern() {
        final FourTupleArray decorator = new FourTupleArray("Decorator");
        decorator.add(new FourTuple("ConcreteComponent", "Component", EdgeType.INHERITANCE));
        decorator.add(new FourTuple("Decorator", "Component", EdgeType.INHERITANCE));
        decorator.add(new FourTuple("Component", "Decorator", EdgeType.AGGREGATE));
        decorator.add(new FourTuple("ConcreteDecorator", "Decorator", EdgeType.INHERITANCE_MULTI));
        return decorator;
    }

    /**
     * Creates a FactoryMethod design pattern.
     *
     * @return a {@link FourTupleArray} representing a FactoryMethod pattern.
     */
    public static FourTupleArray createFactoryMethodPattern() {
        final FourTupleArray factoryMethod = new FourTupleArray("Factory Method");
        factoryMethod.add(new FourTuple("Product", "ConcreteProduct", EdgeType.INHERITANCE));
        factoryMethod.add(new FourTuple("ConcreteCreator", "Product", EdgeType.DEPENDENCY));
        factoryMethod.add(new FourTuple("ConcreteCreator", "Creator", EdgeType.INHERITANCE));
        return factoryMethod;
    }

    /**
     * Creates a flyweight pattern. Checked with the UML diagram in [GoF].
     *
     * @return a {@link FourTupleArray} representing a flyweight pattern.
     */
    public static FourTupleArray createFlyweightPattern() {
        final FourTupleArray flyweight = new FourTupleArray("Flyweight");
        flyweight.add(new FourTuple("Client", "FlyweightFactory", EdgeType.ASSOCIATION_DIRECTED));
        flyweight.add(new FourTuple("Client", "ConcreteFlyweight", EdgeType.ASSOCIATION_DIRECTED));
        flyweight.add(new FourTuple("Client", "UnsharedConcreteFlyweight", EdgeType.ASSOCIATION_DIRECTED));
        flyweight.add(new FourTuple("Flyweight", "FlyweightFactory", EdgeType.AGGREGATE));
        flyweight.add(new FourTuple("ConcreteFlyweight", "Flyweight", EdgeType.INHERITANCE));
        flyweight.add(new FourTuple("UnsharedConcreteFlyweight", "Flyweight", EdgeType.INHERITANCE));
        return flyweight;
    }

    /**
     * Creates an Iterator design pattern.
     *
     * @return a {@link FourTupleArray} representing an Iterator pattern.
     */

    public static FourTupleArray createIteratorPattern() {
        final FourTupleArray iterator = new FourTupleArray("Iterator");
        iterator.add(new FourTuple("ConcreteAggregate", "Aggregate", EdgeType.INHERITANCE));
        iterator.add(new FourTuple("Client", "Aggregate", EdgeType.ASSOCIATION_DIRECTED));
        iterator.add(new FourTuple("Client", "Iterator", EdgeType.ASSOCIATION_DIRECTED));
        iterator.add(new FourTuple("ConcreteIterator", "Iterator", EdgeType.INHERITANCE));
        iterator.add(new FourTuple("ConcreteIterator", "ConcreteAggregate", EdgeType.ASSOCIATION_DIRECTED));
        iterator.add(new FourTuple("ConcreteAggregate", "ConcreteIterator", EdgeType.DEPENDENCY));
        return iterator;
    }

    /**
     * Creates a Mediator design pattern.
     *
     * @return a {@link FourTupleArray} representing a Mediator pattern.
     */

    public static FourTupleArray createMediatorPattern() {
        final FourTupleArray mediator = new FourTupleArray("Mediator");
        mediator.add(new FourTuple("ConcreteMediator", "Mediator", EdgeType.INHERITANCE));
        mediator.add(new FourTuple("Colleague", "Mediator", EdgeType.ASSOCIATION_DIRECTED));
        mediator.add(new FourTuple("ConcreteColleague1", "Colleague", EdgeType.INHERITANCE));
        mediator.add(new FourTuple("ConcreteColleague2", "Colleague", EdgeType.INHERITANCE));
        mediator.add(new FourTuple("ConcreteMediator", "ConcreteColleague1", EdgeType.ASSOCIATION_DIRECTED));
        mediator.add(new FourTuple("ConcreteMediator", "ConcreteColleague2", EdgeType.ASSOCIATION_DIRECTED));
        return mediator;
    }

    /**
     * Creates a Memento design pattern.
     *
     * @return a {@link FourTupleArray} representing a Memento pattern.
     */

    public static FourTupleArray createMementoPattern() {
        final FourTupleArray memento = new FourTupleArray("Memento");
        memento.add(new FourTuple("Memento", "Caretaker", EdgeType.AGGREGATE));
        memento.add(new FourTuple("Originator", "Memento", EdgeType.DEPENDENCY));
        return memento;
    }

    /**
     * Creates an Observer design pattern.
     *
     * @return a {@link FourTupleArray} representing an Observer pattern.
     */
    public static FourTupleArray createObserverPattern() {
        final FourTupleArray observer = new FourTupleArray("Observer");
        observer.add(new FourTuple("ConcreteSubject", "Subject", EdgeType.INHERITANCE));
        observer.add(new FourTuple("Subject", "Observer", EdgeType.AGGREGATE));
        observer.add(new FourTuple("ConcreteObserver", "Observer", EdgeType.INHERITANCE));
        observer.add(new FourTuple("ConcreteObserver", "ConcreteSubject", EdgeType.ASSOCIATION_DIRECTED));
        return observer;
    }

    /**
     * Creates a Proxy design pattern.
     *
     * @return a {@link FourTupleArray} representing a Proxy pattern.
     */
    public static FourTupleArray createProxyPattern() {
        final FourTupleArray proxy = new FourTupleArray("Proxy");
        proxy.add(new FourTuple("Client", "Subject", EdgeType.ASSOCIATION_DIRECTED));
        proxy.add(new FourTuple("Proxy", "Subject", EdgeType.INHERITANCE));
        proxy.add(new FourTuple("RealSubject", "Subject", EdgeType.INHERITANCE));
        proxy.add(new FourTuple("Proxy", "RealSubject", EdgeType.ASSOCIATION_DIRECTED));
        return proxy;
    }

    /**
     * Creates a State/Strategy design pattern.
     *
     * @return a {@link FourTupleArray} representing a State/Strategy pattern.
     */
    public static FourTupleArray createStateStrategyPattern() {
        final FourTupleArray strategy = new FourTupleArray("State - Strategy");
        strategy.add(new FourTuple("Strategy", "Context", EdgeType.AGGREGATE));
        strategy.add(new FourTuple("ConcreteStrategy", "Strategy", EdgeType.INHERITANCE_MULTI));
        return strategy;
    }

    /**
     * Creates a bridge pattern. Checked with the UML diagram in [GoF].
     *
     * @return a {@link FourTupleArray} representing a bridge pattern.
     */
    public static FourTupleArray createBridgePattern() {
        final FourTupleArray bridge = new FourTupleArray("Bridge");
        bridge.add(new FourTuple("Client", "Abstraction", EdgeType.ASSOCIATION));
        bridge.add(new FourTuple("Implementor", "Abstraction", EdgeType.AGGREGATE));
        bridge.add(new FourTuple("ConcreteImplementorA", "Implementor", EdgeType.INHERITANCE));
        bridge.add(new FourTuple("ConcreteImplementorB", "Implementor", EdgeType.INHERITANCE));
        bridge.add(new FourTuple("RefinedAbstraction", "Abstraction", EdgeType.INHERITANCE));
        return bridge;
    }

    /**
     * Creates a prototype pattern.
     *
     * @return a {@link FourTupleArray} representing a protoype pattern.
     */
    public static FourTupleArray createPrototypePattern() {
        final FourTupleArray prototype = new FourTupleArray("Prototype");
        prototype.add(new FourTuple("P", "Q", EdgeType.ASSOCIATION_DIRECTED));
        prototype.add(new FourTuple("R", "Q", EdgeType.INHERITANCE));
        return prototype;
    }

    /**
     * Creates a "system under consideration" containing quite a number of patterns that the application should be able
     * to detect.
     *
     * @return a {@link FourTupleArray} representing the design of a "system under consideration".
     */
    public static FourTupleArray createComplexSystemUnderConsideration() {
        final FourTupleArray system = new FourTupleArray();

        // Bridge
        system.add(new FourTuple("Client", "Ab", EdgeType.ASSOCIATION));
        system.add(new FourTuple("ConcAb1", "Ab", EdgeType.INHERITANCE));
        system.add(new FourTuple("ConcAb2", "Ab", EdgeType.INHERITANCE));
        system.add(new FourTuple("ConcAb2", "F_Factory", EdgeType.ASSOCIATION));
        system.add(new FourTuple("Impl", "Ab", EdgeType.AGGREGATE));
        system.add(new FourTuple("F_Factory", "Impl", EdgeType.INHERITANCE));
        system.add(new FourTuple("P_Subject", "Impl", EdgeType.INHERITANCE));
        system.add(new FourTuple("ConcImpl3", "Impl", EdgeType.INHERITANCE));

        // Factory Method
        system.add(new FourTuple("F_ConcreteFactory", "F_Factory", EdgeType.INHERITANCE));
        system.add(new FourTuple("F_ConcreteFactory", "F_Product", EdgeType.ASSOCIATION));
        system.add(new FourTuple("F_Product", "F_ProdInterface", EdgeType.INHERITANCE));

        // Proxy
        system.add(new FourTuple("P_Proxy", "P_Subject", EdgeType.INHERITANCE));
        system.add(new FourTuple("P_RealSubject", "P_Subject", EdgeType.INHERITANCE));
        system.add(new FourTuple("P_Proxy", "P_RealSubject", EdgeType.ASSOCIATION));

        // Decorator
        system.add(new FourTuple("DecInterface", "ConcImpl3", EdgeType.ASSOCIATION));
        system.add(new FourTuple("DecBasis", "DecInterface", EdgeType.INHERITANCE));
        system.add(new FourTuple("DecInterface", "DecWrapper", EdgeType.COMPOSITE));
        system.add(new FourTuple("DecOption1", "DecWrapper", EdgeType.INHERITANCE));
        system.add(new FourTuple("DecOption2", "DecWrapper", EdgeType.INHERITANCE));
        system.add(new FourTuple("DecWrapper", "DecInterface", EdgeType.INHERITANCE));

        // Memento
        system.add(new FourTuple("Client", "Maintainer", EdgeType.DEPENDENCY));
        system.add(new FourTuple("Status", "Maintainer", EdgeType.AGGREGATE));

        // Adapter
        system.add(new FourTuple("Client", "T_Interface", EdgeType.ASSOCIATION));
        system.add(new FourTuple("Aanpasser", "T_Interface", EdgeType.INHERITANCE));
        system.add(new FourTuple("Aanpasser", "NietPassend", EdgeType.ASSOCIATION));

        // Composite
        system.add(new FourTuple("Leaflet", "T_Interface", EdgeType.INHERITANCE));
        system.add(new FourTuple("Union", "T_Interface", EdgeType.INHERITANCE));
        system.add(new FourTuple("T_Interface", "Union", EdgeType.AGGREGATE));

        // Mediator
        system.add(new FourTuple("DecInterface", "Med", EdgeType.ASSOCIATION));
        system.add(new FourTuple("ConcMed", "Med", EdgeType.INHERITANCE));
        system.add(new FourTuple("ConcMed", "DecBasis", EdgeType.ASSOCIATION));
        system.add(new FourTuple("ConcMed", "DecWrapper", EdgeType.ASSOCIATION));

        // Command
        system.add(new FourTuple("Maintainer", "Opdracht", EdgeType.INHERITANCE));
        system.add(new FourTuple("Client", "Ontvanger", EdgeType.ASSOCIATION));
        system.add(new FourTuple("Opdracht", "Aanroeper", EdgeType.AGGREGATE));
        system.add(new FourTuple("Maintainer", "Ontvanger", EdgeType.ASSOCIATION));

        // Iterator
        system.add(new FourTuple("User", "DecInterface", EdgeType.ASSOCIATION));
        system.add(new FourTuple("User", "Med", EdgeType.ASSOCIATION));

        // ChainOfResponsibility
        system.add(new FourTuple("Aanroeper", "Behandelaar", EdgeType.ASSOCIATION));
        system.add(new FourTuple("ConcreteBehandelaar_1", "Behandelaar", EdgeType.INHERITANCE));
        system.add(new FourTuple("Behandelaar", "Behandelaar", EdgeType.ASSOCIATION));
        system.add(new FourTuple("ConcreteBehandelaar_2", "Behandelaar", EdgeType.INHERITANCE));

        // AbstractFactory
        system.add(new FourTuple("DecOption1", "AbstrFact", EdgeType.ASSOCIATION));
        system.add(new FourTuple("ConcrFact1", "AbstrFact", EdgeType.INHERITANCE));
        system.add(new FourTuple("ConcrFact2", "AbstrFact", EdgeType.INHERITANCE));
        system.add(new FourTuple("DecOption1", "AbstrProA", EdgeType.ASSOCIATION));
        system.add(new FourTuple("DecOption1", "AbstrProB", EdgeType.ASSOCIATION));
        system.add(new FourTuple("Pro1A", "AbstrProA", EdgeType.INHERITANCE));
        system.add(new FourTuple("Pro2A", "AbstrProA", EdgeType.INHERITANCE));
        system.add(new FourTuple("Pro1B", "AbstrProB", EdgeType.INHERITANCE));
        system.add(new FourTuple("Pro2B", "AbstrProB", EdgeType.INHERITANCE));
        system.add(new FourTuple("ConcrFact1", "Pro1A", EdgeType.ASSOCIATION));
        system.add(new FourTuple("ConcrFact2", "Pro2A", EdgeType.ASSOCIATION));
        system.add(new FourTuple("ConcrFact1", "Pro1B", EdgeType.ASSOCIATION));
        system.add(new FourTuple("ConcrFact2", "Pro2B", EdgeType.ASSOCIATION));

        // Flyweight
        system.add(new FourTuple("FW_Cl", "FlywFact", EdgeType.ASSOCIATION));
        system.add(new FourTuple("FW_Cl", "ConcFlyw", EdgeType.ASSOCIATION));
        system.add(new FourTuple("FW_Cl", "UnshConcFlyw", EdgeType.ASSOCIATION));
        system.add(new FourTuple("Flyw", "NietPassend", EdgeType.AGGREGATE));
        system.add(new FourTuple("ConcFlyw", "Flyw", EdgeType.INHERITANCE));
        system.add(new FourTuple("UnshConcFlyw", "Flyw", EdgeType.INHERITANCE));

        return system;
    }

}
