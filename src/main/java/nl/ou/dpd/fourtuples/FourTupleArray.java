package nl.ou.dpd.fourtuples;

import java.util.ArrayList;

/**
 *
 * @author E.M. van Doorn
 */
public
        class FourTupleArray {

    private ArrayList<FourTuple> fourTuples;
    private String dp_name;
    private Solutions solutions;

    public FourTupleArray() {
        fourTuples = new ArrayList();
        dp_name = FT_constants.EMPTY;
        solutions = new Solutions();
    }

    public FourTupleArray(String name) {
        this();
        dp_name = name;
    }

    boolean match(FourTupleArray fta, int maxNotMatchable) {
        // Check if edges --in Design Pattern (this)-- 
        // occur in SE (fta),
        // but maxNotMatchable edges may be missing.

        MatchedNames matchedClassNames;

        matchedClassNames = fillMatchedNames(fta);
        // Put every classname which occur in a 4-tuples in fta in MatchedNames with value = EMPTY.
        
        order();

        return recursiveMatch(fta, maxNotMatchable, 0, matchedClassNames);
    }

    private boolean recursiveMatch(FourTupleArray fta, int maxNotMatchable,
            int startIndex, MatchedNames matchedClassNames) {
        // --this-- should contain the edges of the design pattern !!
        // For a particular edge (fourtuple.get(startIndex) in the design pattern
        // try to find the corresponding edge(s) in fta.

        int nNotMatchable;
        boolean  found;
        FourTuple dpFt;

        if (startIndex >= fourTuples.size())
        {
            if (maxNotMatchable >= 0)
            //  This is an acceptable solution.
            {
                if (solutions.isUniq(matchedClassNames.getBoundedSortedKeySet()))
                {
                    solutions.add(new Solution(matchedClassNames.getBoundedSortedKeySet()));

                     matchedClassNames.show(dp_name);

                    // Does the detected design patter have more edges than the design pattern?
                    // If so, show those edges.
                    
                    showSupplementaryEdges(fta, matchedClassNames);
                    System.out.println();
                    
                }

                return true;
            }

            System.out.println("Should not be shown 1");

            // Should not occur. The search should be stopped before.
            
            return false;
        }

        dpFt = fourTuples.get(startIndex);
        // improves readbality

        found = false; // makes compiler happy;;

        // For this (startIndex) edge in DP, zoek passende edges in SE
        for (int j = 0; j < fta.fourTuples.size(); j++)
        {
            // For alle edges in SE

            FourTuple seFt = fta.fourTuples.get(j);
            // improves readablity

            MatchedNames copyMatchedClassNames = new MatchedNames(matchedClassNames);
            ArrayList<Integer> extraMatched = new ArrayList<Integer>();

            if (!seFt.getMatched()
                    && seFt.isMatch(dpFt, matchedClassNames))
            {
                boolean hulp;

                dpFt.makeMatch(seFt, copyMatchedClassNames);
                dpFt.setMatched(true);
                seFt.setMatched(true);
                
                if (dpFt.getTypeRelation() == FT_constants.INHERITANCE_MULTI)
                {
                    int k;

                    // There may be more edges of se which
                    // contains an inheritance to the same parent and
                    // have unmatched childs
                    for (k = j + 1; k < fta.fourTuples.size(); k++)
                    {
                        FourTuple sekFt = fta.fourTuples.get(k);
                        // for readablity;
                        if (!sekFt.getMatched()
                                && sekFt.getClassName2().equals(seFt.getClassName2())
                                && sekFt.isMatch(dpFt, matchedClassNames))
                        {
                            dpFt.makeMatch(sekFt, copyMatchedClassNames);
                            extraMatched.add(new Integer(k));
                            sekFt.setMatched(true);
                        }
                    }
                }

                hulp = recursiveMatch(fta, maxNotMatchable,
                        startIndex + 1, copyMatchedClassNames);

                found = found || hulp;

                dpFt.setMatched(false);
                seFt.setMatched(false);
                for (Integer getal : extraMatched)
                // undo multiple matched edges.
                {
                    fta.fourTuples.get(getal.intValue()).setMatched(false);
                }

                if (found && extraMatched.size() > 0)
                {
                    // In case of inheritance with multiple childs
                    // all matching  edges has been found.
                    // Therefore searching for more edges may be stopped.

                    j = fta.fourTuples.size();
                }
            }
        }
        
        if (!found)
        {
            if (--maxNotMatchable >= 0)
            // is the number of not matched edges acceptable?
            {
                return recursiveMatch(fta, maxNotMatchable,
                        startIndex + 1, matchedClassNames);
            }

            return false;
        }

        
        return found; //  == true !!
    }

    private void order() {
        // Zorgt er voor de volgorde van de edges A -> B, C->D, A->C
        // wordt A->B, A->C, C->D
        // In het algemeen: vanaf de tweede edges moet de kant een knoop bevatten
        // die reeds is voorgekomen.
        
        boolean found;
        String name1, name2, name3, name4;
        FourTuple ft;

        for (int i = 1; i < fourTuples.size(); i++)
        {
            // The i-element of fourTuple should have a classname which occurs
            // in the elements 0.. (i-1)
            
            found = false;
            for (int j = i; j < fourTuples.size() && !found; j++)
            {
                // Does element j satifies these condition ?
                // yes --> found = true
                
                name1 = fourTuples.get(j).getClassName1();
                name2 = fourTuples.get(j).getClassName2();
            
                for (int k = 0; k < i && !found; k++)
                {
                   name3 = fourTuples.get(k).getClassName1();
                   name4 = fourTuples.get(k).getClassName2();
                   
                   if (name1.equals(name3) || name1.equals(name4) ||
                       name2.equals(name3) || name2.equals(name4))
                   {
                       found = true;
                       
                       if (j != i)
                       {
                         ft = fourTuples.get(j);
                         fourTuples.set(j, fourTuples.get(i));
                         fourTuples.set(i, ft);
                       }
                   }
                }
            }
            
            if (!found)
                System.out.println("Warning: Template is not a connected graph.");
        }
    }

    private MatchedNames fillMatchedNames() {
        MatchedNames names = new MatchedNames();

        for (FourTuple ft : fourTuples)
        {
            names.add(ft.getClassName1());
            names.add(ft.getClassName2());
        }

        return names;
    }

    private MatchedNames fillMatchedNames(FourTupleArray fta) {
        // No classname will be matched
        MatchedNames names = new MatchedNames();

        for (FourTuple ft : fta.fourTuples)
        {
            names.add(ft.getClassName1());
            names.add(ft.getClassName2());
        }

        return names;
    }

    public void add(FourTuple ft) {
        fourTuples.add(new FourTuple(ft));
        
        if (ft.getTypeRelation() == FT_constants.ASSOCIATION)
            // For edge (A, B, ....) a second but virtual edge (B, A, ...)
            // will be added.
        {
            FourTuple tmp;
            
            tmp = new FourTuple(ft);
            tmp.makeVirtual();
            fourTuples.add(tmp);
        }
    }

    public void show() {
        if (!dp_name.equals(FT_constants.EMPTY))
        {
            System.out.println("Design pattern: " + dp_name);
        }

        for (FourTuple ft : fourTuples)
        {
            ft.show();
        }

        System.out.println();
    }

    void showSupplementaryEdges(FourTupleArray fta, MatchedNames matchedClassNames) {
        // Show all edges in fta which both classes are matched in this 
        // design pattern but ther is no coressponding edge in the design
        // pattern.
        
        boolean found;
        
        found = false;

        for (FourTuple edge : fta.fourTuples)
        {
            if (matchedClassNames.keyIsBounded(edge.getClassName1())
                    && matchedClassNames.keyIsBounded(edge.getClassName2()))
            {
                if (!edge.getMatched())
                {
                    if (!found)
                    {
                        System.out.println("Edges which do not belong to this design pattern:");
                        found = true;
                    }
                        
                    edge.showSimple();
                }
            }
        }
        
        if (found)
        {
            System.out.println("==================================================");
        }
    }
}
