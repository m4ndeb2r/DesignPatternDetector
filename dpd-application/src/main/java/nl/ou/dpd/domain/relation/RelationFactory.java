package nl.ou.dpd.domain.relation;

import nl.ou.dpd.domain.node.Node;
import org.jgrapht.EdgeFactory;

/**
 * A {@link RelationFactory} implements an {@link EdgeFactory}, that is used by any JGraph implementation based
 * graphs.
 *
 * @author Martin de Boer
 */
public class RelationFactory implements EdgeFactory<Node, Relation> {

    public Relation createEdge(Node leftNode, Node rightNode) {
        final String id = String.format("%s-%s", leftNode.getId(), rightNode.getId());
        final String name = String.format("%s-%s", leftNode.getName(), rightNode.getName());
        return new Relation(id, name);
    }
}
