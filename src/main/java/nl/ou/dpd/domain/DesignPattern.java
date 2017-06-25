package nl.ou.dpd.domain;

import nl.ou.dpd.domain.matching.CompoundComparator;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationFactory;
import org.jgrapht.graph.DefaultDirectedGraph;

public class DesignPattern extends DefaultDirectedGraph<Node, Relation> {

    final private String name;
    final private String family;

    private CompoundComparator<Relation> relationComparator;
    private CompoundComparator<Node> nodeComparator;

    public DesignPattern(String name, String family) {
        super(new RelationFactory());
        this.name = name;
        this.family = family;
    }

    public String getName() {
        return name;
    }

    public String getFamily() {
        return family;
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
