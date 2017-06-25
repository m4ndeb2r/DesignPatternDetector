package nl.ou.dpd.domain.node;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Peter Vansweevelt
 *
 */
public class AttributeTest {
	
	@Test
	public void testConstructor() {
		Node parent = new Node("parent");
		Node type = new Node("type");
		
		Attribute attr = new Attribute("attr1", parent);
		assertEquals("attr1", attr.getId());
		assertEquals(parent, attr.getParentNode());
		assertNull(attr.getName());
		assertNull(attr.getType());
		attr.setName("param1");
		assertEquals("param1", attr.getName());
		attr.setType(type);
		assertEquals(type, attr.getType());
		assertEquals(Visibility.PUBLIC, attr.getVisibility());
		attr.setVisibility(Visibility.PRIVATE);
		assertEquals(Visibility.PRIVATE, attr.getVisibility());		

	}


	@Test
	public void testSignatureEquals() {
		Node parent = new Node("parent");
		Node parent2 = new Node("parent2");
		Node type = new Node("type");
		Node type2 = new Node("type2");

		Attribute attr1 = new Attribute("attr1", parent);
		Attribute attr2 = new Attribute("attr1", parent);
		Attribute attr3 = new Attribute("attr2", parent);
		Attribute attr4 = new Attribute(null, parent);
		Attribute attr5 = new Attribute("attr1", null);
		Attribute attr6 = new Attribute(null, parent);
		Attribute attr7 = new Attribute(null, null);
		Attribute attr8 = new Attribute(null, null);
		Attribute attr9 = new Attribute("attr1", parent2);

		assertTrue(attr1.equalsSignature(attr1));
		assertTrue(attr1.equalsSignature(attr2));
		assertFalse(attr1.equalsSignature(null));
		assertTrue(attr1.equalsSignature(attr3));
		assertTrue(attr1.equalsSignature(attr4));
		assertTrue(attr4.equalsSignature(attr1));
		assertTrue(attr1.equalsSignature(attr4));
		assertTrue(attr5.equalsSignature(attr1));
		assertTrue(attr4.equalsSignature(attr6));
		assertTrue(attr7.equalsSignature(attr8));
		assertTrue(attr1.equalsSignature(attr9));

		attr1.setName("attr1");
		attr2.setName("attr1");
		assertTrue(attr1.equalsSignature(attr2));
		attr2.setName("attr2");
		assertFalse(attr1.equalsSignature(attr2));
		attr2.setName(null);
		assertFalse(attr1.equalsSignature(attr2));
		assertFalse(attr2.equalsSignature(attr1));
		
		attr2.setName("attr1");
		attr2.setType(type);
		assertFalse(attr1.equalsSignature(attr2));
		assertFalse(attr2.equalsSignature(attr1));
		attr1.setType(type2);
		assertFalse(attr1.equalsSignature(attr2));
		attr1.setType(type);
		assertTrue(attr2.equalsSignature(attr1));
		
		attr2.setVisibility(Visibility.PROTECTED);
		assertTrue(attr1.equalsSignature(attr2));
		assertTrue(attr2.equalsSignature(attr1));

		assertTrue(attr5.equalsSignature(attr9));
	}

}
