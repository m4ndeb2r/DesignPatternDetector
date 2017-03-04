package nl.ou.dpd.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

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
        solution1a = new Solution("Pattern1a", createMatchedClasses_AB_CD_AC(), createSuperfluousEdges());
        solution1b = new Solution("Pattern1b", createMatchedClasses_AC_CD_AB(), createSuperfluousEdges());
        solution2a = new Solution("Pattern2a", createMatchedClasses_WX_WY_YZ(), createSuperfluousEdges());
        solution2b = new Solution("Pattern2b", createMatchedClasses_WX_YZ_WY(), createSuperfluousEdges());
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

    private List<Edge> createSuperfluousEdges() {
        List<Edge> result = new ArrayList<>();
        result.add(new Edge(new Clazz("sysE"), new Clazz("sysB"), EdgeType.ASSOCIATION_DIRECTED));
        return result;
    }

    private MatchedClasses createMatchedClasses_WX_WY_YZ() {
        MatchedClasses result = new MatchedClasses();
        createMatch(result, "W", "X");
        createMatch(result, "W", "Y");
        createMatch(result, "Y", "Z");
        return result;
    }

    private MatchedClasses createMatchedClasses_WX_YZ_WY() {
        MatchedClasses result = new MatchedClasses();
        createMatch(result, "W", "X");
        createMatch(result, "Y", "Z");
        createMatch(result, "W", "Y");
        return result;
    }

    private MatchedClasses createMatchedClasses_AB_CD_AC() {
        MatchedClasses result = new MatchedClasses();
        createMatch(result, "A", "B");
        createMatch(result, "C", "D");
        createMatch(result, "A", "C");
        return result;
    }

    private MatchedClasses createMatchedClasses_AC_CD_AB() {
        MatchedClasses result = new MatchedClasses();
        createMatch(result, "A", "C");
        createMatch(result, "C", "D");
        createMatch(result, "A", "B");
        return result;
    }

    private void createMatch(MatchedClasses matchedClasses, String a, String b) {
        matchedClasses.makeMatch(
                new Edge(new Clazz("sys" + a), new Clazz("sys" + b), EdgeType.AGGREGATE),
                new Edge(new Clazz("dp" + a), new Clazz("dp" + b), EdgeType.DEPENDENCY)
        );
    }

}
