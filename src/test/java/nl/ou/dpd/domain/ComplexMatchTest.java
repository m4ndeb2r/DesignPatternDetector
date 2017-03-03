package nl.ou.dpd.domain;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Tests the {@link Matcher} class for a complex systeem.
 *
 * @author Martin de Boer
 */
public class ComplexMatchTest {

    // Template containing GoF design patterns
    private List<DesignPattern> dpsTemplates;

    // A matcher doing the patter matching
    private Matcher matcher;

    /**
     * Initialize the test(s).
     */
    @Before
    public void setUp() {
        dpsTemplates = TestHelper.createDesignPatternsTemplates();
        matcher = new Matcher();
    }

    /**
     * Tests the {@link Matcher#match(DesignPattern, SystemUnderConsideration, int)} method. We set up a complex
     * "System under consideration", containing many patterns. We then analyse this with a template containing GoF
     * design patterns. Finally we check if the expected patterns are detected (TODO).
     * <p>
     * TODO: currently this test is always successful. We cannot check the output (yet), since it is printed to System.out.
     */
    @Test
    public void testMatch() {
        final SystemUnderConsideration system = TestHelper.createComplexSystemUnderConsideration();
        dpsTemplates.forEach(pattern -> matcher.match(pattern, system, 1));
    }

}
