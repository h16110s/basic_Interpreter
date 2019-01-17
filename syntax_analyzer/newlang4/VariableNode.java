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
        type = NodeType.VARIABLE;
        this.name = name;
    }
    private VariableNode(LexicalUnit u) {
        name = u.getValue().getSValue();
    }

    private VariableNode(Environment env){
        super(env);
        type = NodeType.VARIABLE;
    }

    public static Node getHandler(LexicalUnit lu){
        return new VariableNode(lu);
    }

    public static Node getHandler(LexicalType first, Environment env) {
        if (first == LexicalType.NAME) {
            VariableNode v;
            try {
                LexicalUnit lu = env.getInput().get();
                String s = lu.getValue().getSValue();
                v = env.getVariable(s);
                return v;
            }
            catch(Exception e) {}
        }
        return null;
    }

    public void setValue(Value input){
        this.value = input;
    }

    @Override
    public String toString(){
        return this.name;
    }

    public Value getValue() throws Exception {
        return value;
    }
}
