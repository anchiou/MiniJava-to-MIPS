import java.util.*;
import cs132.vapor.ast.*;

public class VM2MVisitor {

    String indent = "  ";

    // Instruction visitor
    public void acceptInstructions(VInstr[] instructions) {
        for (VInstr instruction : instructions) {
            instruction.accept(new VInstr.Visitor<RuntimeException>() {

                // Get assignment information
                @Override
                public void visit(VAssign a) {
                    if (a.source instanceof VVarRef) {
                        System.out.println(indent + "move " + a.dest.toString() + " " + a.source.toString());
                    } else {
                        System.out.println(indent + "li " + a.dest.toString() + " " + a.source.toString());
                    }

                }

                // Get branch information
                @Override
                public void visit(VBranch b) {

                    String target = b.target.toString();

                    // if the branch is positive
                    if (b.positive) {
                        // use bnez
                        target = target.substring(1, target.length());
                        System.out.println(indent + "bnez " + b.value + " " + target);
                    } else {
                        // use beqz
                        target = target.substring(1, target.length());
                        System.out.println(indent + "beqz " + b.value + " " + target);
                    }
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

                    for (VOperand argument : b.args) {
                        if (argument instanceof VVarRef) {
                            System.out.println("                           **" + argument.toString());
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
                    String target = g.target.toString();
                    target = target.substring(1, target.length());
                    System.out.println(indent + "j " + target);
                }

                // Get memread information
                @Override
                public void visit(VMemRead r) {

                    if (r.source instanceof VMemRef.Global) {
                        VMemRef.Global ref = (VMemRef.Global) r.source;
                        System.out.println(indent + "lw " + r.dest.toString() + " " + ref.byteOffset
                                            + "(" + ref.base.toString() + ")");
                    } else {
                        VMemRef.Stack ref = (VMemRef.Stack) r.source;
                        System.out.println(indent + "lw " + r.dest.toString() + " 0($sp)");
                        System.out.println(indent + "lw $ra -4($fp)");
                        System.out.println(indent + "lw $fp -8($fp)");
                        System.out.println(indent + "addu $sp $sp 12");
                    }

                }

                // Get memwrite information
                @Override
                public void visit(VMemWrite w) {

                    if (w.dest instanceof VMemRef.Global) {
                        // TODO figure out source value here
                        System.out.println("                       TODO: FIX SOURCE VALUE (TEMP MAYBE?)");
                        VMemRef.Global destination_0 = (VMemRef.Global) w.dest;
                        System.out.println(indent + "sw " + w.source.toString() + " " + destination_0.byteOffset
                                            + "(" + destination_0.base.toString() + ")");
                    } else {
                        // TODO figure out destination here
                        VMemRef.Stack destination_1 = (VMemRef.Stack) w.dest;
                        System.out.println(indent + "sw " + w.source.toString() + " 0($sp)");
                    }

                }

                // Get return information
                @Override
                public void visit(VReturn r) {
                    System.out.println(indent + "jr $ra");
                }

            });
        }
    }
}
