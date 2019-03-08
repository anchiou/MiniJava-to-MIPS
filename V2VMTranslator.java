import cs132.vapor.ast.*;

public class V2VMTranslator {

    // Private data members
    private InstructionVisitor translator;
    private FlowGraph graph;

    // Constructor
    public V2VMTranslator() {
        translator = new InstructionVisitor();
        graph = new FlowGraph();
    }

    // Translator
    public void translate(VaporProgram p) {

        // Print data segments
        for (VDataSegment segment : p.dataSegments) {
            translateDataSegments(segment);
        }

        // Print functions
        for (VFunction function : p.functions) {
            translateFunction(function);
        }

    }

    // Translate data segments
    public void translateDataSegments(VDataSegment segment) {

        System.out.println("const " + segment.ident.toString());
        for (VOperand val : segment.values) {
            System.out.println("  " + val.toString());
        }
        System.out.print("\n");

    }

    // Function translator
    public void translateFunction(VFunction function) {

        System.out.print("func " + function.ident);
        System.out.println(" [in " + function.stack.in + " out "
            + function.stack.out + " local " + function.stack.local + "]");

        for (VVarRef.Local param : function.params) {
            System.out.println(param.toString());
        }

        for (VCodeLabel label : function.labels) {
            System.out.println(label.ident);
        }

        for (String var : function.vars) {
            System.out.println(var.toString());
        }

        graph = translator.translate(function.body);

        System.out.println("");

    }
}
