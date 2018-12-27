package newlang4;

import newlang3.LexicalType;
import newlang3.LexicalUnit;

import java.util.*;

public class VariableNode extends Node {
    static final Set<LexicalType> first = new HashSet<>(Arrays.asList(LexicalType.NAME));
    private String name;

    private VariableNode(Environment env){
        super(env);
        type = NodeType.VARIABLE;
    }

    public void parse() throws Exception{
        LexicalUnit lu = env.input.get();
        if(lu.getType() == LexicalType.NAME){
            this.name = lu.getValue().getSValue();
        }
        else{
            env.getInput().unget(lu);
            throw new Exception("Variable Node Error: " + lu);
        }
    }

    public static Node getHandler(LexicalType type, Environment env){
        return new VariableNode(env);
    }

    public String toString(){
        return "Variable: " + name;
    }
}
