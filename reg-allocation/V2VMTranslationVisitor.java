import java.util.*;
import java.util.stream.Collectors;
import cs132.vapor.ast.*;

public class V2VMTranslationVisitor {
    private RegisterPool localPool = RegisterPool.CreateLocalPool();
    String indent = "";

    public void printFunc(FlowGraph graph, VFunction function, Liveness liveness, AllocationMap map) {
        List<Register> callee = map.usedCalleeRegister();
        // System.out.println("********************" + callee);

        // for (Register reg : callee) {
        //     System.out.print(reg.toString() + " ");
        // }

        Map<Integer, Set<String>> labels = new HashMap<>();
        for (VCodeLabel label : function.labels) {
            labels.computeIfAbsent(label.instrIndex, k -> new LinkedHashSet<>()).add(label.ident);
        }

        int in = Math.max(function.params.length - 4, 0);
        int local = map.stackSize();
        int out = 0;

        for (Node node : graph.getNodeList()) {
            VInstr instr = node.getInstr();
            if (instr instanceof VCall) {
                VCall call = (VCall) instr;
                out = Math.max(call.args.length - 4, out);

                // out[n] - def[n]
                Set<String> liveOut = liveness.out.get(node);
                liveOut.removeAll(node.getDefSet()); // remove all variables in out[n] that are in def[n]

                // Save $t before function call, saved on high address of local stack
                int saves = (int) liveOut.stream().map(map::lookupRegister).filter(o -> o != null
                        && o.isCallerSaved()).distinct().count();
                local = Math.max(local, map.stackSize() + saves);
            }
        }

        // Output function sig
        System.out.print("func " + function.ident);
        System.out.println(" [in " + in + ", out " + out + ", local " + local + "]");

        indent += "  ";

        // Save all $s registers
        for (int i = 0; i < callee.size(); i++) {
            String output = "local[" + Integer.toString(i) + "]";
            System.out.println(indent + output + " = " + callee.get(i).toString());
        }

        // load params into registers or (local) stack
        Register[] registers = {
            Register.a0, Register.a1, Register.a2, Register.a3
        };

        for (int i = 0; i < function.params.length; ++i) {
            Register dest = map.lookupRegister(function.params[i].ident);
            if (dest != null) {
                if (i < 4) { // parameters passed by registers
                    System.out.println(indent + dest.toString() + " = " + registers[i].toString());
                } else { // parameters passed by (in) stack
                    System.out.println(indent + dest.toString() + " = in[" + Integer.toString(i - 4) + "]");
                }
            } else {
                int offset = map.lookupStack(function.params[i].ident);
                if (offset != -1) { // Some parameters are never used
                    // Put remaining parameters into (local) stack
                    Register load = localPool.getRegister();
                    System.out.println(indent + load.toString() + " = in[" + Integer.toString(i - 4) + "]");
                    System.out.println(indent + "local[" + Integer.toString(offset) + "] = " + load.toString());
                    localPool.releaseRegister(load);
                }
            }
        }

        // print body stuff
        for (int i = 0; i < function.body.length; ++i) {
            Node node = graph.getNodeList().get(i);

            // out[n] - def[n]
            final Set<String> liveOut = liveness.out.get(node);
            liveOut.removeAll(node.getDefSet());

            // Output labels
            if (labels.containsKey(i)) {
                indent = indent.substring(0, indent.length() - 2);
                Set<String> set = labels.get(i);
                for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
                    String s = it.next();
                    System.out.println(s + ":");
                }
                indent += "  ";
            }

            // Visitor for instruction printing
            function.body[i].accept(new VInstr.Visitor<RuntimeException>() {

                @Override
                public void visit(VAssign assignment) {
                    Register destination = loadVar(map, assignment.dest.toString(), true);

                    if (assignment.source instanceof VVarRef) {
                        Register source = loadVar(map, assignment.source.toString(), false);
                        System.out.println(indent + destination.toString() + " = " + source.toString());
                        // if (localPool.contains(source)){
                        //     localPool.releaseRegister(source);
                        // }
                    } else {
                        System.out.println(indent + destination.toString() + " = " + assignment.source.toString());
                    }

                    // if (localPool.contains(destination)){
                    //     localPool.releaseRegister(destination);
                    // }

                    writeVar(destination, map, assignment.dest.toString());
                    releaseLocalReg(destination);
                }

                @Override
                public void visit(VBranch branch) {
                    String statement = "";

                    if (branch.value instanceof VVarRef) {
                        Register source = loadVar(map, branch.value.toString(), false);
                        statement = source.toString();
                        releaseLocalReg(source);
                    }

                    String output = "  ";
                    if (branch.positive) {
                        output += "if ";
                    } else {
                        output += "if0 ";
                    }
                    System.out.println(output + statement + " goto " + branch.target);
                }

                @Override
                public void visit(VBuiltIn builtIn) {
                    String output = builtIn.op.name + "(";
                    List<Register> registers = new ArrayList<>();
                    for (VOperand operand : builtIn.args) {
                        if (operand instanceof VVarRef) {
                            Register source = loadVar(map, operand.toString(), false);
                            registers.add(source);
                            output += source.toString();
                            output += " ";
                        } else {
                            output += operand.toString();
                            output += " ";
                        }
                    }

                    output = output.substring(0, output.length() - 1);
                    output += ")";

                    for (Register reg : registers) {
                        if (localPool.contains(reg)) {
                            localPool.releaseRegister(reg);
                        }
                    }

                    if (builtIn.dest == null) {
                        System.out.println(indent + output);
                    } else if (builtIn.dest != null) {
                        Register destination = loadVar(map, builtIn.dest.toString(), true);
                        System.out.println(indent + destination.toString() + " = " + output);
                        if (localPool.contains(destination)) {
                            localPool.releaseRegister(destination);
                        }
                    }
                }

                @Override
                public void visit(VCall call) {
                    // Register[] registers = {
                    //     Register.a0, Register.a1, Register.a2, Register.a3
                    // };
                    // //////////////////////////////////////////////
                    // for (int i = 0; i < call.args.length; ++i) {
                    //     if (i < registers.length) System.out.println(indent + registers[i] + " = " + call.args[i]);
                    //     else System.out.println(indent + "out[" + (i - registers.length) + "] = " + call.args[i]);
                    // }
                    // //////////////////////////////////////////
                    // if (call.addr instanceof VAddr.Label) {
                    //     System.out.println(indent + "call " + call.addr.toString());
                    // } else {
                    //     Register reg = loadVar(map, call.addr.toString(), false);
                    //     System.out.println(indent + "call " + reg.toString());
                    //     if (localPool.contains(reg)) {
                    //         localPool.releaseRegister(reg);
                    //     }
                    // }

                    List<Register> save = liveOut.stream().map(map::lookupRegister).filter(o -> o != null
                            && o.isCallerSaved()).distinct().collect(Collectors.toList());
                    save.sort(Comparator.comparing(Register::toString));

                    // Save all $t registers
                    int offset;
                    for (int i = 0; i < save.size(); ++i) {
                        offset = map.stackSize() + i;
                        System.out.println(indent +
                            "local[" + Integer.toString(offset) + "]" + " = " + save.get(i).toString());
                    }

                    Register[] argumentRegisters = { Register.a0, Register.a1, Register.a2, Register.a3 };
                    Register register;
                    for (int i = 0; i < call.args.length; ++i) {
                        String var = call.args[i].toString();
                        if (call.args[i] instanceof VVarRef) {
                            if (i < 4) { // registers
                                register = map.lookupRegister(var);
                                if (register != null) {
                                    System.out.println(indent + argumentRegisters[i].toString() + " = " + register.toString());
                                } else {
                                    offset = map.lookupStack(var);
                                    System.out.println(indent +
                                        argumentRegisters[i].toString() + " = local[" + Integer.toString(offset) + "]");
                                }
                            } else { // (out) stack
                                register = loadVar(map, var, false);
                                System.out.println(indent + "out[" + Integer.toString(i - 4) + "] = " + register.toString());
                                releaseLocalReg(register);
                            }
                        } else {
                            if (i < 4) { // store in registers $a0-$a3
                                System.out.println(indent + argumentRegisters[i].toString() + " = " + var);
                            } else { // store in (out) stack
                                System.out.println(indent + "out[" + Integer.toString(i - 4) + "] = " + var);
                            }
                        }
                    }

                    if (call.addr instanceof VAddr.Label) {
                        System.out.println(indent + "call " + call.addr.toString());
                    } else {
                        Register address = loadVar(map, call.addr.toString(), false);
                        System.out.println(indent + "call " + address.toString());
                        releaseLocalReg(address);
                    }

                    Register dest = loadVar(map, call.dest.toString(), true);
                    if (dest != Register.v0)
                        System.out.println(indent + dest.toString() + " = " + Register.v0.toString());
                    writeVar(dest, map, call.dest.toString());
                    releaseLocalReg(dest);

                    // Restore all $t registers
                    for (int i = 0; i < save.size(); i++) {
                        System.out.println(indent + save.get(i).toString()
                            + " = local[" + Integer.toString(map.stackSize() + i) + "]");
                    }
                }

                @Override
                public void visit(VGoto go) {
                    System.out.println(indent + "goto " + go.target.toString());
                }

                @Override
                public void visit(VMemRead memRead) {
                    Register dst = loadVar(map, memRead.dest.toString(), true);

                    VMemRef.Global mem = (VMemRef.Global) memRead.source;
                    Register src = loadVar(map, mem.base.toString(), false);

                    int offset = mem.byteOffset;
                    String output = "";
                    if (offset > 0) {
                        output = "[" + src.toString() + "+" + Integer.toString(offset) + "]";
                    } else {
                        output = "[" + src.toString() + "]";
                    }

                    System.out.println(indent + dst.toString() + " = " + output);
                    releaseLocalReg(src);

                    writeVar(dst, map, memRead.dest.toString());
                    releaseLocalReg(dst);
                }

                @Override
                public void visit(VMemWrite memWrite) {
                    VMemRef.Global ref = (VMemRef.Global) memWrite.dest;
                    Register base = loadVar(map, ref.base.toString(), false);
                    String output = "";
                    int offset = ref.byteOffset;
                    if (offset > 0) {
                        output = "[" + base.toString() + "+" + Integer.toString(offset) + "]";
                    } else {
                        output = "[" + base.toString() + "]";
                    }

                    if (memWrite.source instanceof VVarRef) {
                        Register src = loadVar(map, memWrite.source.toString(), false);
                        System.out.println(indent + output + " = " + src.toString());
                        releaseLocalReg(src);
                    } else {
                        System.out.println(indent + output + " = " + memWrite.source.toString());
                    }

                    releaseLocalReg(base);
                }

                @Override
                public void visit(VReturn ret) {
                    if (ret.value != null) {
                        if (ret.value instanceof VVarRef) {
                            Register src = loadVar(map, ret.value.toString(), false);
                            if (src != Register.v0)
                                System.out.println(indent + Register.v0.toString() + " = " + src.toString());
                            releaseLocalReg(src);
                        } else {
                            System.out.println(indent + Register.v0.toString() + " = " + ret.value.toString());
                        }
                    }

                    // Restore $s registers
                    String output;
                    for (int i = 0; i < callee.size(); i++) {
                        output = "local[" + Integer.toString(i) + "]";
                        System.out.println(indent + callee.get(i).toString() + " = " + output);
                    }

                    System.out.println(indent + "ret");
                }
            });
        }

        indent = indent.substring(0, indent.length() - 2);
    }

    private Register loadVar(AllocationMap map, String var, boolean dest) {
        Register register = map.lookupRegister(var);
        if (register != null) { // variable in register
            return register;
        } else { // variable on (local) stack
            int offset = map.lookupStack(var);
            String output = "ocal[" + Integer.toString(offset) + "]";
            Register load = localPool.getRegister();
            if (!dest) // for destinations, only a register
                System.out.println(indent + load.toString() + " = " + output);
            return load;
        }
    }

    private void writeVar(Register register, AllocationMap map, String var) {
        int offset = map.lookupStack(var);
        String output = "local[" + Integer.toString(offset) + "]";
        if (offset != -1) {
            System.out.println(indent + output + " = " + register.toString());
        }
    }

    private void releaseLocalReg(Register register) {
        if (localPool.contains(register)) {
            localPool.releaseRegister(register);
        }
    }
}
