package newlang4;

import newlang3.LexicalType;
import newlang3.LexicalUnit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ExprNode extends Node {

    static final Set<LexicalType> first = new HashSet<LexicalType>(Arrays.asList(
            LexicalType.NAME,
            LexicalType.LP,
            LexicalType.INTVAL,
            LexicalType.DOUBLEVAL,
            LexicalType.LITERAL));

    private ExprNode(Environment env){
        super(env);
        type = NodeType.EXPR;
    }

    public static boolean isMatch(LexicalType type) {
        return first.contains(type);
    }
    public static Node getHandler(LexicalType type, Environment env){
        return new ExprNode(env);
    }

    public void parse(){

    }


}
