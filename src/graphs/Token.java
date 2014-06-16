package graphs;

/**
 * A token. A Token has a type and a value. The value is a String containing the
 * exact characters that make up the Token. The type is a TokenType that tells
 * what kind of thing the Token represents--a variable, keyword, number, or
 * symbol (any single character that isn't whitespace and isn't part of a name
 * or number). End-of-lines and the (one) end of input are also returned as
 * tokens.
 * 
 * @author kevinlee
 * 
 */
public class Token {

	private TokenType type;
	private String value;

	/**
	 * Constructor for Token
	 * 
	 * @param type
	 *            Type of token
	 * @param value
	 *            Value of token
	 */
	public Token(TokenType type, String value) {
		if (type == null || value == null)
			throw new NullPointerException();
		this.type = type;
		this.value = value;
	}

	/**
	 * A getter method for the token's value
	 * 
	 * @return The tokens' value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * A getter method for the token's type
	 * 
	 * @return The token's type
	 */
	public TokenType getType() {
		return this.type;
	}

	/**
	 * Equals method. True if types match and values match
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Token))
			return false;
		Token that = (Token) o;
		return (this.type == that.type && this.value == that.value);
	}

	/**
	 * Hashcode for Token. Equals the hashcode of the value of the token.
	 */
	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	/**
	 * String representation of the Token. Displays in the format value:type
	 */
	@Override
	public String toString() {
		return value + ":" + type;
	}

}
