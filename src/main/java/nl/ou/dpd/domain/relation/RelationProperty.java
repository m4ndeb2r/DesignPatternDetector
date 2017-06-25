package nl.ou.dpd.domain.relation;

import java.util.Objects;

/**
 * TODO
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

    public void setCardinalityLeft(Cardinality cardinalityLeft) {
        this.cardinalityLeft = cardinalityLeft;
    }

    public Cardinality getCardinalityRight() {
        return cardinalityRight;
    }

    public void setCardinalityRight(Cardinality cardinalityRight) {
        this.cardinalityRight = cardinalityRight;
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

    @Override
    public String toString() {
        return String.format(
                "[ relation type = '%s', cardinality left = '%s', cardinality right = '%s' ]",
                relationType,
                cardinalityLeft,
                cardinalityRight);
    }
}
