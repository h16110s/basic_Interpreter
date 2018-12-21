package newlang4;

import newlang3.LexicalType;

import java.util.*;

public class StmtListNode extends Node{
    List<Node> child = new ArrayList<>();
    static final Set<LexicalType> first = new HashSet<>(Arrays.asList(
            LexicalType.IF,
            LexicalType.DO,
            LexicalType.WHILE,
            LexicalType.NAME,
            LexicalType.FOR,
            LexicalType.END,
            LexicalType.NL));

    public static boolean isMatch(LexicalType type){ return first.contains(type); }
    private StmtListNode(Environment e){
        super(e);
        type = NodeType.STMT_LIST;
    }

    public static Node getHandler(LexicalType type, Environment env) {
        if(!isMatch(type)) return null;
        return new StmtListNode(env);
    }
}
