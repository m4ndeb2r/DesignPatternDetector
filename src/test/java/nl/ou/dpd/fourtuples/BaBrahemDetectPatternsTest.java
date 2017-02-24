package nl.ou.dpd.fourtuples;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Tests the {@link DetectPatterns} class for the BaBrahem-example in Ed van Doorn's article "Design Patterns -
 * Supporting design process by automatically detecting design patterns and giving some feedback". See: Van Doorn
 * (2016), page 22/23.
 *
 * @author Martin de Boer
 */
public class BaBrahemDetectPatternsTest {

    // Test subject
    private DetectPatterns detectPatterns;

    /**
     * Initialises the test(s).
     */
    @Before
    public void setUp() {
        detectPatterns = new DetectPatterns();
    }

    /**
     * Tests de {@link DetectPatterns#detectDP(FourTupleArray, ArrayList, int)} method. We set up a simple "System under
     * consideration", based on the BaBrahem-example in Ed van Doorn's article, and check if the prototype pattern is
     * detected.
     *
     * TODO: currently this test is alway successful. We cannot check the output (yet), since it is printed to System.out.
     */
    @Test
    public void testDetectPrototype() {
        detectPatterns.detectDP(createBaBrahemExampleSystem(), createPrototypeTemplate(), 0);
    }

    /**
     * Tests de {@link DetectPatterns#detectDP(FourTupleArray, ArrayList, int)} method. We set up a simple "System under
     * consideration", based on the BaBrahem-example in Ed van Doorn's article, and check if the bridge pattern is
     * detected.
     *
     * TODO: currently this test is alway successful. We cannot check the output (yet), since it is printed to System.out.
     */
    @Test
    public void testDetectBridge() {
        detectPatterns.detectDP(createBaBrahemExampleSystem(), createBridgeTemplate(), 1);
    }

    private FourTupleArray createBaBrahemExampleSystem() {
        final FourTupleArray system = new FourTupleArray();
        system.add(new FourTuple("A", "B", FT_constants.ASSOCIATION_DIRECTED));
        system.add(new FourTuple("A", "C", FT_constants.ASSOCIATION_DIRECTED));
        system.add(new FourTuple("C", "B", FT_constants.ASSOCIATION_DIRECTED));
        system.add(new FourTuple("D", "B", FT_constants.INHERITANCE));
        system.add(new FourTuple("E", "C", FT_constants.INHERITANCE));
        return system;
    }

    private ArrayList<FourTupleArray> createPrototypeTemplate() {
        final ArrayList<FourTupleArray> dps = new ArrayList<>();
        dps.add(TestHelper.createPrototypePattern());
        return dps;
    }


    private ArrayList<FourTupleArray> createBridgeTemplate() {
        final ArrayList<FourTupleArray> dps = new ArrayList<>();
        dps.add(createBridgePattern());
        return dps;
    }

    /**
     * Note 1: the bridge pattern returned by this method is taken from Ed van Doorn's article, page 23. It deviates
     * from the pattern as described by GoF, in that it only has one ConcreteImplementor (instead of two:
     * ConcreteImplementorA and ConcreteImplementorB).
     *
     * Note 2: The {@link TestHelper#createBridgePattern()} utility method, however, returns the version as described
     * by GoF. Would we have used that version here (the BaBrahem example used by Van Doorn) we would get a different
     * result: no match.
     *
     * @return a {@link FourTupleArray} representing a bridge pattern, as used by Ed van Doorn in his 2016 article,
     * page 23.
     */
    private FourTupleArray createBridgePattern() {
        final FourTupleArray bridge = new FourTupleArray("Bridge");
        bridge.add(new FourTuple("Client", "Abstraction", FT_constants.ASSOCIATION_DIRECTED));
        bridge.add(new FourTuple("Implementor", "Abstraction", FT_constants.AGGREGATE));
        bridge.add(new FourTuple("ConcreteImplementor", "Implementor", FT_constants.INHERITANCE));
        bridge.add(new FourTuple("RefinedAbstraction", "Abstraction", FT_constants.INHERITANCE));
        return bridge;
    }
}

