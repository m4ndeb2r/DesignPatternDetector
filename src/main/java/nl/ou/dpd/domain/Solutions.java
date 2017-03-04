package nl.ou.dpd.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of {@link Solution}s.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public class Solutions {

    private List<Solution> solutions;

    /**
     * Default constructor. Constructs a {@link Solution}s instance containing an empty list of {@link Solution}s.
     */
    Solutions() {
        solutions = new ArrayList();
    }

    /**
     * Adds a {@link Solution} to the list.
     *
     * @param sol the {@link Solution} to add to the list
     */
    public void add(Solution sol) {
        solutions.add(sol);
    }

    /**
     * TODO....
     *
     * @param sol
     * @return
     */
    public boolean isUniq(Solution sol) {
        boolean result = true;
        for (Solution s : solutions) {
            result = result && !s.isEqual(sol);
            if (!result) {
                return false;
            }
        }
        return result;
    }

    /**
     * @deprecated no System.out prints in this application!!
     */
    public void show() {
        for (Solution solution : solutions) {
            final String designPatternName = solution.getDesignPatternName();
            final MatchedClasses matchedClasses = solution.getMatchedClasses();
            final List<Edge> superfluousEdges = solution.getSuperfluousEdges();

            if (!designPatternName.isEmpty()) {
                System.out.printf("Design Pattern: %s\n", designPatternName);
            }
            for (Clazz key : matchedClasses.getBoundedSortedKeySet()) {
                System.out.printf("%20s --> %25s\n", key.getName(), matchedClasses.get(key).getName());
            }
            System.out.println("------------------------");

            if (superfluousEdges.size() > 0) {
                System.out.println("Edges which do not belong to this design pattern:");
                for (Edge edge : superfluousEdges) {
                    if (!edge.isVirtual()) {
                        System.out.println(edge.getClass1().getName() + " --> " + edge.getClass2().getName());
                    }
                }
                System.out.println("==================================================");
            }
            System.out.println();
        }
    }
}
