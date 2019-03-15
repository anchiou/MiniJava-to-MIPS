import java.util.*;
import cs132.vapor.ast.*;

public class VM2MVisitor {

    String indent = "  ";
    boolean arrayError = false; // flag for array out of bounds error

    // Instruction visitor
    public void acceptInstructions(VInstr[] instructions, int size) {

        for (VInstr instruction : instructions) {
            instruction.accept(new VInstr.Visitor<RuntimeException>() {

                // Get assignment information
                @Override
                public void visit(VAssign a) {
                    // System.out.println("                           Assign");

                    if (a.source instanceof VVarRef) {
                        System.out.println(indent + "move " +
                                            a.dest.toString() + " " + a.source.toString());
                    } else {
                        System.out.println(indent + "li " +
                                            a.dest.toString() + " " + a.source.toString());
                    }
                }

                // Get branch information
                @Override
                public void visit(VBranch b) {
                    // System.out.println("                           Branch");

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
                    String opName = ""; // operation name
                    String dest = "";   // destination register
                    VOperand arg1 = null;
                    VOperand arg2 = null;

                    if (b.op != null) opName = b.op.name; // built-in operation
                    if (b.dest != null) dest = b.dest.toString(); // destination

                    if (b.op.numParams == 2) {
                        arg1 = b.args[0];
                        arg2 = b.args[1];
                    }

                    if (opName == "HeapAllocZ") { // HeapAllocZ
                        System.out.println(indent + "li $a0 " + b.args[0].toString());
                        System.out.println(indent + "jal _heapAlloc");

                    } else if (opName == "PrintIntS") { // PrintIntS
                        System.out.println(indent + "move $a0 " + b.args[0].toString());
                        System.out.println(indent + "jal _print");

                    } else if (opName == "Error") { // Error
                        if (b.args[0].toString().contains("null pointer")) {
                            System.out.println(indent + "la $a0 _str0");
                        } else if (b.args[0].toString().contains("array")) {
                            System.out.println(indent + "la $a0 _str1");
                            arrayError = true;
                        }
                        System.out.println(indent + "j _error");

                    } else if (opName == "Lt") {    // Lt
                        if (arg1 instanceof VVarRef) {
                            if (arg2 instanceof VVarRef) { // Lt(reg reg)
                                System.out.println(indent + "sltu " + dest + " " +
                                                    arg1.toString() + " " + arg2.toString());
                            } else { // Lt(reg imm)
                                System.out.println(indent + "li $t9 " + arg2.toString());
                                System.out.println(indent + "sltu " + dest + " "
                                                    + arg1.toString() + " $t9");
                            }
                        } else {
                            if (arg2 instanceof VVarRef) { // Lt(imm reg)
                                System.out.println(indent + "li $t9 " + arg1.toString());
                                System.out.println(indent + "sltu " + dest + " $t9 " + arg2.toString());
                            }
                        }
                    } else if (opName == "LtS") {   // LtS
                        if (arg1 instanceof VVarRef) {
                            if (arg2 instanceof VVarRef) { // LtS(reg reg)
                                System.out.println(indent + "slt " + dest + " " +
                                                    arg1.toString() + " " + arg2.toString());
                            } else { // LtS(reg imm)
                                System.out.println(indent + "slti " + dest + " "
                                                    + arg1.toString() + " " + arg2.toString());
                            }
                        } else {
                            if (arg2 instanceof VVarRef) { // LtS(imm reg)
                                System.out.println(indent + "li $t9 " + arg1.toString());
                                System.out.println(indent + "slt " + dest + " $t9 " + arg2.toString());
                            }
                        }
                    } else if (opName == "MulS") {  // MulS
                        if (arg1 instanceof VLitStr) {
                            if (arg2 instanceof VLitStr) { // MulS(imm imm)
                                int imm1 = Integer.parseInt(arg1.toString()); // immediate value 1
                                int imm2 = Integer.parseInt(arg2.toString()); // immediate value 2
                                int product = imm1 * imm2;
                                System.out.println(indent + "li " + dest + " " + Integer.toString(product));
                            } else { // MulS(imm reg)
                                System.out.println(indent + "mul " + dest + " "
                                                    + arg2.toString() + " " + arg1.toString());
                            }
                        } else { // MulS(reg imm) or MulS(reg reg)
                            System.out.println(indent + "mul " + dest + " "
                                                + arg1.toString() + " " + arg2.toString());
                        }
                    } else if (opName == "Add") {   // Add
                        if (arg1 instanceof VVarRef) { // Add(reg imm) or Add(reg reg)
                            System.out.println(indent + "addu " + dest + " "
                                                + arg1.toString() + " " + arg2.toString());
                        } else {
                            if (arg2 instanceof VVarRef) { // Add(imm reg)
                                System.out.println(indent + "li $t9 " + arg1.toString());
                                System.out.println(indent + "addu " + dest + " $t9 " + arg2.toString());
                            } else { // Add (imm imm)
                                int imm1 = Integer.parseInt(arg1.toString()); // immediate value 1
                                int imm2 = Integer.parseInt(arg2.toString()); // immediate value 2
                                int sum = imm1 + imm2;
                                System.out.println(indent + "li " + dest + " " + Integer.toString(sum));
                            }
                        }
                    } else if (opName == "Sub") {   // Sub
                        if (arg1 instanceof VVarRef) { // Sub(reg imm) or Sub(reg reg)
                            System.out.println(indent + "subu " + dest + " "
                                                + arg1.toString() + " " + arg2.toString());
                        } else {
                            if (arg2 instanceof VVarRef) { // Sub(imm reg)
                                System.out.println(indent + "li $t9 " + arg1.toString());
                                System.out.println(indent + "subu " + dest + " $t9 " + arg2.toString());
                            } else { // Sub(imm imm)
                                int imm1 = Integer.parseInt(arg1.toString()); // immediate value 1
                                int imm2 = Integer.parseInt(arg2.toString()); // immediate value 2
                                int diff = imm1 - imm2; // difference
                                System.out.println(indent + "li " + dest + " " + Integer.toString(diff));
                            }
                        }
                    } else {
                        System.out.println("Other: " + opName);
                    }
                }

                // Get Call information
                @Override
                public void visit(VCall c) {
                    // System.out.println("                           Call");
                    // def.add(c.dest.toString());
                    // if (c.addr instanceof VAddr.Var) {
                    //     use.add(c.addr.toString());
                    // }
                    // for (VOperand operand : c.args) {
                    //     if (operand instanceof VVarRef) {
                    //         use.add(operand.toString());
                    //     }
                    // }
                    // System.out.println("                             " + c.addr.toString());
                }

                // Get goto information
                @Override
                public void visit(VGoto g) {
                    // System.out.println("                           Goto");

                    String target = g.target.toString();
                    target = target.substring(1, target.length());
                    System.out.println(indent + "j " + target);
                }

                // Get memread information
                @Override
                public void visit(VMemRead r) {
                    // System.out.println("                           MemRead");

                    if (r.source instanceof VMemRef.Global) {
                        VMemRef.Global ref = (VMemRef.Global) r.source;
                        System.out.println(indent + "lw " + r.dest.toString() + " " + ref.byteOffset
                                            + "(" + ref.base.toString() + ")");
                    } else {
                        VMemRef.Stack ref = (VMemRef.Stack) r.source;
                        System.out.println(indent + "lw " + r.dest.toString() + " 0($sp)");
                    }
                }

                // Get memwrite information
                @Override
                public void visit(VMemWrite w) {
                    // System.out.println("                           MemWrite");

                    if (w.dest instanceof VMemRef.Global) {
                        // TODO figure out source value here
                        System.out.println("                           TODO: FIX SOURCE VALUE (TEMP?)");
                        VMemRef.Global destination_0 = (VMemRef.Global) w.dest;
                        System.out.println(indent + "sw " + w.source.toString() + " " +
                                            destination_0.byteOffset + "(" + destination_0.base.toString() + ")");
                    } else {
                        // TODO figure out destination here
                        VMemRef.Stack destination_1 = (VMemRef.Stack) w.dest;
                        System.out.println(indent + "sw " + w.source.toString() + " 0($sp)");
                    }

                }

                // Return from function
                @Override
                public void visit(VReturn r) {
                    System.out.println(indent + "lw $ra -4($fp)"); // restore $ra from ($fp-4)
                    System.out.println(indent + "lw $fp -8($fp)"); // restore $fp from ($fp-8)
                    System.out.println(indent + "addu $sp $sp " + Integer.toString(size)); // increase $sp by size
                    System.out.println(indent + "jr $ra");
                }

            });
        }
    }

    boolean hasArrayError() {
        return this.arrayError;
    }
}
