package GUI;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JScrollPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.mindfusion.drawing.*;
import com.mindfusion.diagramming.*;
import parser.*;
import parser.Class;


public class DrawPanel extends JScrollPane {
	public DrawPanel() {
		super();
	}


	public void draw(Project project) {
		diag = new Diagram();
		diagView = new DiagramView(diag);

		setViewportView(diagView);

		initContent(project);

		//		diag.getNodeEffects().add(new GlassEffect());
		TableNodeStyle nodeStyle = diag.getTableNodeStyle();
		nodeStyle.setBrush(new SolidBrush(new Color(101, 100, 99)));
		nodeStyle.setShadowBrush(new SolidBrush(Color.white));

		diagView.setBehavior(Behavior.PanAndModify);
		diag.setSelectAfterCreate(false);
//		diag.setLinkCascadeOrientation(Orientation.Auto);
//		diag.setLinkShape(LinkShape.Polyline);
		diag.setFont(new Font("Arial", Font.PLAIN, 5));
		diag.setEnableLanes(true);
		diag.setLinkRouter(new GridRouter());
		diag.setLinkCrossings(LinkCrossings.Cut);
//		diag.setDynamicLinks(true);
//		diag.setBackBrush(new SolidBrush(Color.white));
	}

	private void initContent(Project project) {
		diag.clearAll();

		for (SourceFile file : project.getSourceFiles()) {
			Class currentClass = file.getContainedClass();
			TableNode node = diag.getFactory().createTableNode(10, 10, 30, 10);
//			TableNode node = new TableNode();
//			diag.add(node);
//			node.setBounds(10, 10, 30, 10);
			node.setId(file.getContainedClass().getName());
			node.redimTable(1, 0);
			node.setCellFrameStyle(CellFrameStyle.Simple);

			node.setCaptionBrush(new SolidBrush(Color.white));
			node.setCaption(file.getContainedClass().getName());
			node.setCaptionFormat(new TextFormat(Align.Center, Align.Center));

			Cell c = node.getCell(0, node.addRow());
			c.setRowSpan(2);
			c.setBrush(new SolidBrush(Color.white));
			c.setText("Attributes:");
			c.setTextFormat(new TextFormat(Align.Center, Align.Center));

			for (Attribute a : currentClass.getAttributes()) {
				c = node.getCell(0, node.addRow());
				c.setBrush(new SolidBrush(Color.white));
				c.setText("    " + a.toString());
				c.setTextFormat(new TextFormat(Align.Near, Align.Center));
			}

			node.addRow();
			if (!currentClass.hasAttribute()) node.addRow();

			c = node.getCell(0, node.addRow());
			c.setRowSpan(2);
			c.setBrush(new SolidBrush(Color.white));
			c.setText("Methods:");
			c.setTextFormat(new TextFormat(Align.Center, Align.Center));

			for (Method m : currentClass.getMethods()) {
				c = node.getCell(0, node.addRow());
				c.setBrush(new SolidBrush(Color.white));
				c.setText("    " + m.toString());
				c.setTextFormat(new TextFormat(Align.Near, Align.Center));
			}

			node.addRow();
			if (!currentClass.hasMethod()) node.addRow();

			node.setEnabledHandles(AdjustmentHandles.Move);
			node.setObstacle(true);
			node.resizeToFitText(false);
		}

		initLinks();

		TreeLayout layout = new TreeLayout();
		layout.setLinkStyle(TreeLayoutLinkType.Straight);
		layout.setLevelDistance(25);
		layout.setNodeDistance(20);
		layout.arrange(diag);

		diag.resizeToFitItems(10);
		diag.setAutoResize(AutoResize.AllDirections);
	}

	private void initLinks() {
//		for (DiagramNode n : diag.getNodes())
//			System.out.println(((TableNode) n).getId());
		DiagramNodeList nodes = diag.getNodes();
		LinkedList<SourceFile> files = App.getProject().getSourceFiles();

		Class currentClass;
		DiagramNode parent;


		for (int i = 0, len = nodes.size(); i < len; ++i) {
			currentClass = files.get(i).getContainedClass();
//			System.out.println(currentClass);
//			System.out.println("************************");

			if (currentClass.getBaseClass() != null) {
				parent = diag.findNodeById((Object) currentClass.getBaseClass());
				if (parent == null) continue;
				DiagramLink link = diag.getFactory().createDiagramLink(parent, nodes.get(i));
				link.setBaseShape(ArrowHeads.Triangle);
				link.setHeadShape(ArrowHeads.None);
				link.setDynamic(true);
				link.setAutoRoute(true);
				link.setShadowBrush(new SolidBrush(Color.white));
			}

			if (currentClass.getBaseInterfaces() != null)
				for (String interfacesName : currentClass.getBaseInterfaces()) {
					parent = diag.findNodeById(interfacesName);
					if (parent == null) continue;
					DiagramLink link = diag.getFactory().createDiagramLink(parent, nodes.get(i));
					link.setBaseShape(ArrowHeads.Triangle);
					link.setHeadShape(ArrowHeads.None);
					link.setDynamic(true);
					link.setDynamic(true);
					link.setShadowBrush(new SolidBrush(Color.white));
				}

			if (currentClass.hasAttribute())
				for (Attribute att : currentClass.getAttributes()) {
					parent = diag.findNodeById(att.getType());
					if (parent == null) continue;
					DiagramLink link = diag.getFactory().createDiagramLink(nodes.get(i), parent);
					link.setBaseShape(ArrowHeads.Rhombus);
					link.setHeadShape(ArrowHeads.None);
					link.setDynamic(true);
					link.setDynamic(true);
					link.setShadowBrush(new SolidBrush(Color.white));
				}
		}
	}

	private Document document;
	private void loadXml(File file) {
		diag.clearAll();

//		try {
//			DocumentBuilder builder = createBuilder();
//			document = builder.parse(file);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.exit(1);
//		} catch (SAXException e) {
//			e.printStackTrace();
//			System.exit(1);
//		}


//		Element root = document.getDocumentElement();

		for (SourceFile f : new Project(".").getSourceFiles()) {
			ShapeNode node = diag.getFactory().createShapeNode(10, 10, 25, 10);
			node.setObstacle(true);
			node.setText(f.getContainedClass().getName());
//			System.out.println(f.getName());
		}


		// Traverse the XML elements in the XML document
//		if ( root != null)
//			createXMLSubTree(null, root);

		TreeLayout layout = new TreeLayout();

		layout.setLinkStyle(TreeLayoutLinkType.Straight);
		layout.setLevelDistance(25);
		layout.setNodeDistance(5);

		layout.arrange(diag);

		diag.resizeToFitItems(10);
		diag.setAutoResize(AutoResize.AllDirections);
	}

	// A recursive routine which creates
	// a sub-tree of the XML tree
	private void createXMLSubTree(TableNode parent, Element element)
	{
		TableNode box;
		Element xmlChild;

		if (element == null)
			return;

		//	        box = doc.Factory.CreateShapeNode(xIndent, _yIndent, 30, 5)
		box = diag.getFactory().createTableNode(10, 10, 35, 10);
		box.setObstacle(true);
		if (element.hasChildNodes())
			box.setCaption(element.getNodeName() + "\nABC");
		else
			box.setCaption(element.getTextContent());

			DiagramLink link;
		if (parent != null) {
			link = diag.getFactory().createDiagramLink(parent, box);
//			link.setAutoRoute(true);
			link.setBaseShape(ArrowHeads.Triangle);
			link.setHeadShape(ArrowHeads.None);
//			link.setDynamic(true);
			link.setShadowBrush(new SolidBrush(Color.white));
		}

		//Traverse the XML elements in the XML document
		for ( int i = 0; i < element.getChildNodes().getLength(); i++)
		{
			//xmlChild = (Element) element.getChildNodes().item(i);
			org.w3c.dom.Node node = element.getChildNodes().item(i);

			if (node.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE)
				continue;

			xmlChild = (Element)node;

			createXMLSubTree(box, xmlChild);
		}
	}

	private DocumentBuilder createBuilder()
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(false);
			return factory.newDocumentBuilder();
		}
		catch (ParserConfigurationException ex)
		{
			System.out.println(ex.getMessage());
			return null;
		}
	}


	private Diagram diag;
	private DiagramView diagView;
}