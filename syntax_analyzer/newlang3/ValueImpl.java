package newlang3;

public class ValueImpl implements Value {
    ValueType type;
    int ivalue;
    double dvalue;
    String svalue;
    boolean bvalue;


    //コンストラクタ
    public ValueImpl(String src, ValueType targetType){
        this.type = targetType;
        switch (targetType){
            //intである場合
            case INTEGER:
                this.ivalue = Integer.parseInt(src);
                break;
            //doubleである場合
            case DOUBLE:
                this.dvalue = Double.parseDouble(src);
                break;
            //String型である場合
            case STRING:
                this.svalue = src;
                break;
            //Bool型である場合
            case BOOL:
                this.bvalue = Boolean.parseBoolean(src);
                break;
        }
    }
    //String型である場合
    @Override
    public String getSValue() { return svalue; }

    //intである場合
    @Override
    public int getIValue() { return ivalue; }

    //doubleである場合
    @Override
    public double getDValue() { return dvalue;  }

    //Bool型である場合
    @Override
    public boolean getBValue() { return bvalue; }

    @Override
    public ValueType getType() { return type; }
}
