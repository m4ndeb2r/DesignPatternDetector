package nl.ou.dpd.domain.relation;

/**
 * This enumeration defines a set of relation types. HAS_ATTRIBUTE_OF relation type is one of the properties that is considered
 * when analysing a relation between two nodes. Such a characteristic falls within a certain {@link RelationCategory}.
 *
 * @author Martin de Boer
 */
public enum RelationType {
    ASSOCIATES_WITH(RelationCategory.RELATIONS),
    DEPENDS_ON(RelationCategory.RELATIONS),
    INHERITS_FROM(RelationCategory.RELATIONS),
    IMPLEMENTS(RelationCategory.RELATIONS),
    HAS_ATTRIBUTE_OF(RelationCategory.ATTRIBUTES),
    CREATES_INSTANCE_OF(RelationCategory.ATTRIBUTES),
    HAS_METHOD_PARAMETER_OF_TYPE(RelationCategory.METHODS),
    HAS_METHOD_RETURNTYPE(RelationCategory.METHODS),
    CALLS_METHOD_OF(RelationCategory.METHODS),
    OVERRIDES_METHOD_OF(RelationCategory.METHODS);

    private final RelationCategory matchingCategory;

    RelationType(RelationCategory matchingCategory) {
        this.matchingCategory = matchingCategory;
    }

    public RelationCategory getMatchingCategory() {
        return matchingCategory;
    }
};
