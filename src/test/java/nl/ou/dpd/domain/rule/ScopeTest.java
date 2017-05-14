package nl.ou.dpd.domain.rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link Scope} enum.
 *
 * @author Martin de Boer
 */
public class ScopeTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testValueOfIgnoreCase() {
        for (Scope scope : Scope.values()) {
            final String lower = scope.name().toLowerCase();
            final String upper = scope.name();
            assertThat(scope.valueOfIgnoreCase(lower), is(scope));
            assertThat(scope.valueOfIgnoreCase(upper), is(scope));
        }
    }

    @Test
    public void testValueOf() {
        assertThat(Scope.valueOf("ATTRIBUTE"), is(Scope.ATTRIBUTE));
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("No enum");
        Scope.valueOf("attribute");
    }
}
