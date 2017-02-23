package nl.ou.dpd.fourtuples;

/**
 *
 * @author E.M. van Doorn
 */

public interface FT_constants {
    String EMPTY = "";
    int ASSOCIATION = 1;           // bidirectional 
    int ASSOCIATION_DIRECTED = 10; // unidirectional (left to right)
    int AGGREGATE = 2;
    int COMPOSITE = 3;
    int INHERITANCE = 4;
    int INHERITANCE_MULTI = 40; // inheritance may have several subclasses,
                                // provided that those subclasses do not 
                                // have any other relationships.
    int DEPENDENCY = 5;
}
