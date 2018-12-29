package newlang4;

import newlang3.LexicalType;
import newlang3.LexicalUnit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class IfBlockNode extends Node {
    static Set<LexicalType>  first = new HashSet<LexicalType>(Arrays.asList(LexicalType.IF));
    Node cond_handler;

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
        env.getInput().get();
        LexicalUnit f = env.getInput().get();
        env.getInput().unget(f);

        if(!CondNode.isMatch(f.getType())) throw new Exception("IFBlockNode cond Parse Error:");
        cond_handler = CondNode.getHandler(f.getType(),env);
        cond_handler.parse();

        LexicalUnit next = env.getInput().get();
        if(next.getType() != LexicalType.THEN) throw new Exception("Syntax Error IFBlockNode" + next);
    }
}