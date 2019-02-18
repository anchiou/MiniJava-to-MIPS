import syntaxtree.*;
import visitor.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class TranslatorVisitor extends GJDepthFirst<String, TranslationHelper> {
    Vector<String> globalVector = new Vector<String>();
    String currScope = "scope0";
    int scopeCount = 0;

    String indent = "";
    String parent = "";
    int tempCount = 0;
    int labelCount = 1;
    int endCount = 1;
    int nullCount = 1;

    /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
    public String visit(Goal n, TranslationHelper helper) {
        String _ret = null;
        Map<String, Map<String, String>> vTables = helper.classList.getAllVTables();
        for (String key : vTables.keySet()) {
            System.out.println("const vmt_" + key);
            helper.classList.printMethods(key);
        }

        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        return _ret;
    }

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
    public String visit(MainClass n, TranslationHelper helper) { // Always scope0
        String _ret=null;
        Map<String, Map<String, String>> vTables = helper.classList.getAllVTables();
        System.out.println("func Main()");
	    indent += "  ";
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        n.f3.accept(this, helper);
        n.f4.accept(this, helper);
        n.f5.accept(this, helper);
        n.f6.accept(this, helper);
        n.f7.accept(this, helper);
        n.f8.accept(this, helper);
        n.f9.accept(this, helper);
        n.f10.accept(this, helper);
        n.f11.accept(this, helper);
        n.f12.accept(this, helper);
        n.f13.accept(this, helper);
        n.f14.accept(this, helper);
        n.f15.accept(this, helper);
        String className = helper.symbolTable.get(currScope).getClassName();
        int classSize = helper.classList.getClassSize(className);
        System.out.println(indent + "t." + tempCount + " = HeapAllocZ(" + classSize + ")");
        --scopeCount;
        this.currScope = "scope" + scopeCount; // update scope
        System.out.println(indent + "[t." + tempCount + "] = :vmt_" + className);
        System.out.println(indent + "if0 t." + tempCount + " goto :null" + nullCount);
        System.out.println(indent + "Error" + "(" + "\"null pointer\"" + ")");
        System.out.println(indent + "null" + nullCount + ":");
        ++nullCount;
        ++tempCount;
        System.out.println(indent + "t." + tempCount + " = [t." + (tempCount-1) + "]");
        System.out.println(indent + "t." + tempCount + " = [t." + tempCount + "+0]");
        n.f16.accept(this, helper);
        n.f17.accept(this, helper);
        indent = indent.substring(0, indent.length() - 2);
        System.out.println("\0");
        return _ret;
    }

    /*
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
    public String visit(TypeDeclaration n, TranslationHelper helper) {
        String _ret=null;
        n.f0.accept(this, helper);
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
    public String visit(ClassDeclaration n, TranslationHelper helper) {
        ++scopeCount;
        this.currScope = "scope" + scopeCount; // update scope
        // System.out.println("ClassDec: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        String name = n.f1.accept(this, helper);
        parent = name;
        n.f2.accept(this, helper);
        n.f3.accept(this, helper);
        n.f4.accept(this, helper);
        n.f5.accept(this, helper);
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
    public String visit(ClassExtendsDeclaration n, TranslationHelper helper) {
        ++scopeCount;
        this.currScope = "scope" + scopeCount; // update scope
        // System.out.println("ClassExtendsDec: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        n.f3.accept(this, helper);
        n.f4.accept(this, helper);
        n.f5.accept(this, helper);
        n.f6.accept(this, helper);
        n.f7.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    public String visit(VarDeclaration n, TranslationHelper helper) {
        // System.out.println("VarDec: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

       String _ret=null;
       String type = n.f0.accept(this, helper);
       String id = n.f1.accept(this, helper);
       n.f2.accept(this, helper);
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
    public String visit(MethodDeclaration n, TranslationHelper helper) {
        // System.out.println("MethDec: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        tempCount = 0;
        String _ret= "MethodDeclaration";
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String name = n.f2.accept(this, helper);
        n.f3.accept(this, helper);
        String param = n.f4.accept(this, helper);
        if (param != null) {
            System.out.println("func " + parent + "." + name + "(this " + param + ")");
        }
        else {
            System.out.println("func " + parent + "." + name + "()");
        }
        indent += "  ";
        n.f5.accept(this, helper);
        n.f6.accept(this, helper);
        n.f7.accept(this, helper);
        n.f8.accept(this, helper);
        n.f9.accept(this, helper);
        String returnId = n.f10.accept(this, helper);
        n.f11.accept(this, helper);
        n.f12.accept(this, helper);
	    System.out.println(indent + "ret " + returnId);
        indent = "";
        System.out.println("\0");
        return _ret;
    }

    /**
     * f0 -> FormalParameter()
     * f1 -> ( FormalParameterRest() )*
     */
    public String visit(FormalParameterList n, TranslationHelper helper) {
        // System.out.println("FormalParamList: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        _ret = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    public String visit(FormalParameter n, TranslationHelper helper) { // Within method scope
        // System.out.println("FormalParam: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret = null;
        String type = n.f0.accept(this, helper);
        _ret = n.f1.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> ArrayType()
     *       | BooleanType()
     *       | IntegerType()
     *       | Identifier()
     */
    public String visit(Type n, TranslationHelper helper) {
        // System.out.println("Type: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret = n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "int"
     * f1 -> "["
     * f2 -> "]"
     */
    public String visit(ArrayType n, TranslationHelper helper) {
        // System.out.println("ArrayType: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "boolean"
     */
    public String visit(BooleanType n, TranslationHelper helper) {
        // System.out.println("BooleanType: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "int"
     */
    public String visit(IntegerType n, TranslationHelper helper) {
        // System.out.println("IntType: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
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
    public String visit(Statement n, TranslationHelper helper) {
        // System.out.println("Statement: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "{"
     * f1 -> ( Statement() )*
     * f2 -> "}"
     */
    public String visit(Block n, TranslationHelper helper) {
        // System.out.println("Block: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    public String visit(AssignmentStatement n, TranslationHelper helper) {
        // System.out.println("AssnStmt: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        String id = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String s = n.f2.accept(this, helper);
        System.out.println(indent + id + " = " + s);
        n.f3.accept(this, helper);
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
public String visit(ArrayAssignmentStatement n, TranslationHelper helper) {
        // System.out.println("ArrayAssnStmt: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        String id = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String first = n.f2.accept(this, helper);
        n.f3.accept(this, helper);
        n.f4.accept(this, helper);
        String second = n.f5.accept(this, helper);
        n.f6.accept(this, helper);
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
    public String visit(IfStatement n, TranslationHelper helper) {
        // System.out.println("IfStmt: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret= "IfStatement";
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String Exp = n.f2.accept(this, helper);
        System.out.println(indent + "if0 t." + tempCount + " goto :if" + labelCount + "_else");
        indent += "  ";
	    tempCount++;
        n.f3.accept(this, helper);
        n.f4.accept(this, helper);
        // First block
        System.out.println(indent + "goto :if" + endCount + "_end");
        n.f5.accept(this, helper);
        indent = indent.substring(0, indent.length() - 2);
        System.out.println(indent + "if" + labelCount + "_else:");
        indent += "  ";
        n.f6.accept(this, helper);
        // second block
        indent = indent.substring(0, indent.length() - 2);
        System.out.println(indent + "if" + endCount + "_end:");
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
    public String visit(WhileStatement n, TranslationHelper helper) {
        // System.out.println("WhileStmt: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

	    String _ret= "WhileStatement";
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String Exp = n.f2.accept(this, helper);
        n.f3.accept(this, helper);
        n.f4.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> ";"
     */
    public String visit(PrintStatement n, TranslationHelper helper) {
        // System.out.println("PrintStmt: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String e = n.f2.accept(this, helper);
        n.f3.accept(this, helper);
        n.f4.accept(this, helper);
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
    public String visit(Expression n, TranslationHelper helper) {
        // System.out.println("Expression: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret = n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "&&"
     * f2 -> PrimaryExpression()
     */
    public String visit(AndExpression n, TranslationHelper helper) {
        // System.out.println("AndExpression: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());
        String _ret="boolean";
        String first = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String second = n.f2.accept(this, helper);
        System.out.println(indent + "t." + tempCount + " = And(" + first + " " + second + ")");
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    public String visit(CompareExpression n, TranslationHelper helper) {
        // System.out.println("CompareExpr: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());
        String _ret="boolean";
        String first = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String second = n.f2.accept(this, helper);
        System.out.println(indent + "t." + tempCount + " = LtS(" + first + " " + second + ")");
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
    public String visit(PlusExpression n, TranslationHelper helper) {
        // System.out.println("PlusExpr: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());
        String _ret="int";
        String first = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String second = n.f2.accept(this, helper);
        System.out.println(indent + "t." + tempCount + " = Add(" + first + " " + second + ")");
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    public String visit(MinusExpression n, TranslationHelper helper) {
        // System.out.println("MinusExpression: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());
        String _ret = "int";
        String first = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String second = n.f2.accept(this, helper);
	    System.out.println(indent + "t." + tempCount + " = Sub(" + first + " " + second + ")");
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    public String visit(TimesExpression n, TranslationHelper helper) {
        // System.out.println("TimesExpression: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());
        String _ret="int";
        String first = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String second = n.f2.accept(this, helper);
	    System.out.println(indent + "MulS(" + first + " " + second + ")");
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */
    public String visit(ArrayLookup n, TranslationHelper helper) {
        // System.out.println("ArrayLookup: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());
        String _ret="int";
        String arr = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String value = n.f2.accept(this, helper);
        n.f3.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    public String visit(ArrayLength n, TranslationHelper helper) {
        // System.out.println("ArrayLength: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret="int";
        String first = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        n.f2.accept(this, helper);
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
    public String visit(MessageSend n, TranslationHelper helper) {
        ++scopeCount;
        this.currScope = "scope" + scopeCount; // update scope
        // System.out.println("MessageSend: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret = null;
        String name = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String methodName = n.f2.accept(this, helper);
        // System.out.println(indent + "t." + tempCount + " = [this]");
        // System.out.println(indent + "t." + tempCount + " = [t." + tempCount + "+FIXME]");
        n.f3.accept(this, helper);
        n.f4.accept(this, helper);
        n.f5.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    public String visit(ExpressionList n, TranslationHelper helper) {
        // System.out.println("ExprList: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret = null;
        String expr = n.f0.accept(this, helper);
        String exprRest = n.f1.accept(this, helper);
        _ret = expr;
        return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public String visit(ExpressionRest n, TranslationHelper helper) {
        // System.out.println("ExprRest: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        _ret = n.f1.accept(this, helper);
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
    public String visit(PrimaryExpression n, TranslationHelper helper) {
        // System.out.println("PrimaryExpr: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret = n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public String visit(IntegerLiteral n, TranslationHelper helper) {
        // System.out.println("IntLiteral: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret = n.f0.toString();
        n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "true"
     */
    public String visit(TrueLiteral n, TranslationHelper helper) {
        // System.out.println("TrueLit: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret="true";
        n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "false"
     */
    public String visit(FalseLiteral n, TranslationHelper helper) {
        // System.out.println("FalseLit: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret="false";
        n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public String visit(Identifier n, TranslationHelper helper) {
        // System.out.println("Identifier: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret = n.f0.toString();
        n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "this"
     */
    public String visit(ThisExpression n, TranslationHelper helper) {
        // System.out.println("ThisExpr: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret = "this";
        n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "new"
     * f1 -> "int"
     * f2 -> "["
     * f3 -> Expression()
     * f4 -> "]"
     */
    public String visit(ArrayAllocationExpression n, TranslationHelper helper) {
        // System.out.println("ArrayAllocExpr: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret="array";
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        String expr = n.f3.accept(this, helper);
        n.f4.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public String visit(AllocationExpression n, TranslationHelper helper) {
        // System.out.println("AllocExpr: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        String id = n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        n.f3.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    public String visit(NotExpression n, TranslationHelper helper) {
        // System.out.println("NotExpr: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());
        String _ret="boolean";
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        //System.out.println(indent + "t." + tempCount + " = Not(" + first + " " + second + ")");
        return _ret;
    }

    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    public String visit(BracketExpression n, TranslationHelper helper) {
        // System.out.println("BracketExpr: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        _ret = n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        return _ret;
    }
}
