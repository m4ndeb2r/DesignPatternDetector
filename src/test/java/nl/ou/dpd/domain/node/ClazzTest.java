package nl.ou.dpd.domain.node;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
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
        Clazz zero = new Clazz(null, null);
        Clazz first = new Clazz("first", "first");
        Clazz second = new Clazz("second", "second");

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

    /**
     * Tests the {@link Clazz#equals(Object)} method.
     */
    @Test
    public void testEquals() {
        final Clazz zero = new Clazz(null, null);
        final Clazz a1 = new Clazz("a", "1");
        final Clazz a2 = new Clazz("a", "2");
        final Clazz b1 = new Clazz("b", "1");
        final Clazz b2 = new Clazz("b", "2");

        assertTrue(Clazz.EMPTY_NODE.equals(Clazz.EMPTY_NODE));
        assertTrue(zero.equals(new Clazz(null, null)));
        assertTrue(a1.equals(a1));
        assertFalse(a1.equals(a2));
        assertFalse(a1.equals(b1));
        assertFalse(a2.equals(b2));

        final Clazz publicClazz = new Clazz("id", "name", Visibility.PUBLIC, null, null, null, null, null);
        Clazz protectedClazz = new Clazz("id", "name", Visibility.PROTECTED, null, null, null, null, null);
        assertFalse(publicClazz.equals(protectedClazz));
        protectedClazz = new Clazz("id", "name", Visibility.PROTECTED, null, null, null, null, null);
        assertFalse(publicClazz.equals(protectedClazz));

        final Clazz rootClazz = new Clazz("id", "name", Visibility.PUBLIC, null, true, null, null, null);
        Clazz nonRootClazz = new Clazz("id", "name", Visibility.PUBLIC, null, false, null, null, null);
        assertFalse(rootClazz.equals(nonRootClazz));
        nonRootClazz = new Clazz("id", "name", Visibility.PUBLIC, null, null, null, null, null);
        assertFalse(rootClazz.equals(nonRootClazz));

        final Clazz leafClazz = new Clazz("id", "name", Visibility.PUBLIC, null, null, true, null, null);
        Clazz nonLeafClazz = new Clazz("id", "name", Visibility.PUBLIC, null, null, false, null, null);
        assertFalse(leafClazz.equals(nonLeafClazz));
        nonLeafClazz = new Clazz("id", "name", Visibility.PUBLIC, null, null, null, null, null);
        assertFalse(leafClazz.equals(nonLeafClazz));

        final Clazz abstractClazz = new Clazz("id", "name", Visibility.PUBLIC, null, null, null, true, null);
        Clazz nonAbstractClazz = new Clazz("id", "name", Visibility.PUBLIC, null, null, null, false, null);
        assertFalse(abstractClazz.equals(nonAbstractClazz));
        nonAbstractClazz = new Clazz("id", "name", Visibility.PUBLIC, null, null, null, null, null);
        assertFalse(abstractClazz.equals(nonAbstractClazz));

        final Clazz activeClazz = new Clazz("id", "name", Visibility.PUBLIC, null, null, null, null, true);
        Clazz nonActiveClazz = new Clazz("id", "name", Visibility.PUBLIC, null, null, null, null, false);
        assertFalse(activeClazz.equals(nonActiveClazz));
        nonActiveClazz = new Clazz("id", "name", Visibility.PUBLIC, null, null, null, null, null);
        assertFalse(activeClazz.equals(nonActiveClazz));

        final List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("attr1", TestHelper.createClazz("myClass1")));
        attributes.add(new Attribute("attr2", TestHelper.createClazz("myClass2")));
        final Clazz clazzWithAttributes = new Clazz("id", "name", Visibility.PUBLIC, attributes, null, null, null, false);
        final Clazz clazzWithoutAttributes = new Clazz("id", "name", Visibility.PUBLIC, null, null, null, null, false);
        assertFalse(activeClazz.equals(nonActiveClazz));

        final Node aClazz = new Clazz("A", "A");
        final Node anInterface = new Interface("A", "A");
        assertFalse(aClazz.equals(anInterface));
    }

    /**
     * Tests the constructors and getters of the {@link Clazz} class.
     */
    @Test
    public void testConstructorsAndGetters() {
        final Clazz c1 = new Clazz("A", "A");
        assertThat(c1.getName(), is("A"));
        assertThat(c1.getId(), is("A"));
        assertThat(c1.getAttributes().size(), is(0));
        assertThat(c1.getType(), is(NodeType.CLASS));
        assertNull(c1.getVisibility());
        assertNull(c1.isRoot());
        assertNull(c1.isLeaf());
        assertNull(c1.isAbstract());
        assertNull(c1.isActive());

        final List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("attr1", TestHelper.createClazz("myClass1")));
        attributes.add(new Attribute("attr2", TestHelper.createClazz("myClass2")));

        final Clazz c2 = new Clazz("id", "name", Visibility.PACKAGE, attributes, true, false, true, false);
        assertThat(c2.getId(), is("id"));
        assertThat(c2.getName(), is("name"));
        assertThat(c2.getType(), is(NodeType.CLASS));
        assertThat(c2.getVisibility(), is(Visibility.PACKAGE));
        assertTrue(c2.isRoot());
        assertFalse(c2.isLeaf());
        assertTrue(c2.isAbstract());
        assertFalse(c2.isActive());
        assertThat(c2.getAttributes().size(), is(2));
        assertThat(c2.getAttributesByName("attr1").size(), is(1));
        assertThat(c2.getAttributesByName("attr2").size(), is(1));
        assertThat(c2.getAttributesByName("attr?").size(), is(0));
        assertThat(c2.getAttributesByType(TestHelper.createClazz("myClass1")).size(), is(1));
        assertThat(c2.getAttributesByType(TestHelper.createClazz("myClass2")).size(), is(1));
        assertThat(c2.getAttributesByType(TestHelper.createClazz("notPresent")).size(), is(0));
    }
}
