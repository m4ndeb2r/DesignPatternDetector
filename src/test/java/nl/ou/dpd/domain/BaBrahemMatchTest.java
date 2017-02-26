package nl.ou.dpd.domain;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link DesignPattern} class for the BaBrahem-example in Ed van Doorn's article "Design Patterns -
 * Supporting design process by automatically detecting design patterns and giving some feedback". See: Van Doorn
 * (2016), page 22/23.
 *
 * @author Martin de Boer
 */
public class BaBrahemMatchTest {

    private SystemUnderConsideration system;

    /**
     * Initialises the test(s).
     */
    @Before
    public void setUp() {
        system = createBaBrahemExample();
    }

    /**
     * Tests de {@link DesignPattern#match(SystemUnderConsideration, int)} method. We have set up a simple "System under
     * consideration", based on the BaBrahem-example in Ed van Doorn's article, and will check here if the prototype
     * pattern is detected.
     * <p>
     * TODO: currently this test is always successful. We cannot check the output (yet), since it is printed to System.out.
     */
    @Test
    public void testDetectPrototype() {
        TestHelper.createPrototypePattern().match(createBaBrahemExample(), 0);
    }

    /**
     * Tests de {@link DesignPattern#match(SystemUnderConsideration, int)} method. We have set up a simple "System under
     * consideration", based on the BaBrahem-example in Ed van Doorn's article, and will check here if the bridge
     * pattern is detected.
     * <p>
     * TODO: currently this test is always successful. We cannot check the output (yet), since it is printed to System.out.
     */
    @Test
    public void testDetectBridge() {
        createBridgePattern().match(createBaBrahemExample(), 1);
    }

    private SystemUnderConsideration createBaBrahemExample() {
        final SystemUnderConsideration system = new SystemUnderConsideration();
        system.add(new FourTuple("A", "B", EdgeType.ASSOCIATION_DIRECTED));
        system.add(new FourTuple("A", "C", EdgeType.ASSOCIATION_DIRECTED));
        system.add(new FourTuple("C", "B", EdgeType.ASSOCIATION_DIRECTED));
        system.add(new FourTuple("D", "B", EdgeType.INHERITANCE));
        system.add(new FourTuple("E", "C", EdgeType.INHERITANCE));
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
     * @return a {@link FourTupleArray} representing a bridge pattern, as used by Ed van Doorn in his 2016 article,
     * page 23.
     */
    private DesignPattern createBridgePattern() {
        final DesignPattern bridge = new DesignPattern("Bridge");
        bridge.add(new FourTuple("Client", "Abstraction", EdgeType.ASSOCIATION_DIRECTED));
        bridge.add(new FourTuple("Implementor", "Abstraction", EdgeType.AGGREGATE));
        bridge.add(new FourTuple("ConcreteImplementor", "Implementor", EdgeType.INHERITANCE));
        bridge.add(new FourTuple("RefinedAbstraction", "Abstraction", EdgeType.INHERITANCE));
        return bridge;
    }
}

