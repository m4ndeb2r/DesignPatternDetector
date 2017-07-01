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
 * Tests the {@link Attribute} class.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class AttributeTest {

    @Mock
    private Node parent1, type1, parent2, type2;

    @Test
    public void testConstructor() {
        // A captor to check the parentNode's addAttribute method's input parameter
        ArgumentCaptor<Attribute> captor = ArgumentCaptor.forClass(Attribute.class);

        // Create an attribute
        final Attribute attr = new Attribute("attr1", parent1);

        // Verify that the parentNode had the new attribute added to it
        verify(parent1).addAttribute(captor.capture());
        assertEquals(attr, captor.getValue());

        // Verify the newly created attibute
        assertEquals("attr1", attr.getId());
        assertEquals(parent1, attr.getParentNode());
        assertNull(attr.getName());
        assertNull(attr.getType());
        assertEquals(Visibility.PUBLIC, attr.getVisibility());
    }

    @Test
    public void testSignatureEquals() {
        final String[] ids = new String[]{"attr1", "attr2"};
        final String[] names = new String[]{null, "name1", "name2"};
        final Node[] parents = new Node[]{null, parent1, parent2};
        final Node[] types = new Node[]{null, type1, type2};
        final Visibility[] visibilities = new Visibility[]{Visibility.PUBLIC, Visibility.PRIVATE};

        final Attribute attr1 = new Attribute(ids[1], parents[1]);
        attr1.setName(names[1]).setType(types[1]).setVisibility(visibilities[1]);

        assertFalse(attr1.equalsSignature(null));

        for (String id : ids) {
            for (String name : names) {
                for (Node parent : parents) {
                    for (Node type : types) {
                        for (Visibility visibility : visibilities) {
                            final Attribute attr2 = new Attribute(id, parent);
                            attr2.setName(name).setType(type).setVisibility(visibility);

                            // The equality of a signature is exclusively based on name and type
                            final boolean nameEquals = attr1.getName().equals(attr2.getName());
                            final boolean typeEquals = attr1.getType().equals(attr2.getType());
                            assertThat(attr1.equalsSignature(attr2), is(nameEquals && typeEquals));
                        }
                    }
                }
            }
        }
    }

}
