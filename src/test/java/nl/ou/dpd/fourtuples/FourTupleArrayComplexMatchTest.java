package nl.ou.dpd.fourtuples;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Tests the {@link FourTupleArray} class for a complex systeem.
 *
 * @author Martin de Boer
 */
public class FourTupleArrayComplexMatchTest {


    // Template containing GoF design patterns
    private ArrayList<FourTupleArray> dpsTemplates;

    /**
     * Initialize the test(s).
     */
    @Before
    public void setUp() {
        dpsTemplates = TestHelper.createDesignPatternsTemplates();
    }

    /**
     * Tests the {@link FourTupleArray#match(FourTupleArray, int)} method. We set up a complex "System under
     * consideration", containing many patterns. We then analyse this with a template containing GoF design patterns.
     * Finally we check if the expected patterns are detected (TODO).
     *
     * TODO: currently this test is always successful. We cannot check the output (yet), since it is printed to System.out.
     */
    @Test
    public void testMatch() {
        final FourTupleArray systemUnderConsideration = TestHelper.createComplexSystemUnderConsideration();
        dpsTemplates.forEach(pattern -> pattern.match(systemUnderConsideration, 1));
    }

}
