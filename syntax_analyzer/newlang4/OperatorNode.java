package newlang4;

import newlang3.LexicalType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OperatorNode extends Node {
    private LexicalType operator;

    private final static Set<LexicalType> first = new HashSet<LexicalType>(Arrays.asList(
            LexicalType.EQ,
            LexicalType.LT,
            LexicalType.GT,
            LexicalType.LE,
            LexicalType.LE,
            LexicalType.GE,
            LexicalType.GE,
            LexicalType.NE,
            LexicalType.DOT,
            LexicalType.ADD,
            LexicalType.SUB,
            LexicalType.MUL,
            LexicalType.DIV,
            LexicalType.LP,
            LexicalType.RP
    ));

    public static boolean isMatch(LexicalType type){
        return first.contains(type);
    }

    private OperatorNode(LexicalType t, Environment env){
        super(env);
        type = NodeType.EXPR;
        operator = t;
    }

    public static Node getHandler(LexicalType type, Environment env){
        return new OperatorNode(type,env);
    }

    public LexicalType getOp(){
        return operator;
    }
}
