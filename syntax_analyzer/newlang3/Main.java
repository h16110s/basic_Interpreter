//package newlang3;
//
//import java.io.*;
//
///**
// *
// * @author Hirohito Saito
// */
//public class Main {
//    public static void main(String[] args) {
//        //1. プログラムコードのOPEN
//        String fname = "/Users/hiro16110/gLocal/basic_interpreter/syntax_analyzer/newlang3/test1.bas";
//        if (args.length > 0){
//            fname = args[0];
//        }
//
//        FileInputStream fr = null;
//        try{
//            fr = new FileInputStream(new File(fname));
//        }catch (FileNotFoundException e) {
//            System.out.println(fname + " : not found");
//            System.exit(-1);
//        }
//
//
//        //2.LexicalAnalyzerとLexicalUnitのインスタンス生成
//        LexicalAnalyzer a = new LexicalAnalyzerImpl(fr);
//        LexicalUnit unit;
//
//        //EOFまでループ
//        while( true ) {
//            //3. １字句の取得
//            try {
//                unit = a.get();
//            } catch (Exception e) {
//                //できなかった場合
//                System.out.println("Exception");
//                break;
//            }
//            //4. 字句の型と内容を表示
//            System.out.println(unit);
//
//            //EOFならば終了
//            if (unit.type == LexicalType.EOF) {
//                break;
//            }
//        }
//
//        //5. ファイルクローズ
//        if (fr != null){
//           try {
//               fr.close();
//           } catch (IOException e){}
//        }
//
//    }
//}