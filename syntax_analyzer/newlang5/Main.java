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
		LexicalUnit		first;
		Environment		env;
		Node			prog;
		System.out.println("basic parser");
		try {
			fin = new FileInputStream("/Users/hiro16110/gLocal/basic_interpreter/syntax_analyzer/newlang5/test.bas");
		}
		catch(Exception e) {
			System.out.println("file not found");
			System.exit(-1);
		}
		lex = new LexicalAnalyzerImpl(fin);
		env = new Environment(lex);

		try {
			first = lex.get();
			lex.unget(first);
			prog = ProgramNode.getHandler(first.getType(), env);
			if (prog != null){
				prog.parse();
				prog.getValue();
			}
			else System.out.println("syntax error");
		}
		catch(Exception e) {
			System.out.println("execution error");
		}
	}

}
