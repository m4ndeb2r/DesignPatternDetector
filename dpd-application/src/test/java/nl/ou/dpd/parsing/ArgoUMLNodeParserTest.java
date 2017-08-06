package nl.ou.dpd.parsing;

import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.NodeType;
import nl.ou.dpd.domain.node.Operation;
import nl.ou.dpd.domain.node.Parameter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static junit.framework.TestCase.fail;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.XMI_FILE_COULD_NOT_BE_PARSED_MSG;
import static nl.ou.dpd.parsing.ParseTestHelper.createAttributeMock;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link ArgoUMLNodeParser} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class ArgoUMLNodeParserTest {

    // XMI attributes
    private static final String ID_ATTRIBUTE = "xmi.id";
    private static final String KIND_ATTRIBUTE = "kind";
    private static final String IDREF_ATTRIBUTE = "xmi.idref";
    private static final String INPUT_ATTRIBUTE = "in";
    private static final String RETURN_ATTRIBUTE = "return";
    private static final String IS_ABSTRACT_ATTRIBUTE = "isAbstract";

    // XMI tags
    private static final String MODEL_TAG = "Model";
    private static final String CLASS_TAG = "Class";
    private static final String DATATYPE_TAG = "DataType";
    private static final String ATTRIBUTE_TAG = "Attribute";
    private static final String INTERFACE_TAG = "Interface";
    private static final String OPERATION_TAG = "Operation";
    private static final String PARAMETER_TAG = "Parameter";

    private static final String INTEGER = "87C";
    private static final String INTEGER_HREF = String.format(".......%s", INTEGER);

    // Id's in the tests
    private static final String OPERATION_ID = "operationId";
    private static final String ATTRIBUTE_ID = "attributeId";
    private static final String CLASS_NODE_ID = "classNodeId";
    private static final String PARAMETER_ID_1 = "parameterId1";
    private static final String PARAMETER_ID_2 = "parameterId2";
    private static final String RETURN_TYPE_ID = "returnTypeId";
    private static final String INTERFACE_NODE_ID = "interfaceNodeId";
    private static final String ABSTRACT_CLASS_NODE_ID = "abstractClassNodeId";

    // This file is created just to satisfy the FileInputStream of the parser
    private String xmiFile = getPath("/argoUML/dummy.xmi");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private XMLInputFactory xmlInputFactory;

    @Mock
    private XMLEventReader xmlEventReader;

    @Mock
    XMLEvent modelEvent, classEvent, abstractClassEvent, interfaceEvent, datatypeEvent,
            attributeEvent, operationEvent, inputParamEvent1, inputParamEvent2,
            inputParamTypeEvent1, inputParamTypeEvent2, returnParamEvent, returnParamTypeEvent;

    private ArgoUMLNodeParser nodeParser;

    /**
     * Initialises the test subject with a mocked xmlInputStream.
     *
     * @throws XMLStreamException not expected.
     */
    @Before
    public void initNodeParser() throws XMLStreamException {
        nodeParser = new ArgoUMLNodeParser(xmlInputFactory);
        when(xmlInputFactory.createXMLEventReader(any(InputStream.class))).thenReturn(xmlEventReader);
    }

    /**
     * Mocks the order in which the event reader reads the XML elements. Here, we put together a complete, mocked,
     * structure of the system design that is being parsed by the {@link ArgoUMLNodeParser}.
     *
     * @throws XMLStreamException not expected
     */
    @Before
    public void initEventReader() throws XMLStreamException {
        when(xmlEventReader.hasNext()).thenReturn(
                true, // model start-element
                true, // interface start-element
                true, // interface end-element
                true, // class start-element
                true, // attribute start-element
                true, // datatype start-element
                true, // datatype end-element
                true, // attribute end-element
                true, // operation start-element
                true, // input parameter start-element 1
                true, // input paramType start-element 1
                true, // input paramType end-element 1
                true, // input parameter end-element 1
                true, // input parameter start-element 2
                true, // input paramType start-element 2
                true, // input paramType end-element 2
                true, // input parameter end-element 2
                true, // return parameter start-element
                true, // return paramType start-element
                true, // return paramType end-element
                true, // return parameter end-element
                true, // operation end-element
                true, // class end-element
                true, // abstract class start-element
                true, // abstract class end-element
                true, // model end-element
                false);
        when(xmlEventReader.nextEvent()).thenReturn(
                modelEvent,
                interfaceEvent, interfaceEvent,
                classEvent,
                attributeEvent, datatypeEvent, datatypeEvent, attributeEvent,
                operationEvent,
                inputParamEvent1, inputParamTypeEvent1, inputParamTypeEvent1, inputParamEvent1,
                inputParamEvent2, inputParamTypeEvent2, inputParamTypeEvent2, inputParamEvent2,
                returnParamEvent, returnParamTypeEvent, returnParamTypeEvent, returnParamEvent,
                operationEvent,
                classEvent,
                abstractClassEvent, abstractClassEvent,
                modelEvent);
    }

    /**
     * Mocks the events that the event reader reads.
     */
    @Before
    public void initEvents() {
        modelEvent = ParseTestHelper.createXMLEventMock(MODEL_TAG);

        // Mock an interface, a class and an abstract class
        interfaceEvent = ParseTestHelper.createXMLEventMock(INTERFACE_TAG, mockId(INTERFACE_NODE_ID));
        classEvent = ParseTestHelper.createXMLEventMock(CLASS_TAG, mockId(CLASS_NODE_ID));
        abstractClassEvent = ParseTestHelper.createXMLEventMock(CLASS_TAG, mockId(ABSTRACT_CLASS_NODE_ID), mockIsAbstract(true));

        // Mock an attribute of type "Integer"
        attributeEvent = ParseTestHelper.createXMLEventMock(ATTRIBUTE_TAG, mockId(ATTRIBUTE_ID));
        datatypeEvent = ParseTestHelper.createXMLEventMock(DATATYPE_TAG, mockHref(INTEGER_HREF));

        // Mock an operation that has two input parameters: one of type "interfaceNode" and one of type Integer, and
        // one return value of type "interfaceNode"
        operationEvent = ParseTestHelper.createXMLEventMock(OPERATION_TAG, mockId(OPERATION_ID));
        inputParamEvent1 = ParseTestHelper.createXMLEventMock(PARAMETER_TAG, mockParamKind(INPUT_ATTRIBUTE), mockId(PARAMETER_ID_1));
        inputParamTypeEvent1 = ParseTestHelper.createXMLEventMock(INTERFACE_TAG, mockIdRef(INTERFACE_NODE_ID));
        inputParamEvent2 = ParseTestHelper.createXMLEventMock(PARAMETER_TAG, mockParamKind(INPUT_ATTRIBUTE), mockId(PARAMETER_ID_2));
        inputParamTypeEvent2 = ParseTestHelper.createXMLEventMock(DATATYPE_TAG, mockHref(INTEGER_HREF));
        returnParamEvent = ParseTestHelper.createXMLEventMock(PARAMETER_TAG, mockParamKind(RETURN_ATTRIBUTE), mockId(RETURN_TYPE_ID));
        returnParamTypeEvent = ParseTestHelper.createXMLEventMock(INTERFACE_TAG, mockIdRef(INTERFACE_NODE_ID));
    }

    private javax.xml.stream.events.Attribute mockHref(String href) {
        return createAttributeMock(ArgoUMLAbstractParser.HREF_ATTRIBUTE, href);
    }

    private javax.xml.stream.events.Attribute mockIsAbstract(boolean isAbstract) {
        return createAttributeMock(IS_ABSTRACT_ATTRIBUTE, Boolean.toString(isAbstract));
    }

    private javax.xml.stream.events.Attribute mockParamKind(String inOrReturn) {
        return createAttributeMock(KIND_ATTRIBUTE, inOrReturn);
    }

    private javax.xml.stream.events.Attribute mockId(String id) {
        return createAttributeMock(ID_ATTRIBUTE, id);
    }

    private javax.xml.stream.events.Attribute mockIdRef(String idRef) {
        return createAttributeMock(IDREF_ATTRIBUTE, idRef);
    }

    @Test
    public void testParsedNodes() {
        assertTrue(nodeParser.events.isEmpty());

        final Map<String, Node> nodeMap = nodeParser.parse(xmiFile);
        assertThat(nodeMap.keySet().size(), is(4));

        assertClassNode(nodeMap);
        assertAbstractClassNode(nodeMap);
        assertInterface(nodeMap);
        assertDataType(nodeMap);

        assertTrue(nodeParser.events.isEmpty());
    }

    private void assertClassNode(Map<String, Node> nodeMap) {
        assertTrue(nodeMap.containsKey(CLASS_NODE_ID));
        final Node classNode = nodeMap.get(CLASS_NODE_ID);
        assertThat(classNode.getId(), is(CLASS_NODE_ID));
        assertTrue(classNode.getTypes().contains(NodeType.CONCRETE_CLASS));
        assertAttributes(classNode);
        assertOperations(classNode);
    }

    private void assertAttributes(Node node) {
        assertThat(node.getAttributes().size(), is(1));
        final Attribute attribute = node.getAttributes().iterator().next();
        assertThat(attribute.getId(), is(ATTRIBUTE_ID));
        assertThat(attribute.getType().getName(), is(Integer.class.getSimpleName()));
        assertThat(attribute.getParentNode().getId(), is(node.getId()));
    }

    private void assertOperations(Node node) {
        assertThat(node.getOperations().size(), is(1));
        final Operation operation = node.getOperations().iterator().next();
        assertThat(operation.getId(), is(OPERATION_ID));
        assertThat(operation.getParentNode().getId(), is(node.getId()));
        assertParameters(operation);
        assertReturnType(operation);
    }

    private void assertParameters(Operation operation) {
        assertThat(operation.getParameters().size(), is(2));

        final Iterator<Parameter> iterator = operation.getParameters().iterator();
        while (iterator.hasNext()) {
            Parameter parameter = iterator.next();
            if (parameter.getId().equals(PARAMETER_ID_1)) {
                assertThat(parameter.getType().getId(), is(INTERFACE_NODE_ID));
            }
            else if (parameter.getId().equals(PARAMETER_ID_2)) {
                assertThat(parameter.getType().getId(), is(INTEGER_HREF));
            }
            else {
                fail(String.format("Unexpected parameter id: '%s'!", parameter.getId()));
            }
            assertThat(parameter.getParentOperation().getId(), is(operation.getId()));
        }
    }

    private void assertReturnType(Operation operation) {
        assertThat(operation.getReturnType().getId(), is(INTERFACE_NODE_ID));
    }

    private void assertAbstractClassNode(Map<String, Node> nodeMap) {
        assertTrue(nodeMap.containsKey(ABSTRACT_CLASS_NODE_ID));
        final Node abstractClassNode = nodeMap.get(ABSTRACT_CLASS_NODE_ID);
        assertThat(abstractClassNode.getId(), is(ABSTRACT_CLASS_NODE_ID));
        assertNodeHasTypes(abstractClassNode, new NodeType[]{NodeType.ABSTRACT_CLASS, NodeType.ABSTRACT_CLASS_OR_INTERFACE});
        assertTrue(abstractClassNode.getOperations().isEmpty());
        assertTrue(abstractClassNode.getAttributes().isEmpty());
    }

    private void assertInterface(Map<String, Node> nodeMap) {
        assertTrue(nodeMap.containsKey(INTERFACE_NODE_ID));
        final Node interfaceNode = nodeMap.get(INTERFACE_NODE_ID);
        assertThat(interfaceNode.getId(), is(INTERFACE_NODE_ID));
        assertNodeHasTypes(interfaceNode, new NodeType[]{NodeType.INTERFACE, NodeType.ABSTRACT_CLASS_OR_INTERFACE});
        assertTrue(interfaceNode.getAttributes().isEmpty());
        assertTrue(interfaceNode.getOperations().isEmpty());
    }

    private void assertDataType(Map<String, Node> nodeMap) {
        assertTrue(nodeMap.containsKey(INTEGER_HREF));
        final Node datatypeNode = nodeMap.get(INTEGER_HREF);
        assertThat(datatypeNode.getId(), is(INTEGER_HREF));
        assertThat(datatypeNode.getName(), is(Integer.class.getSimpleName()));
        assertNodeHasTypes(datatypeNode, new NodeType[]{NodeType.DATATYPE});
        assertTrue(datatypeNode.getAttributes().isEmpty());
        assertTrue(datatypeNode.getOperations().isEmpty());
    }

    private void assertNodeHasTypes(Node node, NodeType... types) {
        assertThat(node.getTypes().size(), is(types.length));
        Arrays.stream(types).forEach(nodeType -> assertTrue(node.getTypes().contains(nodeType)));
    }

    @Test
    public void testAnyException() {
        when(interfaceEvent.asEndElement()).thenThrow(new IllegalArgumentException());
        thrown.expect(ParseException.class);
        thrown.expectCause(is(IllegalArgumentException.class));
        thrown.expectMessage(String.format(XMI_FILE_COULD_NOT_BE_PARSED_MSG, xmiFile));
        nodeParser.parse(xmiFile);
    }

    @Test
    public void testParseException() {
        final String errorMsg = "Darn!";
        when(interfaceEvent.asEndElement()).thenThrow(new ParseException(errorMsg, null));
        thrown.expect(ParseException.class);
        thrown.expectMessage(errorMsg);
        nodeParser.parse(xmiFile);
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }

}
