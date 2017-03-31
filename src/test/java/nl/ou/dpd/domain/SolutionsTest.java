package nl.ou.dpd.domain;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Clazz;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the class {@link Solution}.
 *
 * @author Martin de Boer
 */
public class SolutionsTest {

    private Solutions solutions1;
    private Solution solution1a;
    private Solution solution1b;

    private Solutions solutions2;
    private Solution solution2a;
    private Solution solution2b;

    /**
     * Initialise the test(s).
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

        solutions1 = new Solutions();
        solutions1.add(solution1a);

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

        solutions2 = new Solutions();
        solutions2.add(solution2a);
    }

    /**
     * Tests the {@link Solutions#isUniq(Solution)} method.
     */
    @Test
    public void testIsUnique() {
        assertFalse(solutions1.isUniq(solution1b));
        assertTrue(solutions1.isUniq(solution2a));
        assertTrue(solutions1.isUniq(solution2b));

        assertFalse(solutions2.isUniq(solution2b));
        assertTrue(solutions2.isUniq(solution1a));
        assertTrue(solutions2.isUniq(solution1b));

        solutions1.add(solution2a);
        assertFalse(solutions1.isUniq(solution2a));
        assertFalse(solutions1.isUniq(solution2b));

        solutions2.add(solution1a);
        assertFalse(solutions2.isUniq(solution1a));
        assertFalse(solutions2.isUniq(solution1b));
    }

    private Set<Edge> createSuperfluousEdges() {
        Set<Edge> result = new HashSet<>();
        result.add(new Edge(new Clazz("sysE"), new Clazz("sysB"), EdgeType.ASSOCIATION_DIRECTED));
        return result;
    }

    private Set<Edge> createMissingEdges() {
        Set<Edge> result = new HashSet<>();
        result.add(new Edge(new Clazz("dpP"), new Clazz("dpQ"), EdgeType.AGGREGATE));
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
                new Edge(new Clazz("sys" + a), new Clazz("sys" + b), EdgeType.AGGREGATE),
                new Edge(new Clazz("dp" + a), new Clazz("dp" + b), EdgeType.DEPENDENCY)
        );
    }

}
