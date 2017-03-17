package nl.ou.dpd.domain;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link Clazz} class.
 *
 * @author Martin de Boer
 */
public class ClazzTest {

    /**
     * Tests the {@link Clazz#compareTo(Clazz)} method.
     */
    @Test
    public void testCompareName() {
        Clazz zero = new Clazz(null);
        Clazz first = new Clazz("first");
        Clazz second = new Clazz("second");

        assertThat(first.compareTo(null), greaterThan(0));
        assertThat(first.compareTo(zero), greaterThan(0));
        assertThat(second.compareTo(null), greaterThan(0));
        assertThat(second.compareTo(first), greaterThan(0));
        assertThat(first.compareTo(second), lessThan(0));
        assertThat(zero.compareTo(first), lessThan(0));
        assertThat(first.compareTo(first), is(0));
        assertThat(second.compareTo(second), is(0));
    }

    /**
     * Tests the {@link Clazz#compareTo(Clazz)} method.
     */
    @Test
    public void testCompareId() {
        Clazz zero = new Clazz(null, null);
        Clazz first = new Clazz("first", null);
        Clazz second = new Clazz("second", null);

        assertThat(first.compareTo(null), greaterThan(0));
        assertThat(first.compareTo(zero), greaterThan(0));
        assertThat(second.compareTo(null), greaterThan(0));
        assertThat(second.compareTo(first), greaterThan(0));
        assertThat(first.compareTo(second), lessThan(0));
        assertThat(zero.compareTo(first), lessThan(0));
        assertThat(first.compareTo(first), is(0));
        assertThat(second.compareTo(second), is(0));
    }

    /**
     * Tests the {@link Clazz#compareTo(Clazz)} method.
     */
    @Test
    public void testCompareNameAndId() {
        Clazz zero = new Clazz(null, null);
        Clazz first = new Clazz("first", null);
        Clazz second = new Clazz("aa", "second");
        Clazz third = new Clazz("third", "second");

        assertThat(first.compareTo(null), greaterThan(0));
        assertThat(first.compareTo(zero), greaterThan(0));
        assertThat(second.compareTo(null), greaterThan(0));
        assertThat(second.compareTo(first), greaterThan(0));
        assertThat(first.compareTo(second), lessThan(0));
        assertThat(zero.compareTo(first), lessThan(0));
        assertThat(first.compareTo(first), is(0));
        assertThat(second.compareTo(second), is(0));
        assertThat(third.compareTo(second), greaterThan(0));
    }
}
