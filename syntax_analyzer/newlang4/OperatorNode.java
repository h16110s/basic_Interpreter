package newlang4;

import newlang3.LexicalType;
import newlang3.LexicalUnit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OperatorNode extends Node {
    static final Set<LexicalType> first = new HashSet<>(Arrays.asList(
            LexicalType.DIV,
            LexicalType.MUL,
            LexicalType.SUB,
            LexicalType.ADD));

    private OperatorNode(LexicalUnit unit, Environment env){
        super(env);
        switch(unit.getType()){
            case ADD:
                type = NodeType.ADD_OPERATOR;
                break;
            case MUL:
                type = NodeType.MUL_OPERATOR;
                break;
            case SUB:
                type = NodeType.SUB_OPERATOR;
                break;
            case DIV:
                type = NodeType.DIV_OPERATOR;
                break;
        }

    }

    static boolean isMatch(LexicalType type){
        return first.contains(type);
    }

    static Node getHandler(LexicalUnit unit, Environment env){
        return new OperatorNode(unit, env);
    }

    @Override
    public String toString(){
        switch (type){
            case ADD_OPERATOR:
                return "+";
            case MUL_OPERATOR:
                return "*";
            case SUB_OPERATOR:
                return "-";
            case DIV_OPERATOR:
                return "/";
            default:
                    return null;
        }
    }
}
