package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Visibility;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link AttributeRule} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class AttributeRuleTest {

    @Mock
    private Attribute systemAttribute;
    @Mock
    private Attribute mould;

    private AttributeRule attributeRule;

    @org.junit.Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testIllegalScopeInAttributeRule() {
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected scope: 'RELATION'.");
        attributeRule = new AttributeRule(mould, Scope.RELATION, Topic.VISIBILITY, Operator.EQUALS);
        attributeRule.process(systemAttribute);
    }

    @Test
    public void testIllegalTopicInAttributeRule() {
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected topic while processing OBJECT: 'CARDINALITY_LEFT'.");
        attributeRule = new AttributeRule(mould, Scope.OBJECT, Topic.CARDINALITY_LEFT, Operator.EQUALS);
        attributeRule.process(systemAttribute);
    }

    @Test
    public void testIllegalOperatorInAttributeRule() {
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected operator while processing VISIBILITY: 'NOT_EXISTS'.");
        attributeRule = new AttributeRule(mould, Scope.OBJECT, Topic.VISIBILITY, Operator.NOT_EXISTS);
        attributeRule.process(systemAttribute);
    }

    /**
     * Test if the following XML rule is handled correctly:
     * <pre>
     *     <rule applies="someAttribute" scope="OBJECT" topic="VISIBILITY" operator="EQUALS" value="private"/>
     * </pre>
     */
    @Test
    public void testPrivateVisibilityOkay() {
        assertVisibility(Visibility.PRIVATE, Visibility.PRIVATE);
    }

    /**
     * Test if the following XML rule is handled correctly:
     * <pre>
     *     <rule applies="someAttribute" scope="OBJECT" topic="VISIBILITY" operator="EQUALS" value="protected"/>
     * </pre>
     */
    @Test
    public void testProtectedVisibilityOkay() {
        assertVisibility(Visibility.PROTECTED, Visibility.PROTECTED);
    }

    /**
     * Test if the following XML rule is handled correctly:
     * <pre>
     *     <rule applies="someAttribute" scope="OBJECT" topic="VISIBILITY" operator="EQUALS" value="public"/>
     * </pre>
     */
    @Test
    public void testPublicVisibilityOkay() {
        assertVisibility(Visibility.PUBLIC, Visibility.PUBLIC);
    }

    /**
     * Test if the following XML rule is handled correctly:
     * <pre>
     *     <rule applies="someAttribute" scope="OBJECT" topic="VISIBILITY" operator="EQUALS" value="package"/>
     * </pre>
     */
    @Test
    public void testPackageVisibilityOkay() {
        assertVisibility(Visibility.PACKAGE, Visibility.PACKAGE);
    }

    /**
     * Test if the following XML rule is handled correctly:
     * <pre>
     *     <rule applies="someAttribute" scope="OBJECT" topic="VISIBILITY" operator="NOT_EQUALS" value="package"/>
     * </pre>
     */
    @Test
    public void testNotPackageVisibilityOkay() {
        assertVisibilityNotEquals(Visibility.PRIVATE, Visibility.PACKAGE);
    }

    /**
     * Test if an attribute rule that fails is handled correctly by the software.
     */
    @Test
    public void testVisibilityNotOkay() {
        assertVisibility(Visibility.PUBLIC, Visibility.PRIVATE);
    }

    private void assertVisibility(Visibility mouldVisibility, Visibility attrVisibility) {
        defineMouldAndAttributeBehaviour(mouldVisibility, attrVisibility);
        attributeRule = new AttributeRule(mould, Scope.OBJECT, Topic.VISIBILITY, Operator.EQUALS);
        assertThat(attributeRule.process(systemAttribute), is(mouldVisibility == attrVisibility));
    }

    private void assertVisibilityNotEquals(Visibility mouldVisibility, Visibility attrVisibility) {
        defineMouldAndAttributeBehaviour(mouldVisibility, attrVisibility);
        attributeRule = new AttributeRule(mould, Scope.OBJECT, Topic.VISIBILITY, Operator.NOT_EQUALS);
        assertThat(attributeRule.process(systemAttribute), is(mouldVisibility != attrVisibility));
    }

    private void defineMouldAndAttributeBehaviour(Visibility mouldVisibility, Visibility attrVisibility) {
        when(mould.getVisibility()).thenReturn(mouldVisibility);
        when(systemAttribute.getVisibility()).thenReturn(attrVisibility);
    }

}
