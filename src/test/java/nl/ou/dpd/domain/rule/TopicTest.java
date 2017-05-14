package nl.ou.dpd.domain.rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link Topic} enum.
 *
 * @author Martin de Boer
 */
public class TopicTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testValueOfIgnoreCase() {
        for (Topic topic : Topic.values()) {
            final String lower = topic.name().toLowerCase();
            final String upper = topic.name();
            assertThat(Topic.valueOfIgnoreCase(lower), is(topic));
            assertThat(Topic.valueOfIgnoreCase(upper), is(topic));
        }
    }

    @Test
    public void testValueOf() {
        assertThat(Topic.valueOf("TYPE"), is(Topic.TYPE));
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("No enum");
        Topic.valueOf("type");
    }
}
