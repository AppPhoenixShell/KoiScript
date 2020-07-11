package test.parseblock;

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;
import java.util.stream.Stream;



public class BlockParser {

	private static final char[] ALPHA = "abcdefghijklmnopqrstuvwxyz_ABCDEFGHIJKLMNOPQRSTUVWXZY".toCharArray();
	private static final char[] LOW = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	private static final char[] DIGIT = "1234567890".toCharArray();
	
	private final char[] buffer = new char[100];
	private int length;
	private int index;
	
	private final Stack<String> pathStack = new Stack<String>();
	
	
	private String currentRef;
	
	private boolean exists(char c, char[] charsets) {
		for(int i=0; i< charsets.length; i++)
			if(c == charsets[i])
				return true;
		return false;
	}
	
	private boolean isAlpha(char c) {
		return exists(c, ALPHA);
	}
	private boolean isAlphaLow(char c) {
		return exists(c, LOW);
	}
	
	public void parse(Reader reader, KoiEventListener listener) {
		
	}
	public void parse(CharStream stream, KoiEventListener listener) {
		STATE_root(stream, listener);
		
	}
	
	public String getCurrentPath() {
		StringBuilder builder=  new StringBuilder();
		if(pathStack.size() == 0)
			return "/";
		for(int i=0; i < pathStack.size(); i++) {
			builder.append("/");
			builder.append(pathStack.get(i));
			
		}
		return builder.toString();
	}
	private void pushPath(String path) {
		pathStack.push(path);
	}
	private void popPath() {
		pathStack.pop();
	}
	
	
	
	private void STATE_root(CharStream stream, KoiEventListener koiEventListener) {
		
		while(!stream.end()) {
			char next = stream.consume();
			
			switch(next) {
				case '@':{
					
					STATE_readObject(stream, koiEventListener);
					
					//System.out.printf("key:value|%s -> %s\n", newObject.key, newObject.value);
					
				}break;
				default:{
					if(Character.isWhitespace(next))
						STATE_ignoreWhitepace(stream);
				}
			}	
		}
	}
	private void STATE_ignoreWhitepace(CharStream stream) {
		while(!stream.end()) {
			char peek = stream.peek();
			if(Character.isWhitespace(peek))
				stream.consume();
			else
				return;
		}
	}
	
	private String STATE_readToken(CharStream stream, char[] charSet) {
		StringBuilder token = new StringBuilder();
		while(!stream.end()) {
			char peek = stream.peek();
			if(exists(peek, charSet))
				token.append(stream.consume());
			else
				return token.toString();
		}
		return token.toString();
	}
	
	
	private boolean STATE_readBool(CharStream stream) {
		STATE_ignoreWhitepace(stream);
		String boolToken = STATE_readToken(stream, LOW);
		if(boolToken.contentEquals("true"))
			return true;
		else if(boolToken.contentEquals("false"))
			return false;
		
		throw new KoiParserException("Must contain boolean (true|false) after ?"); 
		
		
	}
	private char STATE_escapeChar(CharStream stream) {
		char escaped = stream.consume();
		return escaped;
	}
	
	private String STATE_readString(CharStream stream) {
		StringBuilder builder= new StringBuilder();
		while(!stream.end()) {
			char peek = stream.peek();
			
			switch(peek) {
			case '"': stream.consume(); return builder.toString();
			case '\\':stream.consume(); builder.append(STATE_escapeChar(stream));
			default: builder.append(stream.consume());
			}
		}
		return builder.toString();
		
	}
	private void STATE_childObject(CharStream stream, KoiEventListener listener) {
		STATE_ignoreWhitepace(stream);
		char type = stream.consume();
		
		switch(type) {
			case '@':{
				STATE_readObject(stream, listener);
			}break;
		}
		
		
	}

	private void STATE_readObject(CharStream stream, KoiEventListener listener) {
		String ref = STATE_readToken(stream, ALPHA);
		
		
		STATE_ignoreWhitepace(stream);
		
		char type = stream.consume();
		String currentPath = getCurrentPath();
	
		
		switch(type) {
			case '?':{
				STATE_ignoreWhitepace(stream);
				boolean nextBool = STATE_readBool(stream);
				listener.onObjectParsed(currentPath, ref, type, nextBool);
			}break;
			case '"':{
				String nextString = STATE_readString(stream);
				listener.onObjectParsed(currentPath, ref, type, nextString);
			}break;
			case '=':{
				STATE_ignoreWhitepace(stream);
				String digitToken = STATE_readToken(stream, DIGIT);
				char peek= stream.peek();
				
				if(peek == '.') {
					stream.consume();
					String decimalToken = STATE_readToken(stream, DIGIT);
					
					double dec = Double.parseDouble(String.format("%s.%s", digitToken, decimalToken));
					listener.onObjectParsed(currentPath, ref, type, dec);
					
				}
				else {
					int nextInt = Integer.parseInt(digitToken);
					listener.onObjectParsed(currentPath, ref, type, nextInt);
				}
			}break;
			case '{':{
				pushPath(ref);
				STATE_childObject(stream, listener);
			}break;
			case '}':{
				popPath();
			}break;
			
		}
	}

	
	
	
	
	private void throwParseException(String msg) {
		throw new RuntimeException(msg);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
