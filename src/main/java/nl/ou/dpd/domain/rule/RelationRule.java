package nl.ou.dpd.domain.rule;

import java.util.Objects;

import nl.ou.dpd.domain.edge.Cardinality;
import nl.ou.dpd.domain.edge.RelationType;

/**
 * TODO
 *
 * @author Martin de Boer
 */
public class RelationRule {

    private final RelationType relationType;
    private final Cardinality cardinalityLeft;
    private final Cardinality cardinalityRight;

    public RelationRule(RelationType relationType, Cardinality cardinalityLeft, Cardinality cardinalityRight) {
        this.relationType = relationType;
        this.cardinalityLeft = cardinalityLeft;
        this.cardinalityRight = cardinalityRight;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelationRule that = (RelationRule) o;
        return relationType == that.relationType &&
                Objects.equals(cardinalityLeft, that.cardinalityLeft) &&
                Objects.equals(cardinalityRight, that.cardinalityRight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relationType, cardinalityLeft, cardinalityRight);
    }
}
