package GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
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

import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.mindfusion.drawing.*;
import com.mindfusion.diagramming.*;


public class TestDraw extends JFrame
{
	public TestDraw(){
		super("MindFusion.Diagramming sample: XML Tree");
		initFrame();
		initDiagram();
	}

	private void initDiagram()
	{
		diag.getNodeEffects().add(new GlassEffect());
		diag.getShapeNodeStyle().setBrush(
				new SolidBrush(new Color(180, 225, 225)));
		diag.setBackBrush(new SolidBrush(Color.white));

		diagView.setBehavior(Behavior.Modify);
		diag.setSelectAfterCreate(false);
		diag.setLinkSegments((short)2);
		diag.setLinkCascadeOrientation(Orientation.Vertical);
		diag.setLinkShape(LinkShape.Polyline);
		diag.setFont(new Font("Arial", Font.PLAIN, 5));
	}

	private void open()
	{
		JFileChooser chooser = new JFileChooser(".");

		FileFilter filter = new XmlFileFilter();

		chooser.setFileFilter(filter);

		if (chooser.showOpenDialog(TestDraw.this) == JFileChooser.APPROVE_OPTION)
		{
			File file = new File(chooser.getSelectedFile().getAbsolutePath());
			loadXml(file);
		}
	}

	Document document;
	private void loadXml(File file)
	{
		diag.clearAll();

		try
		{
			DocumentBuilder builder = createBuilder();
			document = builder.parse(file);

		}
		catch (IOException ex)
		{
			return;
		}
		catch (SAXException ex)
		{
			return;
		}

		Element root = document.getDocumentElement();
		// Traverse the XML elements in the XML document
		if ( root != null)
			createXMLSubTree(null, root);
		createXMLSubTree(null, root);

		TreeLayout layout = new TreeLayout();

		layout.setLinkStyle(TreeLayoutLinkType.Cascading3);
		layout.setLevelDistance(25);
		layout.setNodeDistance(5);

		layout.arrange(diag);

		diag.resizeToFitItems(10);
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
		if (element.hasChildNodes())
			box.setText(element.getNodeName() + "\nABC");
		else
			box.setText(element.getTextContent());

		if (parent != null)
			diag.getFactory().createDiagramLink(parent, box);

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

	class XmlFileFilter extends javax.swing.filechooser.FileFilter
	{

		public String getDescription()
		{
			return ".xml files";
		}

		@Override
		public boolean accept(File f)
		{
			return f.isDirectory() || f.getName().toLowerCase().endsWith(".xml");
		}
	}

	private void initFrame()
	{
		diag = new Diagram();
		diagView = new DiagramView(diag);
		toolBar = new JToolBar();
		btnLoad = new JButton();
		scrollPane = new JScrollPane(diagView);

		//
		//toolBar
		//
		toolBar.add(btnLoad);

		//
		//btnLoad
		//
		btnLoad.setText("Load");
		btnLoad.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				open();
			}
		});

		Container cp = this.getContentPane();
		cp.setLayout(null);
		cp.add(toolBar);
		cp.add(scrollPane);

		SpringLayout sp = new SpringLayout();

		sp.putConstraint(SpringLayout.EAST, cp, 0, SpringLayout.EAST, toolBar);
		sp.putConstraint(SpringLayout.WEST, cp, 0, SpringLayout.WEST , toolBar);
		sp.putConstraint(SpringLayout.NORTH, toolBar, 0, SpringLayout.NORTH, cp);

		sp.putConstraint(SpringLayout.WEST, cp, 0, SpringLayout.WEST , scrollPane);
		sp.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.SOUTH, toolBar);
		sp.putConstraint(SpringLayout.EAST, cp, 0, SpringLayout.EAST, scrollPane);
		sp.putConstraint(SpringLayout.SOUTH, cp, 0, SpringLayout.SOUTH, scrollPane);

		cp.setLayout(sp);

		// Add window listener.
		this.addWindowListener
				     (
						     new WindowAdapter()
						     {
							     public void windowClosing(WindowEvent e)
							     {
								     // Exit application.
								     System.exit(0);
							     }
						     }
				     );

		this.setSize(500,500);
	}

	private JButton btnLoad;
	private JToolBar toolBar;
	private JScrollPane scrollPane;
	private Diagram diag;
	private DiagramView diagView;

	private static final long serialVersionUID = 1L;

	public static void main(String[] args){
		TestDraw test = new TestDraw();
		test.setVisible(true);
	}
}
