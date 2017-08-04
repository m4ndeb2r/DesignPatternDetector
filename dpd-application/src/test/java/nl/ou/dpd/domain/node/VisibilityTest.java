package nl.ou.dpd.domain.node;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link Visibility} enum.
 *
 * @author Martin de Boer
 */
public class VisibilityTest {

    @Test
    public void testValueOfIgnoreCase() {
        for (Visibility visibility : Visibility.values()) {
            final String lower = visibility.name().toLowerCase();
            final String upper = visibility.name();
            assertThat(Visibility.valueOfIgnoreCase(lower), is(visibility));
            assertThat(Visibility.valueOfIgnoreCase(upper), is(visibility));
            assertThat(Visibility.valueOfIgnoreCase(null), is((Visibility)null));
        }
    }
    
}
