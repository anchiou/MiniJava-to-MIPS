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
<<<<<<< HEAD
=======
                    System.out.println("                         Assign");

>>>>>>> 6b32182d5b2ed9f5a18ccb423c1de723867060f6
                    // def.add(a.dest.toString());
                    // if (a.source instanceof VVarRef) {
                    //     use.add(a.source.toString());
                    // }
                }

                // Get branch information
                @Override
                public void visit(VBranch b) {
<<<<<<< HEAD
=======
                    System.out.println("                         Branch");

>>>>>>> 6b32182d5b2ed9f5a18ccb423c1de723867060f6
                    // if (b.value instanceof VVarRef) {
                    //     use.add(b.value.toString());
                    // }
                }

                // Get BuiltIn information
                @Override
                public void visit(VBuiltIn b) {
<<<<<<< HEAD
                    // if (b.dest != null) {
                    //     def.add(b.dest.toString());
                    // }
                    // for (VOperand operand : b.args) {
                    //     if (operand instanceof VVarRef) {
                    //         use.add(operand.toString());
                    //     }
                    // }
=======
                    String opName; // operation name

                    if (b.op != null) opName = b.op.name; // built-in operation

                    if (b.dest != null)
                        System.out.println("                           " + b.dest.toString());


                    for (VOperand operand : b.args) {
                        if (operand instanceof VVarRef) {
                            System.out.println("                           " + operand.toString());
                        }
                    }
>>>>>>> 6b32182d5b2ed9f5a18ccb423c1de723867060f6
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
<<<<<<< HEAD
=======
                    System.out.println("                         " + c.addr.toString());
>>>>>>> 6b32182d5b2ed9f5a18ccb423c1de723867060f6
                }

                // Get goto information
               @Override
                public void visit(VGoto g) {
<<<<<<< HEAD
=======
                    System.out.println("                         Goto");

>>>>>>> 6b32182d5b2ed9f5a18ccb423c1de723867060f6
                	// if (g.target instanceof VAddr.Var) {
                	// 	use.add(g.target.toString());
                	// }
                }

                // Get memread information
                @Override
                public void visit(VMemRead r) {
<<<<<<< HEAD
=======
                    System.out.println("                         MemRead");

>>>>>>> 6b32182d5b2ed9f5a18ccb423c1de723867060f6
                	// def.add(r.dest.toString());
                    // VMemRef.Global globalReference = (VMemRef.Global) r.source;
                    // use.add(globalReference.base.toString());
                }

                // Get memwrite information
                @Override
                public void visit(VMemWrite w) {
<<<<<<< HEAD
=======
                    System.out.println("                         MemWrite");

>>>>>>> 6b32182d5b2ed9f5a18ccb423c1de723867060f6
                    // VMemRef.Global globalReference = (VMemRef.Global) w.dest;
                    // use.add(globalReference.base.toString());
                    // if (w.source instanceof VVarRef) {
                    //     use.add(w.source.toString());
                    // }
                }

                // Get return information
                @Override
                public void visit(VReturn r) {
<<<<<<< HEAD
=======
                    System.out.println("                         Return");

>>>>>>> 6b32182d5b2ed9f5a18ccb423c1de723867060f6
                    // if (r.value != null && r.value instanceof VVarRef) {
                    //     use.add(r.value.toString());
                    // }
                }

            });
        }
    }
}