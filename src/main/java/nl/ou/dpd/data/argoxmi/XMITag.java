/**
 * 
 */
package nl.ou.dpd.data.argoxmi;

/**
 * @author Peter Vansweevelt
 *
 */
public interface XMITag {
	
	String MODEL = "Model";
	String CLASS = "Class";
	String INTERFACE = "Interface";
	String ATTRIBUTE = "Attribute";
	String OPERATION = "Operation";
	String ASSOCIATION ="Association";
	String ASSOCIATION_END = "AssociationEnd";
	String MULTIPLICITY_RANGE = "MultiplicityRange";
	String MULTIPLICITY_DOT_RANGE = "Multiplicity.range";
	String ASSOCIATION_END_DOT_PARTICIPANT = "AssociationEnd.participant";
	String ABSTRACTION = "Abstraction";
	String DEPENDENCY = "Dependency";
	String DEPENDENCY_DOT_CLIENT = "Dependency.client";
	String DEPENDENCY_DOT_SUPPLIER = "Dependency.supplier";
	String GENERALIZATION = "Generalization";
	String GENERALIZATION_DOT_CHILD = "Generalization.child";
	String GENERALIZATION_DOT_PARENT = "Generalization.parent";
	String NAMESPACE_DOT_OWNED_ELEMENT = "Namespace.ownedElement";
	String DATATYPE = "DataType";
}
