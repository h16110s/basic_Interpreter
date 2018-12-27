package newlang4;

import jdk.nashorn.internal.runtime.ECMAException;
import newlang3.LexicalType;
import newlang3.LexicalUnit;

import java.util.*;
//
//<expr>	::=
//  <expr> <ADD> <expr>
//	| <expr> <SUB> <expr>
//	| <expr> <MUL> <expr>
//	| <expr> <DIV> <expr>
//	| <SUB> <expr>
//	| <LP> <expr> <RP>
//	| <NAME>
//	| <INTVAL>
//	| <DOUBLEVAL>
//	| <LITERAL>
//	| <call_func>

public class ExprNode extends Node {
    LexicalType operator;
    Node left;
    Node right;

    static final Set<LexicalType> first = new HashSet<LexicalType>(Arrays.asList(
            LexicalType.NAME,
            LexicalType.LP,
            LexicalType.INTVAL,
            LexicalType.DOUBLEVAL,
            LexicalType.LITERAL));

    static final Set<LexicalType> operators = new HashSet<>(Arrays.asList(
            LexicalType.DIV,
            LexicalType.MUL,
            LexicalType.SUB,
            LexicalType.ADD));

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

    public void parse() throws Exception{
        List<Node> exprs = new ArrayList<>();

        while(true){
//      <Operand>
            LexicalUnit lu = env.input.get();
            switch (lu.getType()){
                case NAME:
                    exprs.add(VariableNode.getHandler(lu.getType(),env));
                    System.out.println( "Expr Node: " + lu);
                    break;
                case LP:
                    break;
                case INTVAL:
                case DOUBLEVAL:
                case LITERAL:
                    exprs.add(ConstNode.getHandler(lu,env));
                    break;
                default:
                    throw new Exception("Expr Node Parse Error: ");
            }

            lu = env.input.get();
            if(operators.contains(lu.getType())){
                System.out.println("Expr Node Operator: " + lu);
            }
            else{
                break;
            }
        }
    }
}
