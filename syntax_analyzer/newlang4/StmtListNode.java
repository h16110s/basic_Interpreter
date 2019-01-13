package newlang4;

import newlang3.LexicalType;
import newlang3.LexicalUnit;
import newlang3.Value;

import java.util.*;

public class StmtListNode extends Node {
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

    public void parse() throws Exception{
        LexicalUnit lu;
        while(true) {
            //空行を読み飛ばす
            do {
                lu = env.getInput().get();
            }while(lu.getType() == LexicalType.NL);
            //ちがったら抜けて一個戻す
            env.getInput().unget(lu);

            if(StmtNode.isMatch(lu.getType())){
                Node handler = StmtNode.getHandler(lu.getType(),env);
                child.add(handler);
                handler.parse();
            }

            else if(BlockNode.isMatch(lu.getType())){
                Node handler = BlockNode.getHandler(lu.getType(),env);
                child.add(handler);
                handler.parse();
            }
            else{
                break;
            }
        }
    }

    @Override
    public String toString(){
        String tmp = "";

        for(int i = 0; i< child.size() ; i++){
            tmp += child.get(i).toString() + ";";
        }

        return tmp;
    }



    public Value getValue() throws Exception {
        for(Node n : child){
            n.getValue();
        }
        return null;
    }
}
