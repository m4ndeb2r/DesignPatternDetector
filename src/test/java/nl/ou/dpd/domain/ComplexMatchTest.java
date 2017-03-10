package nl.ou.dpd.domain;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


/**
 * Tests the {@link Matcher} class for a complex systeem.
 * <p>
 * TODO: This test detects a tremendous number of patterns. We must rethink this one before implementing it in-depth.
 *
 * @author Martin de Boer
 */
@Ignore("Is this useful for a unittest? E. van Doorn used this for performance testing.")
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
     */
    @Test
    public void testMatch() {
        final SystemUnderConsideration system = TestHelper.createComplexSystemUnderConsideration();
        final List<Solutions> solutionsList = new ArrayList<>();

        dpsTemplates.forEach(pattern -> solutionsList.add(matcher.match(pattern, system, 1)));

        assertThat(solutionsList.size(), is(17));
    }

}
