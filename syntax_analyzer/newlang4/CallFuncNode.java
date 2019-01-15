package newlang4;

import newlang3.LexicalType;
import newlang3.LexicalUnit;
import newlang3.Value;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CallFuncNode extends Node {
    static Set<LexicalType> first = new HashSet<>(Arrays.asList(LexicalType.NAME));
    String name;
    Node exprList;
    LexicalUnit lp_unit = null;

    private CallFuncNode(Environment env){
        super(env);
        type = NodeType.FUNCTION_CALL;
    }

    public static boolean isMatch(LexicalType type){
        return first.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env){
        return new CallFuncNode(env);
    }

    public void parse() throws Exception{
        LexicalUnit name_unit = env.getInput().get();

//        function name
        if(name_unit.getType() != LexicalType.NAME)
            throw new Exception("CallSub parse error:" + env.getInput().getLine());
        this.name = name_unit.getValue().getSValue();

//        <LP> あったら回収
        lp_unit = env.getInput().get();
        if(lp_unit.getType() != LexicalType.LP)
            throw new Exception("CallSub missing \"(\"" + env.getInput().getLine());
//            env.getInput().unget(lp_unit);

//        <Expr>
        LexicalUnit expr_unit = env.getInput().get();
        if(ExprNode.isMatch(expr_unit.getType())){
            env.getInput().unget(expr_unit);
            this.exprList = ExprNode.getHandler(expr_unit.getType(), env);
            this.exprList.parse();
        }

//        <RP>　あったら回収
        LexicalUnit rp_unit = env.getInput().get();
        if(rp_unit.getType() != LexicalType.RP)
            throw new Exception("CallSub missing \")\"" + env.getInput().getLine());
//            env.getInput().unget(rp_unit);


    }

    @Override
    public String toString(){
        return name + exprList.toString();
    }

    @Override
    public Value getValue() throws Exception {
        return null;
    }
}
