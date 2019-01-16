package newlang4;

import newlang3.LexicalType;
import newlang3.LexicalUnit;
import newlang3.Value;

public class ProgramNode extends Node {
    Node stmtlist_handler;
    private ProgramNode(Environment env){
        super(env);
        type = NodeType.PROGRAM;
    }

    public static Node getHandler(LexicalType type, Environment env){
        return new ProgramNode(env);
    }

    public void parse(){
        try{
            LexicalUnit lu = env.input.get();
            env.input.unget(lu);
            stmtlist_handler = StmtListNode.getHandler(lu.getType() ,env);
            stmtlist_handler.parse();
        }catch (Exception e){
            System.out.println(e.fillInStackTrace());
        }
    }

    @Override
    public String toString(){
        return stmtlist_handler.toString();
    }

    public Value getValue() throws Exception{
        stmtlist_handler.getValue();
        return null;
    }
}
