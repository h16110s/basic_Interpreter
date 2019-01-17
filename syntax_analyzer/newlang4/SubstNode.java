package newlang4;

import newlang3.LexicalType;
import newlang3.LexicalUnit;
import newlang3.Value;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SubstNode extends Node {
    static final Set<LexicalType> first = new HashSet<LexicalType>(Arrays.asList(LexicalType.NAME));
    Node leftVar;
    Node expr_handler;


    private SubstNode(Environment env){
        super(env);
        type = NodeType.SUBST_NODE;
    }

    public static boolean isMatch(LexicalType type){
        return first.contains(type);
    }

    public static Node getHandler(LexicalType type , Environment env){
        return new SubstNode(env);
    }

    public void parse() throws Exception{
        LexicalUnit lu;
        //<NAME>
        lu = env.input.get();
        env.getInput().unget(lu);
        leftVar = VariableNode.getHandler(lu.getType(),env);

        //<EQ>
        lu = env.getInput().get();
        if(lu.getType() != LexicalType.EQ){
            throw new Exception("Subst Parse eq Error" + lu);
        }

        //<expr>
        lu = env.getInput().get();
        if(ExprNode.isMatch(lu.getType())){
            env.getInput().unget(lu);
            expr_handler = ExprNode.getHandler(lu.getType(), env);
            expr_handler.parse();
        }
        else{
            throw new Exception("Subst Parse expr Error" + lu);
        }
    }

    @Override
    public String toString(){
        return leftVar.toString()+ expr_handler.toString();
    }

    @Override
    public Value getValue() throws Exception {
        Value rv = expr_handler.getValue();
        leftVar.setValue(rv);
        return null;
    }
}
