package newlang4;

import newlang3.LexicalType;
import newlang3.LexicalUnit;
import newlang3.Value;

import java.util.*;

public class VariableNode extends Node {
    static final Set<LexicalType> first = new HashSet<>(Arrays.asList(LexicalType.NAME));
    private String name;
    private Value value;

    public VariableNode(String name) {
        name = name;
    }
    public VariableNode(LexicalUnit u) {
        name = u.getValue().getSValue();
    }

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
            throw new Exception("Variable Node Error: " + lu);
        }
    }

    public static Node getHandler(LexicalType type, Environment env){
        return new VariableNode(env);
    }

    public static Node getHandler(LexicalUnit lu){
        return new VariableNode(lu);
    }


    public void setValue(Value input){
        this.value = input;
    }
    @Override
    public String toString(){
        return this.name;
    }
}
