package nl.ou.dpd.domain.node;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Peter Vansweevelt
 *
 */
public class OperationTest {
	
	@Test
	public void testConstructor() {
		Node node = new Node("node");
		Node type = new Node("type");
		Operation op = new Operation("operation", node);
		
		assertEquals("operation", op.getId());
		assertEquals(node, op.getParentNode());
		assertNull(op.getName());
		assertNull(op.getReturnType());
		op.setName("op1");
		assertEquals("op1", op.getName());
		op.setReturnType(type);
		assertEquals(type, op.getReturnType());	
		assertEquals(Visibility.PUBLIC, op.getVisibility());	
		op.setVisibility(Visibility.PRIVATE);
		assertEquals(Visibility.PRIVATE, op.getVisibility());
		
		new Parameter("param1", op);
		new Parameter("param2", op);
		assertEquals(2,  op.getParameters().size());		
	}

	@Test
	public void testSignatureEquals() {
		Node parent = new Node("parent");
		Node parent2 = new Node("parent2");
		Node type = new Node("type");
		type.setName("type");
		Node type2 = new Node("type2");
		type2.setName("type");
		Node type3 = new Node("type3");
		type3.setName("anotherType");

		Operation op1 = new Operation("op1", parent);
		Operation op2 = new Operation("op1", parent);
		Operation op3 = new Operation("op2", parent);
		Operation op4 = new Operation(null, parent);
		Operation op5 = new Operation("op1", null);
		Operation op6 = new Operation(null, parent);
		Operation op7 = new Operation(null, null);
		Operation op8 = new Operation(null, null);
		Operation op9 = new Operation("op1", parent2);

		assertTrue(op1.equalsSignature(op1));
		assertTrue(op1.equalsSignature(op2));
		assertFalse(op1.equalsSignature(null));
		assertTrue(op1.equalsSignature(op3));
		assertTrue(op1.equalsSignature(op4));
		assertTrue(op4.equalsSignature(op1));
		assertTrue(op1.equalsSignature(op4));
		assertTrue(op5.equalsSignature(op1));
		assertTrue(op4.equalsSignature(op6));
		assertTrue(op7.equalsSignature(op8));
		assertTrue(op1.equalsSignature(op9));

		op1.setName("op1");
		op2.setName("op1");
		assertTrue(op1.equalsSignature(op2));
		op2.setName("op2");
		assertFalse(op1.equalsSignature(op2));
		op2.setName(null);
		assertFalse(op1.equalsSignature(op2));
		assertFalse(op2.equalsSignature(op1));
		
		op2.setName("op1");
		op2.setReturnType(type);
		assertFalse(op1.equalsSignature(op2));
		assertFalse(op2.equalsSignature(op1));
		op1.setReturnType(type2);
		assertTrue(op1.equalsSignature(op2)); //returntype is evaluated on equalsSignature too!
		op1.setReturnType(type3);
		assertFalse(op2.equalsSignature(op1));
		
		op1.setReturnType(type);
		Parameter param = new Parameter("param", op1);
		param.setName("param");
		assertFalse(op1.equalsSignature(op2));
		assertFalse(op2.equalsSignature(op1));
		Parameter param2 = new Parameter("param", op2);
		param2.setName("param");
		assertTrue(op1.equalsSignature(op2)); //parameters are evaluated on equalsSignature
		Parameter param3 = new Parameter("param1", op2);
		param3.setName("param3");
		assertFalse(op1.equalsSignature(op2));
		Parameter param4 = new Parameter("param4", op1);
		param4.setName("param4");
		assertFalse(op1.equalsSignature(op2));
		param4.setName("param3");
		assertTrue(op1.equalsSignature(op2));
		
	}

}
