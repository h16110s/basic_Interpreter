package newlang5;

import newlang3.LexicalAnalyzer;

import java.util.HashMap;
import java.util.Map;

public class Environment extends newlang4.Environment {
	   Map library;		//Basicから呼び出せる関数の表
	   Map var_map;

	    public Environment(LexicalAnalyzer my_input) {
	    	super(my_input);
	        library = new HashMap();
	        library.put("PRINT", new PrintFunction() );		//PRINTはあらかじめ用意
	        var_map = new HashMap();
	    }

	    //引数ー関数名
		//NULLだったら関数未定義
	    public Function getFunction(String fname) {
	        return (Function) library.get(fname);
	    }

	    //変数そのものはvar_mapに格納
		//変数の記録はここで行われる．
	    public Variable getVariable(String vname) {
	        Variable v;
	        v = (Variable) var_map.get(vname);
	        if (v == null) {
	            v = new Variable(vname);
	            var_map.put(vname, v);
	        }
	        return v;
	    }
}
