package nl.ou.dpd.domain;

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
        solution1a = new Solution(createClasses("A", "B", "C"));
        solution1b = new Solution(createClasses("A", "B", "D"));
        solutions1 = new Solutions();
        solutions1.add(solution1a);
        solutions1.add(solution1b);

        solution2a = new Solution(createClasses("A", "B", "X"));
        solution2b = new Solution(createClasses("A", "C", "X"));
        solutions2 = new Solutions();
        solutions2.add(solution2a);
        solutions2.add(solution2b);
    }

    @Test
    public void testIsUnique() {
        assertThat(solutions1.isUniq(createClasses("A", "B", "C")), is(false));
        assertThat(solutions1.isUniq(createClasses("A", "C", "B")), is(false));
        assertThat(solutions1.isUniq(createClasses("C", "B", "A")), is(false));
        assertThat(solutions1.isUniq(createClasses("C", "A", "B")), is(false));
        assertThat(solutions1.isUniq(createClasses("F", "B", "C")), is(true));
        assertThat(solutions1.isUniq(createClasses("F", "C", "B")), is(true));
        assertThat(solutions1.isUniq(createClasses("C", "B", "F")), is(true));
        assertThat(solutions1.isUniq(createClasses("C", "F", "B")), is(true));

        assertThat(solutions2.isUniq(createClasses("A", "B", "X")), is(false));
        assertThat(solutions2.isUniq(createClasses("A", "X", "B")), is(false));
        assertThat(solutions2.isUniq(createClasses("X", "B", "A")), is(false));
        assertThat(solutions2.isUniq(createClasses("X", "A", "B")), is(false));
        assertThat(solutions2.isUniq(createClasses("F", "B", "C")), is(true));
        assertThat(solutions2.isUniq(createClasses("F", "C", "B")), is(true));
        assertThat(solutions2.isUniq(createClasses("C", "B", "F")), is(true));
        assertThat(solutions2.isUniq(createClasses("C", "F", "B")), is(true));
    }

    private SortedSet<Clazz> createClasses(String... names) {
        SortedSet<Clazz> set = new TreeSet<>();
        for (String name : names) {
            set.add(new DesignPatternClass(name));
        }
        return set;
    }

}
