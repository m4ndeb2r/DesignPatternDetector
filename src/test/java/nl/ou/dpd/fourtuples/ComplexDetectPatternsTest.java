package nl.ou.dpd.fourtuples;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Tests the {@link DetectPatterns} class for a complex systeem.
 *
 * @author Martin de Boer
 */
public class ComplexDetectPatternsTest {

    // Test subject
    private DetectPatterns detectPatterns;

    // Template containing GoF design patterns
    private ArrayList<FourTupleArray> dpsTemplate;

    /**
     * Initialize the test(s).
     */
    @Before
    public void setUp() {
        dpsTemplate = TestHelper.createDesignPatternsTemplate();
        detectPatterns = new DetectPatterns();
    }

    /**
     * Tests the {@link DetectPatterns#detectDP(FourTupleArray, ArrayList, int)} method. We set up a "System under
     * consideration", containing all patterns. We then analyse this with a template containing GoF design patterns.
     * Finally we check if the expected patterns are detected (TODO).
     *
     * TODO: currently this test is alway successful. We cannot check the output (yet), since it is printed to System.out.
     */
    @Test
    public void testDetectDP() {
        detectPatterns.detectDP(createSystemUnderConsideration(), dpsTemplate, 1);
    }

    private FourTupleArray createSystemUnderConsideration() {
        final FourTupleArray system = new FourTupleArray();

        // Bridge
        system.add(new FourTuple("Client", "Ab", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcAb1", "Ab", FT_constants.INHERITANCE));
        system.add(new FourTuple("ConcAb2", "Ab", FT_constants.INHERITANCE));
        system.add(new FourTuple("ConcAb2", "F_Factory", FT_constants.ASSOCIATION));
        system.add(new FourTuple("Impl", "Ab", FT_constants.AGGREGATE));
        system.add(new FourTuple("F_Factory", "Impl", FT_constants.INHERITANCE));
        system.add(new FourTuple("P_Subject", "Impl", FT_constants.INHERITANCE));
        system.add(new FourTuple("ConcImpl3", "Impl", FT_constants.INHERITANCE));

        // Factory Method
        system.add(new FourTuple("F_ConcreteFactory", "F_Factory", FT_constants.INHERITANCE));
        system.add(new FourTuple("F_ConcreteFactory", "F_Product", FT_constants.ASSOCIATION));
        system.add(new FourTuple("F_Product", "F_ProdInterface", FT_constants.INHERITANCE));

        // Proxy
        system.add(new FourTuple("P_Proxy", "P_Subject", FT_constants.INHERITANCE));
        system.add(new FourTuple("P_RealSubject", "P_Subject", FT_constants.INHERITANCE));
        system.add(new FourTuple("P_Proxy", "P_RealSubject", FT_constants.ASSOCIATION));

        // Decorator
        system.add(new FourTuple("DecInterface", "ConcImpl3", FT_constants.ASSOCIATION));
        system.add(new FourTuple("DecBasis", "DecInterface", FT_constants.INHERITANCE));
        system.add(new FourTuple("DecInterface", "DecWrapper", FT_constants.COMPOSITE));
        system.add(new FourTuple("DecOption1", "DecWrapper", FT_constants.INHERITANCE));
        system.add(new FourTuple("DecOption2", "DecWrapper", FT_constants.INHERITANCE));
        system.add(new FourTuple("DecWrapper", "DecInterface", FT_constants.INHERITANCE));

        // Memento
        system.add(new FourTuple("Client", "Maintainer", FT_constants.DEPENDENCY));
        system.add(new FourTuple("Status", "Maintainer", FT_constants.AGGREGATE));

        // Adapter
        system.add(new FourTuple("Client", "T_Interface", FT_constants.ASSOCIATION));
        system.add(new FourTuple("Aanpasser", "T_Interface", FT_constants.INHERITANCE));
        system.add(new FourTuple("Aanpasser", "NietPassend", FT_constants.ASSOCIATION));

        // Composite
        system.add(new FourTuple("Leaflet", "T_Interface", FT_constants.INHERITANCE));
        system.add(new FourTuple("Union", "T_Interface", FT_constants.INHERITANCE));
        system.add(new FourTuple("T_Interface", "Union", FT_constants.AGGREGATE));

        // Mediator
        system.add(new FourTuple("DecInterface", "Med", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcMed", "Med", FT_constants.INHERITANCE));
        system.add(new FourTuple("ConcMed", "DecBasis", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcMed", "DecWrapper", FT_constants.ASSOCIATION));

        // Command
        system.add(new FourTuple("Maintainer", "Opdracht", FT_constants.INHERITANCE));
        system.add(new FourTuple("Client", "Ontvanger", FT_constants.ASSOCIATION));
        system.add(new FourTuple("Opdracht", "Aanroeper", FT_constants.AGGREGATE));
        system.add(new FourTuple("Maintainer", "Ontvanger", FT_constants.ASSOCIATION));

        // Iterator
        system.add(new FourTuple("User", "DecInterface", FT_constants.ASSOCIATION));
        system.add(new FourTuple("User", "Med", FT_constants.ASSOCIATION));

        // ChainOfResponsibility
        system.add(new FourTuple("Aanroeper", "Behandelaar", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcreteBehandelaar_1", "Behandelaar", FT_constants.INHERITANCE));
        system.add(new FourTuple("Behandelaar", "Behandelaar", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcreteBehandelaar_2", "Behandelaar", FT_constants.INHERITANCE));

        // AbstractFactory
        system.add(new FourTuple("DecOption1", "AbstrFact", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcrFact1", "AbstrFact", FT_constants.INHERITANCE));
        system.add(new FourTuple("ConcrFact2", "AbstrFact", FT_constants.INHERITANCE));
        system.add(new FourTuple("DecOption1", "AbstrProA", FT_constants.ASSOCIATION));
        system.add(new FourTuple("DecOption1", "AbstrProB", FT_constants.ASSOCIATION));
        system.add(new FourTuple("Pro1A", "AbstrProA", FT_constants.INHERITANCE));
        system.add(new FourTuple("Pro2A", "AbstrProA", FT_constants.INHERITANCE));
        system.add(new FourTuple("Pro1B", "AbstrProB", FT_constants.INHERITANCE));
        system.add(new FourTuple("Pro2B", "AbstrProB", FT_constants.INHERITANCE));
        system.add(new FourTuple("ConcrFact1", "Pro1A", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcrFact2", "Pro2A", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcrFact1", "Pro1B", FT_constants.ASSOCIATION));
        system.add(new FourTuple("ConcrFact2", "Pro2B", FT_constants.ASSOCIATION));

        // Flyweight
        system.add(new FourTuple("FW_Cl", "FlywFact", FT_constants.ASSOCIATION));
        system.add(new FourTuple("FW_Cl", "ConcFlyw", FT_constants.ASSOCIATION));
        system.add(new FourTuple("FW_Cl", "UnshConcFlyw", FT_constants.ASSOCIATION));
        system.add(new FourTuple("Flyw", "NietPassend", FT_constants.AGGREGATE));
        system.add(new FourTuple("ConcFlyw", "Flyw", FT_constants.INHERITANCE));
        system.add(new FourTuple("UnshConcFlyw", "Flyw", FT_constants.INHERITANCE));

        return system;
    }
}
