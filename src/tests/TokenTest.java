package tests;
import graphs.Token;
import graphs.TokenType;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TokenTest {
	
	Token name, keyword, number, name2, name3;
	
	@Before
	public void setUp() throws Exception {
		name = new Token(TokenType.NAME, "bob");
		name2 = new Token(TokenType.NAME, "Bob");
		name3 = new Token(TokenType.NAME, "Bob");
		keyword = new Token(TokenType.KEYWORD, "Bob");
		number = new Token(TokenType.NUMBER, "14E");
	}

	@Test
	public void testGetValue() {
		assertEquals("bob", name.getValue());
	}
	
	@Test
	public void testGetType() {
		assertEquals(TokenType.NAME, name.getType());
	}
	
	@Test
	public void testEquals(){
		assertFalse(name.equals(name2));
		assertFalse(name2.equals(keyword));
		assertFalse(name2.equals(number));
		assertTrue(name2.equals(name3));
	}

	@Test
	public void testHashCode(){
		assertTrue(name2.hashCode() == "Bob".hashCode());
		assertTrue(name2.hashCode() == name3.hashCode());
		assertTrue(name2.hashCode() == keyword.hashCode());
		assertFalse(name2.hashCode() == name.hashCode());
	}
	
	@Test
	public void testToString(){
		assertEquals("bob:NAME", name.toString());
	}
}
