package nl.ou.dpd.domain.node;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link Operation} class.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class OperationTest {

    @Mock
    private Node parent1, parent2;
    @Mock
    private Node returnType1, returnType2;
    @Mock
    private Parameter param1, param2, param3, param4;

    @Test
    public void testConstructor() {
        // A captor to check the parent's addOperation method's input parameter
        ArgumentCaptor<Operation> captor = ArgumentCaptor.forClass(Operation.class);

        // Create an operation
        final Operation operation = new Operation("operation", parent1);

        // Verify that the parentNode had the new attribute added to it
        verify(parent1).addOperation(captor.capture());
        assertEquals(operation, captor.getValue());

        // Verify the newly created attibute
        assertEquals("operation", operation.getId());
        assertEquals(parent1, operation.getParentNode());
        assertNull(operation.getName());
        assertNull(operation.getReturnType());
        assertEquals(Visibility.PUBLIC, operation.getVisibility());
    }

    @Test
    public void testSignatureEqualsDefault() {
        final Operation op1 = new Operation("id1", parent1);
        final Operation op2 = new Operation("id2", parent2);

        assertFalse(op1.equalsSignature(null));
        assertTrue(op1.equalsSignature(op1));
        assertTrue(op1.equalsSignature(op2));
        assertTrue(op2.equalsSignature(op1));
    }

    @Test
    public void testSignatureEqualsByName() {
        final Operation op1 = new Operation("id1", parent1);
        final Operation op2 = new Operation("id2", parent2);

        op1.setName("name");
        assertFalse(op1.equalsSignature(op2));
        assertFalse(op2.equalsSignature(op1));

        op2.setName("name");
        assertTrue(op1.equalsSignature(op2));
        assertTrue(op2.equalsSignature(op1));
    }

    @Test
    public void testSignatureEqualsByReturnType() {
        final Operation op1 = new Operation("id1", parent1);
        final Operation op2 = new Operation("id2", parent2);
        assertByReturnType(op1, op2, true);
        assertByReturnType(op1, op2, false);
    }

    private void assertByReturnType(Operation op1, Operation op2, boolean expectation) {
        when(returnType1.equalsSignature(returnType2)).thenReturn(expectation);
        when(returnType2.equalsSignature(returnType1)).thenReturn(expectation);
        op1.setReturnType(returnType1);
        op2.setReturnType(returnType2);
        assertThat(op1.equalsSignature(op2), is(expectation));
        assertThat(op2.equalsSignature(op1), is(expectation));
    }

    @Test
    public void testSignatureEqualsByParamType() {
        final Operation op1 = new Operation("id1", parent1);
        final Operation op2 = new Operation("id2", parent2);
        assertByParamType(op1, op2, true);
        assertByParamType(op1, op2, false);
    }

    private void assertByParamType(Operation op1, Operation op2, boolean expectation) {
        when(param1.equalsSignature(param2)).thenReturn(expectation);
        when(param2.equalsSignature(param1)).thenReturn(expectation);
        when(param3.equalsSignature(param4)).thenReturn(expectation);
        when(param4.equalsSignature(param3)).thenReturn(expectation);

        op1.addParameter(param1);
        op1.addParameter(param3);
        op2.addParameter(param2);
        op2.addParameter(param4);

        assertThat(op1.equalsSignature(op2), is(expectation));
        assertThat(op2.equalsSignature(op1), is(expectation));
    }

    @Test
    public void testSignatureEqualsByParamCount() {
        final Operation op1 = new Operation("id1", parent1);
        final Operation op2 = new Operation("id2", parent2);

        when(param1.equalsSignature(param2)).thenReturn(true);
        when(param2.equalsSignature(param1)).thenReturn(true);

        op1.addParameter(param1);
        assertFalse(op1.equalsSignature(op2));
        op2.addParameter(param2);
        assertTrue(op1.equalsSignature(op2));
        assertThat(op1.getParameters().size(), is(op2.getParameters().size()));
        op1.addParameter(param2);
        assertFalse(op1.equalsSignature(op2));
    }

}
