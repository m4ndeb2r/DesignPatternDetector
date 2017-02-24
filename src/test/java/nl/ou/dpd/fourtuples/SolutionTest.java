package nl.ou.dpd.fourtuples;

import org.junit.Before;
import org.junit.Test;

import java.util.SortedSet;
import java.util.TreeSet;

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

    @Before
    public void initSolution() {
        solution1a = new Solution(createNames("A", "B", "C"));
        solution1b = new Solution(createNames("A", "C", "B"));
        solution2a = new Solution(createNames("A", "B", "X"));
        solution2b = new Solution(createNames("A", "X", "B"));
    }

    private SortedSet<String> createNames(String ... names) {
        SortedSet<String> set = new TreeSet<>();
        for (String name : names) {
            set.add(name);
        }
        return set;
    }

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
}
