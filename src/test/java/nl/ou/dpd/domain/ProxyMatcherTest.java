package nl.ou.dpd.domain;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
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
public class ProxyMatcherTest {

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
     * Tests if the proxy pattern is detected with no missing edge allowed.
     */
    @Test
    public void testMatch() {
        final DesignPattern pattern = TestHelper.createProxyPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedClasses mc0 = s0.getMatchedClasses();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Proxy"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("DocumentEditor")).getName(), is("Client"));
        assertThat(mc0.get(new Clazz("Graphic")).getName(), is("Subject"));
        assertThat(mc0.get(new Clazz("Image")).getName(), is("RealSubject"));
        assertThat(mc0.get(new Clazz("ImageProxy")).getName(), is("Proxy"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(0));
    }

    private SystemUnderConsideration createSystemUnderConsideration() {
        SystemUnderConsideration result = new SystemUnderConsideration();
        result.add(new Edge(new Clazz("DocumentEditor"), new Clazz("Graphic"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("Image"), new Clazz("Graphic"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("ImageProxy"), new Clazz("Graphic"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("ImageProxy"), new Clazz("Image"), EdgeType.ASSOCIATION_DIRECTED));
        return result;
    }

}
