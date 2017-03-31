package nl.ou.dpd.domain.node;

import nl.ou.dpd.domain.node.Clazz;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testEquals() {
        Clazz zero = new Clazz(null, null);
        Clazz a1 = new Clazz("a", "1");
        Clazz a2 = new Clazz("a", "2");
        Clazz b1 = new Clazz("b", "1");
        Clazz b2 = new Clazz("b", "2");

        assertTrue(zero.equals(new Clazz(null)));
        assertTrue(a1.equals(a1));
        assertFalse(a1.equals(a2));
        assertFalse(a1.equals(b1));
        assertFalse(a2.equals(b2));
    }
}
