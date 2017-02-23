/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ou.dpd.fourtuples;

/**
 *
 * @author E.M. van Doorn
 */

public class TagValue implements FT_constants {

    public static int getTagValue(String tag) {
        switch (tag)
        {
            case "ASSOCIATION":
                return 1;

            case "ASSOCIATION_DIRECTED":
                return 10;

            case "AGGREGATE":
                return 2;

            case "COMPOSITE":
                return 3;

            case "INHERITANCE":
                return 4;

            case "INHERITANCE_MULTI":
                return 40;

            case "DEPENDENCY":
                return 5;
                
            default:
                System.out.println("Unknown type of relationship: " + tag);

        }
        
        return -1;
    }
}
