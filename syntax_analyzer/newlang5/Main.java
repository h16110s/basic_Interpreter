package newlang5;

import newlang3.LexicalAnalyzer;
import newlang3.LexicalAnalyzerImpl;
import newlang3.LexicalUnit;
import newlang4.Node;
import newlang4.ProgramNode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	       FileInputStream fin = null;
	        LexicalAnalyzer lex;
	        LexicalUnit first;
	        Environment env;
	        Node program;
	  
	        System.out.println("basic parser");
	        try {
	            fin = new FileInputStream("test.txt");
				lex = new LexicalAnalyzerImpl(fin);
				env = new Environment(lex);
				first = env.getInput().get();
				env.getInput().unget(first);
				program = ProgramNode.getHandler(first.getType(), env);
				if (program != null ) {
					program.parse();
					program.getValue();
				}
				else System.out.println("syntax error");

	        }
	        catch(FileNotFoundException e) {
	            System.out.println("file not found");
	            System.exit(-1);
	        }
	        catch (Exception e){
				System.out.println(e.fillInStackTrace());
			}

	}

}
