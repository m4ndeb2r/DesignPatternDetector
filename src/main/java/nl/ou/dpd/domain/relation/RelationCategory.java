package nl.ou.dpd.domain.relation;

/**
 * Defines a set of matching categories, to be used for enabling/disabling groups of characteristics to consider during
 * matching.
 *
 * @author Martin de Boer
 */
public enum RelationCategory {
    RELATIONS("Inheritance or implementation relations."),
    ATTRIBUTES("Relations, based on attributes or variables."),
    PARAMETERS("Relations, based on method parameters."),
    METHODS("Relations, based on method calls."),
    OTHER("Miscelaneous");

    private final String description;

    RelationCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
