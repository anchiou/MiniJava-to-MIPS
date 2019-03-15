import java.util.*;
import cs132.vapor.ast.*;

public class VM2MVisitor {

    String indent = "  ";
    boolean arrayError = false; // flag for array out of bounds error

    // Instruction visitor
    public void acceptInstructions(VInstr[] instructions, Map<Integer, Set<String>> labels, int size) {
        int index = 0;

        for (VInstr instruction : instructions) {
            // Output labels
            if (labels.containsKey(index)) {
                indent = indent.substring(0, indent.length() - 2);
                Set<String> set = labels.get(index);
                for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
                    String label = it.next();
                    System.out.println(label + ":");
                }
                indent += "  ";
            }
            ++index;

            instruction.accept(new VInstr.Visitor<RuntimeException>() {

                // Translate assignment information
                @Override
                public void visit(VAssign a) {
                    String src = a.source.toString();
                    if (a.source instanceof VVarRef) { // source is register
                        System.out.println(indent + "move " + a.dest.toString() + " " + src);
                    } else if (src.startsWith(":")) { // source is label
                        System.out.println(indent + "la " + a.dest.toString() + " " + src.substring(1));
                    } else { // source is immediate
                        System.out.println(indent + "li " + a.dest.toString() + " " + src);
                    }
                }

                // Translate branch information
                @Override
                public void visit(VBranch b) {
                    String target = b.target.toString();

                    if (b.positive) { // if the branch is positive
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
                        System.out.println(indent + "move " + dest + " $v0");

                    } else if (opName == "PrintIntS") { // PrintIntS
                        if (b.args[0] instanceof VVarRef) { // PrintIntS(reg)
                            System.out.println(indent + "move $a0 " + b.args[0].toString());
                        } else { // PrintIntS(imm)
                            System.out.println(indent + "li $a0 " + b.args[0].toString());
                        }
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
                        if (!arg1.toString().startsWith("$")) { // Not a register therefore an immediate
                            if (!arg2.toString().startsWith("$")) { // MulS(imm imm)
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
                    String addr = c.addr.toString(); // call address
                    if (addr.startsWith(":")) { // address is to a function
                        System.out.println(indent + "jal " + addr.substring(1));
                    } else { // address is in register
                        System.out.println(indent + "jalr " + addr);
                    }
                }

                // Translate goto information
                @Override
                public void visit(VGoto g) {
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
                        String region = src.region.toString();
                        if (region == "In") { // (in) stack pointed to by fp
                            System.out.println(indent + "lw " + r.dest.toString() + " " + byteOffset + "($fp)");
                        } else { // (local) and (out) pointed to by sp
                            System.out.println(indent + "lw " + r.dest.toString() + " " + byteOffset + "($sp)");
                        }
                    }
                }

                // Translate memwrite information
                @Override
                public void visit(VMemWrite w) {
                    if (w.dest instanceof VMemRef.Global) { // destination is a register
                        VMemRef.Global dest = (VMemRef.Global) w.dest;
                        String src = w.source.toString();
                        if (src.startsWith(":")) { // source is vmt
                            System.out.println(indent + "la $t9 " + src.substring(1));
                            System.out.println(indent + "sw $t9 0($t0)");
                        } else if (src.startsWith("$")) { // source is register
                            System.out.println(indent + "sw " + w.source.toString() + " " +
                                                dest.byteOffset + "(" + dest.base.toString() + ")");
                        } else { // source is immediate
                            System.out.println(indent + "li $t9 " +  w.source.toString());
                            System.out.println(indent + "sw $t9 " +
                                                dest.byteOffset + "(" + dest.base.toString() + ")");
                        }
                    }
                    else { // destination is stack
                        VMemRef.Stack dest = (VMemRef.Stack) w.dest;
                        int byteOffset = dest.index * 4;
                        String region = dest.region.toString();
                        if (region != "In") { // (local) or (out)
                            if (w.source instanceof VVarRef) { // source is a register
                                System.out.println(indent + "sw " + w.source.toString() +
                                                    " " + Integer.toString(byteOffset) + "($sp)");
                            } else { // source is an immediate
                                System.out.println(indent + "li $t9 " +  w.source.toString());
                                System.out.println(indent + "sw $t9 " + Integer.toString(byteOffset) + "($sp)");
                            }
                        } else { // (in)
                            if (w.source instanceof VVarRef) { // source is a register
                                System.out.println(indent + "sw " + w.source.toString() +
                                                    " " + Integer.toString(byteOffset) + "($fp)");
                            } else { // source is an immediate
                                System.out.println(indent + "li $t9 " +  w.source.toString());
                                System.out.println(indent + "sw $t9 " + Integer.toString(byteOffset) + "($fp)");
                            }
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
