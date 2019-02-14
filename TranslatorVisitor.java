import syntaxtree.*;
import visitor.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.ArrayList;

public class TranslatorVisitor extends GJDepthFirst<String, HashMap<String, Scope>> {
    Vector<String> globalVector = new Vector<String>();
    String currScope = "scope0";
    int scopeCounter = 0;

    String indent = "";
    String parent = "";
    int tempCount = 0;
    int labelCount = 1;

    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> ( VarDeclaration() )*
    * f15 -> ( Statement() )*
    * f16 -> "}"
    * f17 -> "}"
    */
    public String visit(MainClass n, HashMap<String, Scope> argu) { // Always scope0
        String _ret=null;
        System.out.println("func Main()");
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        n.f10.accept(this, argu);
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        n.f13.accept(this, argu);
        n.f14.accept(this, argu);
        n.f15.accept(this, argu);
        n.f16.accept(this, argu);
        n.f17.accept(this, argu);
        System.out.println("\0");
        return _ret;
    }

    /**
     * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
    public String visit(TypeDeclaration n, HashMap<String, Scope> argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    public String visit(ClassDeclaration n, HashMap<String, Scope> argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        String name = n.f1.accept(this, argu);
        parent = name;
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
    public String visit(ClassExtendsDeclaration n, HashMap<String, Scope> argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    public String visit(VarDeclaration n, HashMap<String, Scope> argu) {
       String _ret=null;
       String type = n.f0.accept(this, argu);
       String id = n.f1.accept(this, argu);
       n.f2.accept(this, argu);
       return _ret;
    }

    /**
     * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
    public String visit(MethodDeclaration n, HashMap<String, Scope> argu) {
        String _ret= "MethodDeclaration";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String name = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        String param = n.f4.accept(this, argu);
        System.out.println("func " + parent + "." + name + "(this " + param + ")");
        indent += "  ";
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        String returnType = n.f10.accept(this, argu);
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        indent = "";
        return _ret;
    }

    /**
     * f0 -> FormalParameter()
     * f1 -> ( FormalParameterRest() )*
     */
    public String visit(FormalParameterList n, HashMap<String, Scope> argu) {
       String _ret=null;
       _ret = n.f0.accept(this, argu);
       n.f1.accept(this, argu);
       return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    public String visit(FormalParameter n, HashMap<String, Scope> argu) { // Within method scope
       String _ret = null;
       String type = n.f0.accept(this, argu);
       _ret = n.f1.accept(this, argu);
       return _ret;
    }

    /**
     * f0 -> ArrayType()
     *       | BooleanType()
     *       | IntegerType()
     *       | Identifier()
     */
    public String visit(Type n, HashMap<String, Scope> argu) {
       String _ret = n.f0.accept(this, argu);
       return _ret;
    }

    /**
     * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
    public String visit(ArrayType n, HashMap<String, Scope> argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "boolean"
    */
    public String visit(BooleanType n, HashMap<String, Scope> argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "int"
    */
    public String visit(IntegerType n, HashMap<String, Scope> argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
    public String visit(Statement n, HashMap<String, Scope> argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
    public String visit(Block n, HashMap<String, Scope> argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
    public String visit(AssignmentStatement n, HashMap<String, Scope> argu) {
        String _ret=null;
        String id = n.f0.accept(this, argu);
        System.out.println(indent + id);
        n.f1.accept(this, argu);
        String s = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        String idType = argu.get(this.currScope).getType(id);
        return _ret;
    }

    /**
     * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
    public String visit(ArrayAssignmentStatement n, HashMap<String, Scope> argu) {
        String _ret=null;
        String id = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String first = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        String second = n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
    public String visit(IfStatement n, HashMap<String, Scope> argu) {
        String _ret= "IfStatement";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String Exp = n.f2.accept(this, argu);
        System.out.println(indent + "if0 t." + tempCount + " goto :if" + labelCount + "_else");
        indent += "  ";
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        indent = indent.substring(0, indent.length() - 2);
        System.out.println(indent + "if" + labelCount + "_else:");
        indent += "  ";
        n.f6.accept(this, argu);
        labelCount++;
        return _ret;
    }

    /**
     * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
    public String visit(WhileStatement n, HashMap<String, Scope> argu) {
        String _ret= "WhileStatement";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String Exp = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
    public String visit(PrintStatement n, HashMap<String, Scope> argu) {
        // System.out.println("PrintStmt");

        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String e = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> AndExpression()
    *       | CompareExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | PrimaryExpression()
    */
    public String visit(Expression n, HashMap<String, Scope> argu) {
        // System.out.println("Expression");

        String _ret = n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
    public String visit(AndExpression n, HashMap<String, Scope> argu) {
        // System.out.println("AndExpression");

        String _ret="boolean";
        String first = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String second = n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
    public String visit(CompareExpression n, HashMap<String, Scope> argu) {
        String _ret="boolean";
        String first = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String second = n.f2.accept(this, argu);
        System.out.println(indent + "t." + tempCount + " = Lts(" + first + " " + second + ")");
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
    public String visit(PlusExpression n, HashMap<String, Scope> argu) {
        String _ret="int";
        String first = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String second = n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
    public String visit(MinusExpression n, HashMap<String, Scope> argu) {
        // System.out.println("MinusExpression");

        String _ret = "int";
        String first = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String second = n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
    public String visit(TimesExpression n, HashMap<String, Scope> argu) {
        // System.out.println("TimesExpression");

        String _ret="int";
        String first = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String second = n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
    public String visit(ArrayLookup n, HashMap<String, Scope> argu) {
        // System.out.println("ArrayLookup");

        String _ret="int";
        String arr = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String value = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
    public String visit(ArrayLength n, HashMap<String, Scope> argu) {
        // System.out.println("ArrayLength");

        String _ret="int";
        String first = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
    public String visit(MessageSend n, HashMap<String, Scope> argu) {
        // System.out.println("MessageSend");
        // System.out.println(" currScope: " + this.currScope);

        String _ret = null;
        String className = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String methodName = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
    public String visit(ExpressionList n, HashMap<String, Scope> argu) {
        String _ret = null;
        String expr = n.f0.accept(this, argu);
        String exprRest = n.f1.accept(this, argu);
        _ret = expr;
        return _ret;
    }

    /**
     * f0 -> ","
    * f1 -> Expression()
    */
    public String visit(ExpressionRest n, HashMap<String, Scope> argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        _ret = n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> IntegerLiteral()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | Identifier()
    *       | ThisExpression()
    *       | ArrayAllocationExpression()
    *       | AllocationExpression()
    *       | NotExpression()
    *       | BracketExpression()
    */
    public String visit(PrimaryExpression n, HashMap<String, Scope> argu) {
        String _ret = n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> <INTEGER_LITERAL>
    */
    public String visit(IntegerLiteral n, HashMap<String, Scope> argu) {
        String _ret = n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "true"
    */
    public String visit(TrueLiteral n, HashMap<String, Scope> argu) {
        String _ret="true";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "false"
    */
    public String visit(FalseLiteral n, HashMap<String, Scope> argu) {
        String _ret="false";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> <IDENTIFIER>
    */
    public String visit(Identifier n, HashMap<String, Scope> argu) {
        String _ret = n.f0.toString();
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "this"
    */
    public String visit(ThisExpression n, HashMap<String, Scope> argu) {
        String _ret = "this";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
    public String visit(ArrayAllocationExpression n, HashMap<String, Scope> argu) {
        String _ret="array";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        String expr = n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
    public String visit(AllocationExpression n, HashMap<String, Scope> argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        String id = n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "!"
    * f1 -> Expression()
    */
    public String visit(NotExpression n, HashMap<String, Scope> argu) {
        String _ret="boolean";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
    public String visit(BracketExpression n, HashMap<String, Scope> argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        _ret = n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }
}
