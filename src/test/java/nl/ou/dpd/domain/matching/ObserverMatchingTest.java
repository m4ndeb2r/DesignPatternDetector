package nl.ou.dpd.domain.matching;


import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.matching.PatternInspector;
import nl.ou.dpd.domain.matching.Solution;
import nl.ou.dpd.parsing.argoxmi.ArgoUMLParser;
import nl.ou.dpd.parsing.pattern.PatternsParser;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * Test xml-files against the patterns.xsd.
 *
 * @author Martin de Boer
 */
public class ObserverMatchingTest {

    private URL xsdUrl;
    private URL patternsXmlUrl;
    private URL sucXmiUrl;
    private PatternsParser patternsParser;
    private ArgoUMLParser xmiParser;

    @Before
    public void initTests() {
        xsdUrl = ObserverMatchingTest.class.getResource("/patterns.xsd");
        xmiParser = new ArgoUMLParser();
    }
   
   //test a pure Observer pattern, written conforming the pattern   
   @Test
   public void testMatchingObserverExample() {

      patternsXmlUrl = ObserverMatchingTest.class.getResource("/patterns_observer_adapted.xml");
      patternsParser = new PatternsParser();
      sucXmiUrl = ObserverMatchingTest.class.getResource("/ObserverExample.xmi");

	   // Parse the observer pattern xml ands create a DesignPattern
       final List<DesignPattern> designPatterns = patternsParser.parse(patternsXmlUrl, xsdUrl);
       final DesignPattern designPattern = designPatterns.get(0);

       // Create a system under consideration containing the observer pattern
       final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);
       final Solution solution = new Solution(designPattern.getName());

       // Inspect the system for patterns
       final PatternInspector patternInspector = new PatternInspector(system, designPattern);
       //TODO
       TestHelper.printFeedback(designPattern, system, patternInspector, patternInspector.getSolutions());
       assertTrue(patternInspector.isomorphismExists());
   }
   
   //test an observerpattern without explicit relations between subject-observer and concreteSubject-concreteObsrver
   //and extra method override between concreteObserver and Observer
   @Test
   public void testMatchingObserverExampleWithExtras() {

      patternsXmlUrl = ObserverMatchingTest.class.getResource("/patterns_observer_adapted.xml");
      patternsParser = new PatternsParser();
      sucXmiUrl = ObserverMatchingTest.class.getResource("/ObserverExampleWithExtras.xmi");

	   // Parse the observer pattern xml ands create a DesignPattern
       final List<DesignPattern> designPatterns = patternsParser.parse(patternsXmlUrl, xsdUrl);
       final DesignPattern designPattern = designPatterns.get(0);

       // Create a system under consideration containing the observer pattern
       final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);
       final Solution solution = new Solution(designPattern.getName());

       // Inspect the system for patterns
       final PatternInspector patternInspector = new PatternInspector(system, designPattern);
       //TODO
       TestHelper.printFeedback(designPattern, system, patternInspector, patternInspector.getSolutions());
       //TODO extra relationproperty 'Overrides' prevents recognizing the pattern 
       assertFalse(patternInspector.isomorphismExists());
   }
}
