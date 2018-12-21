package newlang4;

import newlang3.LexicalType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EndNode extends Node {
    static final Set<LexicalType> first = new HashSet<LexicalType>(Arrays.asList( LexicalType.END));
    private EndNode(Environment env){
        super(env);
        type = NodeType.END;
    }
    public static boolean isMatch(LexicalType type) {
        return first.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) {
        if(type != LexicalType.END) return null;
        return new EndNode(env);
    }

    @Override
    public String toString(){
        return "END";
    }
}
