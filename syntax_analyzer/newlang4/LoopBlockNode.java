package newlang4;

import newlang3.LexicalType;
import newlang3.LexicalUnit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//	| <WHILE> <cond> <NL> <stmt_list> <WEND> <NL>
//	| <DO> <WHILE> <cond> <NL> <stmt_list> <LOOP> <NL>
//	| <DO> <UNTIL> <cond> <NL> <stmt_list> <LOOP> <NL>
//	| <DO> <NL> <stmt_list> <LOOP> <WHILE> <cond> <NL>
//	| <DO> <NL> <stmt_list> <LOOP> <UNTIL> <cond> <NL>


public class LoopBlockNode extends Node {
    Node cond_handler;
    Node stmtList_handler;

    static final Set<LexicalType> first = new HashSet<>(Arrays.asList(
            LexicalType.DO,
            LexicalType.WHILE
    ));


    public LoopBlockNode(Environment env){
        super(env);
        type = NodeType.LOOP_BLOCK;
    }
    public static Node getHandler(LexicalType type, Environment env){
        if(!isMatch(type)) return null;
        return new LoopBlockNode(env);
    }

    static boolean isMatch(LexicalType type){
        return first.contains(type);
    }

    public void parse() throws Exception{
//        <WHILE>
        LexicalUnit lu = env.getInput().get();
        if(lu.getType() == LexicalType.WHILE){

//            <COND>
            LexicalUnit cond_unit = env.getInput().get();
            if(ConstNode.isMatch(cond_unit.getType())){
                env.getInput().unget(cond_unit);
                cond_handler = CondNode.getHandler(cond_unit.getType(), env);
                cond_handler.parse();
            }
            else{
                throw new Exception("WHILE cond Parse Error:" + env.getInput().getLine());
            }
//            <NL>
            LexicalUnit nl_unit = env.getInput().get();
            if(nl_unit.getType() != LexicalType.NL)
                throw new Exception("WHILE Parse Error: " + env.getInput().getLine());

//            <StmtList>
            LexicalUnit stmtList_unit = env.getInput().get();
            if(StmtListNode.isMatch(stmtList_unit.getType())){
                env.getInput().unget(stmtList_unit);
                stmtList_handler = StmtListNode.getHandler(stmtList_unit.getType(),env);
                stmtList_handler.parse();
            }
            else{
                throw new Exception("Stmt List Parse Error:"+ env.getInput().getLine());
            }
//            <WEND>
            LexicalUnit wend_unit = env.getInput().get();
            if(wend_unit.getType() != LexicalType.WEND)
                throw new Exception("WHND Parse Error:" + env.getInput().getLine());

//            <NL>
            LexicalUnit nl_unit2 = env.getInput().get();
            if(nl_unit2.getType() != LexicalType.NL)
                throw new Exception("WHILE Parse Error: " + env.getInput().getLine());

        }

//        <DO>
        else if(lu.getType() == LexicalType.DO){
            LexicalUnit second_unit = env.getInput().get();
            if(second_unit.getType() == LexicalType.WHILE
                    ||second_unit.getType() == LexicalType.UNTIL) {

//            <COND>
                LexicalUnit cond_unit = env.getInput().get();
                if(CondNode.isMatch(cond_unit.getType())){
                    env.getInput().unget(cond_unit);
                    cond_handler = CondNode.getHandler(cond_unit.getType(), env);
                    cond_handler.parse();
                }
                else{
                    throw new Exception("DO cond Parse Error:" + env.getInput().getLine());
                }

//            <NL>
                LexicalUnit nl_unit = env.getInput().get();
                if(nl_unit.getType() != LexicalType.NL)
                    throw new Exception("DO Parse Error:" + env.getInput().getLine());

//            <StmtList>
                LexicalUnit stmtList_unit = env.getInput().get();
                if(StmtListNode.isMatch(stmtList_unit.getType())){
                    env.getInput().unget(stmtList_unit);
                    stmtList_handler = StmtListNode.getHandler(stmtList_unit.getType(),env);
                    stmtList_handler.parse();
                }
                else{
                    throw new Exception("DO Stmt List Parse Error:"+ env.getInput().getLine());
                }

//            <LOOP>
                LexicalUnit loop_unit = env.getInput().get();
                if(loop_unit.getType() != LexicalType.LOOP)
                    throw new Exception("DO LOOP Parse Error:" + env.getInput().getLine());

            }



            else if (second_unit.getType() == LexicalType.NL){
//            <StmtList>
                LexicalUnit stmtList_unit = env.getInput().get();
                if(StmtListNode.isMatch(stmtList_unit.getType())){
                    env.getInput().unget(stmtList_unit);
                    stmtList_handler = StmtListNode.getHandler(stmtList_unit.getType(),env);
                    stmtList_handler.parse();
                }
                else{
                    throw new Exception("Stmt List Parse Error:"+ env.getInput().getLine());
                }

//            <LOOP>
                LexicalUnit loop_unit = env.getInput().get();
                if(loop_unit.getType() != LexicalType.LOOP)
                    throw new Exception("LOOP Parse Error:" + env.getInput().getLine());

//            <WHILE> <UNTIL>
                LexicalUnit sixth_unit = env.getInput().get();
                if(sixth_unit.getType() != LexicalType.WHILE ||
                    sixth_unit.getType() != LexicalType.UNTIL)
                    throw new Exception("DO block syntax error");

//            <COND>
                LexicalUnit cond_unit = env.getInput().get();
                if(ConstNode.isMatch(cond_unit.getType())){
                    env.getInput().unget(cond_unit);
                    cond_handler = CondNode.getHandler(cond_unit.getType(), env);
                    cond_handler.parse();
                }
                else{
                    throw new Exception("WHILE cond Parse Error:" + env.getInput().getLine());
                }
            }

            else{
                throw new Exception("DO Parse Error: " + env.getInput().getLine());
            }

//            <NL>
            LexicalUnit nl_unit = env.getInput().get();
            if(nl_unit.getType() != LexicalType.NL)
                throw new Exception("WHILE Parse Error:" + env.getInput().getLine());

        }
        else throw new Exception("Syntax Error WHILE block" + env.getInput().getLine());
    }

}
