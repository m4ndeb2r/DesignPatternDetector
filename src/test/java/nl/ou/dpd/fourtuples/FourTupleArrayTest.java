package nl.ou.dpd.fourtuples;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the class {@link FourTupleArray}.
 *
 * @author Martin de Boer
 */
public class FourTupleArrayTest {

    private FourTupleArray fta = createBaBrahemExampleSystem();

    /**
     * Tests the {@link FourTupleArray#match(FourTupleArray, int)} method.
     *
     * TODO: currently this test is alway successful. We cannot check the output (yet), since it is printed to System.out.
     */
    @Test
    public void testMatch() {
        final FourTupleArray pattern = TestHelper.createPrototypePattern();
        pattern.match(fta, 0);
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
}
