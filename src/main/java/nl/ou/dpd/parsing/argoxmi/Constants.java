package nl.ou.dpd.parsing.argoxmi;

/**
 * Constants related to XMI-parsing.
 *
 * @author E.M. van Doorn
 */
public interface Constants {

    String ABSTRACTION_TAG = "UML:Abstraction";
    String ASSOCIATION_TAG = "UML:Association";
    String ASSOCIATION_END_TAG = "UML:AssociationEnd";
    String CLASS_TAG = "UML:Class";
    String DEPENDENCY_TAG = "UML:Dependency";
    String DEPENDENCY_SUPPLIER_TAG = "UML:Dependency.supplier";
    String GENERALIZATION_TAG = "UML:Generalization";
    String INTERFACE_TAG = "UML:Interface";

    String IS_NAVIGABLE = "isNavigable";
    String AGGREGATION = "aggregation";
    String AGGREGATE = "aggregate";
    String COMPOSITE = "composite";

    String ID = "xmi.id";
    String ID_REF = "xmi.idref";
    String NAME = "name";
}
