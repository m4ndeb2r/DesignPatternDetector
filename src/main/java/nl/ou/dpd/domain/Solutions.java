package nl.ou.dpd.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
     * Determines if the specified set of {@code names} (or a premutation of it) is already present as a
     * {@link Solution} in this {@link Solutions}.
     *
     * @param names a set of class names to detect in the current {@link Solutions}.
     * @return {@code true} if {@code names} is NOT detected in the list, or {@code false} otherwise.
     */
    public boolean isUniq(Set names) {
        Solution sol = new Solution(names);

        boolean result = true;
        for (Solution s : solutions) {
            result = result && !s.isEqual(sol);
            if (!result) {
                return false;
            }
        }
        return result;
    }
}
