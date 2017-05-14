package nl.ou.dpd.domain;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Clazz;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

/**
 * Tests the class {@link Solution}.
 *
 * @author Martin de Boer
 */
public class SolutionTest {

    private Solution solution1a;
    private Solution solution1b;
    private Solution solution2a;
    private Solution solution2b;

    /**
     * Creates four {@link Solution}s 1a, 1b, 2a, and 2b. The first two have the samen edges, but in a different
     * order, which means they are equal in the context of pattern detection. The same is true for the latter two. But
     * the first and the second differ from the latter two, because they contain different classes.
     */
    @Before
    public void initSolution() {
        solution1a = new Solution(
                "Pattern1a",
                createMatchedClasses_AB_CD_AC(),
                createSuperfluousEdges(),
                createMissingEdges());
        solution1b = new Solution(
                "Pattern1b",
                createMatchedClasses_AC_CD_AB(),
                createSuperfluousEdges(),
                createMissingEdges());
        solution2a = new Solution(
                "Pattern2a",
                createMatchedClasses_WX_WY_YZ(),
                createSuperfluousEdges(),
                createMissingEdges());
        solution2b = new Solution(
                "Pattern2b",
                createMatchedClasses_WX_YZ_WY(),
                createSuperfluousEdges(),
                createMissingEdges());
    }

    /**
     * Test the constructor and getters of the {@link Solution} class.
     */
    @Test
    public void testConstructor() {
        assertThat(solution1a.getDesignPatternName(), is("Pattern1a"));

        assertThat(solution1a.getMissingEdges().size(), is(1));
        final Edge expMissing = new Edge(new Clazz("dpP", "dpP"), new Clazz("dpQ", "dpQ"), EdgeType.AGGREGATE);
        assertTrue(solution1a.getMissingEdges().contains(expMissing));

        assertThat(solution1a.getSuperfluousEdges().size(), is(1));
        final Edge expSuperfluous = new Edge(new Clazz("sysE", "sysE"), new Clazz("sysB", "sysB"), EdgeType.ASSOCIATION_DIRECTED);
        assertTrue(solution1a.getSuperfluousEdges().contains(expSuperfluous));

    }

    /**
     * Tests the {@link Solution#isEqual(Solution)} method. We have the following expectation: solution1a is considered
     * equal to solution 1b, but not equals to solution2a and solution2b; solution2a is considered to be equal to
     * solution2b.
     */
    @Test
    public void testIsEqual() {
        assertThat(solution1a.isEqual(solution1a), is(true));
        assertThat(solution1a.isEqual(solution1b), is(true));
        assertThat(solution2a.isEqual(solution1a), is(false));
        assertThat(solution2b.isEqual(solution1a), is(false));
        assertThat(solution2a.isEqual(solution1b), is(false));
        assertThat(solution2b.isEqual(solution1b), is(false));
        assertThat(solution2a.isEqual(solution2a), is(true));
        assertThat(solution2a.isEqual(solution2b), is(true));
    }

    private Set<Edge> createSuperfluousEdges() {
        Set<Edge> result = new HashSet<>();
        result.add(new Edge(new Clazz("sysE", "sysE"), new Clazz("sysB", "sysB"), EdgeType.ASSOCIATION_DIRECTED));
        return result;
    }

    private Set<Edge> createMissingEdges() {
        Set<Edge> result = new HashSet<>();
        result.add(new Edge(new Clazz("dpP", "dpP"), new Clazz("dpQ", "dpQ"), EdgeType.AGGREGATE));
        return result;
    }

    private MatchedNodes createMatchedClasses_WX_WY_YZ() {
        MatchedNodes result = new MatchedNodes();
        createMatch(result, "W", "X");
        createMatch(result, "W", "Y");
        createMatch(result, "Y", "Z");
        return result;
    }

    private MatchedNodes createMatchedClasses_WX_YZ_WY() {
        MatchedNodes result = new MatchedNodes();
        createMatch(result, "W", "X");
        createMatch(result, "Y", "Z");
        createMatch(result, "W", "Y");
        return result;
    }

    private MatchedNodes createMatchedClasses_AB_CD_AC() {
        MatchedNodes result = new MatchedNodes();
        createMatch(result, "A", "B");
        createMatch(result, "C", "D");
        createMatch(result, "A", "C");
        return result;
    }

    private MatchedNodes createMatchedClasses_AC_CD_AB() {
        MatchedNodes result = new MatchedNodes();
        createMatch(result, "A", "C");
        createMatch(result, "C", "D");
        createMatch(result, "A", "B");
        return result;
    }

    private void createMatch(MatchedNodes matchedNodes, String a, String b) {
        matchedNodes.makeMatch(
                new Edge(new Clazz("sys" + a, "sys" + a), new Clazz("sys" + b, "sys" + b), EdgeType.AGGREGATE),
                new Edge(new Clazz("dp" + a, "dp" + a), new Clazz("dp" + b, "dp" + b), EdgeType.DEPENDENCY)
        );
    }

}
