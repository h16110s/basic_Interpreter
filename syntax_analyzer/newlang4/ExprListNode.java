package newlang4;


import newlang3.LexicalAnalyzer;
import newlang3.LexicalType;
import newlang3.LexicalUnit;

import java.util.*;

public class ExprListNode extends Node {
    List<Node> child = new ArrayList<>();
    static final Set<LexicalType> first = new HashSet<>(Arrays.asList(
            LexicalType.NAME,
            LexicalType.LP,
            LexicalType.INTVAL,
            LexicalType.DOUBLEVAL,
            LexicalType.LITERAL));

    public static boolean isMatch(LexicalType type){
        return first.contains(type);
    }

    private ExprListNode(Environment env){
        super(env);
        type = NodeType.EXPR_LIST;
    }

    public static Node getHandler(LexicalType type, Environment env){
        if(!isMatch(type)) return null;
        return new ExprListNode(env);
    }

    public void parse() throws Exception{
        while (true){
            LexicalUnit lu = env.getInput().get();
            if(ExprNode.isMatch(lu.getType())){
                env.getInput().unget(lu);
                Node expr_handler = ExprNode.getHandler(lu.getType(),env);
                expr_handler.parse();
                child.add(expr_handler);
            }
            else{
                throw new Exception("ExprList Parse Error " + env.getInput().getLine());
            }

            LexicalUnit comma_unit = env.getInput().get();
            if(comma_unit.getType() == LexicalType.COMMA) {
                continue;
            }
            else if(comma_unit.getType() == LexicalType.NL){
                break;
            }
        }
    }

    public int size(){
        return child.size();
    }
}
