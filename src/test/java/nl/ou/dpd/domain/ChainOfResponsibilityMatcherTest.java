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
 * <p>
 * TODO:
 * The example in GoF on page 240/241 is not properly detected. The application is not able to skip the abstract
 * Widget and detect two implementation: Dialog and Button. This unittest focusses on a somewhat simplified
 * example to make it succeed. This is something to be fixed in the application. FIXME *
 *
 * @author Martin de Boer
 */
public class ChainOfResponsibilityMatcherTest {

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
     * Tests if the chain of responsibility pattern is detected with no missing edge allowed.
     */
    @Test
    public void testMatch() {
        final DesignPattern pattern = TestHelper.createChainOfResponsibilityPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("ChainOfResponsibility"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("HelpClient", "HelpClient")).getName(), is("Client"));
        assertThat(mc0.get(new Clazz("HelpHandler", "HelpHandler")).getName(), is("Handler"));
        assertThat(mc0.get(new Clazz("Application", "Application")).getName(), is("ConcreteHandler"));
        assertThat(mc0.get(new Clazz("Widget", "Widget")).getName(), is("ConcreteHandler"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(0));
    }

    private SystemUnderConsideration createSystemUnderConsideration() {
        SystemUnderConsideration result = new SystemUnderConsideration();
        result.add(new Edge(new Clazz("HelpClient", "HelpClient"), new Clazz("HelpHandler", "HelpHandler"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("HelpHandler", "HelpHandler"), new Clazz("HelpHandler", "HelpHandler"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("Application", "Application"), new Clazz("HelpHandler", "HelpHandler"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("Widget", "Widget"), new Clazz("HelpHandler", "HelpHandler"), EdgeType.INHERITANCE));
        return result;
    }

}
