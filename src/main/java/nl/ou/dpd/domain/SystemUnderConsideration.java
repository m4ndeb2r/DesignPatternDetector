package nl.ou.dpd.domain;

import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationFactory;
import org.jgrapht.graph.DefaultDirectedGraph;

/**
 * A {@link SystemUnderConsideration} is a {@link DefaultDirectedGraph} representation of a system under consideration
 * (a system design that is to be processed to find matching design patterns).
 *
 * @author Martin de Boer
 */
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
