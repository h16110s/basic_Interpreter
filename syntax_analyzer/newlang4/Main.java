package newlang4;

import newlang3.LexicalAnalyzer;
import newlang3.LexicalAnalyzerImpl;
import newlang3.LexicalUnit;

import java.io.FileInputStream;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
	       FileInputStream fin = null;
	        LexicalAnalyzer lex;
	        LexicalUnit first;
	        Environment		env;
	        Node			program;
	  
	        System.out.println("basic parser");
	        fin = new FileInputStream("test.txt");
	        lex = new LexicalAnalyzerImpl(fin);
	        env = new Environment(lex);
	        first = lex.get();
	        lex.unget(first);
	        
	        program = ProgramNode.getHandler(first.getType(), env);
	        if (program != null ) {
				program.parse();
	        	System.out.println(program);
//	        	System.out.println("value = " + program.getValue());
	        }
	        else System.out.println("syntax error");
	}

}
