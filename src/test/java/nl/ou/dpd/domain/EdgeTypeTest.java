package nl.ou.dpd.domain;

import org.hamcrest.core.Is;
import org.junit.Test;

import static nl.ou.dpd.domain.EdgeType.AGGREGATE;
import static nl.ou.dpd.domain.EdgeType.ASSOCIATION;
import static nl.ou.dpd.domain.EdgeType.ASSOCIATION_DIRECTED;
import static nl.ou.dpd.domain.EdgeType.COMPOSITE;
import static nl.ou.dpd.domain.EdgeType.DEPENDENCY;
import static nl.ou.dpd.domain.EdgeType.EMPTY;
import static nl.ou.dpd.domain.EdgeType.INHERITANCE;
import static nl.ou.dpd.domain.EdgeType.INHERITANCE_MULTI;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


/**
 * Tests the {@link EdgeType} class.
 *
 * @author Martin de Boer
 */
public class EdgeTypeTest {

    /**
     * Tests the {@link EdgeType#valueOf(String)} method.
     */
    @Test
    public void testValueof() {
        assertThat(EdgeType.valueOf("EMPTY"), is(EMPTY));
        assertThat(EdgeType.valueOf("ASSOCIATION"), Is.is(ASSOCIATION));
        assertThat(EdgeType.valueOf("ASSOCIATION_DIRECTED"), is(ASSOCIATION_DIRECTED));
        assertThat(EdgeType.valueOf("AGGREGATE"), is(AGGREGATE));
        assertThat(EdgeType.valueOf("COMPOSITE"), is(COMPOSITE));
        assertThat(EdgeType.valueOf("INHERITANCE"), is(INHERITANCE));
        assertThat(EdgeType.valueOf("INHERITANCE_MULTI"), is(INHERITANCE_MULTI));
        assertThat(EdgeType.valueOf("DEPENDENCY"), is(DEPENDENCY));
    }

    /**
     * Tests the {@link EdgeType#getCode()} method.
     */
    @Test
    public void testGetCode() {
        assertThat(EdgeType.EMPTY.getCode(), is(-1));
        assertThat(EdgeType.ASSOCIATION.getCode(), Is.is(1));
        assertThat(EdgeType.ASSOCIATION_DIRECTED.getCode(), is(10));
        assertThat(EdgeType.AGGREGATE.getCode(), is(2));
        assertThat(EdgeType.COMPOSITE.getCode(), is(3));
        assertThat(EdgeType.INHERITANCE.getCode(), is(4));
        assertThat(EdgeType.INHERITANCE_MULTI.getCode(), is(40));
        assertThat(EdgeType.DEPENDENCY.getCode(), is(5));
    }

    /**
     * Tests the {@link EdgeType#getName()} method.
     */
    @Test
    public void testGetName() {
        assertThat(EdgeType.EMPTY.getName(), is(""));
        assertThat(EdgeType.ASSOCIATION.getName(), Is.is("ASSOCIATION"));
        assertThat(EdgeType.ASSOCIATION_DIRECTED.getName(), is("ASSOCIATION_DIRECTED"));
        assertThat(EdgeType.AGGREGATE.getName(), is("AGGREGATE"));
        assertThat(EdgeType.COMPOSITE.getName(), is("COMPOSITE"));
        assertThat(EdgeType.INHERITANCE.getName(), is("INHERITANCE"));
        assertThat(EdgeType.INHERITANCE_MULTI.getName(), is("INHERITANCE_MULTI"));
        assertThat(EdgeType.DEPENDENCY.getName(), is("DEPENDENCY"));
    }
}
