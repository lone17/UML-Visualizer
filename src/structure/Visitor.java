package structure;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.PrettyPrinter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;

import java.util.List;

public class Visitor {
	private class A {
		A() {}
		void foo(){};
	}
	public static class ExtendableVisitor extends VoidVisitorAdapter<List<Extendable>> {
		@Override
		public void visit(ClassOrInterfaceDeclaration declaration, List<Extendable> list) {
			super.visit(declaration, list);
			Extendable object;
			if (declaration.isInterface())
				object = new Interface(declaration);
			else
				object = new Class(declaration);

//			(new ConstructorVisitor()).visit(declaration, object);
//			(new MethodVisitor()).visit(declaration, object);
//			(new AttributeVisitor()).visit(declaration, object);

			Visitor.getAllMethodsAndAttributes(declaration, object);

			list.add(object);
		}
	}

	private static void getAllMethodsAndAttributes(ClassOrInterfaceDeclaration declaration, Extendable object) {
		for (ConstructorDeclaration item : declaration.getConstructors())
			object.addMethod(new Method(item.getDeclarationAsString(true, false, true)));

		for (Node node : declaration.getChildNodes())
			if (node instanceof FieldDeclaration) {
				FieldDeclaration attribute = (FieldDeclaration) node;

				PrettyPrinterConfiguration conf = new PrettyPrinterConfiguration();
				conf.setPrintComments(false);

				PrettyPrinter pp = new PrettyPrinter(conf);

				object.addAllAttributes(Attribute.generateInstances(pp.print(attribute)));
			}
			else if (node instanceof MethodDeclaration) {
				MethodDeclaration method = (MethodDeclaration) node;
				object.addMethod(new Method(method.getDeclarationAsString(true, false, true)));
			}
	}

	public static class ConstructorVisitor extends VoidVisitorAdapter<Extendable> {
		@Override
		public void visit(ConstructorDeclaration declaration, Extendable object) {
			object.addMethod(new Method(declaration.getDeclarationAsString(true, false, true)));
		}
	}

	public static class MethodVisitor extends VoidVisitorAdapter<Extendable> {
		@Override
		public void visit(MethodDeclaration declaration, Extendable object) {
			object.addMethod(new Method(declaration.getDeclarationAsString(true, false, true)));
		}
	}

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
