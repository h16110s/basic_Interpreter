package newlang5;

import newlang3.Value;
import newlang4.ExprListNode;

public class PrintFunction extends Function {
    //関数呼び出し
    //ExprListで引数渡しをしなくても良い
    public Value invoke(ExprListNode arg) throws Exception {
        for(int i = 0; i < arg.size() ; i++){
            Value val = arg.getElement(i);
            System.out.println(val.getSValue());
        }
        return null;
    }
}
