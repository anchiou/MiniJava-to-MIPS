import cs132.util.*;
import cs132.vapor.ast.VBuiltIn.Op;
import cs132.vapor.ast.*;
import cs132.vapor.parser.*;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class V2VM {

    public static VaporProgram parseVapor(InputStream in, PrintStream err) throws IOException {
        Op[] ops = {
          Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS,
          Op.PrintIntS, Op.HeapAllocZ, Op.Error,
        };
        boolean allowLocals = true;
        String[] registers = null;
        boolean allowStack = false;

        VaporProgram tree;
        try {
          tree = VaporParser.run(new InputStreamReader(in), 1, 1,
                                 java.util.Arrays.asList(ops),
                                 allowLocals, registers, allowStack);
        }
        catch (Exception ex) {
          err.println(ex.getMessage());
          return null;
        }

        return tree;
    }

    public static void main(String[] args) throws ProblemException, IOException {
        VaporProgram program = parseVapor(System.in, System.out);

        AllocationTranslator translator = new AllocationTranslator();
        translator.translate(program);
    }
}