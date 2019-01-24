package typecheck;

import syntaxtree.IntegerLiteral;
import visitor.*;

public class VisitorPhaseOne extends DepthFirstVisitor {
    public void visit(IntegerLiteral n) {
        System.out.println(n.toString());
        super.visit(n);
    }
}
