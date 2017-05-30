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
public class IteratorMatcherTest {

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
     * Tests if the iterator pattern is detected with no missing edge allowed.
     */
    @Test
    public void testMatch() {
/*        final DesignPattern pattern = TestHelper.createIteratorPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(2));

        final Solution s0 = solutions.get(0);
        final Solution s1 = solutions.get(1);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final MatchedNodes mc1 = s1.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> se1 = s1.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();
        final Set<Edge> me1 = s1.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Iterator"));
        assertThat(s1.getDesignPatternName(), is("Iterator"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("Client", "Client")).getName(), is("Client"));
        assertThat(mc0.get(new Clazz("AbstractList", "AbstractList")).getName(), is("Aggregate"));
        assertThat(mc0.get(new Clazz("Iterator", "Iterator")).getName(), is("Iterator"));
        assertThat(mc0.get(new Clazz("List", "List")).getName(), is("ConcreteAggregate"));
        assertThat(mc0.get(new Clazz("ListIterator", "ListIterator")).getName(), is("ConcreteIterator"));

        assertThat(mc1.get(new Clazz("Client", "Client")).getName(), is("Client"));
        assertThat(mc1.get(new Clazz("AbstractList", "AbstractList")).getName(), is("Aggregate"));
        assertThat(mc1.get(new Clazz("Iterator", "Iterator")).getName(), is("Iterator"));
        assertThat(mc1.get(new Clazz("SkipList", "SkipList")).getName(), is("ConcreteAggregate"));
        assertThat(mc1.get(new Clazz("SkipListIterator", "SkipListIterator")).getName(), is("ConcreteIterator"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));
        assertThat(se1.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(0));
        assertThat(me1.size(), is(0));
 */   }

    private SystemUnderConsideration createSystemUnderConsideration() {
        SystemUnderConsideration result = new SystemUnderConsideration();
        result.add(new Edge(new Clazz("Client", "Client"), new Clazz("AbstractList", "AbstractList"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("Client", "Client"), new Clazz("Iterator", "Iterator"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("List", "List"), new Clazz("AbstractList", "AbstractList"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("SkipList", "SkipList"), new Clazz("AbstractList", "AbstractList"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("ListIterator", "ListIterator"), new Clazz("Iterator", "Iterator"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("SkipListIterator", "SkipListIterator"), new Clazz("Iterator", "Iterator"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("List", "List"), new Clazz("ListIterator", "ListIterator"), EdgeType.DEPENDENCY));
        result.add(new Edge(new Clazz("ListIterator", "ListIterator"), new Clazz("List", "List"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("SkipList", "SkipList"), new Clazz("SkipListIterator", "SkipListIterator"), EdgeType.DEPENDENCY));
        result.add(new Edge(new Clazz("SkipListIterator", "SkipListIterator"), new Clazz("SkipList", "SkipList"), EdgeType.ASSOCIATION_DIRECTED));
        return result;
    }

}
