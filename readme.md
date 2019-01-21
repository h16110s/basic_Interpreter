# 実践的プログラミング　
実践的プログラミングの授業にて作成したBASICインタプリタ（仮）
最低限以下のソースコードが実行できるようになっている.
``` BASIC
a = 5
DO UNTIL a < 1
PRINT ("Hello World”)
a = a – 1
LOOP
END
```


## LexicalAnalyzer (newlang3)
字句解析プログラム 
- LexicalAnalyzer   
    字句解析インターフェース
- LexicalAnalyzerImpl   
    字句解析の実行クラス
- LexicalType 
    字句の型
- LexicalUnit 
    １字句分のクラス
- Main 
- Value 
    １字句の値のインターフェース
- ValueImpl 
    字句値の実行クラス
- ValueType 
    値の型

## SystaxAnalyzer (newlang4)
構文解析プログラム
- Environment
   LexicalAnalyzer管理
- Node
    ノードの抽象クラス
    - Block
        ブロックノード
    - CallSub
        関数呼び出しノード
    - Cond
        条件文ノード
    - Const
        定数ノード
    - End
        エンドノード
    - Expr
        式ノード
    - IfBlock
        if分岐ノード
    - LoopBlock
        繰り返しDo, Whileノード
    - Program
        プログラムノード
    - StmtList
        構文リストノード
    - Stmt
        構文ノード
    - Subst
        代入文ノード
    - Variable
        いろいろ
- Main

## Interpreter (newlang5)
インタプリタ
- Function
    関数管理の抽象クラス
- PrintFunction
    PRINT関数定義
- Main
