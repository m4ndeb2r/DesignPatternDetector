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

/**
 * Tests the {@link Matcher} class.
 *
 * @author Martin de Boer
 */
public class CompositeMatcherTest {

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
     * Tests if the composite pattern is detected with no missing edge allowed.
     */
    @Test
    public void testMatch() {
        final DesignPattern pattern = TestHelper.createCompositePattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Composite"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("GraphicClient", "GraphicClient")).getName(), is("Client"));
        assertThat(mc0.get(new Clazz("Graphic", "Graphic")).getName(), is("Component"));
        assertThat(mc0.get(new Clazz("Rectangle", "Rectangle")).getName(), is("Leaf"));
        assertThat(mc0.get(new Clazz("Picture", "Picture")).getName(), is("Composite"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(0));
    }

    private SystemUnderConsideration createSystemUnderConsideration() {
        SystemUnderConsideration result = new SystemUnderConsideration();
        result.add(new Edge(new Clazz("GraphicClient", "GraphicClient"), new Clazz("Graphic", "Graphic"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("Rectangle", "Rectangle"), new Clazz("Graphic", "Graphic"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("Picture", "Picture"), new Clazz("Graphic", "Graphic"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("Graphic", "Graphic"), new Clazz("Picture", "Picture"), EdgeType.AGGREGATE));
        return result;
    }

}
