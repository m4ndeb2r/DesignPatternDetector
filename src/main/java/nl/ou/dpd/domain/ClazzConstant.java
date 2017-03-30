package nl.ou.dpd.domain;

/**
 * An enumeration for constants used by {@link Clazz}.
 * List is taken from specifications in argoUML ({@link http://argouml.tigris.org/}).
 *
 * @author Peter Vansweevelt
 */
public enum ClazzConstant {

	/*
	//type of the class
	TYPE_NOTSET,
    TYPE_CLASS,
    TYPE_INTERFACE,
    
    //visibility of the class (exclusive)
    VISIBILITY_NOTSET,
    VISIBILITY_PUBLIC,
    VISIBILITY_PACKAGE,
    VISIBILITY_PROTECTED,
    VISIBILITY_PRIVATE,
*/    
    //modifiers.Can be set as boolean values
    MODIFIER_ISROOT,
    MODIFIER_ISLEAF,
    MODIFIER_ISABSTRACT,
    MODIFIER_ISACTIVE,
    
    //topics used in the Rules
    TOPIC_NOTSET,
    TOPIC_RELATION,
    TOPIC_OBJECT,
    TOPIC_ATTRIBUTE,
/*    
    //operators used in the Rules
    OP_NOTSET,
    OP_EQUALS,
    OP_EXISTS,
    
    //target
    TARGET_NOTSET,
    TARGET_TYPE,
    TARGET_VISIBILITY,
    TARGET_MODIFIER,
    TARGET_CARDINALITY
*/    
}
