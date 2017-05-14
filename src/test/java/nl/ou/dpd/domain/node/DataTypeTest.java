package nl.ou.dpd.domain.node;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link DataType} class.
 *
 * @author Martin de Boer
 */
public class DataTypeTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testUnexpectedDataTypeId() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Unexpected DataType id: 'unknown'.");
        new DataType("unknown");
    }

    @Test
    public void testNewBooleanOkay() {
        final DataType bool = new DataType(DataType.Allowed.DATATYPE_BOOLEAN.getId());
        assertThat(bool.getName(), is(DataType.Allowed.DATATYPE_BOOLEAN.getName()));
    }

    @Test
    public void testNewStringOkay() {
        final DataType bool = new DataType(DataType.Allowed.DATATYPE_STRING.getId());
        assertThat(bool.getName(), is(DataType.Allowed.DATATYPE_STRING.getName()));
    }

    @Test
    public void testNewIntegerOkay() {
        final DataType bool = new DataType(DataType.Allowed.DATATYPE_INTEGER.getId());
        assertThat(bool.getName(), is(DataType.Allowed.DATATYPE_INTEGER.getName()));
    }

    @Test
    public void testNewUnlimitedIntegerOkay() {
        final DataType bool = new DataType(DataType.Allowed.DATATYPE_UNLIMITED_INTEGER.getId());
        assertThat(bool.getName(), is(DataType.Allowed.DATATYPE_UNLIMITED_INTEGER.getName()));
    }
}
