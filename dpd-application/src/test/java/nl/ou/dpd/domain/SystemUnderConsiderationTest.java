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

    @Test
    public void testConstructor() {
        SystemUnderConsideration sys = new SystemUnderConsideration("id", "name");
        assertThat(sys.getId(), is("id"));
        assertThat(sys.getName(), is("name"));
    }
}
