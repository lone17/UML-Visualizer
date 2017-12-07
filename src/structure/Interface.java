package structure;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.LinkedList;

/**
 * Class Interface represents an interface
 *
 * @author Vu Minh Hieu
 */
public class Interface extends Extendable {

    public Interface(ClassOrInterfaceDeclaration declaration) {
        baseInterfaces = new LinkedList<>();
        methods = new LinkedList<>();
        attributes = new LinkedList<>();

        isStatic = declaration.isStatic();
        isAbstract = declaration.isAbstract();
        isFinal = declaration.isFinal();

        type = "class";
        name = declaration.getNameAsString();

        if (declaration.isPublic()) accessModifier = "public";
        else if (declaration.isProtected()) accessModifier = "protected";
        else if (declaration.isPrivate()) accessModifier = "private";

        for (ClassOrInterfaceType item : declaration.getExtendedTypes())
            baseInterfaces.add(item.getName().toString());

        for (ClassOrInterfaceType item : declaration.getImplementedTypes())
            baseInterfaces.add(item.getName().toString());

    }

    /**
     * Local testing
     */
    public static void main(String[] args) {

    }

    /**
     * @return a string representation of Interface for printing
     */
    @Override
    public String toString() {

        String s = "";
        if (!accessModifier.equals("default")) s += accessModifier + " ";

        s += type + " " + name + "\n";

        for (String item : baseInterfaces)
            s += name + " ---> " + item + "\n";


        for (Attribute item : attributes)
            s += item.toString() + "\n";

        for (Method item : methods)
            s += item.toString() + "\n";

        return s;
    }
}
