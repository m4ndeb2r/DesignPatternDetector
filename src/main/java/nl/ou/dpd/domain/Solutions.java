package nl.ou.dpd.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a list of {@link Solution}s.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public class Solutions {

    private final List<Solution> solutions;

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
     * Indicates whether the specified {@link Solution} is not already present in this {@link Solutions}.
     *
     * @param sol the {@link Solution} to determine uniqueness for.
     * @return {@code true} if {@code sol} is not already present, or {@code false} otherwise.
     */
    public boolean isUniq(Solution sol) {
        for (Solution s : solutions) {
            if (s.isEqual(sol)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the list of {@link Solution}s.
     *
     * @return the list of  {@link Solution}s.
     */
    public List<Solution> getSolutions() {
        return solutions;
    }

}
