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
public class SolutionsTest {

    private Solutions solutions1;
    private Solution solution1a;
    private Solution solution1b;

    private Solutions solutions2;
    private Solution solution2a;
    private Solution solution2b;

    @Before
    public void initSolution() {
        solution1a = new Solution(createNames("A", "B", "C"));
        solution1b = new Solution(createNames("A", "B", "D"));
        solutions1 = new Solutions();
        solutions1.add(solution1a);
        solutions1.add(solution1b);

        solution2a = new Solution(createNames("A", "B", "X"));
        solution2b = new Solution(createNames("A", "C", "X"));
        solutions2 = new Solutions();
        solutions2.add(solution2a);
        solutions2.add(solution2b);
    }

    @Test
    public void testIsUnique() {
        assertThat(solutions1.isUniq(createNames("A", "B", "C")), is(false));
        assertThat(solutions1.isUniq(createNames("A", "C", "B")), is(false));
        assertThat(solutions1.isUniq(createNames("C", "B", "A")), is(false));
        assertThat(solutions1.isUniq(createNames("C", "A", "B")), is(false));
        assertThat(solutions1.isUniq(createNames("F", "B", "C")), is(true));
        assertThat(solutions1.isUniq(createNames("F", "C", "B")), is(true));
        assertThat(solutions1.isUniq(createNames("C", "B", "F")), is(true));
        assertThat(solutions1.isUniq(createNames("C", "F", "B")), is(true));

        assertThat(solutions2.isUniq(createNames("A", "B", "X")), is(false));
        assertThat(solutions2.isUniq(createNames("A", "X", "B")), is(false));
        assertThat(solutions2.isUniq(createNames("X", "B", "A")), is(false));
        assertThat(solutions2.isUniq(createNames("X", "A", "B")), is(false));
        assertThat(solutions2.isUniq(createNames("F", "B", "C")), is(true));
        assertThat(solutions2.isUniq(createNames("F", "C", "B")), is(true));
        assertThat(solutions2.isUniq(createNames("C", "B", "F")), is(true));
        assertThat(solutions2.isUniq(createNames("C", "F", "B")), is(true));
    }

    private SortedSet<String> createNames(String ... names) {
        SortedSet<String> set = new TreeSet<>();
        for (String name : names) {
            set.add(name);
        }
        return set;
    }

}
