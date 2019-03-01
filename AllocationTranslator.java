import cs132.vapor.ast.*;

public class AllocationTranslator {

    public AllocationTranslator() {}

    public void translate(VaporProgram p) {

        // Print datasegments
        for (VDataSegment value : p.dataSegments) {
            System.out.println("const " + value.ident.toString());
            for (VOperand val : value.values) {
                System.out.println("  " + val.toString());
            }
            System.out.print("\n");
        }

        for (VFunction function : p.functions) {
            translateFunction(function);
        }

    }

    public void translateFunction(VFunction function) {

        

        for (VCodeLabel label : function.labels) {
            System.out.println(label.ident);
        }

        for (VVarRef.Local param : function.params) {
            System.out.println(param.toString());
        }

        for (String var : function.vars) {
            System.out.println(var.toString());
        }

        System.out.println(function.stack.in);
        System.out.println(function.stack.out);
        System.out.println(function.stack.local);
        System.out.println(function.ident);

    }

}
