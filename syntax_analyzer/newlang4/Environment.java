package newlang4;

import newlang3.LexicalAnalyzer;

public class Environment {
	   LexicalAnalyzer input;
	    
	    public Environment(LexicalAnalyzer my_input) {
	        input = my_input;
	    }
	    
	    public LexicalAnalyzer getInput() {
	        return input;
	    }	    
}
