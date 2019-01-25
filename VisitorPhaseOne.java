// package typecheck;

import syntaxtree.*;
import visitor.*;

public class VisitorPhaseOne extends DepthFirstVisitor {
    /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
    // @Override
    public void visit (Goal n) {
        System.out.println("Visitor: visitGoal");
        String _ret= "";
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        // return _ret;
    }

    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> ( VarDeclaration() )*
    * f15 -> ( Statement() )*
    * f16 -> "}"
    * f17 -> "}"
    */
    // @Override
    // public String visit(MainClass n, A argu) {
    //     String _ret = "";
    //     n.f0.accept(this, argu);
    //     n.f1.accept(this, argu);
    //     n.f2.accept(this, argu);
    //     n.f3.accept(this, argu);
    //     n.f4.accept(this, argu);
    //     n.f5.accept(this, argu);
    //     n.f6.accept(this, argu);
    //     n.f7.accept(this, argu);
    //     n.f8.accept(this, argu);
    //     n.f9.accept(this, argu);
    //     n.f10.accept(this, argu);
    //     n.f11.accept(this, argu);
    //     n.f12.accept(this, argu);
    //     n.f13.accept(this, argu);
    //     n.f14.accept(this, argu);
    //     n.f15.accept(this, argu);
    //     n.f16.accept(this, argu);
    //     n.f17.accept(this, argu);
    //     return _ret;
    // }

}