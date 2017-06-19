package nl.ou.dpd.domain;

import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationFactory;
import nl.ou.dpd.domain.node.Node;
import org.jgrapht.graph.DefaultDirectedGraph;

public class SystemUnderConsideration extends DefaultDirectedGraph<Node, Relation> {
	
	private String id;
    private String name;

    public SystemUnderConsideration(String id, String name) {
        super(new RelationFactory());
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
