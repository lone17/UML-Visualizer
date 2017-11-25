package GUI;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
		diagView.setModificationStart(ModificationStart.AutoHandles);

		diag.setSelectAfterCreate(false);
		//		diag.setLinkCascadeOrientation(Orientation.Auto);
		//		diag.setLinkShape(LinkShape.Polyline);
		diag.setFont(new Font("Arial", Font.PLAIN, 5));
		diag.setEnableLanes(true);
		diag.setLinkRouter(new GridRouter());
		diag.setLinkCrossings(LinkCrossings.Cut);
		//		diag.setDynamicLinks(true);
		//		diag.setBackBrush(new SolidBrush(Color.white));
		diagView.setZoomFactor(85);
	}

	private HashMap<String, ContainerNode> diagramNodes = new HashMap<>();
	private void initContent(Project project) {
		diag.clearAll();

		for (SourceFile file : project.getSourceFiles()) {
			Class currentClass = file.getContainedClass();
			ContainerNode container = diag.getFactory().createContainerNode(0, 0, 0, 0);
			TableNode node = diag.getFactory().createTableNode(0, 0, 0, 0);
//			TableNode node = new TableNode();
//			diag.add(node);
//			node.setBounds(10, 10, 30, 10);

			node.redimTable(1, 0);
			node.setLocked(true);
			node.setCellFrameStyle(CellFrameStyle.Simple);
//			node.setCaptionBrush(new SolidBrush(Color.white));
//			node.setCaptionFormat(new TextFormat(Align.Center, Align.Center));
			node.setCaptionHeight(0);
			node.setCaption(null);

			Cell c = node.getCell(0, node.addRow());
			c.setRowSpan(2);
//			c.setBrush(new SolidBrush(Color.white));
			c.setTextColor(Color.white);
			c.setFont(new Font("Arial", Font.PLAIN, 4));
			c.setText("        Attributes:");
			c.setTextFormat(new TextFormat(Align.Near, Align.Center));
			node.addRow();

			for (Attribute a : currentClass.getAttributes()) {
				c = node.getCell(0, node.addRow());
				c.setTextPadding(new Thickness(8, 2, 5, 2));
				c.setBrush(new SolidBrush(Color.white));
				c.setText(a.toString());
				c.setTextFormat(new TextFormat(Align.Near, Align.Center));
			}

//			node.addRow();
//			if (currentClass.hasAttribute()) node.addRow();

			c = node.getCell(0, node.addRow());
			c.setRowSpan(2);
//			c.setBrush(new SolidBrush(Color.white));
			c.setTextColor(Color.white);
			c.setFont(new Font("Arial", Font.PLAIN, 4));
			c.setText("        Methods:");
			c.setTextFormat(new TextFormat(Align.Near, Align.Center));
			node.addRow();

			for (Method m : currentClass.getMethods()) {
				c = node.getCell(0, node.addRow());
				c.setTextPadding( new Thickness(8, 2, 5, 2));
				c.setBrush(new SolidBrush(Color.white));
				c.setText(m.toString());
				c.setTextFormat(new TextFormat(Align.Near, Align.Center));
			}

			node.addRow();

//			node.setEnabledHandles(AdjustmentHandles.Move);
			node.setObstacle(true);
			node.resizeToFitText(true);


			container.setId(file.getContainedClass().getName());
			container.setAllowAddChildren(false);
			container.setCaption(file.getContainedClass().getName());
			container.setCaptionHeight(10);
			container.setFont(new Font("Arial", Font.BOLD, 5));
			container.setMargin(0);
			container.add(node);
			container.updateBounds(true);
			container.setObstacle(true);

			diagramNodes.put(container.getCaption(), container);
			diag.add(container);
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
//		LinkedList<SourceFile> files = App.getProject().getSourceFiles();

		Class currentClass;
		DiagramNode parent;
		DiagramNode child;


		for (SourceFile file : App.getProject().getSourceFiles()) {
			currentClass = file.getContainedClass();
			child = diagramNodes.get(currentClass.getName());
//			System.out.println(currentClass);
//			System.out.println("************************");

			if (currentClass.getBaseClass() != null) {
				parent = diagramNodes.get(currentClass.getBaseClass());
				if (parent == null) continue;
				DiagramLink link = diag.getFactory().createDiagramLink(parent, child);
				link.setBaseShape(ArrowHeads.Triangle);
				link.setHeadShape(ArrowHeads.None);
				link.setDynamic(true);
//				link.setAutoRoute(true);
				link.setShadowBrush(new SolidBrush(Color.white));
				link.setLocked(true);
			}

			if (currentClass.getBaseInterfaces() != null)
				for (String interfaceName : currentClass.getBaseInterfaces()) {
					parent = diagramNodes.get(interfaceName);
					if (parent == null) continue;
					DiagramLink link = diag.getFactory().createDiagramLink(parent, child);
					link.setBaseShape(ArrowHeads.Triangle);
					link.setHeadShape(ArrowHeads.None);
					link.setDynamic(true);
//					link.setAutoRoute(true);
					link.setShadowBrush(new SolidBrush(Color.white));
					link.setLocked(true);
				}

			parent = child;
			if (currentClass.hasAttribute())
				for (Attribute att : currentClass.getAttributes()) {
					child = diag.findNodeById(att.getType());
					if (child == null) continue;
					DiagramLink link = diag.getFactory().createDiagramLink(parent, child);
					link.setBaseShape(ArrowHeads.Rhombus);
					link.setHeadShape(ArrowHeads.None);
					link.setDynamic(true);
//					link.setAutoRoute(true);
					link.setShadowBrush(new SolidBrush(Color.white));
					link.setLocked(true);
					link.setRetainForm(true);
				}
		}
	}

	public Diagram diag;
	private DiagramView diagView;
}