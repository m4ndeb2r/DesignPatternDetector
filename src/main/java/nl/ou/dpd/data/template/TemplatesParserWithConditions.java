/**
 * 
 */
package nl.ou.dpd.data.template;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.domain.node.Interface;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.rule.AttributeRule;
import nl.ou.dpd.domain.rule.Condition;
import nl.ou.dpd.domain.rule.EdgeRule;
import nl.ou.dpd.domain.rule.NodeRule;
import nl.ou.dpd.domain.rule.Operator;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.Scope;
import nl.ou.dpd.domain.rule.Topic;
import nl.ou.dpd.exception.DesignPatternDetectorException;

/**
 * This class implements a parser for a template.xml with a structure- and conditions part, including parsing nodes, edges, attributes and conditions.
 * If successfully, the parsing results in a list of templates containing the edges with corresponding nodes with their attributes and all properties defining the pattern.
 * Also, conditions with rules are made, ready to evaluate the pattern.
 * 
 * TODO: implement parsing of methods.  
 * 
 * @author Peter Vansweevelt
 *
 */
public class TemplatesParserWithConditions {
	
    private static final Logger LOGGER = LogManager.getLogger(TemplatesParserWithConditions.class);
    
    private List<DesignPattern> designPatterns;
    private DesignPattern designPattern;
    private List<Condition> conditions;
    private List<Node> nodes;

    private Map<XMLEvent, Node> attributeEventsPerNode;
    /**
     * Parses a template file with the specified {@code filename}.
     *
     * @param filename the name of the file to be parsed.
     * @return a list of {@link DesignPattern}s.
     */
    public List<DesignPattern> parse(String filename) {
 
		//initialize lists
		designPatterns = new ArrayList<DesignPattern>();
		nodes = new ArrayList<Node>();
		conditions = new ArrayList<Condition>();
		attributeEventsPerNode = new HashMap<XMLEvent, Node>();
	
    	XMLInputFactory factory = XMLInputFactory.newInstance();
    	
    	try {
	    	InputStream input = new FileInputStream(new File(filename));
	        XMLEventReader eventReader = factory.createXMLEventReader(input);	        
	        handleEvents(eventReader);
	    } catch (XMLStreamException e) {
            final String msg = "The pattern template file " + filename + " could not be parsed.";
            LOGGER.error(msg, e);
            throw new DesignPatternDetectorException(msg, e);
        } catch (FileNotFoundException e) {
            final String msg = "The pattern template file " + filename + " could not be found.";
            LOGGER.error(msg, e);
            throw new DesignPatternDetectorException(msg, e);
        }    	
    	return designPatterns;
    }

	/**
	 * General method to iterate the events of the XML.
	 * @param eventReader
	 * @throws XMLStreamException 
	 */
	private void handleEvents(XMLEventReader eventReader) throws XMLStreamException {
	    while (eventReader.hasNext()) {
	    	XMLEvent event = eventReader.nextEvent();
	    	handleEvent(event);
	    }        
	}

	/**
	 * General method to handle events.
	 * All possible startelements are listed. If an element is read which is not listed, an error is thrown.
	 * Endelements are only listed if there is an action bound to the endevent. 
	 * @param event
	 */
	private void handleEvent(XMLEvent event) {
		if (event.isStartElement()) {
			switch (event.asStartElement().getName().getLocalPart()) {
				case TemplateTag.TEMPLATES:
					break;
				case TemplateTag.TEMPLATE:
					//create a new template and add it to the templates list 
					designPattern = createTemplate(readAttributes(event));
					designPatterns.add(designPattern);
					//clear lists for the new template
					nodes.clear();
					conditions.clear();
					attributeEventsPerNode.clear();
					break;
				case TemplateTag.STRUCTURE:
					break;
				case TemplateTag.NODES:
 					break;
				case TemplateTag.NODE:
					//add a new node to the nodes list
					Node node = createNode(readAttributes(event));
					if (!nodeIdIsUnique(node)) {
						error("The node id " + node.getId() + " is not unique in this pattern.");
					}
					nodes.add(node);
					//make a condition concerning the node type and add it to the conditions list
					Condition condition = makeNodeTypeCondition(node);
					conditions.add(condition);
					break;
				case TemplateTag.ATTRIBUTE:
					//Save the attribute event with its corresponding node
					attributeEventsPerNode.put(event, nodes.get(nodes.size() - 1));
					break;
				case TemplateTag.METHOD:
					break;
				case TemplateTag.EDGES:
					break;
				case TemplateTag.EDGE:
					//add a new edge to the designPattern
					Edge edge = createEdge(readAttributes(event));
					designPattern.addRealEdge(edge);
					break;
				case TemplateTag.CONDITIONS:
					break;
				case TemplateTag.CONDITION:
					//make a condition and add it to the conditions list
					condition = makeCondition(readAttributes(event));
					conditions.add(condition);
					break;
				case TemplateTag.RULE:
					//make a rule and add it to the condition
					Rule rule = makeRule(readAttributes(event));
					addRuleToCondition(rule, conditions.get(conditions.size() - 1));
					//apply rule on designpattern node, edge or attribute
					applyRule(rule, readAttributes(event).get(TemplateAttribute.VALUE));
					break;
				default:
		            error("The pattern template tag " + event.asStartElement().getName().getLocalPart() + " could not be handled.");
			}
		}
		
		if (event.isEndElement()) {
			switch (event.asEndElement().getName().getLocalPart()) {
				case TemplateTag.NODES:
					//make attributes and add them to the corresponding nodes using the attributeEventsPerNode Map
					createAttributes();
					break;
				case TemplateTag.EDGES:
					//make attribute exist conditions
					makeAttributeExistsConditions();
					break;
			}
		}	
	}

	/**
	 * General method to read {@link Attribute}s of an xml-startevent.
	 * @param {@link XMLEvent}
	 * @return a Map<String, String> holding the attributes with the attribute name as key and the attribute value as value
	 */
	private Map<String, String> readAttributes(XMLEvent event) {
		//holds the attributes with the attribute name as key and the attribute value as value
		Map<String, String> attributes = new HashMap<String, String>();
		Iterator<Attribute> attrIterator = event.asStartElement().getAttributes();
		while (attrIterator.hasNext()) {
			Attribute attr = attrIterator.next();
			attributes.put(attr.getName().getLocalPart(), attr.getValue());			
		}
		return attributes;		
	}
	
	/**
	 * Create a new {@link DesignPattern) with given name
	 * @param attributes a map of attributes holding the name
	 * @return a new DesignPattern with the specified name.
	 */
	private DesignPattern createTemplate(Map<String, String> attributes) {
		String name = attributes.get(TemplateAttribute.NAME);
		return new DesignPattern(name);		
	}

	/**
	 * Create a new {@link Node) with given id and type.
	 * @param attributes a map of attributes holding the id and type
	 * @return a new Node with the specified name and type.
	 */
	private Node createNode(Map<String, String> attributes) {
		String id = attributes.get(TemplateAttribute.ID);
		String type = attributes.get(TemplateAttribute.TYPE);
		if (type.equalsIgnoreCase("class")) {
			return new Clazz(id, id);
		}
		if (type.equalsIgnoreCase("interface")) {
			return new Interface(id, id);
		}
		return null;
	}
	
	/**
	 * Make a {@link Condition) stating that a {@ Node} must have a particular type as given in the {@code structure} of the template.
	 * @param node
	 * @return a new Condition with an auto-generated id and description.
	 */
	private Condition makeNodeTypeCondition(Node node) {
		String id = "SystemCondition " + (conditions.size() + 1);
		String description = "The node " + node.getName() + " must be of type "  + node.getType().toString(); 
		Condition condition = new Condition(id, description);
		NodeRule rule = new NodeRule(node, Scope.OBJECT, Topic.TYPE, Operator.EQUALS);
		condition.getNodeRules().add(rule);
		return condition;		
	}

	/**
	 * Checks if the id of the new {@link Node) is unique in this pattern.
	 * @param node
	 * @return true if the node is unique in this pattern, false otherwise.
	 */
	private boolean nodeIdIsUnique(Node node) {
		for (Node n : nodes) {
			if (n.getId().equals(node.getId())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the id of the new {@link Edge) is unique in this pattern.
	 * @param edge
	 * @return true if the edge is unique in this pattern, false otherwise.
	 */
	private boolean edgeIdIsUnique(Edge edge) {
		for (Edge e : designPattern.getEdges()) {
			if (e.getId().equals(edge.getId())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Create all the attributes and add them to the corresponding {@link Node)s, using the Set of {@link XMLEvent)s assembled during the streamed reading.
	 */
	private void createAttributes() {
		Iterator<XMLEvent> attributeIterator = attributeEventsPerNode.keySet().iterator();
		while (attributeIterator.hasNext()) {
			XMLEvent event = attributeIterator.next();
			Map <String, String> parameters = readAttributes(event);
			nl.ou.dpd.domain.node.Attribute attribute = createAttribute(parameters);
			
			Node node = attributeEventsPerNode.get(event);
			node.getAttributes().add(attribute);
		}		
	}

	/**
	 * Create a new node-attribute with the name and type specified in the Map.
	 * @param attributes a Map of attributes of the Attribute XML StartEvent.
	 * @return a new node-attribute with the specified name and type (found in the list of Nodes).
	 */
	private nl.ou.dpd.domain.node.Attribute createAttribute(Map<String, String> attributes) {
		String id = attributes.get(TemplateAttribute.ID);
		String type = attributes.get(TemplateAttribute.TYPE);
		Node node = findNodeById(type);
		nl.ou.dpd.domain.node.Attribute attr = new nl.ou.dpd.domain.node.Attribute(id, node);
		return attr;
	}

	/**
	 * Find a Node by id.
	 * @param id the id as a String
	 * @return the node object with the name of 'id'
	 */
	private Node findNodeById(String id) {
		for (Node n : nodes) {
			if (n.getId().equals(id)) {
				return n;
			}
		}
		return null;
	}

	/**
	 * Find an Attribute by id.
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

	/**
	 * Find an Edge by id.
	 * @param id the id as a String
	 * @return the edge object with the id of 'id'
	 */
	private Edge findEdgeById(String id) {
		for (Edge e : designPattern.getEdges()) {
			if (e.getId().equals(id)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * Create an {@link Edge} with the specified attributes: id, node1 and node2.
	 * @param attributes
	 * @return a new Edge with the specified name and type.
	 */
	private Edge createEdge(Map<String, String> attributes) {
		String id = attributes.get(TemplateAttribute.ID);
		String node1Name = attributes.get(TemplateAttribute.NODE1);
		String node2Name = attributes.get(TemplateAttribute.NODE2);
		Node node1 = findNodeById(node1Name);
		Node node2 = findNodeById(node2Name);
		Edge edge = new Edge(id, id, node1, node2);
		if (!edgeIdIsUnique(edge)) {
			error("The edge id " + edge.getId() + " is not unique in this pattern.");
		}
		return edge;
	}
	
	/**
	 * Make {@link Condition)s stating that a node must have the attributes as given in the {@code structure} of the {@link Node}.
	 */
	private Condition makeAttributeExistsConditions() {
		for(Node node : nodes) {
			for (nl.ou.dpd.domain.node.Attribute attr : node.getAttributes()) {
				makeAttributeExistsCondition(node, attr);
			}
		}
	return null;
	}
	
	/**
	 * Make a {@link Condition) stating that a node must have an attribute as given in the {@code structure} of the {@link Node}.
	 * @param node
	 * @return condition a new Condition with an auto-generated id and description.
	 */
	private Condition makeAttributeExistsCondition(Node node, nl.ou.dpd.domain.node.Attribute attr) {
		
		Node attributeType = attr.getType();		
		String id = "SystemCondition " + conditions.size() + 1;
		String description = "The node " + node.getName() + " must have an attribute of type "  + attributeType.getId();
		Condition condition = new Condition(id, description);

		Edge edge = findEdgeByTwoNodes(node, attributeType);
		if (edge == null) {
			error("An edge between " + node.getId() + " and " + attributeType.getId() + " could not be found.");			
		}
		EdgeRule rule = new EdgeRule(edge, Scope.ATTRIBUTE, Topic.TYPE, Operator.EXISTS);
		condition.getEdgeRules().add(rule);		
		return condition;		
	}

	private Edge findEdgeByTwoNodes(Node leftNode, Node rightNode) {
		for (Edge e : designPattern.getEdges()) {
			if (e.getLeftNode() == leftNode && e.getRightNode() == rightNode) {
				return e;
			}
		}
		return null;
	}

	/**
	 * Makes a {@link Condition} with the id and description as given in the Map.
	 * @param attributes a Map of attributenames and -values
	 * @return a new Condition with the specified id and description
	 */
	private Condition makeCondition(Map<String, String> attributes) {
		String id = attributes.get(TemplateAttribute.ID);
		String description = attributes.get(TemplateAttribute.DESCRIPTION);
		return new Condition(id, description);
	}
	
	/**
	 * Makes a {@link Rule} with the elements (applies,scope, topic, operator and not) as given in the Map.
	 * @param attributes a Map of attributenames and -values
	 * @return a new Rule with the specified elements
	 */
	private Rule makeRule(Map<String, String> attributes) {
		String applies = attributes.get(TemplateAttribute.APPLIES);
		String scope = attributes.get(TemplateAttribute.SCOPE);
		String topic = attributes.get(TemplateAttribute.TOPIC);
		String operator = attributes.get(TemplateAttribute.OPERATOR);
		String not = attributes.get(TemplateAttribute.NOT);
		
		if (findNodeById(applies) != null) {
			//a nodeRule
			return new NodeRule(findNodeById(applies), findScope(scope), findTopic(topic), findOperator(operator, not));						
		}
		if (findEdgeById(applies) != null) {
			//an edgeRule
			return new EdgeRule(findEdgeById(applies), findScope(scope), findTopic(topic), findOperator(operator, not));						
		}
		if (findAttributeById(applies) != null) {
			//an attributeRule
			return new AttributeRule(findAttributeById(applies), findScope(scope), findTopic(topic), findOperator(operator, not));						
		}

		return null;
	}

	/**
	 * Find an Enum element of type {@link Scope} by a given String.
	 * @param scope given as a String
	 * @return the {@link Scope} element if found, {@code null} otherwise.
	 */
	public Scope findScope(String scope) {
		if (scope.equalsIgnoreCase("RELATION")) {
				return Scope.RELATION;
		}
		if (scope.equalsIgnoreCase("OBJECT")) {
			return Scope.OBJECT;
		}
		if (scope.equalsIgnoreCase("ATTRIBUTE")) {
			return Scope.ATTRIBUTE;
		}
		return null;		
	}
	/**
	 * Find an Enum element of type {@link Topic} by a given String.
	 * @param scope given as a String
	 * @return the {@link Topic} element if found, {@code null} otherwise.
	 */
	public Topic findTopic(String topic) {
		if (topic.equalsIgnoreCase("TYPE")) {
			return Topic.TYPE;
		}
		if (topic.equalsIgnoreCase("VISIBILITY")) {
			return Topic.VISIBILITY;
		}
		if (topic.equalsIgnoreCase("MODIFIER_ROOT")) {
			return Topic.MODIFIER_ROOT;
		}
		if (topic.equalsIgnoreCase("MODIFIER_LEAF")) {
			return Topic.MODIFIER_LEAF;
		}
		if (topic.equalsIgnoreCase("MODIFIER_ABSTRACT")) {
			return Topic.MODIFIER_ABSTRACT;
		}
		if (topic.equalsIgnoreCase("MODIFIER_ACTIVE")) {
			return Topic.MODIFIER_ACTIVE;
		}
		if (topic.equalsIgnoreCase("CARDINALITY-FRONT")) {
			return Topic.CARDINALITY_LEFT;
		}
		if (topic.equalsIgnoreCase("CARDINALITY-END")) {
			return Topic.CARDINALITY_RIGHT;
		}
		return null;
	}
	
	/**
	 * Find an Enum element of type {@link Operator} by a given String.
	 * @param scope given as a String
	 * @return the {@link Operator} element if found, {@code null} otherwise.
	 */
	public Operator findOperator(String operator, String not) {
		if (not == null || not == "") not = "false";
		
		if (operator.equalsIgnoreCase("EQUALS") && not.equalsIgnoreCase("FALSE")) {
				return Operator.EQUALS;
		}
		if (operator.equalsIgnoreCase("NOT_EQUALS") && not.equalsIgnoreCase("TRUE")) {
			return Operator.NOT_EQUALS;
		}
		if (operator.equalsIgnoreCase("EXISTS") && not.equalsIgnoreCase("FALSE")) {
			return Operator.EXISTS;
		}
		if (operator.equalsIgnoreCase("NOT_EXISTS") && not.equalsIgnoreCase("TRUE")) {
			return Operator.NOT_EXISTS;
		}
		return null;		
	}
	
	/**
	 * Adds a {@link Rule} to a {@link Condition}.
	 * @param rule
	 * @param condition
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
        final Exception e = new XMLStreamException();
        LOGGER.error(message);
        throw new DesignPatternDetectorException(message, e);
    }
}
