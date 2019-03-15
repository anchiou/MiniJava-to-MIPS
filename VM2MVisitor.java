import java.util.*;
import cs132.vapor.ast.*;

public class VM2MVisitor {

    String indent = "  ";
    boolean arrayError = false; // flag for array out of bounds error

    // Instruction visitor
    public void acceptInstructions(VInstr[] instructions, int size) {

        for (VInstr instruction : instructions) {
            instruction.accept(new VInstr.Visitor<RuntimeException>() {

                // Translate assignment information
                @Override
                public void visit(VAssign a) {
                    if (a.source instanceof VVarRef) {
                        System.out.println(indent + "move " +
                                            a.dest.toString() + " " + a.source.toString());
                    } else {
                        System.out.println(indent + "li " +
                                            a.dest.toString() + " " + a.source.toString());
                    }
                }

                // Translate branch information
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

                // Translate BuiltIn information
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
                        if (b.args[0].toString().startsWith("$")) { // register value
                            System.out.println(indent + "move $a0 " + b.args[0].toString());
                        } else { // immediate value
                            System.out.println(indent + "li $a0 " + b.args[0].toString());
                        }
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

                // Translate Call information
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

                // Translate goto information
                @Override
                public void visit(VGoto g) {
                    // System.out.println("                           Goto");

                    String target = g.target.toString();
                    target = target.substring(1, target.length());
                    System.out.println(indent + "j " + target);
                }

                // Translate memread information
                @Override
                public void visit(VMemRead r) {
                    if (r.source instanceof VMemRef.Global) { // read from register
                        VMemRef.Global src = (VMemRef.Global) r.source;
                        System.out.println(indent + "lw " + r.dest.toString() + " " + src.byteOffset
                                            + "(" + src.base.toString() + ")");
                    } else { // read from stack
                        VMemRef.Stack src = (VMemRef.Stack) r.source;
                        int byteOffset = src.index * 4;
                        // in stack pointed to by fp
                        System.out.println(indent + "lw " + r.dest.toString() + " " + byteOffset + "($fp)");
                    }
                }

                // Translate memwrite information
                @Override
                public void visit(VMemWrite w) {
                    if (w.dest instanceof VMemRef.Global) { // destination is a register
                        VMemRef.Global dest = (VMemRef.Global) w.dest;
                        String src = w.source.toString();
                        if (src.startsWith(":")) { // source is vmt
                            System.out.println(indent + "move $t0 $v0");
                            System.out.println(indent + "la $t9 " + src.substring(1));
                            System.out.println(indent + "sw $t9 0($t0)");
                        } else {
                            System.out.println(indent + "sw " + w.source.toString() + " " +
                                                dest.byteOffset + "(" + dest.base.toString() + ")");
                        }
                    }
                    else { // destination is stack
                        VMemRef.Stack dest = (VMemRef.Stack) w.dest;
                        int byteOffset = dest.index * 4;
                        // String region = dest.region.toString();
                        if (w.source instanceof VVarRef) { // source is a register
                            System.out.println(indent + "sw " + w.source.toString() +
                                                " " + Integer.toString(byteOffset) + "($sp)");
                        } else { // source is an immediate
                            System.out.println(indent + "li $t9 " +  w.source.toString());
                            System.out.println(indent + "sw $t9 " + Integer.toString(byteOffset) + "($sp)");
                        }
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
