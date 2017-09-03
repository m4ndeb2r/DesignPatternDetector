package nl.ou.dpd.domain.node;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link Node} class.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
@RunWith(MockitoJUnitRunner.class)
public class NodeTest {

    @Mock
    private Attribute attribute, attributeEqSign, attributeNeSign;

    @Mock
    private Operation operation, operationEqSign, operationNeSign;

    private Node node, equalSignatureNode, differentSignatureNode;

    @Before
    public void initTestSubjectNodes() {
        String id = "id";
        String name = "name";

        node = new Node(id, name);
        equalSignatureNode = new Node(id, name);
        differentSignatureNode = new Node(id, name);
    }

    @Before
    public void initMocks() {
        when(attributeEqSign.equalsSignature(attribute)).thenReturn(true);
        when(attributeNeSign.equalsSignature(attribute)).thenReturn(false);
        when(attribute.equalsSignature(attributeEqSign)).thenReturn(true);
        when(attribute.equalsSignature(attributeNeSign)).thenReturn(false);

        when(operationEqSign.equalsSignature(operation)).thenReturn(true);
        when(operationNeSign.equalsSignature(operation)).thenReturn(false);
        when(operation.equalsSignature(operationEqSign)).thenReturn(true);
        when(operation.equalsSignature(operationNeSign)).thenReturn(false);
    }

    @Test
    public void testConstructors() {
        testConstructor("id");
        testConstructor("id", "name");
        testConstructor("id", "name", NodeType.INTERFACE);
    }

    private void testConstructor(String id) {
        final Node node = new Node(id);
        assertEquals(id, node.getId());
        assertNull(node.getName());
        assertEquals(0, node.getTypes().size());
        assertEquals(0, node.getAttributes().size());
        assertEquals(0, node.getOperations().size());
        assertEquals(Visibility.PUBLIC, node.getVisibility());
    }

    private void testConstructor(String id, String name) {
        final Node node = new Node(id, name);
        assertEquals(id, node.getId());
        assertEquals(name, node.getName());
        assertEquals(0, node.getTypes().size());
        assertEquals(0, node.getAttributes().size());
        assertEquals(0, node.getOperations().size());
        assertEquals(Visibility.PUBLIC, node.getVisibility());
    }

    private void testConstructor(String id, String name, NodeType type) {
        final Node node = new Node(id, name, type);
        assertEquals(id, node.getId());
        assertEquals(name, node.getName());
        assertEquals(1, node.getTypes().size());
        assertEquals(type, node.getTypes().iterator().next());
        assertEquals(0, node.getAttributes().size());
        assertEquals(0, node.getOperations().size());
        assertEquals(Visibility.PUBLIC, node.getVisibility());
    }

    @Test
    public void testEqualsSignatureByName() {
        differentSignatureNode.setName("otherName");

        assertTrue(node.equalsSignature(equalSignatureNode));
        assertFalse(node.equalsSignature(differentSignatureNode));
    }

    @Test
    public void testEqualsSignatureByType() {
        addTypeToNode(node, NodeType.INTERFACE);
        addTypeToNode(node, NodeType.ABSTRACT_CLASS_OR_INTERFACE);
        addTypeToNode(equalSignatureNode, NodeType.INTERFACE);
        addTypeToNode(equalSignatureNode, NodeType.ABSTRACT_CLASS_OR_INTERFACE);
        addTypeToNode(differentSignatureNode, NodeType.ABSTRACT_CLASS);
        addTypeToNode(differentSignatureNode, NodeType.ABSTRACT_CLASS_OR_INTERFACE);

        assertTrue(node.equalsSignature(equalSignatureNode));
        assertFalse(node.equalsSignature(differentSignatureNode));
    }

    private void addTypeToNode(Node node, NodeType type) {
        final int size = node.getTypes().size();
        node.addType(type);
        assertThat(node.getTypes().size(), is(size + 1));
    }

    @Test
    public void testEqualsSignatureByAttribute() {
        addAttributeToNode(node, attribute);
        addAttributeToNode(equalSignatureNode, attributeEqSign);
        addAttributeToNode(differentSignatureNode, attributeNeSign);

        assertTrue(node.equalsSignature(equalSignatureNode));
        assertFalse(node.equalsSignature(differentSignatureNode));
    }

    @Test
    public void testEqualsSignatureByNumberOfAttributes() {
        addAttributeToNode(node, attribute);
        addAttributeToNode(equalSignatureNode, attributeEqSign);
        addAttributeToNode(differentSignatureNode, attribute);
        addAttributeToNode(differentSignatureNode, attributeEqSign);

        assertTrue(node.equalsSignature(equalSignatureNode));
        assertFalse(node.equalsSignature(differentSignatureNode));
    }

    private void addAttributeToNode(Node node, Attribute attribute) {
        final int size = node.getAttributes().size();
        node.addAttribute(attribute);
        assertThat(node.getAttributes().size(), is(size + 1));
    }

    @Test
    public void testEqualsSignatureByOperation() {
        addOperationToNode(node, operation);
        addOperationToNode(equalSignatureNode, operationEqSign);
        addOperationToNode(differentSignatureNode, operationNeSign);

        assertTrue(node.equalsSignature(equalSignatureNode));
        assertFalse(node.equalsSignature(differentSignatureNode));
    }

    @Test
    public void testEqualsSignatureByNumberOfOperations() {
        addOperationToNode(node, operation);
        addOperationToNode(equalSignatureNode, operationEqSign);
        addOperationToNode(differentSignatureNode, operation);
        addOperationToNode(differentSignatureNode, operationEqSign);

        assertTrue(node.equalsSignature(equalSignatureNode));
        assertFalse(node.equalsSignature(differentSignatureNode));
    }

    private void addOperationToNode(Node node, Operation operation) {
        int count = node.getOperations().size();
        node.addOperation(operation);
        assertThat(node.getOperations().size(), is(count + 1));
    }

    @Test
    public void testEqualsSignatureByVisibility() {
        node.setVisibility(Visibility.PUBLIC);
        equalSignatureNode.setVisibility(Visibility.PUBLIC);
        assertTrue(node.equalsSignature(equalSignatureNode));
        differentSignatureNode.setVisibility(Visibility.PACKAGE);
        assertFalse(node.equalsSignature(differentSignatureNode));
        differentSignatureNode.setVisibility(Visibility.PROTECTED);
        assertFalse(node.equalsSignature(differentSignatureNode));
        differentSignatureNode.setVisibility(Visibility.PRIVATE);
        assertFalse(node.equalsSignature(differentSignatureNode));
    }

}
