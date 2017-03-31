package nl.ou.dpd.domain;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link Matcher} class handling empty system or design pattern input.
 *
 * @author Martin de Boer
 */
public class EmptyMatcherTest {

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
     * Test if the {@link Matcher} handles an empty system under consideration well.
     */
    @Test
    public void testEmptySystem() {
        final DesignPattern pattern = TestHelper.createBridgePattern();
        final SystemUnderConsideration system = new SystemUnderConsideration();
        final Solutions solutions = matcher.match(pattern, system, 1);
        assertThat(solutions.getSolutions().size(), is(0));
    }

    /**
     * Test if the {@link Matcher} handles a pattern with no edges (no match) well.
     */
    @Test
    public void testNoMatchingPattern() {
        final DesignPattern pattern = new DesignPattern("NonMatchingPattern");
        final Solutions matchResult = matcher.match(pattern, system, 1);
        assertThat(matchResult.getSolutions().size(), is(0));
    }

    private SystemUnderConsideration createSystemUnderConsideration() {
        SystemUnderConsideration result = new SystemUnderConsideration();
        result.add(new Edge(new Clazz("D"), new Clazz("E"), EdgeType.DEPENDENCY));
        result.add(new Edge(new Clazz("E"), new Clazz("C"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("D"), new Clazz("B"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("C"), new Clazz("B"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("A"), new Clazz("B"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("A"), new Clazz("C"), EdgeType.ASSOCIATION_DIRECTED));
        return result;
    }

}
