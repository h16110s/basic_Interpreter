package newlang3;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Queue;

/**
 *
 * @author Hirohito Saito
 */
public class LexicalAnalyzerImpl implements LexicalAnalyzer {
    Queue<LexicalUnit> ungetQue = new ArrayDeque<LexicalUnit>();

    PushbackReader reader;

    int line;

    //予約語のmap
    static HashMap<String,LexicalType> reserve = new HashMap<>();
    //記号のmap
    static HashMap<String,LexicalType> symbols = new HashMap<>();
    //関数名のmap
    static HashMap<String,LexicalType> functions = new HashMap<>();

    public  LexicalAnalyzerImpl(InputStream in){
        Reader ir = new InputStreamReader(in);
        reader = new PushbackReader(ir);
        line = 1;
        //予約語，記号，関数リスト の作成
        makeList();
    }

    @Override
    public LexicalUnit get() throws Exception {

        if(ungetQue.size() > 0){
            return ungetQue.poll();
        }

        while(true){
            int ci = reader.read();
            char c = (char)ci;

            //EOF
            if (ci < 0) {
                return new LexicalUnit(LexicalType.EOF);
            }

            //SPACE
            if((c == ' ') || (c == '\t')) {

            }

            //New Line
            else if(c == '\n'){
                line++;
                return new LexicalUnit(LexicalType.NL);
            }

            //String
            else if((c >= 'a' && c <= 'z')||(c >= 'A' && c <= 'Z')){
                reader.unread(ci);
                return getString();
            }
            //Number
            else if (c >= '0' && c <= '9'){
                reader.unread(ci);
                return getNumber();
            }
            //Literal
            else if (c == '"'){
                return getLiteral();
            }
            //Symbol
            else if(symbols.containsKey(c + "")) {
                reader.unread(ci);
                return getSymbol();
            }
        }
    }

    //文字を抜き出す関数
    private LexicalUnit getString() throws Exception {
        String target = "";
        while (true) {
            int ci = reader.read();
            char c = (char) ci;
            //先頭の文字がa~Zの時
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                target += c;
                continue;
            }
            reader.unread(c);
            break;
        }
        if(reserve.containsKey(target)){
            return (new LexicalUnit(reserve.get(target.toUpperCase()), new ValueImpl(target, ValueType.STRING)));
        }
        else if (functions.containsKey(target)) {
            return (new LexicalUnit(functions.get(target.toUpperCase()), new ValueImpl(target, ValueType.STRING)));
        }
        return (new LexicalUnit(LexicalType.NAME, new ValueImpl(target, ValueType.STRING)));
    }

//    数値を抜き出す関数
    private LexicalUnit getNumber() throws Exception {
        String target = "";
        LexicalType lType = LexicalType.INTVAL;
        ValueType vType = ValueType.INTEGER;
        while (true) {
            int ci = reader.read();
            char c = (char) ci;

            if (c >= '0' && c <= '9') {
                target += c;
                continue;
            }
            else if( c == '.'){
                lType = LexicalType.DOUBLEVAL;
                vType = ValueType.DOUBLE;
                target += c;
                continue;
            }
            else{
                reader.unread(c);
                break;
            }
        }
        return (new LexicalUnit(lType, new ValueImpl(target, vType)));
    }

//  リテラルを取得する
    private LexicalUnit getLiteral() throws Exception {
        String target = "";
        LexicalType lType = LexicalType.LITERAL;
        ValueType vType = ValueType.STRING;
        while (true) {
            int ci = reader.read();
            char c = (char) ci;
            if (c != '"') {
                target += c;
                continue;
            }
            break;
        }
        return (new LexicalUnit(lType, new ValueImpl(target, vType)));
    }

//    記号の取得
    private LexicalUnit getSymbol()throws Exception{
        String target = "";
        target += (char)reader.read();
        while (true) {
            int ci = reader.read();
            char c = (char) ci;
            //連続記号なら取得
            if (symbols.containsKey(target + c)) {
                target += c;
                continue;
            }
            else {
                reader.unread(ci);
                break;
            }
        }
        return (new LexicalUnit(symbols.get(target),new ValueImpl(target, ValueType.VOID)));
    }

    private static void makeList(){
        reserve.put("LITERAL",LexicalType.LITERAL);
        reserve.put("INTVAL",LexicalType.INTVAL);
        reserve.put("DOUBLEVAL",LexicalType.DOUBLEVAL);
        reserve.put("NAME",LexicalType.NAME);
        reserve.put("IF",LexicalType.IF);
        reserve.put("THEN",LexicalType.THEN);
        reserve.put("ELSE",LexicalType.ELSE);
        reserve.put("ELSEIF", LexicalType.ELSEIF);
        reserve.put("ENDIF",LexicalType.ENDIF);
        reserve.put("FOR",LexicalType.FOR);
        reserve.put("FORALL",LexicalType.FORALL);
        reserve.put("NEXT",LexicalType.NEXT);
        reserve.put("FUNC",LexicalType.FUNC);
        reserve.put("DIM",LexicalType.DIM);
        reserve.put("AS",LexicalType.AS);
        reserve.put("END",LexicalType.END);
        reserve.put("NL",LexicalType.NL);
        reserve.put("WHILE",LexicalType.WHILE);
        reserve.put("DO",LexicalType.DO);
        reserve.put("UNTIL",LexicalType.UNTIL);
        reserve.put("LOOP",LexicalType.LOOP);
        reserve.put("TO",LexicalType.TO);
        reserve.put("WEND",LexicalType.WEND);
        reserve.put("EOF",LexicalType.EOF);
        symbols.put("=",LexicalType.EQ);
        symbols.put("<",LexicalType.LT);
        symbols.put(">",LexicalType.GT);
        symbols.put("<=",LexicalType.LE);
        symbols.put("=<",LexicalType.LE);
        symbols.put(">=",LexicalType.GE);
        symbols.put("=>",LexicalType.GE);
        symbols.put("<>",LexicalType.NE);
        symbols.put(".",LexicalType.DOT);
        symbols.put("+",LexicalType.ADD);
        symbols.put("-",LexicalType.SUB);
        symbols.put("*",LexicalType.MUL);
        symbols.put("/",LexicalType.DIV);
        symbols.put(")",LexicalType.RP);
        symbols.put("(",LexicalType.LP);
        symbols.put(",",LexicalType.COMMA);
    }


    @Override
    public boolean expect(LexicalType type) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getLine(){
        return this.line;
    }

    @Override
    public void unget(LexicalUnit token) throws Exception {
        ungetQue.add(token);
    }
    
}
