package nl.ou.dpd.domain;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link Matcher} class.
 *
 * @author Martin de Boer
 */
public class FactoryMethodMatcherTest {

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
     * Tests if the factory method pattern is detected with no missing edge allowed.
     */
    @Test
    public void testMatch() {
        final DesignPattern pattern = TestHelper.createFactoryMethodPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Factory Method"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("Application", "Application")).getName(), is("Creator"));
        assertThat(mc0.get(new Clazz("Document", "Document")).getName(), is("Product"));
        assertThat(mc0.get(new Clazz("MyDocument", "MyDocument")).getName(), is("ConcreteProduct"));
        assertThat(mc0.get(new Clazz("MyApplication", "MyApplication")).getName(), is("ConcreteCreator"));

        // Check superfluous edges
        assertThat(se0.size(), is(1));
        assertTrue(se0.contains(new Edge(new Clazz("Document", "Document"), new Clazz("Application", "Application"), EdgeType.AGGREGATE)));

        // Check missing edges
        assertThat(me0.size(), is(0));
    }

    private SystemUnderConsideration createSystemUnderConsideration() {
        SystemUnderConsideration result = new SystemUnderConsideration();
        result.add(new Edge(new Clazz("Document", "Document"), new Clazz("Application", "Application"), EdgeType.AGGREGATE));
        result.add(new Edge(new Clazz("MyDocument", "MyDocument"), new Clazz("Document", "Document"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("MyApplication", "MyApplication"), new Clazz("Application", "Application"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("MyApplication", "MyApplication"), new Clazz("MyDocument", "MyDocument"), EdgeType.DEPENDENCY));
        return result;
    }

}
