package nl.ou.dpd.domain.edge;

import org.jgrapht.EdgeFactory;

import nl.ou.dpd.domain.node.Node;

public class RelationFactory implements EdgeFactory<Node, Relation> {

    public Relation createEdge(Node leftNode, Node rightNode) {
        final String id = String.format("%s-%s", leftNode.getId(), rightNode.getId());
        final String name = String.format("%s-%s", leftNode.getName(), rightNode.getName());
        return new Relation(id, name);
    }

}
