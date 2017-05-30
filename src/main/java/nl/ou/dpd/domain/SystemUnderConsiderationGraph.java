package nl.ou.dpd.domain;

import nl.ou.dpd.domain.edge.Relation;
import nl.ou.dpd.domain.node.Node;
import org.jgrapht.graph.DefaultDirectedGraph;

public class SystemUnderConsiderationGraph extends DefaultDirectedGraph<Node, Relation> {
	
	private String id;
    private String name;

    public SystemUnderConsiderationGraph(String id, String name) {
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
