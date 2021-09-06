package Evaluator.Treenode;

import com.fathzer.soft.javaluator.*;

import java.util.Iterator;

public class TreeEvaluator extends AbstractEvaluator<TreeNode> {

    static final Operator NOT = new Operator("-", 1, Operator.Associativity.RIGHT, 5);
    static final Operator AND = new Operator("&", 2, Operator.Associativity.LEFT, 4);
    static final Operator OR = new Operator("|", 2, Operator.Associativity.LEFT, 3);
    static final Operator IMPLIES = new Operator("->", 2, Operator.Associativity.LEFT, 2);
    static final Operator EQUIV = new Operator("<->", 2, Operator.Associativity.LEFT, 1);



    private static final Parameters PARAMETERS;

    static {
        PARAMETERS = new Parameters();
        PARAMETERS.add(NOT);
        PARAMETERS.add(AND);
        PARAMETERS.add(OR);
        PARAMETERS.add(IMPLIES);
        PARAMETERS.add(EQUIV);
        PARAMETERS.addExpressionBracket(BracketPair.PARENTHESES);
    }

    public TreeEvaluator() {
        super(PARAMETERS);
    }

    @Override
    protected TreeNode toValue(String s, Object o) {
        if (s.equals("0")){
            return new Falsum();
        }
        if (s.equals("1")){
            return new Verum();
        }
        else{
            return new Literal(s);
        }
    }

    @Override
    public TreeNode evaluate(Operator operator, Iterator<TreeNode> operands, Object expr) {
        if (operator == NOT){
            return new Not(operands.next());
        }
        if (operator == AND){
            return new And(operands.next(),operands.next());
        }
        if (operator == OR){
            return new Or(operands.next(),operands.next());
        }
        if (operator == IMPLIES){
            return new Implication(operands.next(),operands.next());
        }
        if (operator == EQUIV){
            return new Equivalence(operands.next(),operands.next());
        }
        throw new IllegalArgumentException();
    }
}
