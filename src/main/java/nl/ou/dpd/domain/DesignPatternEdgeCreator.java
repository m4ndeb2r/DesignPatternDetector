package nl.ou.dpd.domain;

/**
 * TODO ...
 *
 * @author Martin de Boer
 */
public class DesignPatternEdgeCreator implements EdgeCreator<DesignPatternEdge> {

    /**
     * TODO ...
     *
     * @param class1
     * @param class2
     * @param type
     * @return
     */
    public DesignPatternEdge create(String class1, String class2, EdgeType type) {
        return new DesignPatternEdge(class1, class2, type);
    }

    /**
     * TODO ...
     * @param edge
     * @return
     */
    public DesignPatternEdge create(DesignPatternEdge edge) {
        return new DesignPatternEdge(edge);
    }

}
