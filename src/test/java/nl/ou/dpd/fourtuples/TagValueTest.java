package nl.ou.dpd.fourtuples;

import org.hamcrest.core.Is;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


/**
 * Tests the {@link TagValue} class.
 *
 * @author Martin de Boer
 */
public class TagValueTest {

    /**
     * Tests the {@link TagValue#getTagValue(String)} method.
     */
    @Test
    public void testGetTagValue() {
        assertThat(TagValue.getTagValue("ASSOCIATION"), Is.is(FT_constants.ASSOCIATION));
        assertThat(TagValue.getTagValue("AGGREGATE"), is(FT_constants.AGGREGATE));
        assertThat(TagValue.getTagValue("ASSOCIATION_DIRECTED"), is(FT_constants.ASSOCIATION_DIRECTED));
        assertThat(TagValue.getTagValue("COMPOSITE"), is(FT_constants.COMPOSITE));
        assertThat(TagValue.getTagValue("DEPENDENCY"), is(FT_constants.DEPENDENCY));
        assertThat(TagValue.getTagValue("INHERITANCE"), is(FT_constants.INHERITANCE));
        assertThat(TagValue.getTagValue("INHERITANCE_MULTI"), is(FT_constants.INHERITANCE_MULTI));
        assertThat(TagValue.getTagValue("EMPTY"), is(-1));
    }
}
