package nl.ou.dpd.domain.relation;

import java.util.Objects;

/**
 * A {@link RelationProperty} is an object containing properties of a relation between two nodes. Currently, it contains
 * the {@link RelationType} and {@link Cardinality}s (left and right) of a relation.
 *
 * @author Martin de Boer
 */
public class RelationProperty {

    private final RelationType relationType;
    private Cardinality cardinalityLeft;
    private Cardinality cardinalityRight;

    public RelationProperty(RelationType relationType, Cardinality cardinalityLeft, Cardinality cardinalityRight) {
        this.relationType = relationType;
        this.cardinalityLeft = cardinalityLeft;
        this.cardinalityRight = cardinalityRight;
    }

    public RelationProperty(RelationType relationType) {
        this.relationType = relationType;
        this.cardinalityLeft = Cardinality.valueOf("1");
        this.cardinalityRight = Cardinality.valueOf("1");
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public Cardinality getCardinalityLeft() {
        return cardinalityLeft;
    }

    public RelationProperty setCardinalityLeft(Cardinality cardinalityLeft) {
        this.cardinalityLeft = cardinalityLeft;
        return this;
    }

    public Cardinality getCardinalityRight() {
        return cardinalityRight;
    }

    public RelationProperty setCardinalityRight(Cardinality cardinalityRight) {
        this.cardinalityRight = cardinalityRight;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelationProperty that = (RelationProperty) o;
        return relationType == that.relationType &&
                Objects.equals(cardinalityLeft, that.cardinalityLeft) &&
                Objects.equals(cardinalityRight, that.cardinalityRight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relationType, cardinalityLeft, cardinalityRight);
    }

}
