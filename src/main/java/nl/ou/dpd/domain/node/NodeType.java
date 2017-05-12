package nl.ou.dpd.domain.node;

/**
 * Representation of a node type. A node can be a class, interface or data type. A data type represents a String,
 * Integer, UnlimitedInteger or Boolean in a system design. Classes and interfaces represent al other classes, including
 * the ones we might encounter in a design pattern.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public enum NodeType {
    CLASS, DATA_TYPE, INTERFACE
};
