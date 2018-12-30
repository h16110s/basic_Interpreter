package newlang4;

import newlang3.LexicalType;
import newlang3.LexicalUnit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//<if_prefix>  ::=
//<IF> <cond> <THEN>
//
//<else_block>  ::=
//<else_if_block>
//	| <else_if_block> <ELSE> <NL> <stmt_list>
//
//<else_if_block>   ::=
//        φ
//        ｜<else_if_block> <ELSEIF> <cond> <THEN> <NL> <stmt_list>



public class IfBlockNode extends Node {
    static Set<LexicalType>  first = new HashSet<LexicalType>(Arrays.asList(LexicalType.IF));
    Node cond_handler = null;
    Node stmt_handler = null;
    Node else_if_stmt_handler = null;
    Node else_stmt_handler = null;



    public static boolean isMatch(LexicalType type) {
        return first.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) {
        if(type != LexicalType.IF) return null;
        return new IfBlockNode(env);
    }
    private IfBlockNode(Environment env){
        super(env);
        type = NodeType.IF_BLOCK;
    }

    public void parse() throws Exception {
//=========<if_prefix>
//        <IF>
        env.getInput().get();
        LexicalUnit f = env.getInput().get();
        env.getInput().unget(f);

//        <COND>
        if(!CondNode.isMatch(f.getType()))
            throw new Exception("IFBlockNode cond Parse Error:");
        cond_handler = CondNode.getHandler(f.getType(),env);
        cond_handler.parse();

//        <THEN>
        LexicalUnit next = env.getInput().get();
        if(next.getType() != LexicalType.THEN)
            throw new Exception("Syntax Error IFBlockNode" + next);
//====================

//        <Stmt>
        LexicalUnit stmt_unit = env.getInput().get();
        if(StmtNode.isMatch(stmt_unit.getType())){
            env.getInput().unget(stmt_unit);
            stmt_handler = StmtNode.getHandler(stmt_unit.getType(),env);
            stmt_handler.parse();

            LexicalUnit third_unit = env.getInput().get();
//            <NL>
            if(third_unit.getType() == LexicalType.NL) {
                env.getInput().unget(third_unit);
            }
//            <ELSE>
            else if(third_unit.getType() == LexicalType.ELSE){
                else_stmt_handler = StmtNode.getHandler(stmt_unit.getType(), env);
                else_stmt_handler.parse();
            }
        }


//        <NL>
        else if (stmt_unit.getType() == LexicalType.NL){
            stmt_handler = StmtListNode.getHandler(stmt_unit.getType(),env);
            stmt_handler.parse();

            LexicalUnit else_unit = env.getInput().get();
            if (else_unit.getType() == LexicalType.ELSEIF){
                env.getInput().unget(else_unit);
                else_if_stmt_handler = IfBlockNode.getHandler(stmt_unit.getType(), env);
                else_if_stmt_handler.parse();
            }

            else if (else_unit.getType() == LexicalType.ELSE){
//                <NL>
                if(env.getInput().get().getType() != LexicalType.NL)
                    throw new Exception("Syntax Error missing NL: " + env.getInput().getLine());

//                <ELSE_STMT>
                LexicalUnit else_stmt_unit = env.getInput().get();
                if(StmtListNode.isMatch(else_stmt_unit.getType())){
                    env.getInput().unget(else_stmt_unit);
                    else_stmt_handler = StmtListNode.getHandler(else_stmt_unit.getType(), env);
                    else_stmt_handler.parse();
                }
            }
            else{
               throw new Exception("Syntax Error missing ELSE or ELSE IF" + env.getInput().getLine());
            }

            LexicalUnit endif_unit = env.getInput().get();
            if(endif_unit.getType() != LexicalType.ENDIF){
                throw new Exception("Missing ENDIF:" + env.getInput().getLine());
            }
        }

        else
            throw new Exception("Syntax Error IFBlockNode: " + env.getInput().getLine());

        LexicalUnit nl_unit = env.getInput().get();
        if(nl_unit.getType() != LexicalType.NL)
            throw new Exception("IFBlockNode End need NL:" + env.getInput().getLine());

    }


    @Override
    public String toString(){
        String tmp = "IF[" + cond_handler.toString() + "]THEN[" + stmt_handler.toString();

        if(else_if_stmt_handler != null){
            tmp += "]ELSEIF[" + else_if_stmt_handler.toString();
        }
        if (else_stmt_handler != null){
            tmp += "]ELSE[" + else_stmt_handler.toString();
        }
        return tmp + "]";
    }
}