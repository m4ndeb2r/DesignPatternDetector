package nl.ou.dpd.domain.rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link Operation} enum.
 *
 * @author Martin de Boer
 */
public class OperationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testValueOfIgnoreCase() {
        for (Operation operation : Operation.values()) {
            final String lower = operation.name().toLowerCase();
            final String upper = operation.name();
            assertThat(Operation.valueOfIgnoreCase(lower), is(operation));
            assertThat(Operation.valueOfIgnoreCase(upper), is(operation));
        }
    }

    @Test
    public void testValueOf() {
        assertThat(Operation.valueOf("EQUALS"), is(Operation.EQUALS));
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("No enum");
        Operation.valueOf("equals");
    }
}
