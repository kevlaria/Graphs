package graphs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;

/**
 * A tokenizer. A token is one of: A word (a name or a keyword), a number, a
 * punctuation mark, an end-of-line, or an end-of-input.
 * 
 * @author kevinlee
 * 
 */
public class Tokenizer {

	private enum States {
		READY, IN_NUMBER, IN_VARIABLE, IN_COMMENTS, ERROR
	}

	private BufferedReader reader;
	private final int BUFFERLIMIT = 100;
	private Set<String> keywords;
	private boolean hasNext;
	private boolean pushBack;
	private Token previousToken = null;

	/**
	 * Constructor for Tokenizer object
	 * 
	 * @param reader
	 *            The Reader object to be tokenized
	 * @param keywords
	 *            A set of keywords that will be marked as keywords rather than
	 *            names
	 */
	public Tokenizer(Reader reader, Set<String> keywords) {
		if (reader == null)
			throw new NullPointerException();
		this.reader = new BufferedReader(reader);
		this.keywords = keywords;
		this.hasNext = true;
	}

	/**
	 * Determines if there are more tokens to be returned.
	 * 
	 * @return true if there are more tokens to be returned
	 */
	public boolean hasNext() {
		return hasNext;
	}

	/**
	 * Returns the next token from the string, or if pushBack is set to true,
	 * returns the pushBack Token. Skips over comments marked by //
	 * 
	 * @return The next Token from the Reader or the previous Token if pushBack is called immediately beforehand
	 */
	public Token next() {
		if (pushBack) {
			pushBack = false;
			return previousToken;
		}
		try {
			States state;
			String value = "";
			if (!this.hasNext())
				throw new RuntimeException("No more tokens!");
			state = States.READY;
			while (true) {
				reader.mark(BUFFERLIMIT);
				int chInt = reader.read();
				char ch = (char) chInt;
				switch (state) {
				case READY: {
					value = ch + "";
					if (Character.isJavaIdentifierStart(ch)) {
						state = States.IN_VARIABLE;
						break;
					}
					if ((Character.isDigit(ch)) || (ch == '.')) {
						state = States.IN_NUMBER;
						break;
					}
 					if (ch == '/') {
						state = States.IN_COMMENTS;
						break;
					}
					if (ch == '\n') {
						return createToken(TokenType.EOL, "\n");
					}
					if (Character.isWhitespace(ch))
						break;
					if (chInt == -1) {
						this.hasNext = false;
						return createToken(TokenType.EOI, "");
					}

					// any single character that isn't whitespace and isn't part
					// of a name or number is a symbol
                    if (keywords.contains(value)) {
                        return createToken(TokenType.KEYWORD, value.trim());
                    } else {
                        return createToken(TokenType.SYMBOL, value.trim());
                    }
				}
				case IN_VARIABLE: {
					if (Character.isJavaIdentifierPart(ch)) {
						value += ch;
					} else {
						reader.reset();
						state = States.READY;
						if (keywords.contains(value)) {
							return createToken(TokenType.KEYWORD, value.trim());
						} else {
							return createToken(TokenType.NAME, value.trim());
						}
					}
					break;
				}
				case IN_NUMBER: {
					String output = validateFloat(value, ch);
					if (output == null) { // Results in invalid number, value is
											// also an invalid number
						if (value.equals(".")) { // Special case where value is
													// a .
							reader.reset();
							state = States.READY;
							return createToken(TokenType.SYMBOL, value.trim());
						} else {
							reader.reset();
							state = States.READY;
							return createToken(TokenType.ERROR, value.trim());
						}
					} else if (output.equals(value)) { // Results in invalid
														// string, but value is
														// a valid number
						reader.reset();
						state = States.READY;
						return createToken(TokenType.NUMBER, value.trim());
					} else { // Results in a valid number
						value += ch;
					}

					break;
				}
				case IN_COMMENTS: {
					if (value.length() == 1) {
						if (ch == '/') { // Valid comment syntax
							value += ch;
						} else { // Invalid comment syntax
							reader.reset();
							state = States.READY;
							return createToken(TokenType.SYMBOL, value.trim());
						}
					} else { // Comment body
						if (ch == '\n' || ch == 65535) { // End of comment
							reader.reset();
							state = States.READY;
						} else {
							value += ch;
						}
					}
					break;
				}

				default: {
					state = States.READY;
					return createToken(TokenType.ERROR, value.trim());
				}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException();
		}

	}

	/**
	 * Tells this Tokenizer to "take back" the token just returned by next(), so
	 * that a subsequent call to next() will return the same token again. The
	 * Tokenizer can only "take back" one token.
	 */
	public void pushBack() {
		if (previousToken != null) {
			this.pushBack = true;
			this.hasNext = true;
		}
	}

	/**
	 * Determines whether a numerical input is valid or not Returns null if it's
	 * invalid
	 */
	private String validateFloat(String value, char thisChar) {
		if (thisChar == 65535){		// Special case when we are at EOI
			try{
				Double.parseDouble(value);
				return value;
			} catch (NumberFormatException e0){
				return null;
			}
		}
		if (thisChar == '\n'){		// Special case when we are at EOL
			try{
				Double.parseDouble(value);
				return value;
			} catch (NumberFormatException e0){
				return null;
			}
		}
		
		String newValue = value + thisChar;
		try {
			Double.parseDouble(newValue);
			return newValue;
		} catch (NumberFormatException e1) {
			// Try and add an extra digit (ie 12e isn't valid but 12e3 is valid)
			String newAmendedValue = newValue + "1";
			try {
				Double.parseDouble(newAmendedValue);
				return newValue;
			} catch (NumberFormatException e2) {
				try {
					// value is valid, but value + thisChar is no longer valid
					// eg 14z, 24ft
					Double.parseDouble(value);
					return value;
				} catch (NumberFormatException e3) {
					// Invalid number eg 34e+A
					return null;
				}
			}

		}
	}

	/**
	 * Creates a token, and sets previousToken to the newly created token
	 */
	private Token createToken(TokenType type, String value) {
		Token token = new Token(type, value);
		this.previousToken = token;
		return token;
	}

}
