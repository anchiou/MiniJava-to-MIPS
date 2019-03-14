import java.util.*;
import cs132.vapor.ast.*;

public class VM2MTranslator {
    // Translate data segments
    public void translateDataSegments(VDataSegment[] segments) {
        System.out.println(".data\n");
        for (VDataSegment segment : segments) {
            System.out.println(segment.ident.toString() + ":");
            for (VOperand value : segment.values) {
                System.out.println("  " + value.toString());
            }
            System.out.print("\n");
        }
    }
}