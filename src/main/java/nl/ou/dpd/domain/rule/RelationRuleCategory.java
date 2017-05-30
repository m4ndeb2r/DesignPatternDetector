package nl.ou.dpd.domain.rule;

/**
 * Defines a set of rule categories, to be used for enabling/disabling groups of rules.
 *
 * @author Martin de Boer
 */
public enum RelationRuleCategory {
    RELATIONS("Inheritance or implementation relations."),
    ATTRIBUTES("Relations, based on attributes or variables."),
    PARAMETERS("Relations, based on method parameters."),
    METHODS("Relations, based on method calls."),
    OTHER("Miscelaneous");

    private final String description;

    RelationRuleCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
