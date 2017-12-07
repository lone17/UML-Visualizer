package structure;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.PrettyPrinter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;

import java.util.List;

/**
 * Class Visitor used to traverse the file's structure tree
 *
 * @author Vu Minh Hieu
 */
public class Visitor {

    /**
     * Get all methods and attributes in a ClassOrInterfaceDeclaration node
     *
     * @param declaration the ClassOrInterfaceDeclaration to be traversed
     * @param object      the Extendable corresponding with the declaration
     */
    private static void getAllMethodsAndAttributes(ClassOrInterfaceDeclaration declaration, Extendable object) {
        for (ConstructorDeclaration item : declaration.getConstructors())
            object.addMethod(new Method(item.getDeclarationAsString(true, false, true)));

        for (Node node : declaration.getChildNodes())
            if (node instanceof FieldDeclaration) {
                FieldDeclaration attribute = (FieldDeclaration) node;

                String tmp = "";
                if (attribute.isPublic()) tmp += "public ";
                else if (attribute.isPrivate()) tmp += "private ";
                else if (attribute.isProtected()) tmp += "protected ";
                if (attribute.isStatic()) tmp += "static ";
                if (attribute.isFinal()) tmp += "final ";

                for (VariableDeclarator item : attribute.getVariables()) {
                    object.addAttribute(new Attribute(tmp + item.getType().toString().replace(" ", "") + " " + item.getName().toString()));
                }

            } else if (node instanceof MethodDeclaration) {
                MethodDeclaration method = (MethodDeclaration) node;
                object.addMethod(new Method(method.getDeclarationAsString(true, false, true)));
            }
    }

    public static class ExtendableVisitor extends VoidVisitorAdapter<List<Extendable>> {
        @Override
        public void visit(ClassOrInterfaceDeclaration declaration, List<Extendable> list) {
            super.visit(declaration, list);
            Extendable object;
            if (declaration.isInterface()) object = new Interface(declaration);
            else object = new Class(declaration);

            Visitor.getAllMethodsAndAttributes(declaration, object);

            list.add(object);
        }
    }

    /**
     * Class ConstructorVisitor to visit all constructor
     */
    public static class ConstructorVisitor extends VoidVisitorAdapter<Extendable> {
        @Override
        public void visit(ConstructorDeclaration declaration, Extendable object) {
            object.addMethod(new Method(declaration.getDeclarationAsString(true, false, true)));
        }
    }


    /**
     * Class MethodVisitor to visit all methods
     */
    public static class MethodVisitor extends VoidVisitorAdapter<Extendable> {
        @Override
        public void visit(MethodDeclaration declaration, Extendable object) {
            object.addMethod(new Method(declaration.getDeclarationAsString(true, false, true)));
        }
    }

    /**
     * Class AttributeVisitor to visit all attributes
     */
    public static class AttributeVisitor extends VoidVisitorAdapter<Extendable> {
        @Override
        public void visit(FieldDeclaration declaration, Extendable object) {
            PrettyPrinterConfiguration conf = new PrettyPrinterConfiguration();
            conf.setPrintComments(false);
            PrettyPrinter pp = new PrettyPrinter(conf);

            object.addAllAttributes(Attribute.generateInstances(pp.print(declaration)));
        }
    }
}
