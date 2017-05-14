package nl.ou.dpd.domain.node;

import org.junit.Test;

import static junit.framework.TestCase.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link Interface} class.
 *
 * @author Martin de Boer
 */
public class InterfaceTest {

    /**
     * Tests the {@link Interface#compareTo(Interface)} method.
     */
    @Test
    public void testCompareName() {
        Interface zero = new Interface(null, null);
        Interface first = new Interface("first", "first");
        Interface second = new Interface("second", "second");

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
     * Tests the {@link Interface#compareTo(Interface)} method.
     */
    @Test
    public void testCompareId() {
        Interface zero = new Interface(null, null);
        Interface first = new Interface("first", null);
        Interface second = new Interface("second", null);

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
     * Tests the {@link Interface#compareTo(Interface)} method.
     */
    @Test
    public void testCompareNameAndId() {
        Interface zero = new Interface(null, null);
        Interface first = new Interface("first", null);
        Interface second = new Interface("aa", "second");
        Interface third = new Interface("third", "second");

        assertThat(first.compareTo(null), greaterThan(0));
        assertThat(first.compareTo(zero), greaterThan(0));
        assertThat(second.compareTo(null), greaterThan(0));
        assertThat(second.compareTo(first), greaterThan(0));

        assertThat(first.compareTo(second), lessThan(0));
        assertThat(zero.compareTo(first), lessThan(0));

        assertThat(zero.compareTo(zero), is(0));
        assertThat(first.compareTo(first), is(0));
        assertThat(second.compareTo(second), is(0));
        assertThat(third.compareTo(second), greaterThan(0));
    }

    /**
     * Tests the {@link Interface#equals(Object)} method.
     */
    @Test
    public void testEquals() {
        Interface zero = new Interface(null, null);
        Interface a1 = new Interface("a", "1");
        Interface a2 = new Interface("a", "2");
        Interface b1 = new Interface("b", "1");
        Interface b2 = new Interface("b", "2");

        assertTrue(Interface.EMPTY_NODE.equals(Interface.EMPTY_NODE));
        assertTrue(zero.equals(new Interface(null, null)));
        assertTrue(a1.equals(a1));
        assertFalse(a1.equals(a2));
        assertFalse(a1.equals(b1));
        assertFalse(a2.equals(b2));

        Node aClazz = new Clazz("A", "A");
        Node anInterface = new Interface("A", "A");
        assertFalse(aClazz.equals(anInterface));
    }

    /**
     * Tests the constructors and getters of the {@link Interface} class.
     */
    @Test
    public void testConstructorsAndGetters() {
        Interface i1 = new Interface("A", "A");
        assertThat(i1.getName(), is("A"));
        assertThat(i1.getId(), is("A"));
        assertThat(i1.getAttributes().size(), is(0));
        assertThat(i1.getType(), is(NodeType.INTERFACE));
        assertThat(i1.getVisibility(), is(Visibility.PUBLIC));
        assertTrue(i1.isAbstract());
    }
}
