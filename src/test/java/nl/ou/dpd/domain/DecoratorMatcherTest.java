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
public class DecoratorMatcherTest {

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
     * Tests if the decorator pattern is detected with no missing edge allowed.
     */
    @Test
    public void testMatch() {
        final DesignPattern pattern = TestHelper.createDecoratorPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Decorator"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("TextView")).getName(), is("ConcreteComponent"));
        assertThat(mc0.get(new Clazz("Decorator")).getName(), is("Decorator"));
        assertThat(mc0.get(new Clazz("VisualComponent")).getName(), is("Component"));
        assertThat(mc0.get(new Clazz("ScrollDecorator")).getName(), is("ConcreteDecorator"));
        assertThat(mc0.get(new Clazz("BorderDecorator")).getName(), is("ConcreteDecorator"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(0));
    }

    private SystemUnderConsideration createSystemUnderConsideration() {
        SystemUnderConsideration result = new SystemUnderConsideration();
        result.add(new Edge(new Clazz("TextView"), new Clazz("VisualComponent"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("Decorator"), new Clazz("VisualComponent"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("VisualComponent"), new Clazz("Decorator"), EdgeType.AGGREGATE));
        result.add(new Edge(new Clazz("ScrollDecorator"), new Clazz("Decorator"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("BorderDecorator"), new Clazz("Decorator"), EdgeType.INHERITANCE));
        return result;
    }

}
