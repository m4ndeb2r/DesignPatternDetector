package nl.ou.dpd.parsing;

import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.Operation;
import nl.ou.dpd.domain.node.Parameter;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationProperty;
import nl.ou.dpd.domain.relation.RelationType;

import java.util.Set;

/**
 * This class extracts extra {@link Relation}s out of a {@link SystemUnderConsideration}, which are not explicitly in
 * the Class diagram. This step is the final part of the parsing process of an ArgoUML export file. Previous steps are
 * taken care of by the {@link ArgoUMLNodeParser} and the {@link ArgoUMLRelationParser}.
 * <p>
 * Relations can be added, based on {@link Attribute}s or based on {@link Operation}s that are present in a
 * {@link Node}.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 * @see ArgoUMLNodeParser
 * @see ArgoUMLRelationParser
 */
public class SystemRelationsExtractor {

    private SystemUnderConsideration system;

    /**
     * This constructor has protected access so it can only be instantiated from within the same package (by the
     * ParserFactory or in a unit test in the same package).
     */
    protected SystemRelationsExtractor() {
    }

    public SystemUnderConsideration execute(SystemUnderConsideration system) {
        this.system = system;
        for (Node node : system.vertexSet()) {
            exploreAttributesRelations(node);
            exploreOperationsRelations(node);
        }
        return this.system;
    }

    private void exploreAttributesRelations(Node node) {
        for (Attribute attr : node.getAttributes()) {
            updateOrCreateAttributeRelation(attr);
        }
    }

    private void updateOrCreateAttributeRelation(Attribute attribute) {
        Relation relation = system.getEdge(attribute.getParentNode(), attribute.getType());
        if (relation == null) {
            relation = new Relation(
                    "SystemRelation-" + attribute.getParentNode().getId() + "-" + attribute.getId(),
                    attribute.getParentNode().getName() + "-" + attribute.getName()
            );
        }
        relation.addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));
        addEdge(attribute, relation);
    }

    private void exploreOperationsRelations(Node node) {
        for (Operation operation : node.getOperations()) {
            updateOrCreateInParameterRelation(operation);
            updateOrCreateReturnParameterRelation(operation);
            updateOverrideRelation(node, operation);
        }
    }

    private void updateOverrideRelation(Node node, Operation operation) {
        //for all outgoing edges of node, look if the operation signature of the source equals the signature of the target
        for (Relation relation : system.outgoingEdgesOf(node)) {
            boolean hasOverrideOperation = containsSameSignatureOperation(relation, operation);
            if (hasOverrideOperation) {
                relation.addRelationProperty(new RelationProperty(RelationType.OVERRIDES_METHOD_OF));
            }
        }

    }

    private boolean containsSameSignatureOperation(Relation relation, Operation operation) {
        Set<Operation> targetOperations = system.getEdgeTarget(relation).getOperations();
        for (Operation targetOperation : targetOperations) {
            if (targetOperation.equalsSignature(operation)) {
                return true;
            }
        }
        return false;
    }

    private void updateOrCreateInParameterRelation(Operation operation) {
        for (Parameter param : operation.getParameters()) {
            Relation relation = system.getEdge(operation.getParentNode(), param.getType());
            if (relation == null) {
                relation = new Relation(
                        "SystemRelation-" + operation.getParentNode().getId() + "-" + operation.getId(),
                        operation.getParentNode().getName() + "-" + operation.getName()
                );
            }
            relation.addRelationProperty(new RelationProperty(RelationType.HAS_METHOD_PARAMETER_OF_TYPE));
            addEdge(operation, param, relation);
        }
    }

    private void updateOrCreateReturnParameterRelation(Operation operation) {
        if (operation.getReturnType() != null) {
            Relation relation = system.getEdge(operation.getParentNode(), operation.getReturnType());
            if (relation == null) {
                relation = new Relation(
                        "SystemRelation-" + operation.getParentNode().getId() + "-" + operation.getId(),
                        operation.getParentNode().getName() + "-" + operation.getName()
                );
            }
            relation.addRelationProperty(new RelationProperty(RelationType.HAS_METHOD_RETURNTYPE));
            addEdge(operation, relation);
        }
    }

    private void addEdge(Attribute attr, Relation relation) {
        Node source = attr.getParentNode();
        Node target = attr.getType();
        system.addEdge(source, target, relation);
    }

    private void addEdge(Operation operation, Parameter p, Relation relation) {
        Node source = operation.getParentNode();
        Node target = p.getType();
        system.addEdge(source, target, relation);
    }

    private void addEdge(Operation op, Relation relation) {
        Node source = op.getParentNode();
        Node target = op.getReturnType();
        system.addEdge(source, target, relation);
    }

}
