package nl.ou.dpd.domain.relation;

/**
 * This enumeration defines a set of relation types.
 *
 * @author Martin de Boer
 */
public enum RelationType {
    ASSOCIATES_WITH,
    DEPENDS_ON,
    INHERITS_FROM,
    IMPLEMENTS,
    HAS_ATTRIBUTE_OF,
    CREATES_INSTANCE_OF,
    HAS_METHOD_PARAMETER_OF_TYPE,
    HAS_METHOD_RETURNTYPE,
    CALLS_METHOD_OF,
    OVERRIDES_METHOD_OF;
};
