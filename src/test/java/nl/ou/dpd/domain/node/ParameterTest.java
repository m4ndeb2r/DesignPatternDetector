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
	public void testHashcode() {
		Node node = new Node("node");
		Node type = new Node("type");
		Operation op = new Operation("operation", node);
		
		Parameter param1 = new Parameter("param1", op);
		Parameter param2 = new Parameter("param1", op);
		Parameter param3 = new Parameter("param2", op);
		assertEquals(param1.hashCode(), param2.hashCode());
		assertNotEquals(param1.hashCode(), param3.hashCode());
		Parameter param4 = new Parameter("param1", op);
		assertEquals(param1.hashCode(), param4.hashCode());
		param4.setName("param4");
		assertNotEquals(param1.hashCode(), param4.hashCode());
		Parameter param5 = new Parameter("param1", op);
		assertEquals(param1.hashCode(), param5.hashCode());
		param5.setType(type);
		assertNotEquals(param1.hashCode(), param5.hashCode());
	}
	
	@Test
	public void testEquals() {
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

		assertEquals(param1, param1);
		assertEquals(param1.hashCode(), param1.hashCode());
		assertEquals(param1, param2);
		assertEquals(param1.hashCode(), param2.hashCode());
		assertNotEquals(param1, null);
		assertNotEquals(param1, node);
		assertNotEquals(param1, param3);
		assertNotEquals(param1, param4);
		assertNotEquals(param4, param1);
		assertNotEquals(param1, param5);
		assertNotEquals(param5, param1);
		assertEquals(param1.hashCode(), param5.hashCode()); //!
		assertEquals(param4, param6);
		assertEquals(param4.hashCode(), param6.hashCode());
		assertEquals(param7, param8);
		assertEquals(param7.hashCode(), param8.hashCode());
		assertNotEquals(param1, param9);
		assertEquals(param1.hashCode(), param9.hashCode()); //!

		param1.setName("param1");
		param2.setName("param1");
		assertEquals(param1, param2);
		assertEquals(param1.hashCode(), param2.hashCode());
		param2.setName("param2");
		assertNotEquals(param1, param2);
		param2.setName(null);
		assertNotEquals(param1, param2);
		assertNotEquals(param2, param1);
		
		param2.setName("param1");
		param2.setType(type);
		assertNotEquals(param1, param2);
		assertNotEquals(param2, param1);
		param1.setType(type2);
		assertNotEquals(param1, param2);
		param1.setType(type);
		assertEquals(param2, param1);
		assertEquals(param2.hashCode(), param1.hashCode());
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
