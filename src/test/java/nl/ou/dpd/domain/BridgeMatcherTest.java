package nl.ou.dpd.domain;

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
public class BridgeMatcherTest {

    private Matcher matcher;
    private SystemUnderConsideration system;

    /**
     * Initialises the test(s).
     */
    @Before
    public void initMatcher() {
        matcher = new Matcher();
        system = createSystemUnderConsiderationContainingBridge();
    }

    /**
     * Tests if the bridge pattern is detected with no missing edge allowed.
     */
    @Test
    public void testMatch() {
        final DesignPattern pattern = TestHelper.createBridgePattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedClasses mc0 = s0.getMatchedClasses();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Bridge"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("WindowClient")).getName(), is("Client"));
        assertThat(mc0.get(new Clazz("Window")).getName(), is("Abstraction"));
        assertThat(mc0.get(new Clazz("WindowImp")).getName(), is("Implementor"));
        assertThat(mc0.get(new Clazz("IconWindow")).getName(), is("RefinedAbstraction"));
        assertThat(mc0.get(new Clazz("TransientWindow")).getName(), is("RefinedAbstraction"));
        assertThat(mc0.get(new Clazz("XWindowImp")).getName(), is("ConcreteImplementor"));
        assertThat(mc0.get(new Clazz("PMWindowImp")).getName(), is("ConcreteImplementor"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(0));
    }

    private SystemUnderConsideration createSystemUnderConsiderationContainingBridge() {
        SystemUnderConsideration result = new SystemUnderConsideration();
        result.add(new Edge(new Clazz("WindowClient"), new Clazz("Window"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("WindowImp"), new Clazz("Window"), EdgeType.AGGREGATE));
        result.add(new Edge(new Clazz("IconWindow"), new Clazz("Window"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("TransientWindow"), new Clazz("Window"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("XWindowImp"), new Clazz("WindowImp"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("PMWindowImp"), new Clazz("WindowImp"), EdgeType.INHERITANCE));
        return result;
    }

}
