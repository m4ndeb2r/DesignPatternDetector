package nl.ou.dpd.fourtuples;

import java.util.ArrayList;

/**
 *
 * @author E.M. van Doorn
 */
public class DetectPatterns {

    FT_constants ftc;
    FourTupleArray dp, se;
    // ArrayList<FourTupleArray> dps;
    // For demo

    public DetectPatterns() {
    }

    public void researchComplexModel(ArrayList<FourTupleArray> dps) {
        int maxMissingEdges = 0;

        se = new FourTupleArray();
        example_complex();

        for (FourTupleArray dpex : dps)
        {
            dpex.match(se, maxMissingEdges);

        }
    }

    public void detectDP(FourTupleArray fta, ArrayList<FourTupleArray> dps,
            int maxMissingEdges) {
        se = fta;
        
       // se.show();
        
        // To research the example with many classes:
        // comment out: se = fta
        //            :complete for-statement
        // uncomment next line
        // researchComplexModel(dps);
        
        for (FourTupleArray dpex : dps)
        {
            //dpex.show();
            dpex.match(se, maxMissingEdges);
        }
    }

    //-----------------------------------------------------------------------
    //                       Examples
    //-----------------------------------------------------------------------


    void example_complex() {
        // Bridge
        se.add(new FourTuple("Client", "Ab", FT_constants.ASSOCIATION));
        se.add(new FourTuple("ConcAb1", "Ab", FT_constants.INHERITANCE));
        se.add(new FourTuple("ConcAb2", "Ab", FT_constants.INHERITANCE));
        se.add(new FourTuple("ConcAb2", "F_Factory", FT_constants.ASSOCIATION));
        se.add(new FourTuple("Impl", "Ab", FT_constants.AGGREGATE));
        se.add(new FourTuple("F_Factory", "Impl", FT_constants.INHERITANCE));
        se.add(new FourTuple("P_Subject", "Impl", FT_constants.INHERITANCE));
        se.add(new FourTuple("ConcImpl3", "Impl", FT_constants.INHERITANCE));

        // Factory Method
        se.add(new FourTuple("F_ConcreteFactory", "F_Factory", FT_constants.INHERITANCE));
        se.add(new FourTuple("F_ConcreteFactory", "F_Product", FT_constants.ASSOCIATION));
        se.add(new FourTuple("F_Product", "F_ProdInterface", FT_constants.INHERITANCE));

        // Proxy
        se.add(new FourTuple("P_Proxy", "P_Subject", FT_constants.INHERITANCE));
        se.add(new FourTuple("P_RealSubject", "P_Subject", FT_constants.INHERITANCE));
        se.add(new FourTuple("P_Proxy", "P_RealSubject", FT_constants.ASSOCIATION));

        // Decorator
        se.add(new FourTuple("DecInterface", "ConcImpl3", FT_constants.ASSOCIATION));
        se.add(new FourTuple("DecBasis", "DecInterface", FT_constants.INHERITANCE));
        se.add(new FourTuple("DecInterface", "DecWrapper", FT_constants.COMPOSITE));
        se.add(new FourTuple("DecOption1", "DecWrapper", FT_constants.INHERITANCE));
        se.add(new FourTuple("DecOption2", "DecWrapper", FT_constants.INHERITANCE));
        se.add(new FourTuple("DecWrapper", "DecInterface", FT_constants.INHERITANCE));

        // Memento
        se.add(new FourTuple("Client", "Maintainer", FT_constants.DEPENDENCY));
        se.add(new FourTuple("Status", "Maintainer", FT_constants.AGGREGATE));

        // Adapter
        se.add(new FourTuple("Client", "T_Interface", FT_constants.ASSOCIATION));
        se.add(new FourTuple("Aanpasser", "T_Interface", FT_constants.INHERITANCE));
        se.add(new FourTuple("Aanpasser", "NietPassend", FT_constants.ASSOCIATION));

        // Composite
        se.add(new FourTuple("Leaflet", "T_Interface", FT_constants.INHERITANCE));
        se.add(new FourTuple("Union", "T_Interface", FT_constants.INHERITANCE));
        se.add(new FourTuple("T_Interface", "Union", FT_constants.AGGREGATE));

        // Mediator
        se.add(new FourTuple("DecInterface", "Med", FT_constants.ASSOCIATION));
        se.add(new FourTuple("ConcMed", "Med", FT_constants.INHERITANCE));
        se.add(new FourTuple("ConcMed", "DecBasis", FT_constants.ASSOCIATION));
        se.add(new FourTuple("ConcMed", "DecWrapper", FT_constants.ASSOCIATION));

        // command
        se.add(new FourTuple("Maintainer", "Opdracht", FT_constants.INHERITANCE));
        se.add(new FourTuple("Client", "Ontvanger", FT_constants.ASSOCIATION));
        se.add(new FourTuple("Opdracht", "Aanroeper", FT_constants.AGGREGATE));
        se.add(new FourTuple("Maintainer", "Ontvanger", FT_constants.ASSOCIATION));

        // iterator
        se.add(new FourTuple("User", "DecInterface", FT_constants.ASSOCIATION));
        se.add(new FourTuple("User", "Med", FT_constants.ASSOCIATION));

        // chainOfResponsibility
        se.add(new FourTuple("Aanroeper", "Behandelaar", FT_constants.ASSOCIATION));
        se.add(new FourTuple("ConcreteBehandelaar_1", "Behandelaar", FT_constants.INHERITANCE));
        se.add(new FourTuple("Behandelaar", "Behandelaar", FT_constants.ASSOCIATION));
        se.add(new FourTuple("ConcreteBehandelaar_2", "Behandelaar", FT_constants.INHERITANCE));

        // abstractFactory
        se.add(new FourTuple("DecOption1", "AbstrFact", FT_constants.ASSOCIATION));
        se.add(new FourTuple("ConcrFact1", "AbstrFact", FT_constants.INHERITANCE));
        se.add(new FourTuple("ConcrFact2", "AbstrFact", FT_constants.INHERITANCE));
        se.add(new FourTuple("DecOption1", "AbstrProA", FT_constants.ASSOCIATION));
        se.add(new FourTuple("DecOption1", "AbstrProB", FT_constants.ASSOCIATION));
        se.add(new FourTuple("Pro1A", "AbstrProA", FT_constants.INHERITANCE));
        se.add(new FourTuple("Pro2A", "AbstrProA", FT_constants.INHERITANCE));
        se.add(new FourTuple("Pro1B", "AbstrProB", FT_constants.INHERITANCE));
        se.add(new FourTuple("Pro2B", "AbstrProB", FT_constants.INHERITANCE));
        se.add(new FourTuple("ConcrFact1", "Pro1A", FT_constants.ASSOCIATION));
        se.add(new FourTuple("ConcrFact2", "Pro2A", FT_constants.ASSOCIATION));
        se.add(new FourTuple("ConcrFact1", "Pro1B", FT_constants.ASSOCIATION));
        se.add(new FourTuple("ConcrFact2", "Pro2B", FT_constants.ASSOCIATION));

        //flyweight
        se.add(new FourTuple("FW_Cl", "FlywFact", FT_constants.ASSOCIATION));
        se.add(new FourTuple("FW_Cl", "ConcFlyw", FT_constants.ASSOCIATION));
        se.add(new FourTuple("FW_Cl", "UnshConcFlyw", FT_constants.ASSOCIATION));
        se.add(new FourTuple("Flyw", "NietPassend", FT_constants.AGGREGATE));
        se.add(new FourTuple("ConcFlyw", "Flyw", FT_constants.INHERITANCE));
        se.add(new FourTuple("UnshConcFlyw", "Flyw", FT_constants.INHERITANCE));
    }
}
