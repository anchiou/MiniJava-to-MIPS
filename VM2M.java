import cs132.util.*;
import cs132.vapor.ast.VBuiltIn.Op;
import cs132.vapor.ast.*;
import cs132.vapor.parser.*;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;

public class VM2M {

    public static VaporProgram parseVapor(InputStream in, PrintStream err) throws IOException {
        Op[] ops = {
            Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS,
            Op.PrintIntS, Op.HeapAllocZ, Op.Error,
        };

        boolean allowLocals = false;
        String[] registers = {
            "v0", "v1",
            "a0", "a1", "a2", "a3",
            "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7",
            "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7",
            "t8",
        };
        boolean allowStack = true;

        VaporProgram tree;
        try {
            tree = VaporParser.run(new InputStreamReader(in), 1, 1,
                                   java.util.Arrays.asList(ops),
                                   allowLocals, registers, allowStack);
        }
        catch (ProblemException ex) {
            err.println(ex.getMessage());
            return null;
        }

        return tree;
    }

    public static void main(String[] args) throws ProblemException, IOException {
        VaporProgram program = parseVapor(System.in, System.out);
        VM2MTranslator translator = new VM2MTranslator();
        translator.translateDataSegments(program.dataSegments);
        translator.beginText();

        for (VFunction function : program.functions) {
            translator.translateFunction(function);
        }

        translator.endText();
    }
}