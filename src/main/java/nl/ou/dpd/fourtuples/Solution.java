package nl.ou.dpd.fourtuples;

import java.util.ArrayList;
import java.util.SortedSet;

/**
 *
 * @author E.M. van Doorn
 */

public class Solution {
    private ArrayList<String> solution;
    
    Solution(SortedSet<String> names)
    {
        solution = new ArrayList();
        solution.addAll(names);
    }
    
    boolean isEqual(Solution sol)
    {
        boolean result;
        int index;
        
        result = true;
        index = 0;
        
        for (String s: sol.solution)
        {
            result = result && s.equals(solution.get(index++));
        }
        
        return result;
    }
}
