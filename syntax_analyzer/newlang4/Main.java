package newlang4;

import newlang3.LexicalAnalyzer;
import newlang3.LexicalAnalyzerImpl;
import newlang3.LexicalUnit;

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
		Environment		env;
		Node program;
//		String path = /"D:\\Git\\basic_Interpreter\\syntax_analyzer\\newlang3\\test1.bas";
		String path = "/Users/hiro16110/gLocal/basic_interpreter/syntax_analyzer/newlang3/test1.bas";

		System.out.println("basic parser");
		try {
			fin = new FileInputStream(path);
			lex = new LexicalAnalyzerImpl(fin);
			env = new Environment(lex);
			first = lex.get();
			lex.unget(first);
			program = ProgramNode.getHandler(first.getType(), env);
			if (program != null) {
				program.parse();
				System.out.println(program);
//	        	System.out.println("value = " + program.getValue());
			}
		} catch (FileNotFoundException e) {
			e.fillInStackTrace();
		} catch (Exception e) {
			e.fillInStackTrace();
		}
	}
}
