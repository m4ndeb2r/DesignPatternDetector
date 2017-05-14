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
public class ObserverMatcherTest {

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
     * Tests if the observer pattern is detected with no missing edge allowed.
     */
    @Test
    public void testMatch() {
        final DesignPattern pattern = TestHelper.createObserverPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(3));

        final Solution s0 = solutions.get(0);
        final Solution s1 = solutions.get(1);
        final Solution s2 = solutions.get(2);

        final MatchedNodes mc0 = s0.getMatchedNodes();
        final MatchedNodes mc1 = s1.getMatchedNodes();
        final MatchedNodes mc2 = s2.getMatchedNodes();

        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> se1 = s1.getSuperfluousEdges();
        final Set<Edge> se2 = s2.getSuperfluousEdges();

        final Set<Edge> me0 = s0.getMissingEdges();
        final Set<Edge> me1 = s1.getMissingEdges();
        final Set<Edge> me2 = s2.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Observer"));
        assertThat(s1.getDesignPatternName(), is("Observer"));
        assertThat(s2.getDesignPatternName(), is("Observer"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("Subject", "Subject")).getName(), is("Subject"));
        assertThat(mc0.get(new Clazz("Data", "Data")).getName(), is("ConcreteSubject"));
        assertThat(mc0.get(new Clazz("Observer", "Observer")).getName(), is("Observer"));
        assertThat(mc0.get(new Clazz("TableView", "TableView")).getName(), is("ConcreteObserver"));

        assertThat(mc1.get(new Clazz("Subject", "Subject")).getName(), is("Subject"));
        assertThat(mc1.get(new Clazz("Data", "Data")).getName(), is("ConcreteSubject"));
        assertThat(mc1.get(new Clazz("Observer", "Observer")).getName(), is("Observer"));
        assertThat(mc1.get(new Clazz("BarGraphView", "BarGraphView")).getName(), is("ConcreteObserver"));

        assertThat(mc2.get(new Clazz("Subject", "Subject")).getName(), is("Subject"));
        assertThat(mc2.get(new Clazz("Data", "Data")).getName(), is("ConcreteSubject"));
        assertThat(mc2.get(new Clazz("Observer", "Observer")).getName(), is("Observer"));
        assertThat(mc2.get(new Clazz("PieGraphView", "PieGraphView")).getName(), is("ConcreteObserver"));

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
        result.add(new Edge(new Clazz("Subject", "Subject"), new Clazz("Observer", "Observer"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("Data", "Data"), new Clazz("Subject", "Subject"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("TableView", "TableView"), new Clazz("Observer", "Observer"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("TableView", "TableView"), new Clazz("Data", "Data"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("BarGraphView", "BarGraphView"), new Clazz("Observer", "Observer"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("BarGraphView", "BarGraphView"), new Clazz("Data", "Data"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("PieGraphView", "PieGraphView"), new Clazz("Observer", "Observer"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("PieGraphView", "PieGraphView"), new Clazz("Data", "Data"), EdgeType.ASSOCIATION_DIRECTED));
        return result;
    }

}
