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
import static org.mockito.Mockito.verify;

/**
 * Tests the {@link Parameter} class.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class ParameterTest {

    @Mock
    private Node type1, type2;
    @Mock
    private Operation operation1, operation2;

    @Test
    public void testConstructor() {
        // A captor to check the operation's addParameter method's input parameter
        ArgumentCaptor<Parameter> captor = ArgumentCaptor.forClass(Parameter.class);

        // Create a parameter
        final Parameter param = new Parameter("param1", operation1);

        // Verify that the operation had the new parameter added to it
        verify(operation1).addParameter(captor.capture());
        assertEquals(param, captor.getValue());

        // Verify the newly created parameter
        assertEquals("param1", param.getId());
        assertEquals(operation1, param.getParentOperation());
        assertNull(param.getName());
        assertNull(param.getType());
    }

    @Test
    public void testSignatureEquals() {
        final String[] ids = new String[]{"par1", "par2"};
        final String[] names = new String[]{null, "name1", "name2"};
        final Operation[] operations = new Operation[]{null, operation1, operation2};
        final Node[] types = new Node[]{null, type1, type2};

        final Parameter par1 = new Parameter(ids[1], operations[1]);
        par1.setName(names[1]);
        par1.setType(types[1]);

        assertFalse(par1.equalsSignature(null));

        for (String id : ids) {
            for (String name : names) {
                for (Operation operation : operations) {
                    for (Node type : types) {
                        final Parameter par2 = new Parameter(id, operation);
                        par2.setName(name);
                        par2.setType(type);

                        // The equality of a signature is exclusively based on name and type
                        final boolean nameEquals = par1.getName().equals(par2.getName());
                        final boolean typeEquals = par1.getType().equals(par2.getType());
                        assertThat(par1.equalsSignature(par2), is(nameEquals && typeEquals));
                    }
                }
            }
        }
    }
}
