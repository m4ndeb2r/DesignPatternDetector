package nl.ou.dpd.utils;

import nl.ou.dpd.fourtuples.FT_constants;
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
        abstractFactory.add(new FourTuple("Client", "AbstractFactory", FT_constants.ASSOCIATION_DIRECTED));
        abstractFactory.add(new FourTuple("Client", "AbstractProductA", FT_constants.ASSOCIATION_DIRECTED));
        abstractFactory.add(new FourTuple("Client", "AbstractProductB", FT_constants.ASSOCIATION_DIRECTED));
        abstractFactory.add(new FourTuple("ConcreteFact1", "AbstractFactory", FT_constants.INHERITANCE));
        abstractFactory.add(new FourTuple("ConcreteFact2", "AbstractFactory", FT_constants.INHERITANCE));
        abstractFactory.add(new FourTuple("ConcreteFact1", "ProductA1", FT_constants.DEPENDENCY));
        abstractFactory.add(new FourTuple("ConcreteFact2", "ProductA2", FT_constants.DEPENDENCY));
        abstractFactory.add(new FourTuple("ConcreteFact1", "ProductB1", FT_constants.DEPENDENCY));
        abstractFactory.add(new FourTuple("ConcreteFact2", "ProductB2", FT_constants.DEPENDENCY));
        abstractFactory.add(new FourTuple("ProductA1", "AbstractProductA", FT_constants.INHERITANCE));
        abstractFactory.add(new FourTuple("ProductA2", "AbstractProductA", FT_constants.INHERITANCE));
        abstractFactory.add(new FourTuple("ProductB1", "AbstractProductB", FT_constants.INHERITANCE));
        abstractFactory.add(new FourTuple("ProductB2", "AbstractProductB", FT_constants.INHERITANCE));
        return abstractFactory;
    }

    /**
     * Creates an Adapter design pattern.
     *
     * @return a {@link FourTupleArray} representing an Adapter pattern.
     */
    public static FourTupleArray createAdapterPattern() {
        final FourTupleArray adapter = new FourTupleArray("Adapter");
        adapter.add(new FourTuple("Client", "Target", FT_constants.ASSOCIATION_DIRECTED));
        adapter.add(new FourTuple("Adapter", "Target", FT_constants.INHERITANCE));
        adapter.add(new FourTuple("Adapter", "Adaptee", FT_constants.ASSOCIATION_DIRECTED));
        return adapter;
    }

    /**
     * Creates a Builder design pattern.
     *
     * @return a {@link FourTupleArray} representing a Builder pattern.
     */
    public static FourTupleArray createBuilderPattern() {
        final FourTupleArray builder = new FourTupleArray("Builder");
        builder.add(new FourTuple("Builder", "Director", FT_constants.AGGREGATE));
        builder.add(new FourTuple("ConcreteBuilder", "Builder", FT_constants.INHERITANCE));
        builder.add(new FourTuple("ConcreteBuilder", "Product", FT_constants.DEPENDENCY));
        return builder;
    }

    /**
     * Creates a ChainOfResponsibility design pattern.
     *
     * @return a {@link FourTupleArray} representing a ChainOfResponsibility pattern.
     */
    public static FourTupleArray createChainOfResponsibilityPattern() {
        final FourTupleArray chainOfResponsibility = new FourTupleArray("ChainOfResponsibility");
        chainOfResponsibility.add(new FourTuple("ConcreteHandler", "Handler", FT_constants.INHERITANCE_MULTI));
        chainOfResponsibility.add(new FourTuple("Handler", "Handler", FT_constants.ASSOCIATION_DIRECTED));
        chainOfResponsibility.add(new FourTuple("Client", "Handler", FT_constants.ASSOCIATION_DIRECTED));
        return chainOfResponsibility;
    }

    /**
     * Creates a Command design pattern.
     *
     * @return a {@link FourTupleArray} representing a Command pattern.
     */
    public static FourTupleArray createCommandPattern() {
        final FourTupleArray command = new FourTupleArray("Command");
        command.add(new FourTuple("Client", "Receiver", FT_constants.ASSOCIATION_DIRECTED));
        command.add(new FourTuple("Client", "ConcreteCommand", FT_constants.DEPENDENCY));
        command.add(new FourTuple("ConcreteCommand", "Receiver", FT_constants.ASSOCIATION_DIRECTED));
        command.add(new FourTuple("ConcreteCommand", "Command", FT_constants.INHERITANCE));
        command.add(new FourTuple("Command", "Invoker", FT_constants.AGGREGATE));
        return command;
    }

    /**
     * Creates a Composite design pattern.
     *
     * @return a {@link FourTupleArray} representing a Composite pattern.
     */
    public static FourTupleArray createCompositePattern() {
        final FourTupleArray composite = new FourTupleArray("Composite");
        composite.add(new FourTuple("Client", "Component", FT_constants.ASSOCIATION_DIRECTED));
        composite.add(new FourTuple("Leaf", "Component", FT_constants.INHERITANCE));
        composite.add(new FourTuple("Composite", "Component", FT_constants.INHERITANCE));
        composite.add(new FourTuple("Component", "Composite", FT_constants.AGGREGATE));
        return composite;
    }

    /**
     * Creates a Decorator design pattern.
     *
     * @return a {@link FourTupleArray} representing a Decorator pattern.
     */
    public static FourTupleArray createDecoratorPattern() {
        final FourTupleArray decorator = new FourTupleArray("Decorator");
        decorator.add(new FourTuple("ConcreteComponent", "Component", FT_constants.INHERITANCE));
        decorator.add(new FourTuple("Decorator", "Component", FT_constants.INHERITANCE));
        decorator.add(new FourTuple("Component", "Decorator", FT_constants.AGGREGATE));
        decorator.add(new FourTuple("ConcreteDecorator", "Decorator", FT_constants.INHERITANCE_MULTI));
        return decorator;
    }

    /**
     * Creates a FactoryMethod design pattern.
     *
     * @return a {@link FourTupleArray} representing a FactoryMethod pattern.
     */
    public static FourTupleArray createFactoryMethodPattern() {
        final FourTupleArray factoryMethod = new FourTupleArray("Factory Method");
        factoryMethod.add(new FourTuple("Product", "ConcreteProduct", FT_constants.INHERITANCE));
        factoryMethod.add(new FourTuple("ConcreteCreator", "Product", FT_constants.DEPENDENCY));
        factoryMethod.add(new FourTuple("ConcreteCreator", "Creator", FT_constants.INHERITANCE));
        return factoryMethod;
    }

    /**
     * Creates a flyweight pattern. Checked with the UML diagram in [GoF].
     *
     * @return a {@link FourTupleArray} representing a flyweight pattern.
     */
    public static FourTupleArray createFlyweightPattern() {
        final FourTupleArray flyweight = new FourTupleArray("Flyweight");
        flyweight.add(new FourTuple("Client", "FlyweightFactory", FT_constants.ASSOCIATION_DIRECTED));
        flyweight.add(new FourTuple("Client", "ConcreteFlyweight", FT_constants.ASSOCIATION_DIRECTED));
        flyweight.add(new FourTuple("Client", "UnsharedConcreteFlyweight", FT_constants.ASSOCIATION_DIRECTED));
        flyweight.add(new FourTuple("Flyweight", "FlyweightFactory", FT_constants.AGGREGATE));
        flyweight.add(new FourTuple("ConcreteFlyweight", "Flyweight", FT_constants.INHERITANCE));
        flyweight.add(new FourTuple("UnsharedConcreteFlyweight", "Flyweight", FT_constants.INHERITANCE));
        return flyweight;
    }

    /**
     * Creates an Iterator design pattern.
     *
     * @return a {@link FourTupleArray} representing an Iterator pattern.
     */

    public static FourTupleArray createIteratorPattern() {
        final FourTupleArray iterator = new FourTupleArray("Iterator");
        iterator.add(new FourTuple("ConcreteAggregate", "Aggregate", FT_constants.INHERITANCE));
        iterator.add(new FourTuple("Client", "Aggregate", FT_constants.ASSOCIATION_DIRECTED));
        iterator.add(new FourTuple("Client", "Iterator", FT_constants.ASSOCIATION_DIRECTED));
        iterator.add(new FourTuple("ConcreteIterator", "Iterator", FT_constants.INHERITANCE));
        iterator.add(new FourTuple("ConcreteIterator", "ConcreteAggregate", FT_constants.ASSOCIATION_DIRECTED));
        iterator.add(new FourTuple("ConcreteAggregate", "ConcreteIterator", FT_constants.DEPENDENCY));
        return iterator;
    }

    /**
     * Creates a Mediator design pattern.
     *
     * @return a {@link FourTupleArray} representing a Mediator pattern.
     */

    public static FourTupleArray createMediatorPattern() {
        final FourTupleArray mediator = new FourTupleArray("Mediator");
        mediator.add(new FourTuple("ConcreteMediator", "Mediator", FT_constants.INHERITANCE));
        mediator.add(new FourTuple("Colleague", "Mediator", FT_constants.ASSOCIATION_DIRECTED));
        mediator.add(new FourTuple("ConcreteColleague1", "Colleague", FT_constants.INHERITANCE));
        mediator.add(new FourTuple("ConcreteColleague2", "Colleague", FT_constants.INHERITANCE));
        mediator.add(new FourTuple("ConcreteMediator", "ConcreteColleague1", FT_constants.ASSOCIATION_DIRECTED));
        mediator.add(new FourTuple("ConcreteMediator", "ConcreteColleague2", FT_constants.ASSOCIATION_DIRECTED));
        return mediator;
    }

    /**
     * Creates a Memento design pattern.
     *
     * @return a {@link FourTupleArray} representing a Memento pattern.
     */

    public static FourTupleArray createMementoPattern() {
        final FourTupleArray memento = new FourTupleArray("Memento");
        memento.add(new FourTuple("Memento", "Caretaker", FT_constants.AGGREGATE));
        memento.add(new FourTuple("Originator", "Memento", FT_constants.DEPENDENCY));
        return memento;
    }

    /**
     * Creates an Observer design pattern.
     *
     * @return a {@link FourTupleArray} representing an Observer pattern.
     */
    public static FourTupleArray createObserverPattern() {
        final FourTupleArray observer = new FourTupleArray("Observer");
        observer.add(new FourTuple("ConcreteSubject", "Subject", FT_constants.INHERITANCE));
        observer.add(new FourTuple("Subject", "Observer", FT_constants.AGGREGATE));
        observer.add(new FourTuple("ConcreteObserver", "Observer", FT_constants.INHERITANCE));
        observer.add(new FourTuple("ConcreteObserver", "ConcreteSubject", FT_constants.ASSOCIATION_DIRECTED));
        return observer;
    }

    /**
     * Creates a Proxy design pattern.
     *
     * @return a {@link FourTupleArray} representing a Proxy pattern.
     */
    public static FourTupleArray createProxyPattern() {
        final FourTupleArray proxy = new FourTupleArray("Proxy");
        proxy.add(new FourTuple("Client", "Subject", FT_constants.ASSOCIATION_DIRECTED));
        proxy.add(new FourTuple("Proxy", "Subject", FT_constants.INHERITANCE));
        proxy.add(new FourTuple("RealSubject", "Subject", FT_constants.INHERITANCE));
        proxy.add(new FourTuple("Proxy", "RealSubject", FT_constants.ASSOCIATION_DIRECTED));
        return proxy;
    }

    /**
     * Creates a State/Strategy design pattern.
     *
     * @return a {@link FourTupleArray} representing a State/Strategy pattern.
     */
    public static FourTupleArray createStateStrategyPattern() {
        final FourTupleArray strategy = new FourTupleArray("State - Strategy");
        strategy.add(new FourTuple("Strategy", "Context", FT_constants.AGGREGATE));
        strategy.add(new FourTuple("ConcreteStrategy", "Strategy", FT_constants.INHERITANCE_MULTI));
        return strategy;
    }

    /**
     * Creates a bridge pattern. Checked with the UML diagram in [GoF].
     *
     * @return a {@link FourTupleArray} representing a bridge pattern.
     */
    public static FourTupleArray createBridgePattern() {
        final FourTupleArray bridge = new FourTupleArray("Bridge");
        bridge.add(new FourTuple("Client", "Abstraction", FT_constants.ASSOCIATION));
        bridge.add(new FourTuple("Implementor", "Abstraction", FT_constants.AGGREGATE));
        bridge.add(new FourTuple("ConcreteImplementorA", "Implementor", FT_constants.INHERITANCE));
        bridge.add(new FourTuple("ConcreteImplementorB", "Implementor", FT_constants.INHERITANCE));
        bridge.add(new FourTuple("RefinedAbstraction", "Abstraction", FT_constants.INHERITANCE));
        return bridge;
    }

    /**
     * Creates a prototype pattern.
     *
     * @return a {@link FourTupleArray} representing a protoype pattern.
     */
    public static FourTupleArray createPrototypePattern() {
        final FourTupleArray prototype = new FourTupleArray("Prototype");
        prototype.add(new FourTuple("P", "Q", FT_constants.ASSOCIATION_DIRECTED));
        prototype.add(new FourTuple("R", "Q", FT_constants.INHERITANCE));
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
        system.add(new FourTuple("Client", "Ab", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcAb1", "Ab", FT_constants.INHERITANCE));
        system.add(new FourTuple("ConcAb2", "Ab", FT_constants.INHERITANCE));
        system.add(new FourTuple("ConcAb2", "F_Factory", FT_constants.ASSOCIATION));
        system.add(new FourTuple("Impl", "Ab", FT_constants.AGGREGATE));
        system.add(new FourTuple("F_Factory", "Impl", FT_constants.INHERITANCE));
        system.add(new FourTuple("P_Subject", "Impl", FT_constants.INHERITANCE));
        system.add(new FourTuple("ConcImpl3", "Impl", FT_constants.INHERITANCE));

        // Factory Method
        system.add(new FourTuple("F_ConcreteFactory", "F_Factory", FT_constants.INHERITANCE));
        system.add(new FourTuple("F_ConcreteFactory", "F_Product", FT_constants.ASSOCIATION));
        system.add(new FourTuple("F_Product", "F_ProdInterface", FT_constants.INHERITANCE));

        // Proxy
        system.add(new FourTuple("P_Proxy", "P_Subject", FT_constants.INHERITANCE));
        system.add(new FourTuple("P_RealSubject", "P_Subject", FT_constants.INHERITANCE));
        system.add(new FourTuple("P_Proxy", "P_RealSubject", FT_constants.ASSOCIATION));

        // Decorator
        system.add(new FourTuple("DecInterface", "ConcImpl3", FT_constants.ASSOCIATION));
        system.add(new FourTuple("DecBasis", "DecInterface", FT_constants.INHERITANCE));
        system.add(new FourTuple("DecInterface", "DecWrapper", FT_constants.COMPOSITE));
        system.add(new FourTuple("DecOption1", "DecWrapper", FT_constants.INHERITANCE));
        system.add(new FourTuple("DecOption2", "DecWrapper", FT_constants.INHERITANCE));
        system.add(new FourTuple("DecWrapper", "DecInterface", FT_constants.INHERITANCE));

        // Memento
        system.add(new FourTuple("Client", "Maintainer", FT_constants.DEPENDENCY));
        system.add(new FourTuple("Status", "Maintainer", FT_constants.AGGREGATE));

        // Adapter
        system.add(new FourTuple("Client", "T_Interface", FT_constants.ASSOCIATION));
        system.add(new FourTuple("Aanpasser", "T_Interface", FT_constants.INHERITANCE));
        system.add(new FourTuple("Aanpasser", "NietPassend", FT_constants.ASSOCIATION));

        // Composite
        system.add(new FourTuple("Leaflet", "T_Interface", FT_constants.INHERITANCE));
        system.add(new FourTuple("Union", "T_Interface", FT_constants.INHERITANCE));
        system.add(new FourTuple("T_Interface", "Union", FT_constants.AGGREGATE));

        // Mediator
        system.add(new FourTuple("DecInterface", "Med", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcMed", "Med", FT_constants.INHERITANCE));
        system.add(new FourTuple("ConcMed", "DecBasis", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcMed", "DecWrapper", FT_constants.ASSOCIATION));

        // Command
        system.add(new FourTuple("Maintainer", "Opdracht", FT_constants.INHERITANCE));
        system.add(new FourTuple("Client", "Ontvanger", FT_constants.ASSOCIATION));
        system.add(new FourTuple("Opdracht", "Aanroeper", FT_constants.AGGREGATE));
        system.add(new FourTuple("Maintainer", "Ontvanger", FT_constants.ASSOCIATION));

        // Iterator
        system.add(new FourTuple("User", "DecInterface", FT_constants.ASSOCIATION));
        system.add(new FourTuple("User", "Med", FT_constants.ASSOCIATION));

        // ChainOfResponsibility
        system.add(new FourTuple("Aanroeper", "Behandelaar", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcreteBehandelaar_1", "Behandelaar", FT_constants.INHERITANCE));
        system.add(new FourTuple("Behandelaar", "Behandelaar", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcreteBehandelaar_2", "Behandelaar", FT_constants.INHERITANCE));

        // AbstractFactory
        system.add(new FourTuple("DecOption1", "AbstrFact", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcrFact1", "AbstrFact", FT_constants.INHERITANCE));
        system.add(new FourTuple("ConcrFact2", "AbstrFact", FT_constants.INHERITANCE));
        system.add(new FourTuple("DecOption1", "AbstrProA", FT_constants.ASSOCIATION));
        system.add(new FourTuple("DecOption1", "AbstrProB", FT_constants.ASSOCIATION));
        system.add(new FourTuple("Pro1A", "AbstrProA", FT_constants.INHERITANCE));
        system.add(new FourTuple("Pro2A", "AbstrProA", FT_constants.INHERITANCE));
        system.add(new FourTuple("Pro1B", "AbstrProB", FT_constants.INHERITANCE));
        system.add(new FourTuple("Pro2B", "AbstrProB", FT_constants.INHERITANCE));
        system.add(new FourTuple("ConcrFact1", "Pro1A", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcrFact2", "Pro2A", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcrFact1", "Pro1B", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcrFact2", "Pro2B", FT_constants.ASSOCIATION));

        // Flyweight
        system.add(new FourTuple("FW_Cl", "FlywFact", FT_constants.ASSOCIATION));
        system.add(new FourTuple("FW_Cl", "ConcFlyw", FT_constants.ASSOCIATION));
        system.add(new FourTuple("FW_Cl", "UnshConcFlyw", FT_constants.ASSOCIATION));
        system.add(new FourTuple("Flyw", "NietPassend", FT_constants.AGGREGATE));
        system.add(new FourTuple("ConcFlyw", "Flyw", FT_constants.INHERITANCE));
        system.add(new FourTuple("UnshConcFlyw", "Flyw", FT_constants.INHERITANCE));

        return system;
    }

}
