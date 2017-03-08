package nl.ou.dpd.domain;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link Matcher} class.
 *
 * @author Martin de Boer
 */
public class BuilderMatcherTest {

    private Matcher matcher;
    private SystemUnderConsideration system;

    /**
     * Initialises the test(s).
     */
    @Before
    public void initMatcher() {
        matcher = new Matcher();
        system = createSystemUnderConsideration();
    }

    /**
     * Tests if the builder pattern is detected with no missing edge allowed.
     * <p>
     * TODO:
     * Here, a problem occurs. I would expect one single pattern to be detected, but I get 3. This is due to the fact
     * that the pattern is defined incorrect. It should contain an INHERITANCE_MULTI where it has an INHERITANCE.
     * However, after correcting that relation in the pettern template, we see a bug coming to the surface: we STILL
     * get 3 patterns: one for each DEPENDENCY relation (product) of the (multiple) concrete builders! This should be
     * fixed. Until than, this test is @ignored.
     */
    @Ignore("Detects too many instances of the pattern. This was already the case in the initial prototype") // FIXME
    @Test
    public void testMatch() {
        final DesignPattern pattern = TestHelper.createBuilderPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedClasses mc0 = s0.getMatchedClasses();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Builder"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("RTFReader")).getName(), is("Director"));
        assertThat(mc0.get(new Clazz("TextConverter")).getName(), is("Builder"));
        assertThat(mc0.get(new Clazz("ASCIIConverter")).getName(), is("ConcreteBuilder"));
        assertThat(mc0.get(new Clazz("TeXConverter")).getName(), is("ConcreteBuilder"));
        assertThat(mc0.get(new Clazz("TextWidgetConverter")).getName(), is("ConcreteBuilder"));
        assertThat(mc0.get(new Clazz("ASCIIText")).getName(), is("Product"));
        assertThat(mc0.get(new Clazz("TeXText")).getName(), is("Product"));
        assertThat(mc0.get(new Clazz("TextWidgetText")).getName(), is("Product"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(0));
    }

    private SystemUnderConsideration createSystemUnderConsideration() {
        SystemUnderConsideration result = new SystemUnderConsideration();
        result.add(new Edge(new Clazz("TextConverter"), new Clazz("RTFReader"), EdgeType.AGGREGATE));
        result.add(new Edge(new Clazz("ASCIIConverter"), new Clazz("TextConverter"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("TeXConverter"), new Clazz("TextConverter"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("TextWidgetConverter"), new Clazz("TextConverter"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("ASCIIConverter"), new Clazz("ASCIIText"), EdgeType.DEPENDENCY));
        result.add(new Edge(new Clazz("TeXConverter"), new Clazz("TeXText"), EdgeType.DEPENDENCY));
        result.add(new Edge(new Clazz("TextWidgetConverter"), new Clazz("TextWidgetText"), EdgeType.DEPENDENCY));
        return result;
    }

}
