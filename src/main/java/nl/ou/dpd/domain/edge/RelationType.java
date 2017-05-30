package nl.ou.dpd.domain.edge;

import nl.ou.dpd.domain.rule.RelationRuleCategory;

/**
 * This enumeration defines a set of relation types. A relation type implies a rule that is applies to a relation
 * between two nodes. Such a rule falls within a certain {@link RelationRuleCategory}, that is one of the attributes of a
 * relation type. The other attribute is a description, that is used for feedback purposes whenever a relation does
 * not comply to the rule.
 * <p>
 * Example: {@link RelationType#X} implies that the first node of the relation extends from the secodn node of the
 * relation. The rule is categorized under {@link RelationRuleCategory#RELATIONS}. Whenever the rule is applied to a relation,
 * and fails, the {@code description} is used to generate feedback, by replacing the first "%s" with the id of the
 * first node, and the second "%s" with the id of the second node.
 *
 * @author Martin de Boer
 */
public enum RelationType {
    S(RelationRuleCategory.RELATIONS, "%s has a directed association with %s."),
    D(RelationRuleCategory.RELATIONS, "%s is dependent on %s."),
    X(RelationRuleCategory.RELATIONS, "%s must extend from %s."),
    I(RelationRuleCategory.RELATIONS, "%s must implement interface %s."),
    A(RelationRuleCategory.ATTRIBUTES, "%s must have an attribute of type %s."),
    T(RelationRuleCategory.OTHER, "%s must use %s in a generic type declaration."),
    L(RelationRuleCategory.OTHER, "%s must must have a method that defines a local variable of type %s that is neither a filed type nor created by the former."),
    P(RelationRuleCategory.METHODS, "%s must have a method that has an input Parameter with type %s."),
    R(RelationRuleCategory.METHODS, "%s must have a method with the return type %s."),
    M(RelationRuleCategory.METHODS, "%s must have a method that calls a method of %s."),
    F(RelationRuleCategory.ATTRIBUTES, "%s directly access fields of %s without method call."),
    C(RelationRuleCategory.ATTRIBUTES, "%s must create objects of %s."),
    O(RelationRuleCategory.METHODS, "%s must override methods of %s.");

    private final RelationRuleCategory relationRuleCategory;
    private final String description;

    RelationType(RelationRuleCategory relationRuleCategory, String description) {
        this.relationRuleCategory = relationRuleCategory;
        this.description = description;
    }

    public RelationRuleCategory getRelationRuleCategory() {
        return relationRuleCategory;
    }

    public String getDescription() {
        return description;
    }
};
