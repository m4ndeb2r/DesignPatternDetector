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
     * I would expect one single pattern to be detected, but I get 3. One for every COncreteBuilder. Can we fix this
     * with an INHERITANCE_MULTI instead of an INHERITANCE? After trying that options in the pattern template, we see
     * another bug coming to the surface: we STILL get 3 patterns: one for each DEPENDENCY relation (product) of the
     * (multiple) concrete builders! This should be fixed. FIXME.
     */
    @Test
    public void testMatch() {
        final DesignPattern pattern = TestHelper.createBuilderPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(3));

        final Solution s0 = solutions.get(0);
        final Solution s1 = solutions.get(1);
        final Solution s2 = solutions.get(2);

        final MatchedClasses mc0 = s0.getMatchedClasses();
        final MatchedClasses mc1 = s1.getMatchedClasses();
        final MatchedClasses mc2 = s2.getMatchedClasses();

        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> se1 = s1.getSuperfluousEdges();
        final Set<Edge> se2 = s2.getSuperfluousEdges();

        final Set<Edge> me0 = s0.getMissingEdges();
        final Set<Edge> me1 = s1.getMissingEdges();
        final Set<Edge> me2 = s2.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Builder"));
        assertThat(s1.getDesignPatternName(), is("Builder"));
        assertThat(s2.getDesignPatternName(), is("Builder"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("RTFReader")).getName(), is("Director"));
        assertThat(mc0.get(new Clazz("TextConverter")).getName(), is("Builder"));
        assertThat(mc0.get(new Clazz("ASCIIConverter")).getName(), is("ConcreteBuilder"));
        assertThat(mc0.get(new Clazz("ASCIIText")).getName(), is("Product"));

        assertThat(mc1.get(new Clazz("RTFReader")).getName(), is("Director"));
        assertThat(mc1.get(new Clazz("TextConverter")).getName(), is("Builder"));
        assertThat(mc1.get(new Clazz("TeXConverter")).getName(), is("ConcreteBuilder"));
        assertThat(mc1.get(new Clazz("TeXText")).getName(), is("Product"));

        assertThat(mc2.get(new Clazz("RTFReader")).getName(), is("Director"));
        assertThat(mc2.get(new Clazz("TextConverter")).getName(), is("Builder"));
        assertThat(mc2.get(new Clazz("TextWidgetConverter")).getName(), is("ConcreteBuilder"));
        assertThat(mc2.get(new Clazz("TextWidgetText")).getName(), is("Product"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));
        assertThat(se1.size(), is(0));
        assertThat(se2.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(0));
        assertThat(me1.size(), is(0));
        assertThat(me2.size(), is(0));
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
