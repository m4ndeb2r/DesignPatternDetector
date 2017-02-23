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
    public static ArrayList<FourTupleArray> createDesignPatternsTemplate() {
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

    private static FourTupleArray createAbstractFactoryPattern() {
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

    private static FourTupleArray createAdapterPattern() {
        final FourTupleArray adapter = new FourTupleArray("Adapter");
        adapter.add(new FourTuple("Client", "Target", FT_constants.ASSOCIATION_DIRECTED));
        adapter.add(new FourTuple("Adapter", "Target", FT_constants.INHERITANCE));
        adapter.add(new FourTuple("Adapter", "Adaptee", FT_constants.ASSOCIATION_DIRECTED));
        return adapter;
    }

    private static FourTupleArray createBuilderPattern() {
        final FourTupleArray builder = new FourTupleArray("Builder");
        builder.add(new FourTuple("Builder", "Director", FT_constants.AGGREGATE));
        builder.add(new FourTuple("ConcreteBuilder", "Builder", FT_constants.INHERITANCE));
        builder.add(new FourTuple("ConcreteBuilder", "Product", FT_constants.DEPENDENCY));
        return builder;
    }

    private static FourTupleArray createChainOfResponsibilityPattern() {
        final FourTupleArray chainOfResponsibility = new FourTupleArray("ChainOfResponsibility");
        chainOfResponsibility.add(new FourTuple("ConcreteHandler", "Handler", FT_constants.INHERITANCE_MULTI));
        chainOfResponsibility.add(new FourTuple("Handler", "Handler", FT_constants.ASSOCIATION_DIRECTED));
        chainOfResponsibility.add(new FourTuple("Client", "Handler", FT_constants.ASSOCIATION_DIRECTED));
        return chainOfResponsibility;
    }

    private static FourTupleArray createCommandPattern() {
        final FourTupleArray command = new FourTupleArray("Command");
        command.add(new FourTuple("Client", "Receiver", FT_constants.ASSOCIATION_DIRECTED));
        command.add(new FourTuple("Client", "ConcreteCommand", FT_constants.DEPENDENCY));
        command.add(new FourTuple("ConcreteCommand", "Receiver", FT_constants.ASSOCIATION_DIRECTED));
        command.add(new FourTuple("ConcreteCommand", "Command", FT_constants.INHERITANCE));
        command.add(new FourTuple("Command", "Invoker", FT_constants.AGGREGATE));
        return command;
    }

    private static FourTupleArray createCompositePattern() {
        final FourTupleArray composite = new FourTupleArray("Composite");
        composite.add(new FourTuple("Client", "Component", FT_constants.ASSOCIATION_DIRECTED));
        composite.add(new FourTuple("Leaf", "Component", FT_constants.INHERITANCE));
        composite.add(new FourTuple("Composite", "Component", FT_constants.INHERITANCE));
        composite.add(new FourTuple("Component", "Composite", FT_constants.AGGREGATE));
        return composite;
    }

    private static FourTupleArray createDecoratorPattern() {
        final FourTupleArray decorator = new FourTupleArray("Decorator");
        decorator.add(new FourTuple("ConcreteComponent", "Component", FT_constants.INHERITANCE));
        decorator.add(new FourTuple("Decorator", "Component", FT_constants.INHERITANCE));
        decorator.add(new FourTuple("Component", "Decorator", FT_constants.AGGREGATE));
        decorator.add(new FourTuple("ConcreteDecorator", "Decorator", FT_constants.INHERITANCE_MULTI));
        return decorator;
    }

    private static FourTupleArray createFactoryMethodPattern() {
        final FourTupleArray factoryMethod = new FourTupleArray("Factory Method");
        factoryMethod.add(new FourTuple("Product", "ConcreteProduct", FT_constants.INHERITANCE));
        factoryMethod.add(new FourTuple("ConcreteCreator", "Product", FT_constants.DEPENDENCY));
        factoryMethod.add(new FourTuple("ConcreteCreator", "Creator", FT_constants.INHERITANCE));
        return factoryMethod;
    }

    /**
     * Creates a flyweight pattern. Checked with the UML diagram in [GoF].
     *
     * @return a {@link FourTupleArray} representing a flyweight pattern
     */
    private static FourTupleArray createFlyweightPattern() {
        final FourTupleArray flyweight = new FourTupleArray("Flyweight");
        flyweight.add(new FourTuple("Client", "FlyweightFactory", FT_constants.ASSOCIATION_DIRECTED));
        flyweight.add(new FourTuple("Client", "ConcreteFlyweight", FT_constants.ASSOCIATION_DIRECTED));
        flyweight.add(new FourTuple("Client", "UnsharedConcreteFlyweight", FT_constants.ASSOCIATION_DIRECTED));
        flyweight.add(new FourTuple("Flyweight", "FlyweightFactory", FT_constants.AGGREGATE));
        flyweight.add(new FourTuple("ConcreteFlyweight", "Flyweight", FT_constants.INHERITANCE));
        flyweight.add(new FourTuple("UnsharedConcreteFlyweight", "Flyweight", FT_constants.INHERITANCE));
        return flyweight;
    }

    private static FourTupleArray createIteratorPattern() {
        final FourTupleArray iterator = new FourTupleArray("Iterator");
        iterator.add(new FourTuple("ConcreteAggregate", "Aggregate", FT_constants.INHERITANCE));
        iterator.add(new FourTuple("Client", "Aggregate", FT_constants.ASSOCIATION_DIRECTED));
        iterator.add(new FourTuple("Client", "Iterator", FT_constants.ASSOCIATION_DIRECTED));
        iterator.add(new FourTuple("ConcreteIterator", "Iterator", FT_constants.INHERITANCE));
        iterator.add(new FourTuple("ConcreteIterator", "ConcreteAggregate", FT_constants.ASSOCIATION_DIRECTED));
        iterator.add(new FourTuple("ConcreteAggregate", "ConcreteIterator", FT_constants.DEPENDENCY));
        return iterator;
    }

    private static FourTupleArray createMediatorPattern() {
        final FourTupleArray mediator = new FourTupleArray("Mediator");
        mediator.add(new FourTuple("ConcreteMediator", "Mediator", FT_constants.INHERITANCE));
        mediator.add(new FourTuple("Colleague", "Mediator", FT_constants.ASSOCIATION_DIRECTED));
        mediator.add(new FourTuple("ConcreteColleague1", "Colleague", FT_constants.INHERITANCE));
        mediator.add(new FourTuple("ConcreteColleague2", "Colleague", FT_constants.INHERITANCE));
        mediator.add(new FourTuple("ConcreteMediator", "ConcreteColleague1", FT_constants.ASSOCIATION_DIRECTED));
        mediator.add(new FourTuple("ConcreteMediator", "ConcreteColleague2", FT_constants.ASSOCIATION_DIRECTED));
        return mediator;
    }

    private static FourTupleArray createMementoPattern() {
        final FourTupleArray memento = new FourTupleArray("Memento");
        memento.add(new FourTuple("Memento", "Caretaker", FT_constants.AGGREGATE));
        memento.add(new FourTuple("Originator", "Memento", FT_constants.DEPENDENCY));
        return memento;
    }

    private static FourTupleArray createObserverPattern() {
        final FourTupleArray observer = new FourTupleArray("Observer");
        observer.add(new FourTuple("ConcreteSubject", "Subject", FT_constants.INHERITANCE));
        observer.add(new FourTuple("Subject", "Observer", FT_constants.AGGREGATE));
        observer.add(new FourTuple("ConcreteObserver", "Observer", FT_constants.INHERITANCE));
        observer.add(new FourTuple("ConcreteObserver", "ConcreteSubject", FT_constants.ASSOCIATION_DIRECTED));
        return observer;
    }

    private static FourTupleArray createProxyPattern() {
        final FourTupleArray proxy = new FourTupleArray("Proxy");
        proxy.add(new FourTuple("Client", "Subject", FT_constants.ASSOCIATION_DIRECTED));
        proxy.add(new FourTuple("Proxy", "Subject", FT_constants.INHERITANCE));
        proxy.add(new FourTuple("RealSubject", "Subject", FT_constants.INHERITANCE));
        proxy.add(new FourTuple("Proxy", "RealSubject", FT_constants.ASSOCIATION_DIRECTED));
        return proxy;
    }

    private static FourTupleArray createStateStrategyPattern() {
        final FourTupleArray strategy = new FourTupleArray("State - Strategy");
        strategy.add(new FourTuple("Strategy", "Context", FT_constants.AGGREGATE));
        strategy.add(new FourTuple("ConcreteStrategy", "Strategy", FT_constants.INHERITANCE_MULTI));
        return strategy;
    }

    /**
     * Creates a bridge pattern. Checked with the UML diagram in [GoF]
     *
     * @return a {@link FourTupleArray} representing a bridge pattern
     */
    private static FourTupleArray createBridgePattern() {
        final FourTupleArray bridge = new FourTupleArray("Bridge");
        bridge.add(new FourTuple("Client", "Abstraction", FT_constants.ASSOCIATION));
        bridge.add(new FourTuple("Implementor", "Abstraction", FT_constants.AGGREGATE));
        bridge.add(new FourTuple("ConcreteImplementorA", "Implementor", FT_constants.INHERITANCE));
        bridge.add(new FourTuple("ConcreteImplementorB", "Implementor", FT_constants.INHERITANCE));
        bridge.add(new FourTuple("RefinedAbstraction", "Abstraction", FT_constants.INHERITANCE));
        return bridge;
    }
}
