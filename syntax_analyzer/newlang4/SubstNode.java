package newlang4;

import newlang3.LexicalType;
import newlang3.LexicalUnit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SubstNode extends Node {
    static final Set<LexicalType> first = new HashSet<LexicalType>(Arrays.asList(LexicalType.NAME));
    LexicalUnit leftVar;
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
        leftVar = env.input.get();
        System.out.println("Subst leftvar: " + leftVar);

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


}
