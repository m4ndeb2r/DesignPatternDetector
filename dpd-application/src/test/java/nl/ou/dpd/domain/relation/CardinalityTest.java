package nl.ou.dpd.domain.relation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static nl.ou.dpd.domain.relation.Cardinality.ILLEGAL_CARDINALITY_MSG;
import static nl.ou.dpd.domain.relation.Cardinality.NEGATIVES_NOT_ALLOWED_MSG;
import static nl.ou.dpd.domain.relation.Cardinality.UNLIMITED_NOT_ALLOWED_MSG;
import static nl.ou.dpd.domain.relation.Cardinality.UPPERBOUND_MUST_BE_GE_LOWERBOUND_MSG;
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
    public void testConstructorOK() {
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
        thrown.expectMessage(UPPERBOUND_MUST_BE_GE_LOWERBOUND_MSG);
        new Cardinality(1, 0);
    }

    @Test
    public void testValueOf() {
        Cardinality cardinality = Cardinality.valueOf("1");
        assertThat(cardinality.getLower(), is(1));
        assertThat(cardinality.getUpper(), is(1));

        cardinality = Cardinality.valueOf("1,2");
        assertThat(cardinality.getLower(), is(1));
        assertThat(cardinality.getUpper(), is(2));

        cardinality = Cardinality.valueOf("1..99");
        assertThat(cardinality.getLower(), is(1));
        assertThat(cardinality.getUpper(), is(99));

        cardinality = Cardinality.valueOf("1..*");
        assertThat(cardinality.getLower(), is(1));
        assertThat(cardinality.getUpper(), is(Cardinality.UNLIMITED));

        cardinality = Cardinality.valueOf("*");
        assertThat(cardinality.getLower(), is(0));
        assertThat(cardinality.getUpper(), is(Cardinality.UNLIMITED));
    }

    @Test
    public void testValueOfLowerValueUnlimited() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(UNLIMITED_NOT_ALLOWED_MSG);
        Cardinality.valueOf("*..0");
    }

    @Test
    public void testValueOfLowerTooLarge() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(UPPERBOUND_MUST_BE_GE_LOWERBOUND_MSG);
        Cardinality.valueOf("1..0");
    }

    @Test
    public void testValueOfTooManyArgs() {
        testValueOfWithIllegalValue("1..2..3");
    }

    @Test
    public void testValueOfCharacters() {
        testValueOfWithIllegalValue("a..*");
    }

    @Test
    public void testValueOfWithIllegalSeparator() {
        testValueOfWithIllegalValue("1/5");
    }

    private void testValueOfWithIllegalValue(String illegal) {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format(ILLEGAL_CARDINALITY_MSG, illegal));
        Cardinality.valueOf(illegal);
    }

    @Test
    public void testValueOfWithNegativeValues() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NEGATIVES_NOT_ALLOWED_MSG);
        Cardinality.valueOf("-2..-1");
    }

    @Test
    public void testIsWithinLimitsOf() {
        assertIsWithinLimitsOf("1", "1", true);
        assertIsWithinLimitsOf("1", "1", true);
        assertIsWithinLimitsOf("1", "*", true);
        assertIsWithinLimitsOf("1..9999", "*", true);
        assertIsWithinLimitsOf("1..*", "*", true);
        assertIsWithinLimitsOf("*", "*", true);
        assertIsWithinLimitsOf("*", "0..*", true);
        assertIsWithinLimitsOf("5..*", "5..*", true);

        assertIsWithinLimitsOf("1", "2", false);
        assertIsWithinLimitsOf("2", "1", false);
        assertIsWithinLimitsOf("*", "1..*", false);
        assertIsWithinLimitsOf("1..*", "1..999", false);
        assertIsWithinLimitsOf("5..*", "6..*", false);
        assertIsWithinLimitsOf("5..8", "6..7", false);
        assertIsWithinLimitsOf("5..8", "15..18", false);
    }

    private void assertIsWithinLimitsOf(String c1, String c2, boolean expect) {
        if (expect) {
            assertTrue(Cardinality.valueOf(c1).isWithinLimitsOf(Cardinality.valueOf(c2)));
        } else {
            assertFalse(Cardinality.valueOf(c1).isWithinLimitsOf(Cardinality.valueOf(c2)));
        }
    }

    @Test
    public void testToString() {
        assertThat(Cardinality.valueOf("1").toString(), is("1"));
        assertThat(Cardinality.valueOf("*").toString(), is("*"));

        assertThat(Cardinality.valueOf("0..*").toString(), is("*"));
        assertThat(Cardinality.valueOf("1..1").toString(), is("1"));
        assertThat(Cardinality.valueOf("3..5").toString(), is("3..5"));
        assertThat(Cardinality.valueOf("1..*").toString(), is("1..*"));

        assertThat(Cardinality.valueOf("0,*").toString(), is("*"));
        assertThat(Cardinality.valueOf("1,1").toString(), is("1"));
        assertThat(Cardinality.valueOf("3,5").toString(), is("3..5"));
        assertThat(Cardinality.valueOf("1,*").toString(), is("1..*"));
    }

    @Test
    public void testEquals() {
        Cardinality c = Cardinality.valueOf("*");
        assertTrue(c.equals(c));
        assertTrue(c.equals(Cardinality.valueOf("*")));
        assertFalse(c.equals(null));
    }

}
