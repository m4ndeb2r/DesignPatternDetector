package nl.ou.dpd.fourtuples;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Tests the {@link DetectPatterns} class for the BaBrahem-example in Ed van Doorn's article. See: Van Doorn (2016),
 * page 22.
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
        dps.add(createPrototypePattern());
        return dps;
    }

    private FourTupleArray createPrototypePattern() {
        final FourTupleArray prototype = new FourTupleArray("Prototype");
        prototype.add(new FourTuple("P", "Q", FT_constants.ASSOCIATION_DIRECTED));
        prototype.add(new FourTuple("R", "Q", FT_constants.INHERITANCE));
        return prototype;
    }

    private ArrayList<FourTupleArray> createBridgeTemplate() {
        final ArrayList<FourTupleArray> dps = new ArrayList<>();
        dps.add(createBridgePattern());
        return dps;
    }

    private FourTupleArray createBridgePattern() {
        final FourTupleArray bridge = new FourTupleArray("Bridge");
        bridge.add(new FourTuple("Client", "Abstraction", FT_constants.ASSOCIATION_DIRECTED));
        bridge.add(new FourTuple("Implementor", "Abstraction", FT_constants.AGGREGATE));
        bridge.add(new FourTuple("ConcreteImplementor", "Implementor", FT_constants.INHERITANCE));
        bridge.add(new FourTuple("RefinedAbstraction", "Abstraction", FT_constants.INHERITANCE));
        return bridge;

    }
}

