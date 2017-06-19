package nl.ou.dpd.domain.relation;


import org.jgrapht.graph.DefaultEdge;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Relation extends DefaultEdge {

    private String id;
    private String name;
    private Set<RelationProperty> relationProperties;

    public Relation(String id, String name) {
        this.id = id;
        this.name = name;
        this.relationProperties = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public Relation setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Relation setName(String name) {
        this.name = name;
        return this;
    }

    public Set<RelationProperty> getRelationProperties() {
        return relationProperties;
    }

    public Relation addRelationProperty(RelationProperty relationProperties) {
        this.relationProperties.add(relationProperties);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relation relation = (Relation) o;
        return Objects.equals(id, relation.id) &&
                Objects.equals(name, relation.name) &&
                Objects.equals(relationProperties, relation.relationProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, relationProperties);
    }
}

