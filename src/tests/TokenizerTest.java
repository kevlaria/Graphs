package tests;
import graphs.Tokenizer;
import graphs.TokenType;
import graphs.Token;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TokenizerTest {

	Tokenizer tokenizer, emptyTokenizer, singleCharTokenizer, t, floatTokenizer, fileSampleTokenizer;
	Set<String> keywords;
	
	@Before
	public void setUp() throws Exception {
		keywords = new HashSet<String>();
		keywords.add("Lorem");
		keywords.add("Ipsum");
		tokenizer = new Tokenizer(new StringReader("Lorem Ipsum is over 2000 years old."), keywords);
		emptyTokenizer = new Tokenizer(new StringReader(""), keywords);
		singleCharTokenizer = new Tokenizer(new StringReader("a"), keywords);
		t = new Tokenizer(new StringReader("token token2"), keywords);
		floatTokenizer = new Tokenizer(new StringReader("1.0E10f340"), keywords);
//		fileSampleTokenizer = new Tokenizer(new FileReader("src/tokenizer/testText"), keywords);
		// You may wish to uncomment the above line and the test under "testFileTokenizer" if you have testText.txt in the appropriate directory
	}

	@Test
	public void testHasNext() {
		assertTrue(tokenizer.hasNext());
		assertTrue(emptyTokenizer.hasNext());
		emptyTokenizer.next();
		assertFalse(emptyTokenizer.hasNext());
		assertTrue(singleCharTokenizer.hasNext());
		assertTrue(singleCharTokenizer.hasNext());
	}
	
//	@Test
//	public void testFileTokenizer(){
//		fileSampleTokenizer.next();
//		fileSampleTokenizer.next();
//		fileSampleTokenizer.next();
//		fileSampleTokenizer.next();
//		assertEquals(TokenType.EOL, fileSampleTokenizer.next().getType());
//		assertEquals(TokenType.EOL, fileSampleTokenizer.next().getType()); // Skips comment
//		assertEquals(TokenType.EOL, fileSampleTokenizer.next().getType());
//		fileSampleTokenizer.pushBack();
//		assertEquals(TokenType.EOL, fileSampleTokenizer.next().getType());
//		assertEquals(TokenType.EOL, fileSampleTokenizer.next().getType());
//		assertEquals("Oi", fileSampleTokenizer.next().getValue());
//		fileSampleTokenizer.pushBack();
//		assertEquals("Oi", fileSampleTokenizer.next().getValue());
//		assertEquals(TokenType.SYMBOL, fileSampleTokenizer.next().getType());
//		assertEquals(".", fileSampleTokenizer.next().getValue());
//		assertEquals(TokenType.EOI, fileSampleTokenizer.next().getType());
//	}
	
	@Test
	public void testEOI(){
		assertEquals("token", t.next().getValue());
		assertEquals("token2", t.next().getValue());
		assertTrue(t.hasNext());
		assertEquals(TokenType.EOI, t.next().getType());
	}
	
	@Test
	public void testPushBack(){
		tokenizer.next();
		tokenizer.pushBack();
		assertEquals("Lorem", tokenizer.next().getValue());
		assertEquals("Ipsum", tokenizer.next().getValue());
		singleCharTokenizer.pushBack();
		assertEquals("a", singleCharTokenizer.next().getValue());
		singleCharTokenizer.next();
		singleCharTokenizer.pushBack();
		assertEquals(TokenType.EOI, singleCharTokenizer.next().getType());
	}
	
	@Test
	public void testTokenizerToken(){
		assertEquals(TokenType.KEYWORD, tokenizer.next().getType()); // Lorem
		assertEquals(TokenType.KEYWORD, tokenizer.next().getType()); // Ipsum
		assertTrue(tokenizer.hasNext());
		assertEquals("is", tokenizer.next().getValue());
		assertEquals("over", tokenizer.next().getValue());
		assertEquals(TokenType.NUMBER, tokenizer.next().getType());
		assertEquals("years", tokenizer.next().getValue());
		assertEquals("old", tokenizer.next().getValue());
		assertEquals(TokenType.SYMBOL, tokenizer.next().getType());
		assertEquals(TokenType.EOI, tokenizer.next().getType());
		assertFalse(tokenizer.hasNext());
		
	}

	@Test
	public void testSlashToken(){
		t = new Tokenizer(new StringReader("Good/*"), keywords);
		t.next();
		assertEquals(TokenType.SYMBOL, t.next().getType());
		assertEquals(TokenType.SYMBOL, t.next().getType());
		assertEquals(TokenType.EOI, t.next().getType());
	}
	
	@Test
	public void testNameToken(){
		t = new Tokenizer(new StringReader("Good_Morning"), keywords);
		assertEquals(TokenType.NAME, t.next().getType());
		assertEquals(TokenType.EOI, t.next().getType());
		t = new Tokenizer(new StringReader("?Good_M0r$ig"), keywords);
		assertEquals(TokenType.SYMBOL, t.next().getType());
		assertEquals(TokenType.NAME, t.next().getType());
		assertEquals(TokenType.EOI, t.next().getType());
		t = new Tokenizer(new StringReader("Double-barrelled"), keywords);
		assertEquals(TokenType.NAME, t.next().getType());		
		assertEquals(TokenType.SYMBOL, t.next().getType());
		assertEquals(TokenType.NAME, t.next().getType());
		assertEquals(TokenType.EOI, t.next().getType());		
	}
	
	@Test
	public void testEOLToken(){
		t = new Tokenizer(new StringReader("Good\n"), keywords);
		t.next();
		assertEquals(TokenType.EOL, t.next().getType());
		assertEquals(TokenType.EOI, t.next().getType());
	}
	
	@Test
	public void testFloatTokenizer(){
		assertEquals("1.0E10f", floatTokenizer.next().getValue());
		assertEquals("340", floatTokenizer.next().getValue());
		assertEquals(TokenType.EOI, floatTokenizer.next().getType());
		floatTokenizer = new Tokenizer(new StringReader(".e4"), keywords);
		assertEquals(TokenType.SYMBOL, floatTokenizer.next().getType());
		assertEquals(TokenType.NAME, floatTokenizer.next().getType());
		assertEquals(TokenType.EOI, floatTokenizer.next().getType());
		floatTokenizer = new Tokenizer(new StringReader(".0e+4"), keywords);
		Token token = floatTokenizer.next();
		assertEquals(TokenType.NUMBER, token.getType());
		assertEquals(".0e+4", token.getValue());
		assertEquals(TokenType.EOI, floatTokenizer.next().getType());
		floatTokenizer = new Tokenizer(new StringReader(".0e-4"), keywords);
		assertEquals(TokenType.NUMBER, floatTokenizer.next().getType());
		assertEquals(TokenType.EOI, floatTokenizer.next().getType());
		floatTokenizer = new Tokenizer(new StringReader("123ABC"), keywords);
		assertEquals("123", floatTokenizer.next().getValue());
		assertEquals("ABC", floatTokenizer.next().getValue());
		floatTokenizer = new Tokenizer(new StringReader("1.123e10d"), keywords);
		assertEquals(TokenType.NUMBER, floatTokenizer.next().getType());
		assertEquals(TokenType.EOI, floatTokenizer.next().getType());
		floatTokenizer = new Tokenizer(new StringReader("123e-10f"), keywords);
		assertEquals(TokenType.NUMBER, floatTokenizer.next().getType());
		assertEquals(TokenType.EOI, floatTokenizer.next().getType());
	}
	
	@Test
	public void testFloatTokenizer2(){
		floatTokenizer = new Tokenizer(new StringReader("21E"), keywords);
		Token token = floatTokenizer.next();
		assertEquals("21E", token.getValue());
		assertEquals(TokenType.ERROR, token.getType());		
		floatTokenizer = new Tokenizer(new StringReader("21E+"), keywords);
		Token token2 = floatTokenizer.next();
		assertEquals("21E+", token2.getValue());
		assertEquals(TokenType.ERROR, token.getType());		
		floatTokenizer = new Tokenizer(new StringReader("21\n31"), keywords);
		assertEquals(TokenType.NUMBER, floatTokenizer.next().getType());
		assertEquals(TokenType.EOL, floatTokenizer.next().getType());
		assertEquals(TokenType.NUMBER, floatTokenizer.next().getType());
		assertEquals(TokenType.EOI, floatTokenizer.next().getType());
		floatTokenizer = new Tokenizer(new StringReader("21."), keywords);
		assertEquals(TokenType.NUMBER, floatTokenizer.next().getType());
		assertEquals(TokenType.EOI, floatTokenizer.next().getType());
	}
	
	@Test
	public void testFloatTokenizer3(){
		floatTokenizer = new Tokenizer(new StringReader("3.3e-2e2.3.24"), keywords);
		assertEquals(TokenType.NUMBER, floatTokenizer.next().getType());
		assertEquals(TokenType.NAME, floatTokenizer.next().getType());
		assertEquals(TokenType.NUMBER, floatTokenizer.next().getType());
		assertEquals(TokenType.NUMBER, floatTokenizer.next().getType());
		assertEquals(TokenType.EOI, floatTokenizer.next().getType());
		
	}
	
	@Test
	public void testComments(){
		t = new Tokenizer(new StringReader("//this is a comment"), keywords);
		assertEquals(TokenType.EOI, t.next().getType());
		t = new Tokenizer(new StringReader("//this is a comment\n"), keywords);
		assertEquals(TokenType.EOL, t.next().getType());
		assertEquals(TokenType.EOI, t.next().getType());		
		t = new Tokenizer(new StringReader("//this is a comment\n314"), keywords);
		assertEquals(TokenType.EOL, t.next().getType());
		assertEquals(TokenType.NUMBER, t.next().getType());
		assertEquals(TokenType.EOI, t.next().getType());		
	}
	
//	@Test
//	public void testValidateFloat(){
//		assertEquals(null, t.validateFloat("123e+", 'a'));
//		assertEquals("123", t.validateFloat("123", 'a'));
//		assertEquals("123e", t.validateFloat("123", 'e'));
//		assertEquals(".", t.validateFloat("", '.'));
//		assertEquals("32", t.validateFloat("3", '2'));
//		assertEquals("32.", t.validateFloat("32.", 'g'));
//		assertEquals("32.f", t.validateFloat("32.", 'f'));
//		assertEquals("32f", t.validateFloat("32", 'f'));
//		assertEquals("32d", t.validateFloat("32", 'd'));
//		assertEquals("32F", t.validateFloat("32", 'F'));
//		assertEquals("32F", t.validateFloat("32F", 'l'));
//	}

}
