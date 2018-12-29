package newlang4;

import newlang3.LexicalType;
import sun.java2d.pipe.LoopBasedPipe;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


//<block> ::=
//<if_prefix> <stmt> <NL>
//	| <if_prefix> <stmt> <ELSE> <stmt> <NL>
//	| <if_prefix> <NL> <stmt_list> <else_block> <ENDIF> <NL>
//	| <WHILE> <cond> <NL> <stmt_list> <WEND> <NL>
//	| <DO> <WHILE> <cond> <NL> <stmt_list> <LOOP> <NL>
//	| <DO> <UNTIL> <cond> <NL> <stmt_list> <LOOP> <NL>
//	| <DO> <NL> <stmt_list> <LOOP> <WHILE> <cond> <NL>
//	| <DO> <NL> <stmt_list> <LOOP> <UNTIL> <cond> <NL>

public class BlockNode extends Node{
    static final Set<LexicalType> first = new HashSet<LexicalType>(Arrays.asList(
            LexicalType.IF,
            LexicalType.DO,
            LexicalType.WHILE));

    static boolean isMatch(LexicalType type){
        return first.contains(type);
    }

    static Node getHandler(LexicalType type, Environment env){
        if(IfBlockNode.isMatch(type)){
            return IfBlockNode.getHandler(type,env);
        }
        else if(LoopBlockNode.isMatch(type)){
            return LoopBlockNode.getHandler(type,env);
        }
        return null;
    }

    public void parse() {}
}
