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
public class BridgeMatcherTest {

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
     * Tests if the bridge pattern is detected with no missing edge allowed.
     */
    @Test
    public void testMatch() {
/*        final DesignPattern pattern = TestHelper.createBridgePattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Bridge"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("WindowClient", "WindowClient")).getName(), is("Client"));
        assertThat(mc0.get(new Clazz("Window", "Window")).getName(), is("Abstraction"));
        assertThat(mc0.get(new Clazz("WindowImp", "WindowImp")).getName(), is("Implementor"));
        assertThat(mc0.get(new Clazz("IconWindow", "IconWindow")).getName(), is("RefinedAbstraction"));
        assertThat(mc0.get(new Clazz("TransientWindow", "TransientWindow")).getName(), is("RefinedAbstraction"));
        assertThat(mc0.get(new Clazz("XWindowImp", "XWindowImp")).getName(), is("ConcreteImplementor"));
        assertThat(mc0.get(new Clazz("PMWindowImp", "PMWindowImp")).getName(), is("ConcreteImplementor"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(0));
 */   }

    private SystemUnderConsideration createSystemUnderConsideration() {
        SystemUnderConsideration result = new SystemUnderConsideration();
        result.add(new Edge(new Clazz("WindowClient", "WindowClient"), new Clazz("Window", "Window"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("WindowImp", "WindowImp"), new Clazz("Window", "Window"), EdgeType.AGGREGATE));
        result.add(new Edge(new Clazz("IconWindow", "IconWindow"), new Clazz("Window", "Window"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("TransientWindow", "TransientWindow"), new Clazz("Window", "Window"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("XWindowImp", "XWindowImp"), new Clazz("WindowImp", "WindowImp"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("PMWindowImp", "PMWindowImp"), new Clazz("WindowImp", "WindowImp"), EdgeType.INHERITANCE));
        return result;
    }

}
