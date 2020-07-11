package test.parseblock;

import java.io.File;
import java.io.FileNotFoundException;

public class ParseBlockRunner {
	
	public static void main(String[] args) {
		
		CharStream stream = null;
		try {
			stream = new CharStream(64, new File("koi.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		
		
		
		BlockParser parser = new BlockParser();
		
		parser.parse(stream, new KoiEventListener() {
			
			@Override
			public void onObjectParsed(String parent, String key, char type, Object object) {
				System.out.printf("%s -> %s(%c) = %s\n", parent, key, type, object.toString());
				
			}
		});
		
		
	}
}
