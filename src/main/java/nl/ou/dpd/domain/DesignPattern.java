package nl.ou.dpd.domain;

import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationFactory;
import nl.ou.dpd.domain.matching.CompoundComparator;
import nl.ou.dpd.domain.node.Node;
import org.jgrapht.graph.DefaultDirectedGraph;

public class DesignPattern extends DefaultDirectedGraph<Node, Relation> {

    final private String name;
    private CompoundComparator<Relation> relationComparator;
    private CompoundComparator<Node> nodeComparator;

    public DesignPattern(String name) {
        super(new RelationFactory());
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public CompoundComparator<Relation> getRelationComparator() {
        return relationComparator;
    }

    public void setRelationComparator(CompoundComparator<Relation> relationComparator) {
        this.relationComparator = relationComparator;
    }

    public CompoundComparator<Node> getNodeComparator() {
        return nodeComparator;
    }

    public void setNodeComparator(CompoundComparator<Node> nodeComparator) {
        this.nodeComparator = nodeComparator;
    }

}
