package newlang4;

import newlang3.LexicalType;
import newlang3.LexicalUnit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StmtNode extends Node {
    static final Set<LexicalType> first = new HashSet<LexicalType>(Arrays.asList(LexicalType.FOR, LexicalType.END, LexicalType.NAME));
    private StmtNode(Environment env){
        super(env);
        type = NodeType.STMT;
    }

    static boolean isMatch(LexicalType type){
        return first.contains(type);
    }

    static  Node getHandler(LexicalType type,Environment env) throws Exception{
        LexicalUnit lu = env.input.get();
        switch(lu.getType()) {
            case END:
                return EndNode.getHandler(lu.getType(), env);
            case NAME:
                LexicalUnit second_unit = env.getInput().get();
                //2個分戻す
                env.getInput().unget(lu);
                env.getInput().unget(second_unit);

                if(second_unit.getType() == LexicalType.EQ){
                    return SubstNode.getHandler(lu.getType(), env);
                }

                else if(CallSubNode.isMatch(lu.getType())){
                    return CallSubNode.getHandler(lu.getType(),env);
                }

                else{
                    throw new Exception("Syntax error StmtNode: " + second_unit);
                }
            default:
                throw new Exception("StmtNode.getHandler Error:" + lu);
        }
    }

    public void parse(){ }
}