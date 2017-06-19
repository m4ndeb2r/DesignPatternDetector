package nl.ou.dpd.parsing.argoxmi;

import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.Operation;
import nl.ou.dpd.domain.node.Parameter;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationProperty;
import nl.ou.dpd.domain.relation.RelationType;
import nl.ou.dpd.parsing.ParseException;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * This class extracts extra {@link Relation}s out of a {@link SystemUnderConsideration}, 
 * which are not explicit in the Class diagram.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 */
public class SystemRelationsExtractor {
    private static final Logger LOGGER = LogManager.getLogger(SystemRelationsExtractor.class);

    private SystemUnderConsideration system;

    /**
     * Constructor of a SystemRelationsExtractor
	 * @param system
	 */
	public SystemRelationsExtractor(SystemUnderConsideration system) {
		this.system = system;
	}
	
	public void execute() {
		for (Node node : system.vertexSet()) {
			exploreAttributesRelations(node);			
			exploreOperationsRelations(node);
		}
	}

	/**
	 * Iterates the attributes of the given node.
	 * @param node
	 */
	private int exploreAttributesRelations(Node node) {
		int t = 0;
		for (Attribute attr : node.getAttributes()) {
			Relation relation = updateOrCreateAttributeRelation(attr);
			if (relation != null) {
				t++;
			}
		}
		return t;
	}

	/**
	 * Make a relation property for the attribute type.
	 */
	private Relation updateOrCreateAttributeRelation(Attribute attribute) {
		Relation relation = system.getEdge(attribute.getParentNode(), attribute.getType());
		if (relation == null) {
			relation = new Relation(
					"SystemRelation-" + attribute.getParentNode().getId() + "-" + attribute.getId(),
					attribute.getParentNode().getName() + "-" + attribute.getName()
					);
		}
		relation.addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));
		addEdge(attribute, relation);
		return relation;
	}

	/**
	 * @param node
	 */
	private void exploreOperationsRelations(Node node) {
		for (Operation operation : node.getOperations()) {
			exploreInParametersRelations(node, operation);
			exploreReturnParametersRelation(node, operation);
			updateOverrideRelation(node, operation);
		}
	}
	
    /**
	 * @param node
	 * @param operation
	 */
	private void updateOverrideRelation(Node node, Operation operation) {
		//for all outgoing edges of node, look if the operation signature of the source equals the signature of the target
		for (Relation relation : system.outgoingEdgesOf(node)) {
			boolean hasOverrideOperation = containsSameSignatureOperation(relation, operation);
			if (hasOverrideOperation) {
				relation.addRelationProperty(new RelationProperty(RelationType.OVERRIDES_METHOD_OF));
			}
		}
		
	}

	/**
	 * @param relation
	 * @param operation
	 */
	private boolean containsSameSignatureOperation(Relation relation, Operation operation) {
		Set<Operation> targetOperations = system.getEdgeTarget(relation).getOperations();
		for (Operation targetOperation : targetOperations) {
			if (targetOperation.equalsSignature(operation)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param node
     * @param operation2 
	 */
	private int exploreInParametersRelations(Node node, Operation operation) {
		int t = 0;
		Relation relation = updateOrCreateInParameterRelation(operation);
		if (relation != null) {
				t++;
		}
		return t;
	}

	/**
	 * @param op
	 */
	private Relation updateOrCreateInParameterRelation(Operation operation) {
		Relation relation = null;
		for (Parameter param : operation.getParameters()) {
			relation = system.getEdge(operation.getParentNode(), param.getType());
			if (relation == null) {
				relation = new Relation(
						"SystemRelation-" + operation.getParentNode().getId() + "-" + operation.getId(),
						operation.getParentNode().getName() + "-" + operation.getName()
						);
			}
			relation.addRelationProperty(new RelationProperty(RelationType.HAS_METHOD_PARAMETER_OF_TYPE));
			addEdge(operation, param, relation);
		}
		return relation;
	}

	private int exploreReturnParametersRelation(Node node, Operation operation) {
		int t = 0;
		Relation relation = updateOrCreateReturnParameterRelation(operation);
		if (relation != null) {
			t++;
		}
		return t;
	}

	private Relation updateOrCreateReturnParameterRelation(Operation operation) {
		Relation relation = null;
		if (operation.getReturnType() != null) {
			relation = system.getEdge(operation.getParentNode(), operation.getReturnType());
			if (relation == null) {
				relation = new Relation(
						"SystemRelation-" + operation.getParentNode().getId() + "-" + operation.getId(),
						operation.getParentNode().getName() + "-" + operation.getName()
						);
			}
			relation.addRelationProperty(new RelationProperty(RelationType.HAS_METHOD_RETURNTYPE));
			addEdge(operation, relation);
		}
		return relation;
	}

	/**
	 * @param attr
	 * @param relation
	 */
	private void addEdge(Attribute attr, Relation relation) {
		Node source = attr.getParentNode();
		Node target = attr.getType();
		system.addEdge(source, target, relation);
	}

	/**
	 * @param operation
	 * @param relation
	 * @param p
	 */
	private void addEdge(Operation operation, Parameter p, Relation relation) {
		Node source = operation.getParentNode();
		Node target = p.getType();
		system.addEdge(source, target, relation);
	}
	
	/**
	 * @param operation
	 * @param relation
	 */
	private void addEdge(Operation op, Relation relation) {
		Node source = op.getParentNode();
		Node target = op.getReturnType();
		system.addEdge(source, target, relation);
	}
	
	private void error(String msg, Exception cause) {
        LOGGER.error(msg, cause);
        throw new ParseException(msg, cause);
    }

}
