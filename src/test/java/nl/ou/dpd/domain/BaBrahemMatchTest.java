package nl.ou.dpd.domain;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link Matcher} class for the BaBrahem-example in Ed van Doorn's article "Design Patterns -
 * Supporting design process by automatically detecting design patterns and giving some feedback". See: Van Doorn
 * (2016), page 22/23.
 *
 * @author Martin de Boer
 */
public class BaBrahemMatchTest {

    private SystemUnderConsideration system;
    private Matcher matcher;

    /**
     * Initialises the test(s).
     */
    @Before
    public void setUp() {
        system = createBaBrahemExample();
        matcher = new Matcher();
    }

    /**
     * Tests de {@link Matcher#match(DesignPattern, SystemUnderConsideration, int)} method. We have set up a simple
     * "System under consideration", based on the BaBrahem-example in Ed van Doorn's article, and will check here if
     * the prototype pattern is detected.
     * <p>
     */
    @Test
    public void testDetectPrototype() {
        final Solutions solutions = matcher.match(TestHelper.createPrototypePattern(), createBaBrahemExample(), 0);
        // TODO: test solutions
    }

    /**
     * Tests de {@link Matcher#match(DesignPattern, SystemUnderConsideration, int)} method. We have set up a simple
     * "System under consideration", based on the BaBrahem-example in Ed van Doorn's article, and will check here if the
     * bridge pattern is detected.
     * <p>
     */
    @Test
    public void testDetectBridge() {
        final Solutions solutions = matcher.match(createBridgePattern(), createBaBrahemExample(), 1);
        // TODO: test solutions
    }

    private SystemUnderConsideration createBaBrahemExample() {
        final SystemUnderConsideration system = new SystemUnderConsideration();
        system.add(TestHelper.createEdge("A", "B", EdgeType.ASSOCIATION_DIRECTED));
        system.add(TestHelper.createEdge("A", "C", EdgeType.ASSOCIATION_DIRECTED));
        system.add(TestHelper.createEdge("C", "B", EdgeType.ASSOCIATION_DIRECTED));
        system.add(TestHelper.createEdge("D", "B", EdgeType.INHERITANCE));
        system.add(TestHelper.createEdge("E", "C", EdgeType.INHERITANCE));
        return system;
    }

    /**
     * Note 1: the bridge pattern returned by this method is taken from Ed van Doorn's article, page 23. It deviates
     * from the pattern as described by GoF, in that it only has one ConcreteImplementor (instead of two:
     * ConcreteImplementorA and ConcreteImplementorB).
     * <p>
     * Note 2: The {@link TestHelper#createBridgePattern()} utility method, however, returns the version as described
     * by GoF. Would we have used that version here (the BaBrahem example used by Van Doorn) we would get a different
     * result: no match.
     *
     * @return a {@link Edges} representing a bridge pattern, as used by Ed van Doorn in his 2016 article,
     * page 23.
     */
    private DesignPattern createBridgePattern() {
        final DesignPattern bridge = new DesignPattern("Bridge");
        bridge.add(TestHelper.createEdge("Client", "Abstraction", EdgeType.ASSOCIATION_DIRECTED));
        bridge.add(TestHelper.createEdge("Implementor", "Abstraction", EdgeType.AGGREGATE));
        bridge.add(TestHelper.createEdge("ConcreteImplementor", "Implementor", EdgeType.INHERITANCE));
        bridge.add(TestHelper.createEdge("RefinedAbstraction", "Abstraction", EdgeType.INHERITANCE));
        return bridge;
    }

}

