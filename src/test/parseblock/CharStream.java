package test.parseblock;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class CharStream
{
	private char[] buffer;
	
	private int ibuff;
	private int length;
	
	private BufferedReader read;
	
	public CharStream(int buffSize, Reader wrap) {
		read = new BufferedReader(wrap,buffSize);
		buffer = new char[buffSize];
		init();
	}
	public CharStream(int buffSize, File file) throws FileNotFoundException {
		read = new BufferedReader(new FileReader(file), buffSize);
		buffer = new char[buffSize];
		init();
	}
	
	public CharStream(int buffSize, String string) {
		buffer = new char[buffSize];
		read = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(string.getBytes()), StandardCharsets.UTF_8));
		init();
	}
	
	private void init() {
		try {
			length = read.read(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean hasNext() {
		return length != -1;
	}
	
	private void checkValid() {
		if(ibuff >= length) {
			try {
				length = read.read(buffer);
				ibuff = 0;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public char peek() {
		checkValid();
		return buffer[ibuff];
	}
	
	public char consume() {
		checkValid();
		return buffer[ibuff++];
	}
	public boolean end() {
		checkValid();
		return !hasNext();
	}
	
}
