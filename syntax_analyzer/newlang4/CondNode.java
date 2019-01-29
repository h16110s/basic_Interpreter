package newlang4;

import newlang3.LexicalType;
import newlang3.LexicalUnit;
import newlang3.Value;
import newlang3.ValueImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static newlang3.ValueType.BOOL;

// <cond> ::=
// <expr> <EQ> <expr>
// | <expr> <GT> <expr>
// | <expr> <LT> <expr>
// | <expr> <GE> <expr>
// | <expr> <LE> <expr>
// | <expr> <NE> <expr>

public class CondNode extends Node {
    Node right;
    Node left;
    LexicalUnit operator_unit;
    static final Set<LexicalType> first = new HashSet<LexicalType>(Arrays.asList(
            LexicalType.NAME,
            LexicalType.LP,
            LexicalType.INTVAL,
            LexicalType.DOUBLEVAL,
            LexicalType.LITERAL));

    private CondNode(Environment env) {
        super(env);
        type = NodeType.COND;
    }

    public static boolean isMatch(LexicalType type) {
        return first.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) {
        return new CondNode(env);
    }

    public void parse() throws Exception{
        LexicalUnit leftExpr_unit = env.getInput().get();
        if(ExprNode.isMatch(leftExpr_unit.getType())){
            env.getInput().unget(leftExpr_unit);
            left = ExprNode.getHandler(leftExpr_unit.getType(),env);
            left.parse();
        }
        else throw new Exception("Cond LeftExpr Parse Error: "+env.getInput().getLine());

        operator_unit = env.getInput().get();
        if(!condOperators.contains(operator_unit.getType()))
            throw new  Exception("Cond Operator missing: " + env.getInput().getLine());

        LexicalUnit rightExpr_unit = env.getInput().get();
        if(ExprNode.isMatch(rightExpr_unit.getType())){
            env.getInput().unget(rightExpr_unit);
            right = ExprNode.getHandler(rightExpr_unit.getType(),env);
            right.parse();
        }else throw new Exception("Cond RightExpr Parse Error: "+env.getInput().getLine());
    }

    @Override
    public String toString(){
        return operator_unit.getType().toString() + left.toString()+ ":" +right.toString();
    }


    public Value getValue() throws Exception{
        Value r = right.getValue();
        Value l = left.getValue();
        switch (operator_unit.getType()){
            case EQ:
                if(l.getIValue() == r.getIValue()){
                    return new ValueImpl("true",BOOL);
                }
                else{
                    return new ValueImpl("false",BOOL);
                }
            case LT:
                if(l.getIValue() < r.getIValue()){
                    return new ValueImpl("true",BOOL);
                }
                else{
                    return new ValueImpl("false",BOOL);
                }
            case GT:
                if(l.getIValue() > r.getIValue()){
                    return new ValueImpl("true",BOOL);
                }
                else{
                    return new ValueImpl("false",BOOL);
                }
            case LE:
                if(l.getIValue() <= r.getIValue()){
                    return new ValueImpl("true",BOOL);
                }
                else{
                    return new ValueImpl("false",BOOL);
                }
            case GE:
                if(l.getIValue() >= r.getIValue()){
                    return new ValueImpl("true",BOOL);
                }
                else{
                    return new ValueImpl("false",BOOL);
                }
            case NE:
                if(l.getIValue() != r.getIValue()){
                    return new ValueImpl("true",BOOL);
                }
                else{
                    return new ValueImpl("false",BOOL);
                }
        }
        return new ValueImpl("false",BOOL);
    }
}