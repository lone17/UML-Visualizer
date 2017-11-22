package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.mindfusion.diagramming.Shape;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.mindfusion.drawing.*;
import com.mindfusion.diagramming.*;


public class DrawPanel extends JScrollPane
{
	public DrawPanel()
	{
		super();
		init();
	}

	private void init(){
		diag = new Diagram();
		diagView = new DiagramView(diag);

		setViewportView(diagView);
		//		setPreferredSize(new Dimension(400, 600));

		File file = new File("D:\\Downloads\\JPack\\samples\\Diagramming.XmlTree\\People.xml");
		loadXml(file);

		//		diag.getNodeEffects().add(new GlassEffect());
		diag.getShapeNodeStyle().setBrush(new SolidBrush(new Color(180, 225, 225)));
//				diag.setBackBrush(new SolidBrush(Color.white));

		diagView.setBehavior(Behavior.PanAndModify);
		diag.setSelectAfterCreate(false);
//				diag.setLinkCascadeOrientation(Orientation.Horizontal);
//				diag.setLinkShape(LinkShape.Polyline);
		diag.setFont(new Font("Arial", Font.PLAIN, 5));
		diag.setEnableLanes(true);
		diag.setLinkRouter(new QuickRouter(diag));
		diag.setLinkCrossings(LinkCrossings.Straight);
	}

	private Document document;
	private void loadXml(File file) {
		diag.clearAll();

		try {
			DocumentBuilder builder = createBuilder();
			document = builder.parse(file);

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (SAXException e) {
			e.printStackTrace();
			System.exit(1);
		}


		Element root = document.getDocumentElement();
		// Traverse the XML elements in the XML document
		if ( root != null)
			createXMLSubTree(null, root);
//		createXMLSubTree(null, root);

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
	private void createXMLSubTree(ShapeNode parent, Element element)
	{
		ShapeNode box;
		Element xmlChild;

		if (element == null)
			return;

		//	        box = doc.Factory.CreateShapeNode(xIndent, _yIndent, 30, 5)
		box = diag.getFactory().createShapeNode(10, 10, 35, 10);
		box.setObstacle(true);
		if (element.hasChildNodes())
			box.setText(element.getNodeName() + "\nABC");
		else
			box.setText(element.getTextContent());

			DiagramLink link;
		if (parent != null) {
			link = diag.getFactory().createDiagramLink(parent, box);
//			link.setAutoRoute(true);
			link.setBaseShape(ArrowHeads.Arrow);
			link.setHeadShape(ArrowHeads.None);
			link.setDynamic(true);
			link.setShadowBrush(new SolidBrush(Color.white));
		}

		//Traverse the XML elements in the XML document
		for ( int i = 0; i < element.getChildNodes().getLength(); i++)
		{
			//xmlChild = (Element) element.getChildNodes().item(i);
			org.w3c.dom.Node node = (org.w3c.dom.Node)element.getChildNodes().item(i);

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