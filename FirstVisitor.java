import syntaxtree.*;
import visitor.*;

public class FirstVisitor extends GJVoidDepthFirst<Scope> {
 /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
    public void visit(Goal n, Scope argu) {
        System.out.println("FirstVisitor --> Goal");
        String s = argu.getTest();
        System.out.println(s);
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
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
     public void visit(MainClass n, Scope argu) {
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
     }

     /**
      * f0 -> ClassDeclaration()
      *       | ClassExtendsDeclaration()
      */
     public void visit(TypeDeclaration n, Scope argu) {
        n.f0.accept(this, argu);
     }

     /**
      * f0 -> "class"
      * f1 -> Identifier()
      * f2 -> "{"
      * f3 -> ( VarDeclaration() )*
      * f4 -> ( MethodDeclaration() )*
      * f5 -> "}"
      */
     public void visit(ClassDeclaration n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
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
     public void visit(ClassExtendsDeclaration n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
     }

     /**
      * f0 -> Type()
      * f1 -> Identifier()
      * f2 -> ";"
      */
     public void visit(VarDeclaration n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
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
     public void visit(MethodDeclaration n, Scope argu) {
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
     }

     /**
      * f0 -> FormalParameter()
      * f1 -> ( FormalParameterRest() )*
      */
     public void visit(FormalParameterList n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
     }

     /**
      * f0 -> Type()
      * f1 -> Identifier()
      */
     public void visit(FormalParameter n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
     }

     /**
      * f0 -> ","
      * f1 -> FormalParameter()
      */
     public void visit(FormalParameterRest n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
     }

     /**
      * f0 -> ArrayType()
      *       | BooleanType()
      *       | IntegerType()
      *       | Identifier()
      */
     public void visit(Type n, Scope argu) {
        n.f0.accept(this, argu);
     }

     /**
      * f0 -> "int"
      * f1 -> "["
      * f2 -> "]"
      */
     public void visit(ArrayType n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
     }

     /**
      * f0 -> "boolean"
      */
     public void visit(BooleanType n, Scope argu) {
        n.f0.accept(this, argu);
     }

     /**
      * f0 -> "int"
      */
     public void visit(IntegerType n, Scope argu) {
        n.f0.accept(this, argu);
     }

     /**
      * f0 -> Block()
      *       | AssignmentStatement()
      *       | ArrayAssignmentStatement()
      *       | IfStatement()
      *       | WhileStatement()
      *       | PrintStatement()
      */
     public void visit(Statement n, Scope argu) {
        n.f0.accept(this, argu);
     }

     /**
      * f0 -> "{"
      * f1 -> ( Statement() )*
      * f2 -> "}"
      */
     public void visit(Block n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
     }

     /**
      * f0 -> Identifier()
      * f1 -> "="
      * f2 -> Expression()
      * f3 -> ";"
      */
     public void visit(AssignmentStatement n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
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
     public void visit(ArrayAssignmentStatement n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
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
     public void visit(IfStatement n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
     }

     /**
      * f0 -> "while"
      * f1 -> "("
      * f2 -> Expression()
      * f3 -> ")"
      * f4 -> Statement()
      */
     public void visit(WhileStatement n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
     }

     /**
      * f0 -> "System.out.println"
      * f1 -> "("
      * f2 -> Expression()
      * f3 -> ")"
      * f4 -> ";"
      */
     public void visit(PrintStatement n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
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
     public void visit(Expression n, Scope argu) {
        n.f0.accept(this, argu);
     }

     /**
      * f0 -> PrimaryExpression()
      * f1 -> "&&"
      * f2 -> PrimaryExpression()
      */
     public void visit(AndExpression n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
     }

     /**
      * f0 -> PrimaryExpression()
      * f1 -> "<"
      * f2 -> PrimaryExpression()
      */
     public void visit(CompareExpression n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
     }

     /**
      * f0 -> PrimaryExpression()
      * f1 -> "+"
      * f2 -> PrimaryExpression()
      */
     public void visit(PlusExpression n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
     }

     /**
      * f0 -> PrimaryExpression()
      * f1 -> "-"
      * f2 -> PrimaryExpression()
      */
     public void visit(MinusExpression n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
     }

     /**
      * f0 -> PrimaryExpression()
      * f1 -> "*"
      * f2 -> PrimaryExpression()
      */
     public void visit(TimesExpression n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
     }

     /**
      * f0 -> PrimaryExpression()
      * f1 -> "["
      * f2 -> PrimaryExpression()
      * f3 -> "]"
      */
     public void visit(ArrayLookup n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
     }

     /**
      * f0 -> PrimaryExpression()
      * f1 -> "."
      * f2 -> "length"
      */
     public void visit(ArrayLength n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
     }

     /**
      * f0 -> PrimaryExpression()
      * f1 -> "."
      * f2 -> Identifier()
      * f3 -> "("
      * f4 -> ( ExpressionList() )?
      * f5 -> ")"
      */
     public void visit(MessageSend n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
     }

     /**
      * f0 -> Expression()
      * f1 -> ( ExpressionRest() )*
      */
     public void visit(ExpressionList n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
     }

     /**
      * f0 -> ","
      * f1 -> Expression()
      */
     public void visit(ExpressionRest n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
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
     public void visit(PrimaryExpression n, Scope argu) {
        n.f0.accept(this, argu);
     }

     /**
      * f0 -> <INTEGER_LITERAL>
      */
     public void visit(IntegerLiteral n, Scope argu) {
        n.f0.accept(this, argu);
     }

     /**
      * f0 -> "true"
      */
     public void visit(TrueLiteral n, Scope argu) {
        n.f0.accept(this, argu);
     }

     /**
      * f0 -> "false"
      */
     public void visit(FalseLiteral n, Scope argu) {
        n.f0.accept(this, argu);
     }

     /**
      * f0 -> <IDENTIFIER>
      */
     public void visit(Identifier n, Scope argu) {
        n.f0.accept(this, argu);
     }

     /**
      * f0 -> "this"
      */
     public void visit(ThisExpression n, Scope argu) {
        n.f0.accept(this, argu);
     }

     /**
      * f0 -> "new"
      * f1 -> "int"
      * f2 -> "["
      * f3 -> Expression()
      * f4 -> "]"
      */
     public void visit(ArrayAllocationExpression n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
     }

     /**
      * f0 -> "new"
      * f1 -> Identifier()
      * f2 -> "("
      * f3 -> ")"
      */
     public void visit(AllocationExpression n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
     }

     /**
      * f0 -> "!"
      * f1 -> Expression()
      */
     public void visit(NotExpression n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
     }

     /**
      * f0 -> "("
      * f1 -> Expression()
      * f2 -> ")"
      */
     public void visit(BracketExpression n, Scope argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
     }
}