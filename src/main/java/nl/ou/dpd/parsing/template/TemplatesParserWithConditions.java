package nl.ou.dpd.parsing.template;

import com.sun.deploy.util.StringUtils;
import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.domain.node.Interface;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.NodeType;
import nl.ou.dpd.domain.rule.AttributeRule;
import nl.ou.dpd.domain.rule.Condition;
import nl.ou.dpd.domain.rule.EdgeRule;
import nl.ou.dpd.domain.rule.NodeRule;
import nl.ou.dpd.domain.rule.Operator;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.Scope;
import nl.ou.dpd.domain.rule.Topic;
import nl.ou.dpd.exception.DesignPatternDetectorException;
import nl.ou.dpd.parsing.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class implements a parser for a template.xml with a structure- and conditions part, including parsing nodes,
 * edges, attributes and conditions. If successfully, the parsing results in a list of templates containing the edges
 * with corresponding nodes with their attributes and all properties defining the pattern. Also, conditions with rules
 * are made, ready to evaluate the pattern.
 * <p>
 * TODO: implement parsing of methods.
 *
 * @author Peter Vansweevelt
 */
public class TemplatesParserWithConditions {

    private static final Logger LOGGER = LogManager.getLogger(TemplatesParserWithConditions.class);

    // Template attributes
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String TYPE = "type";
    private static final String NODE1 = "node1";
    private static final String NODE2 = "node2";
    private static final String DESCRIPTION = "description";
    private static final String APPLIES = "applies";
    private static final String SCOPE = "scope";
    private static final String TOPIC = "topic";
    private static final String OPERATOR = "operator";
    private static final String VALUE = "value";

    // Template tags
    private static final String TEMPLATE = "template";
    private static final String NODES = "nodes";
    private static final String NODE = "node";
    private static final String ATTRIBUTE = "attribute";
    private static final String METHOD = "method";
    private static final String EDGES = "edges";
    private static final String EDGE = "edge";
    private static final String CONDITION = "condition";
    private static final String RULE = "rule";

    private List<DesignPattern> designPatterns;
    private DesignPattern designPattern;
    //private List<Condition> conditions;
    private List<Node> nodes;

    private Map<XMLEvent, Node> attributeEventsPerNode;

    /**
     * Parses a template file with the specified {@code filename}.
     *
     * @param filename the name of the file to be parsed.
     * @return a list of {@link DesignPattern}s.
     */
    public List<DesignPattern> parse(String xmlFilename, URL xsdUrl) {
        designPatterns = new ArrayList<>();
        nodes = new ArrayList<>();
        //conditions = new ArrayList<>();
        attributeEventsPerNode = new HashMap<>();

        try {
            validate(xmlFilename, xsdUrl);
            parse(xmlFilename);
        }
        catch (ParseException pe) {
            // We don't need to repackage a ParseException in a ParseException.
            // Rethrow ParseExceptions directly
            throw pe;
        }
        catch (Exception e) {
            final String message = String.format("The pattern template file '%s' could not be parsed.", xmlFilename);
            error(message, e);
        }

        return designPatterns;
    }

    private void parse(String xmlFilename) throws FileNotFoundException, XMLStreamException {
        final InputStream input = new FileInputStream(new File(xmlFilename));
        final XMLInputFactory factory = XMLInputFactory.newInstance();
        final XMLEventReader eventReader = factory.createXMLEventReader(input);
        handleEvents(eventReader);
    }

    private void validate(String xmlFilename, URL xsdUrl) throws IOException, SAXException {
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Schema schema = schemaFactory.newSchema(xsdUrl);
        final Validator validator = schema.newValidator();
        final InputStream stream = new FileInputStream(new File(xmlFilename));
        validator.validate(new StreamSource(stream));
    }

    private void handleEvents(XMLEventReader eventReader) throws XMLStreamException {
        while (eventReader.hasNext()) {
            handleEvent(eventReader.nextEvent());
        }
    }

    private void handleEvent(XMLEvent event) {
        if (event.isStartElement()) {
            handleStartElement(event);
        }
        if (event.isEndElement()) {
            handleEndElement(event);
        }
    }

    private void handleStartElement(XMLEvent event) {
        switch (event.asStartElement().getName().getLocalPart()) {
            case TEMPLATE:
                handleTemplateStartElement(event);
                break;
            case NODE:
                handleNodeStartElement(event);
                break;
            case ATTRIBUTE:
                handleAttributeStartElement(event);
                break;
            case EDGE:
                handleEdgeStartElement(event);
                break;
            case CONDITION:
                handleConditionStartElement(event);
                break;
            case RULE:
                handleRuleStartElement(event);
                break;
            case METHOD:
                handleMethodStartElement(event);
                break;
            default:
                break;
        }
    }

    private void handleMethodStartElement(XMLEvent event) {
        // TODO: not implemented yet
    }

    private void handleRuleStartElement(XMLEvent event) {
        final Rule rule = makeRule(readAttributes(event));
        final List<Condition> conditions = designPattern.getConditions();
        addRuleToCondition(rule, conditions.get(conditions.size() - 1));
        applyRule(rule, readAttributes(event).get(VALUE));
    }

    private void handleConditionStartElement(XMLEvent event) {
        final List<Condition> conditions = designPattern.getConditions();
        conditions.add(makeCondition(readAttributes(event)));
    }

    private void handleEdgeStartElement(XMLEvent event) {
        Edge edge = createEdge(readAttributes(event));
        designPattern.addRealEdge(edge);
    }

    private void handleAttributeStartElement(XMLEvent event) {
        attributeEventsPerNode.put(event, nodes.get(nodes.size() - 1));
    }

    private void handleNodeStartElement(XMLEvent event) {
        final List<Condition> conditions = designPattern.getConditions();
        final Node node = createNode(readAttributes(event));
        if (!nodeIdIsUnique(node)) {
            error(String.format("The node id '%s' is not unique in this pattern.", node.getId()));
        }
        nodes.add(node);
        conditions.add(makeNodeTypeCondition(node));
    }

    private void handleTemplateStartElement(XMLEvent event) {
        designPattern = createTemplate(readAttributes(event));
        designPatterns.add(designPattern);

        nodes.clear();
        attributeEventsPerNode.clear();
    }

    private void handleEndElement(XMLEvent event) {
        switch (event.asEndElement().getName().getLocalPart()) {
            case NODES:
                createAttributes();
                break;
            case EDGES:
                makeAttributeExistsConditions();
                break;
        }
    }

    /**
     * General method to read {@link Attribute}s of an xml-startevent.
     *
     * @param {@link XMLEvent}
     * @return a Map<String, String> holding the attributes with the attribute name as key and the attribute value as value
     */
    private Map<String, String> readAttributes(XMLEvent event) {
        Map<String, String> attributes = new HashMap<>();
        Iterator<Attribute> attrIterator = event.asStartElement().getAttributes();
        while (attrIterator.hasNext()) {
            Attribute attr = attrIterator.next();
            attributes.put(attr.getName().getLocalPart(), attr.getValue());
        }
        return attributes;
    }

    /**
     * Create a new {@link DesignPattern) with given name.
     *
     * @param attributes a map of attributes holding the name
     * @return a new DesignPattern with the specified name.
     */
    private DesignPattern createTemplate(Map<String, String> attributes) {
        String name = attributes.get(NAME);
        return new DesignPattern(name);
    }

    /**
     * Create a new {@link Node) with given id and type.
     *
     * @param attributes a map of attributes holding the id and type
     * @return a new Node with the specified name and type.
     */
    private Node createNode(Map<String, String> attributes) {
        final String id = attributes.get(ID);
        final NodeType type = NodeType.valueOf(attributes.get(TYPE));
        if (type == NodeType.INTERFACE) {
            return new Interface(id, id);
        }
        return new Clazz(id, id);
    }

    /**
     * Make a {@link Condition) stating that a {@link Node} must have a particular type, defined in the
     * {@code structure} of the template.
     *
     * @param node
     * @return a new Condition with an auto-generated id and description.
     */
    private Condition makeNodeTypeCondition(Node node) {
        final List<Condition> conditions = designPattern.getConditions();
        final String id = String.format("SystemCondition %d", conditions.size() + 1);
        final String description = String.format("The node '%s' must be of type '%s'.", node.getName(), node.getType());
        final Condition condition = new Condition(id, description);
        final NodeRule rule = new NodeRule(node, Scope.OBJECT, Topic.TYPE, Operator.EQUALS);
        condition.getNodeRules().add(rule);
        return condition;
    }

    private boolean nodeIdIsUnique(Node node) {
        return nodes.stream().noneMatch(n -> n.getId().equals(node.getId()));
    }

    private boolean edgeIdIsUnique(Edge edge) {
        return designPattern.getEdges().stream().noneMatch(e -> e.getId().equals(edge.getId()));
    }

    /**
     * Create all the attributes and add them to the corresponding {@link Node)s, using the Set of {@link XMLEvent)s
     * assembled during the streamed reading.
     */
    private void createAttributes() {
        final Iterator<XMLEvent> attributeIterator = attributeEventsPerNode.keySet().iterator();
        while (attributeIterator.hasNext()) {
            final XMLEvent event = attributeIterator.next();
            final Map<String, String> parameters = readAttributes(event);
            final nl.ou.dpd.domain.node.Attribute attribute = createAttribute(parameters);

            final Node node = attributeEventsPerNode.get(event);
            node.getAttributes().add(attribute);
        }
    }

    /**
     * Create a new node-attribute with the name and type specified in the Map.
     *
     * @param attributes a Map of attributes of the Attribute XML StartEvent.
     * @return a new node-attribute with the specified name and type (found in the list of Nodes).
     */
    private nl.ou.dpd.domain.node.Attribute createAttribute(Map<String, String> attributes) {
        final String id = attributes.get(ID);
        final String type = attributes.get(TYPE);
        final Node node = findNodeById(type);
        return new nl.ou.dpd.domain.node.Attribute(id, node);
    }

    private Node findNodeById(String id) {
        return nodes.stream()
                .filter(node -> node.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Find an Attribute by id.
     *
     * @param id the id as a String
     * @return the attribute object with the id of 'id'
     */
    private nl.ou.dpd.domain.node.Attribute findAttributeById(String id) {
        for (Node n : nodes) {
            for (nl.ou.dpd.domain.node.Attribute attr : n.getAttributes()) {
                if (attr.getId().equals(id)) {
                    return attr;
                }
            }
        }
        return null;
    }

    private Edge findEdgeById(String id) {
        return designPattern.getEdges().stream()
                .filter(edge -> edge.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Create an {@link Edge} with the specified attributes: id, node1 and node2.
     *
     * @param attributes
     * @return a new Edge with the specified name and type.
     */
    private Edge createEdge(Map<String, String> attributes) {
        final String id = attributes.get(ID);
        final String node1Name = attributes.get(NODE1);
        final String node2Name = attributes.get(NODE2);
        final Node node1 = findNodeById(node1Name);
        final Node node2 = findNodeById(node2Name);
        final Edge edge = new Edge(id, id, node1, node2);
        if (!edgeIdIsUnique(edge)) {
            error(String.format("The edge id '%s' is not unique in this pattern.", edge.getId()));
        }
        return edge;
    }

    /**
     * Make {@link Condition)s stating that a node must have the attributes as given in the {@code structure} of the
     * {@link Node}.
     */
    private void makeAttributeExistsConditions() {
        for (Node node : nodes) {
            for (nl.ou.dpd.domain.node.Attribute attr : node.getAttributes()) {
                makeAttributeExistsCondition(node, attr);
            }
        }
    }

    /**
     * A {@link Node} can have an attribute. This attribute, then, is of a certain type (which is also a {@link Node}
     * in the design pattern). If class A has an attribute of type B, then there must be a relation from A to B. In
     * other words, there must be an edge with a node A and a node B. This method creates a {@link Condition) stating
     * that a node must have an attribute hat complies with the {@code structure} of the {@link Node}.
     *
     * @param node
     * @param attr
     * @return a new {@link Condition} with an auto-generated id and description
     */
    private void makeAttributeExistsCondition(Node node, nl.ou.dpd.domain.node.Attribute attr) {
        final List<Condition> conditions = designPattern.getConditions();
        final Node attributeType = attr.getType();
        final String id = "SystemCondition " + conditions.size() + 1;
        final String description = String.format("The node '%s' must have an attribute of type '%s'.", node.getName(), attributeType.getId());
        final Condition condition = new Condition(id, description);

        final Edge edge = findEdgeByTwoNodes(node, attributeType);
        if (edge == null) {
            error(String.format("An edge between '%s' and '%s' could not be found.", node.getId(), attributeType.getId()));
        }
        final EdgeRule rule = new EdgeRule(edge, Scope.ATTRIBUTE, Topic.TYPE, Operator.EXISTS);
        condition.getEdgeRules().add(rule);
        conditions.add(condition); // TODO Dit even checken....?? Ook unittesten??
    }

    private Edge findEdgeByTwoNodes(Node leftNode, Node rightNode) {
        return designPattern.getEdges().stream()
                .filter(edge -> edge.getLeftNode() == leftNode && edge.getRightNode() == rightNode)
                .findFirst()
                .orElse(null);
    }

    /**
     * Makes a {@link Condition} with the id and description as given in the Map.
     *
     * @param attributes a Map of attributenames and -values
     * @return a new Condition with the specified id and description
     */
    private Condition makeCondition(Map<String, String> attributes) {
        final String id = attributes.get(ID);
        final String description = attributes.get(DESCRIPTION);
        return new Condition(id, description);
    }

    /**
     * Makes a {@link Rule} with the elements (applies,scope, topic, operator and not) as given in the Map.
     *
     * @param attributes a Map of attributenames and -values
     * @return a new Rule with the specified elements
     */
    private Rule makeRule(Map<String, String> attributes) {
        final String applies = attributes.get(APPLIES);
        final String scope = attributes.get(SCOPE);
        final String topic = attributes.get(TOPIC);
        final String operator = attributes.get(OPERATOR);

        if (findNodeById(applies) != null) {
            return new NodeRule(findNodeById(applies), Scope.valueOf(scope), Topic.valueOf(topic), Operator.valueOf(operator));
        }
        if (findEdgeById(applies) != null) {
            return new EdgeRule(findEdgeById(applies), Scope.valueOf(scope), Topic.valueOf(topic), Operator.valueOf(operator));
        }
        if (findAttributeById(applies) != null) {
            return new AttributeRule(findAttributeById(applies), Scope.valueOf(scope), Topic.valueOf(topic), Operator.valueOf(operator));
        }

        return null;
    }

    /**
     * Adds a {@link Rule} to a {@link Condition}.
     *
     * @param rule      the {@link Rule} to add
     * @param condition the {@link Condition} to add it to
     */
    private void addRuleToCondition(Rule rule, Condition condition) {
        if (rule instanceof NodeRule) {
            condition.getNodeRules().add((NodeRule) rule);
        }
        if (rule instanceof EdgeRule) {
            condition.getEdgeRules().add((EdgeRule) rule);
        }
        if (rule instanceof AttributeRule) {
            condition.getAttributeRules().add((AttributeRule) rule);
        }
    }

    /**
     * Applies a {@link Rule} using the specified value.
     * Results in the node of the Rule implementing the Rule in the Node itself.
     *
     * @param rule
     * @param value
     */
    private void applyRule(Rule rule, String value) {
        if (rule instanceof NodeRule) {
            ApplyRule applyRule = new ApplyNodeRule(rule, value);
            applyRule.apply();
        }
        if (rule instanceof EdgeRule) {
            ApplyRule applyRule = new ApplyEdgeRule(rule, value);
            applyRule.apply();
        }
        if (rule instanceof AttributeRule) {
            ApplyRule applyRule = new ApplyAttributeRule(rule, value);
            applyRule.apply();
        }
    }

    private void error(String message) {
        LOGGER.error(message);
        throw new ParseException(message);
    }

    private void error(String message, Exception cause) {
        LOGGER.error(message);
        throw new ParseException(message, cause);
    }
}
