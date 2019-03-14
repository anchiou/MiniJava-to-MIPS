import java.util.*;
import cs132.vapor.ast.*;

public class VM2MVisitor {
    // Instruction visitor
    public void acceptInstructions(VInstr[] instructions) {
        for (VInstr instruction : instructions) {
            instruction.accept(new VInstr.Visitor<RuntimeException>() {

                // Get assignment information
                @Override
                public void visit(VAssign a) {

                    System.out.println("                         Assign");

                    // def.add(a.dest.toString());
                    // if (a.source instanceof VVarRef) {
                    //     use.add(a.source.toString());
                    // }
                }

                // Get branch information
                @Override
                public void visit(VBranch b) {

                    System.out.println("                         Branch");

                    // if (b.value instanceof VVarRef) {
                    //     use.add(b.value.toString());
                    // }
                }

                // Get BuiltIn information
                @Override
                public void visit(VBuiltIn b) {
                    // if (b.dest != null) {
                    //     def.add(b.dest.toString());
                    // }
                    // for (VOperand operand : b.args) {
                    //     if (operand instanceof VVarRef) {
                    //         use.add(operand.toString());
                    //     }
                    // }
                    String opName; // operation name

                    if (b.op != null) opName = b.op.name; // built-in operation

                    if (b.dest != null)
                        System.out.println("                           " + b.dest.toString());


                    for (VOperand operand : b.args) {
                        if (operand instanceof VVarRef) {
                            System.out.println("                           " + operand.toString());
                        }
                    }
                }

                // Get Call information
                @Override
                public void visit(VCall c) {
                    // def.add(c.dest.toString());
                    // if (c.addr instanceof VAddr.Var) {
                    //     use.add(c.addr.toString());
                    // }
                    // for (VOperand operand : c.args) {
                    //     if (operand instanceof VVarRef) {
                    //         use.add(operand.toString());
                    //     }
                    // }
                    System.out.println("                         " + c.addr.toString());
                }

                // Get goto information
               @Override
                public void visit(VGoto g) {
                    System.out.println("                         Goto");

                	// if (g.target instanceof VAddr.Var) {
                	// 	use.add(g.target.toString());
                	// }
                }

                // Get memread information
                @Override
                public void visit(VMemRead r) {
                    System.out.println("                         MemRead");

                	// def.add(r.dest.toString());
                    // VMemRef.Global globalReference = (VMemRef.Global) r.source;
                    // use.add(globalReference.base.toString());
                }

                // Get memwrite information
                @Override
                public void visit(VMemWrite w) {
                    System.out.println("                         MemWrite");

                    // VMemRef.Global globalReference = (VMemRef.Global) w.dest;
                    // use.add(globalReference.base.toString());
                    // if (w.source instanceof VVarRef) {
                    //     use.add(w.source.toString());
                    // }
                }

                // Get return information
                @Override
                public void visit(VReturn r) {
                    System.out.println("                         Return");

                    // if (r.value != null && r.value instanceof VVarRef) {
                    //     use.add(r.value.toString());
                    // }
                }

            });
        }
    }
}
