package nl.ou.dpd.domain;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Test the {@link SystemUnderConsideration} class.
 *
 * @author Martin de Boer
 */
public class SystemUnderConsiderationTest {

    private static final String ID = "id";
    private static final String NAME = "name";

    @Test
    public void testConstructor() {
        SystemUnderConsideration sys = new SystemUnderConsideration(ID, NAME);
        assertThat(sys.getId(), is(ID));
        assertThat(sys.getName(), is(NAME));
    }
}
