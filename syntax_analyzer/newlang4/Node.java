package newlang4;

import newlang3.LexicalType;
import newlang3.Value;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Node {
    NodeType type;
    Environment env;
    static final Set<LexicalType> operators = new HashSet<>(Arrays.asList(
            LexicalType.DIV,
            LexicalType.MUL,
            LexicalType.SUB,
            LexicalType.ADD));

    static final Set<LexicalType> condOperators = new HashSet<>(Arrays.asList(
            LexicalType.EQ,
            LexicalType.LT,
            LexicalType.GT,
            LexicalType.LE,
            LexicalType.LE,
            LexicalType.GE,
            LexicalType.GE,
            LexicalType.NE,
            LexicalType.DOT,
            LexicalType.ADD,
            LexicalType.SUB,
            LexicalType.MUL,
            LexicalType.DIV,
            LexicalType.LP,
            LexicalType.RP
    ));


    /** Creates a new instance of Node */
    public Node() {
    }
    public Node(NodeType my_type) {
        type = my_type;
    }
    public Node(Environment my_env) {
        env = my_env;
    }
    
    public NodeType getType() {
        return type;
    }
    
    public void parse() throws Exception { }
    
    public Value getValue() throws Exception {
        return null;
    }
 
    public String toString() {
    	if (type == NodeType.END) return "END";
    	else return "Node";        
    }

}
