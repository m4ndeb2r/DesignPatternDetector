package nl.ou.dpd.domain.node;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Peter Vansweevelt
 *
 */
public class ParameterTest {
	
	@Test
	public void testConstructor() {
		Node node = new Node("node");
		Node type = new Node("type");
		Operation op = new Operation("operation", node);
		
		Parameter param = new Parameter("param1", op);
		assertEquals("param1", param.getId());
		assertEquals(op, param.getParentOperation());
		assertNull(param.getName());
		assertNull(param.getType());
		param.setName("param1");
		assertEquals("param1", param.getName());
		param.setType(type);
		assertEquals(type, param.getType());		
	}

	@Test
	public void testSignatureEquals() {
		Node node = new Node("node");
		Node type = new Node("type");
		Node type2 = new Node("type2");
		Operation op = new Operation("operation", node);
		Operation op2 = new Operation("operation", node);

		Parameter param1 = new Parameter("param1", op);
		Parameter param2 = new Parameter("param1", op);
		Parameter param3 = new Parameter("param2", op);
		Parameter param4 = new Parameter(null, op);
		Parameter param5 = new Parameter("param1", null);
		Parameter param6 = new Parameter(null, op);
		Parameter param7 = new Parameter(null, null);
		Parameter param8 = new Parameter(null, null);
		Parameter param9 = new Parameter("param1", op2);

		assertTrue(param1.equalsSignature(param1));
		assertTrue(param1.equalsSignature(param2));
		assertFalse(param1.equalsSignature(null));
		assertTrue(param1.equalsSignature(param3));
		assertTrue(param1.equalsSignature(param4));
		assertTrue(param4.equalsSignature(param1));
		assertTrue(param1.equalsSignature(param4));
		assertTrue(param5.equalsSignature(param1));
		assertTrue(param4.equalsSignature(param6));
		assertTrue(param7.equalsSignature(param8));
		assertTrue(param1.equalsSignature(param9));

		param1.setName("param1");
		param2.setName("param1");
		assertTrue(param1.equalsSignature(param2));
		param2.setName("param2");
		assertFalse(param1.equalsSignature(param2));
		param2.setName(null);
		assertFalse(param1.equalsSignature(param2));
		assertFalse(param2.equalsSignature(param1));
		
		param2.setName("param1");
		param2.setType(type);
		assertFalse(param1.equalsSignature(param2));
		assertFalse(param2.equalsSignature(param1));
		param1.setType(type2);
		assertFalse(param1.equalsSignature(param2));
		param1.setType(type);
		assertTrue(param2.equalsSignature(param1));
	}

}
