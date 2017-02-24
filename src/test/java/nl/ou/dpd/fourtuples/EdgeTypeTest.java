package nl.ou.dpd.fourtuples;

import org.hamcrest.core.Is;
import org.junit.Test;

import static nl.ou.dpd.fourtuples.EdgeType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


/**
 * Tests the {@link EdgeType} class.
 *
 * @author Martin de Boer
 */
public class EdgeTypeTest {

    @Test
    public void testGetTagValue() {
        assertThat(EdgeType.valueOf("ASSOCIATION"), Is.is(ASSOCIATION));
        assertThat(EdgeType.valueOf("AGGREGATE"), is(AGGREGATE));
        assertThat(EdgeType.valueOf("ASSOCIATION_DIRECTED"), is(ASSOCIATION_DIRECTED));
        assertThat(EdgeType.valueOf("COMPOSITE"), is(COMPOSITE));
        assertThat(EdgeType.valueOf("DEPENDENCY"), is(DEPENDENCY));
        assertThat(EdgeType.valueOf("INHERITANCE"), is(INHERITANCE));
        assertThat(EdgeType.valueOf("INHERITANCE_MULTI"), is(INHERITANCE_MULTI));
        assertThat(EdgeType.valueOf("EMPTY"), is(EMPTY));
    }
}
