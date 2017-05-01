package nl.ou.dpd.domain.edge;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link Cardinality} class.
 *
 * @author Martin de Boer
 */
public class CardinalityTest {

    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests the constructor of the {@link Cardinality} class.
     */
    @Test
    public void testContrstructorOK() {
        assertThat(new Cardinality(0, 1).getLower(), is(0));
        assertThat(new Cardinality(1, 1).getLower(), is(1));
        assertThat(new Cardinality(1, Cardinality.UNLIMITED).getUpper(), is(Cardinality.UNLIMITED));
    }

    /**
     * Tests if constructor of the {@link Cardinality} handles illegal arguments correctly.
     */
    @Test
    public void testIllegalArguments() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Upperbound value must be >= lowerbound value.");
        new Cardinality(1, 0);
    }
}
