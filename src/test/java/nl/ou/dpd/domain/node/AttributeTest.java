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
	public void testHashcode() {
		Node parent = new Node("parent");
		Node type = new Node("type");
		
		Attribute attr1 = new Attribute("attr1", parent);
		Attribute attr2 = new Attribute("attr1", parent);
		Attribute attr3 = new Attribute("attr2", parent);
		assertEquals(attr1.hashCode(), attr2.hashCode());
		assertNotEquals(attr1.hashCode(), attr3.hashCode());
		Attribute attr4 = new Attribute("attr1", parent);
		assertEquals(attr1.hashCode(), attr4.hashCode());
		attr4.setName("attr4");
		assertNotEquals(attr1.hashCode(), attr4.hashCode());
		Attribute attr5 = new Attribute("attr1", parent);
		assertEquals(attr1.hashCode(), attr5.hashCode());
		attr5.setType(type);
		assertNotEquals(attr1.hashCode(), attr5.hashCode());
		Attribute attr6 = new Attribute("attr1", parent);
		assertEquals(attr1.hashCode(), attr6.hashCode());
		attr6.setVisibility(Visibility.PROTECTED);
		assertNotEquals(attr1.hashCode(), attr6.hashCode());
	}
	
	@Test
	public void testEquals() {
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

		assertEquals(attr1, attr1);
		assertEquals(attr1.hashCode(), attr1.hashCode());
		assertEquals(attr1, attr2);
		assertEquals(attr1.hashCode(), attr2.hashCode());
		assertNotEquals(attr1, null);
		assertNotEquals(attr1, parent);
		assertNotEquals(attr1, attr3);
		assertNotEquals(attr1, attr4);
		assertNotEquals(attr4, attr1);
		assertNotEquals(attr1, attr5);
		assertNotEquals(attr5, attr1);
		assertEquals(attr1.hashCode(), attr5.hashCode()); //!
		assertEquals(attr4, attr6);
		assertEquals(attr4.hashCode(), attr6.hashCode());
		assertEquals(attr7, attr8);
		assertEquals(attr7.hashCode(), attr8.hashCode());
		assertNotEquals(attr1, attr9);
		assertEquals(attr1.hashCode(), attr9.hashCode()); //!

		attr1.setName("attr1");
		attr2.setName("attr1");
		assertEquals(attr1, attr2);
		assertEquals(attr1.hashCode(), attr2.hashCode());
		attr2.setName("attr2");
		assertNotEquals(attr1, attr2);
		attr2.setName(null);
		assertNotEquals(attr1, attr2);
		assertNotEquals(attr2, attr1);
		
		attr2.setName("attr1");
		attr2.setType(type);
		assertNotEquals(attr1, attr2);
		assertNotEquals(attr2, attr1);
		attr1.setType(type2);
		assertNotEquals(attr1, attr2);
		attr1.setType(type);
		assertEquals(attr2, attr1);
		assertEquals(attr2.hashCode(), attr1.hashCode());

		attr2.setVisibility(Visibility.PROTECTED);
		assertNotEquals(attr1, attr2);
		assertNotEquals(attr2, attr1);
		attr1.setVisibility(Visibility.PROTECTED);
		assertEquals(attr2, attr1);
		assertEquals(attr2.hashCode(), attr1.hashCode());
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
