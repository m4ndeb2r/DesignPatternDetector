package nl.ou.dpd.domain.node;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link Visibility} enum.
 *
 * @author Martin de Boer
 */
public class VisibilityTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testValueOfIgnoreCase() {
        for (Visibility visibility : Visibility.values()) {
            final String lower = visibility.name().toLowerCase();
            final String upper = visibility.name();
            assertThat(Visibility.valueOfIgnoreCase(lower), is(visibility));
            assertThat(Visibility.valueOfIgnoreCase(upper), is(visibility));
        }
    }

    @Test
    public void testValueOf() {
        assertThat(Visibility.valueOf("PUBLIC"), is(Visibility.PUBLIC));
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("No enum");
        Visibility.valueOf("public");
    }
}
