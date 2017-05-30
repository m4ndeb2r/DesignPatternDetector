package nl.ou.dpd.domain.edge;


import org.jgrapht.graph.DefaultEdge;

import nl.ou.dpd.domain.rule.RelationRule;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Relation extends DefaultEdge {

    private String id;
    private String name;
    private Set<RelationRule> relationRules;

    private Set<RelationType> relationTypes;
    private Cardinality cardinalityLeft;
    private Cardinality cardinalityRight;

    /**
	 * @return the relationTypes
	 */
	public Set<RelationType> getRelationTypes() {
		return relationTypes;
	}

	/**
	 * @param relationTypes the relationTypes to set
	 */
	public void setRelationTypes(Set<RelationType> relationTypes) {
		this.relationTypes = relationTypes;
	}

	/**
	 * @param relationTypes the relationTypes to set
	 */
	public void addRelationType(RelationType relationType) {
		this.relationTypes.add(relationType);
	}


	/**
	 * @return the cardinalityLeft
	 */
	public Cardinality getCardinalityLeft() {
		return cardinalityLeft;
	}

	/**
	 * @param cardinalityLeft the cardinalityLeft to set
	 */
	public void setCardinalityLeft(Cardinality cardinalityLeft) {
		this.cardinalityLeft = cardinalityLeft;
	}

	/**
	 * @return the cardinalityRight
	 */
	public Cardinality getCardinalityRight() {
		return cardinalityRight;
	}

	/**
	 * @param cardinalityRight the cardinalityRight to set
	 */
	public void setCardinalityRight(Cardinality cardinalityRight) {
		this.cardinalityRight = cardinalityRight;
	}

	public Relation(String id, String name) {
        this.id = id;
        this.name = name;
        this.relationRules = new HashSet<>();
        this.relationTypes = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RelationRule> getRelationRules() {
        return relationRules;
    }

    public void addRelationRule(RelationRule relationRule) {
        this.relationRules.add(relationRule);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relation relation = (Relation) o;
        return Objects.equals(id, relation.id) &&
                Objects.equals(name, relation.name) &&
                Objects.equals(relationRules, relation.relationRules);
    }

 /*   @Override
    public int hashCode() {
        return Objects.hash(id, name, relationRules);
  }
*/}

