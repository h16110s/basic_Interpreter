package newlang4;

import newlang3.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConstNode extends Node {
    private LexicalUnit const_value;
    private final static Set<LexicalType> first = new HashSet<LexicalType>(Arrays.asList(
            LexicalType.INTVAL,
            LexicalType.DOUBLEVAL,
            LexicalType.LITERAL));

    private ConstNode(LexicalUnit lu, Environment env){
        super(env);
        switch (lu.getValue().getType()){
            case INTEGER:
                type = NodeType.INT_CONSTANT;
                break;
            case DOUBLE:
                type = NodeType.DOUBLE_CONSTANT;
                break;
            case STRING:
                type = NodeType.STRING_CONSTANT;
                break;
            case BOOL:
                type = NodeType.BOOL_CONSTANT;
                break;
        }
        const_value = lu;
//        System.out.println("ConstNode " + lu);
    }

    public static Node getHandler(LexicalUnit lu, Environment env){
        return new ConstNode(lu , env);
    }

    public static boolean isMatch(LexicalType type){
        return first.contains(type);
    }

    @Override
    public String toString(){
        switch (const_value.getValue().getType()){
            case INTEGER:
                return  String.valueOf(const_value.getValue().getIValue());
            case DOUBLE:
                return String.valueOf(const_value.getValue().getDValue());
            case STRING:
                return const_value.getValue().getSValue();
                default:
                    return null;
        }
    }

 public Value getValue(){
        return const_value.getValue();
    }
}
