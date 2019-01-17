package newlang4;

import newlang3.LexicalType;
import newlang3.LexicalUnit;
import newlang3.Value;
import newlang3.ValueImpl;

import java.util.*;

import static newlang3.ValueType.*;

//
//<expr>	::=
//  <expr> <ADD> <expr>
//	| <expr> <SUB> <expr>
//	| <expr> <MUL> <expr>
//	| <expr> <DIV> <expr>
//	| <SUB> <expr>
//	| <LP> <expr> <RP>
//	| <NAME>
//	| <INTVAL>
//	| <DOUBLEVAL>
//	| <LITERAL>
//	| <call_func>

public class ExprNode extends Node {
    List<Node> operands = new ArrayList<>();
    List<Node> operatorsList = new ArrayList<>();
    Queue<Node> mainQueue = new ArrayDeque<>();
    Deque<Node> opStack = new ArrayDeque<>();

    static final Map<NodeType,Integer> PRIORITY = new HashMap<>();
    static{
        PRIORITY.put(NodeType.DIV_OPERATOR,1);
        PRIORITY.put(NodeType.MUL_OPERATOR,2);
        PRIORITY.put(NodeType.SUB_OPERATOR,3);
        PRIORITY.put(NodeType.ADD_OPERATOR,3);
    }


    static final Set<LexicalType> first = new HashSet<LexicalType>(Arrays.asList(
            LexicalType.NAME,
            LexicalType.LP,
            LexicalType.INTVAL,
            LexicalType.DOUBLEVAL,
            LexicalType.LITERAL));

    private ExprNode(Environment env) {
        super(env);
        type = NodeType.EXPR;
    }

    public static boolean isMatch(LexicalType type) {
        return first.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) {
        return new ExprNode(env);
    }

    public void parse() throws Exception {
        while (true) {
//      <Operand>
            LexicalUnit lu = env.getInput().get();
            switch (lu.getType()) {
                case NAME:
                    env.getInput().unget(lu);
                    Node vn = VariableNode.getHandler(lu.getType(),env);
                    vn.parse();
                    operands.add(vn);
                    mainQueue.add(vn);
                    break;
                case LP:
                    LexicalUnit lp_unit = env.getInput().get();
                    env.getInput().unget(lp_unit);
                    Node n = ExprNode.getHandler(lp_unit.getType(), env);
                    n.parse();
                    LexicalUnit rp_unit = env.getInput().get();
                    if (rp_unit.getType() != LexicalType.RP)
                        throw new Exception("Expr Node Missing RP: " + env.getInput().getLine());
                    operands.add(n);
                    mainQueue.add(n);
                    break;
                case INTVAL:
                case DOUBLEVAL:
                case LITERAL:
                    Node cn = ConstNode.getHandler(lu, env);
                    operands.add(cn);
                    mainQueue.add(cn);
                    cn.parse();
                    break;
                default:
                    throw new Exception("Expr Node Parse Error: ");
            }

            lu = env.getInput().get();
            if (OperatorNode.isMatch(lu.getType())) {
                Node op = OperatorNode.getHandler(lu,env);
                operatorsList.add(op);

                if(opStack.peekFirst() == null){
                    opStack.push(op);
                }
                else if(PRIORITY.get(op.getType()) < PRIORITY.get(opStack.peekFirst().getType())){
                    opStack.push(op);
                }
                else {
                    while(true){
                        if(opStack.peekFirst() == null ||
                                PRIORITY.get(opStack.peekFirst().getType()) > PRIORITY.get(op.getType()))
                            break;
                        mainQueue.add(opStack.poll());
                    }
                    opStack.push(op);
                }
            } else {
                env.getInput().unget(lu);
                while(true){
                    if(opStack.peekFirst() == null)
                        break;
                    mainQueue.add(opStack.poll());
                }
                break;
            }
        }
    }

    @Override
    public String toString() {
        String tmp = "[";

        for (int i = 0; i < operatorsList.size(); i++) {
            tmp += operatorsList.get(i).toString();
        }
        tmp += ":";

        for (int i = 0; i < operands.size() - 1; i++) {
            tmp += operands.get(i);
            if (operands.get(i + 1) != null)
                tmp += ",";
            else {
                break;
            }
        }
        tmp += operands.get(operands.size() - 1).toString();
        return tmp + "]";
    }



    @Override
    public Value getValue() throws Exception{
        if(mainQueue.peek().getType() == NodeType.STRING_CONSTANT){
            String s = "";
            while (mainQueue.peek() != null){
                switch (mainQueue.peek().getType()){
                    case SUB_OPERATOR:
                    case MUL_OPERATOR:
                    case DIV_OPERATOR:
                        throw new Exception("String Can't This Operator:" + mainQueue.peek().toString());
                    case ADD_OPERATOR:
                        mainQueue.poll();
                        continue;
                    case STRING_CONSTANT:
                        s += mainQueue.poll().getValue().getSValue();
                        break;
                    default:
                        throw new Exception("Can't Expr (STRING_CONSTANT)");
                }
            }
            return new ValueImpl(s,STRING);
        }
        else if (mainQueue.peek().getType() == NodeType.INT_CONSTANT){
            Deque<Value> subQueue = new ArrayDeque<>();
            while (mainQueue.peek() != null){
                switch (mainQueue.peek().getType()){
                    case SUB_OPERATOR:
                        mainQueue.poll();
                        int s1 = subQueue.poll().getIValue();
                        int s2 = subQueue.poll().getIValue();
                        subQueue.push(new ValueImpl(String.valueOf(s1 - s2),INTEGER));
                        break;
                    case MUL_OPERATOR:
                        mainQueue.poll();
                        int m1 = subQueue.poll().getIValue();
                        int m2 = subQueue.poll().getIValue();
                        subQueue.push(new ValueImpl(String.valueOf(m1 * m2),INTEGER));
                        break;
                    case DIV_OPERATOR:
                        mainQueue.poll();
                        int d1 = subQueue.poll().getIValue();
                        int d2 = subQueue.poll().getIValue();
                        subQueue.push(new ValueImpl(String.valueOf(d1 / d2),INTEGER));
                        break;
                    case ADD_OPERATOR:
                        mainQueue.poll();
                        int a1 = subQueue.poll().getIValue();
                        int a2 = subQueue.poll().getIValue();
                        subQueue.push(new ValueImpl(String.valueOf(a1 + a2),INTEGER));
                        break;
                    case INT_CONSTANT:
                        subQueue.push(mainQueue.poll().getValue());
                        break;
                    default:
                        throw new Exception("Can't Expr (INT_CONSTANT)");
                }
            }
            return new ValueImpl(String.valueOf(subQueue.poll().getIValue()),INTEGER);
        }
        else if (mainQueue.peek().getType() == NodeType.DOUBLE_CONSTANT){
            Deque<Value> subQueue = new ArrayDeque<>();
            while (mainQueue.peek() != null){
                switch (mainQueue.peek().getType()){
                    case SUB_OPERATOR:
                        mainQueue.poll();
                        double s1 = subQueue.poll().getDValue();
                        double s2 = subQueue.poll().getDValue();
                        subQueue.push(new ValueImpl(String.valueOf(s1 - s2),DOUBLE));
                        break;
                    case MUL_OPERATOR:
                        mainQueue.poll();
                        double m1 = subQueue.poll().getDValue();
                        double m2 = subQueue.poll().getDValue();
                        subQueue.push(new ValueImpl(String.valueOf(m1 * m2),DOUBLE));
                        break;
                    case DIV_OPERATOR:
                        mainQueue.poll();
                        double d1 = subQueue.poll().getDValue();
                        double d2 = subQueue.poll().getDValue();
                        subQueue.push(new ValueImpl(String.valueOf(d1 / d2),DOUBLE));
                        break;
                    case ADD_OPERATOR:
                        mainQueue.poll();
                        double a1 = subQueue.poll().getDValue();
                        double a2 = subQueue.poll().getDValue();
                        subQueue.push(new ValueImpl(String.valueOf(a1 + a2),DOUBLE));
                        break;
                    case DOUBLE_CONSTANT:
                        subQueue.push(mainQueue.poll().getValue());
                        break;
                    default:
                        throw new Exception("Can't Expr (INT_CONSTANT)");
                }
            }
            return new ValueImpl(String.valueOf(subQueue.poll().getDValue()),DOUBLE);
        }

        else if (mainQueue.peek().getType() == NodeType.VARIABLE){
            Deque<Value> subQueue = new ArrayDeque<>();
            while (mainQueue.peek() != null){
                switch (mainQueue.peek().getType()){
                    case SUB_OPERATOR:
                        mainQueue.poll();
                        int  s1 = subQueue.poll().getIValue();
                        int  s2 = subQueue.poll().getIValue();
                        subQueue.push(new ValueImpl(String.valueOf(s1 - s2),INTEGER));
                        break;
                    case MUL_OPERATOR:
                        mainQueue.poll();
                        int  m1 = subQueue.poll().getIValue();
                        int  m2 = subQueue.poll().getIValue();
                        subQueue.push(new ValueImpl(String.valueOf(m1 * m2),INTEGER));
                        break;
                    case DIV_OPERATOR:
                        mainQueue.poll();
                        int d1 = subQueue.poll().getIValue();
                        int d2 = subQueue.poll().getIValue();
                        subQueue.push(new ValueImpl(String.valueOf(d1 / d2),INTEGER));
                        break;
                    case ADD_OPERATOR:
                        mainQueue.poll();
                        int a1 = subQueue.poll().getIValue();
                        int a2 = subQueue.poll().getIValue();
                        subQueue.push(new ValueImpl(String.valueOf(a1 + a2),INTEGER));
                        break;
                    case VARIABLE:
                        Node vn = env.getVariable(mainQueue.poll().toString());
                        subQueue.push(vn.getValue());
                        break;
                    case DOUBLE_CONSTANT:
                        subQueue.push(mainQueue.poll().getValue());
                        break;
                    case INT_CONSTANT:
                        subQueue.push(mainQueue.poll().getValue());
                        break;
                    default:
                        throw new Exception("Can't Expr (VARIABLE)");
                }
            }

            switch (subQueue.peekFirst().getType()){
                case INTEGER:
                    return new ValueImpl(String.valueOf(subQueue.poll().getIValue()),INTEGER);
                case DOUBLE:
                    return new ValueImpl(String.valueOf(subQueue.poll().getDValue()),DOUBLE);
                case STRING:
                    return new ValueImpl(String.valueOf(subQueue.poll().getSValue()),STRING);
                case BOOL:
                    return new ValueImpl(String.valueOf(subQueue.poll().getBValue()),BOOL);
            }

        }
        return null;
    }
//    @Override
//    public Value getValue() throws Exception {
//        NodeType mode = null;
//        String s = "";
//        int i = 0;
//        double d =0;
//
//        if (operands.get(0).getType() == NodeType.STRING_CONSTANT) {
//            for (Node n : operands) {
//                s += n.getValue().getSValue();
//            }
//            return new ValueImpl(s, STRING);
//        } else if (operands.get(0).getType() == NodeType.INT_CONSTANT) {
//            i = operands.get(0).getValue().getIValue();
//            for (int j = 0; j < operatorsList.size(); j++) {
//                switch (operatorsList.get(j).getType()){
//                    case SUB_OPERATOR:
//                        i -= operands.get(j+1).getValue().getIValue();
//                        break;
//                    case DIV_OPERATOR:
//                        i /= operands.get(j+1).getValue().getIValue();
//                        break;
//                    case MUL_OPERATOR:
//                        i *= operands.get(j+1).getValue().getIValue();
//                        break;
//                    case ADD_OPERATOR:
//                        i += operands.get(j+1).getValue().getIValue();
//                        break;
//                }
//            }
//            return new ValueImpl(String.valueOf(i), INTEGER);
//        } else if (operands.get(0).getType() == NodeType.DOUBLE_CONSTANT) {
//            d = operands.get(0).getValue().getDValue();
//            for (int j = 0; j < operatorsList.size(); j++) {
//                switch (operatorsList.get(j).getType()){
//                    case SUB_OPERATOR:
//                        d -= operands.get(j+1).getValue().getDValue();
//                        break;
//                    case DIV_OPERATOR:
//                        d /= operands.get(j+1).getValue().getDValue();
//                        break;
//                    case MUL_OPERATOR:
//                        d *= operands.get(j+1).getValue().getDValue();
//                        break;
//                    case ADD_OPERATOR:
//                        d += operands.get(j+1).getValue().getDValue();
//                        break;
//                }
//            }
//            return new ValueImpl(String.valueOf(d), DOUBLE);
//        } else if (operands.get(0).getType() == NodeType.VARIABLE){
//        }
//        return null;
//    }
}

