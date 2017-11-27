package GUI;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Dimension2D;
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
		diagram = new Diagram();
		diagramView = new DiagramView(diagram);

		setViewportView(diagramView);

		initContent(project);

		diagram.getNodeEffects().add(new AeroEffect());
		TableNodeStyle tableNodeStyle = diagram.getTableNodeStyle();
		tableNodeStyle.setBrush(new SolidBrush(new Color(5, 97, 127)));

		ContainerNodeStyle containerNodeStyle = diagram.getContainerNodeStyle();
//		containerNodeStyle.setBrush(new SolidBrush(new Color(169, 238, 152)));
		containerNodeStyle.setShadowBrush(new SolidBrush(Color.white));

		diagramView.setBehavior(Behavior.PanAndModify);
		diagramView.setModificationStart(ModificationStart.AutoHandles);

//		diagram.setBackBrush(new SolidBrush(new Color(17, 24, 23)));
		diagram.setSelectAfterCreate(false);
		diagram.setFont(new Font("Arial", Font.PLAIN, 5));
		diagram.setEnableLanes(true);
		diagram.setLinkRouter(new GridRouter());
		diagram.setLinkCrossings(LinkCrossings.Cut);
		diagramView.setZoomFactor(70);
		addZoomListener();
	}

	private HashMap<String, ContainerNode> diagramNodes = new HashMap<>();
	private void initContent(Project project) {
		diagram.clearAll();

		for (SourceFile file : project.getSourceFiles()) {
			Class currentClass = file.getContainedClass();
			ContainerNode container = diagram.getFactory().createContainerNode(0, 15, 0, 0);
			TableNode node = diagram.getFactory().createTableNode(0, 15, 0, 0);

			node.redimTable(1, 0);
			node.setLocked(true);
			node.setCellFrameStyle(CellFrameStyle.Simple);
			node.setCaptionHeight(0);
			node.setCaption(null);

			Cell c = node.getCell(0, node.addRow());
			c.setRowSpan(2);
			c.setTextColor(Color.white);
			c.setFont(new Font("Arial", Font.PLAIN, 4));
			c.setText("Attributes:");
			c.setTextPadding(new Thickness(6, 1, 50, 1));
			c.setTextFormat(new TextFormat(Align.Near, Align.Center));
			node.addRow();

			for (Attribute a : currentClass.getAttributes()) {
				c = node.getCell(0, node.addRow());
				c.setTextPadding(new Thickness(8, 2, 5, 2));
//				c.setBrush(new SolidBrush(new Color(193, 255, 243)));
				c.setBrush(new SolidBrush(Color.white));
				c.setText(a.toString());
				c.setTextFormat(new TextFormat(Align.Near, Align.Center));
			}

			c = node.getCell(0, node.addRow());
			c.setRowSpan(2);
			c.setTextColor(Color.white);
			c.setFont(new Font("Arial", Font.PLAIN, 4));
			c.setText("Methods:");
			c.setTextPadding(new Thickness(6, 1, 50, 1));
			c.setTextFormat(new TextFormat(Align.Near, Align.Center));
			node.addRow();

			for (Method m : currentClass.getMethods()) {
				c = node.getCell(0, node.addRow());
				c.setTextPadding( new Thickness(8, 2, 5, 2));
//				c.setBrush(new SolidBrush(new Color(193, 255, 243)));
				c.setBrush(new SolidBrush(Color.white));
				c.setText(m.toString());
				c.setTextFormat(new TextFormat(Align.Near, Align.Center));
			}

			node.addRow();

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
			container.setObstacle(false);

			diagramNodes.put(container.getCaption(), container);
			diagram.add(container);
		}

		initLinks();

		TreeLayout layout = new TreeLayout();
		layout.setLinkStyle(TreeLayoutLinkType.Straight);
		layout.setLevelDistance(50);
		layout.setNodeDistance(50);
		layout.arrange(diagram);

		diagram.resizeToFitItems(10);
		diagram.setAutoResize(AutoResize.AllDirections);
	}

	private void initLinks() {

		Class currentClass;
		DiagramNode parent;
		DiagramNode child;


		for (SourceFile file : App.getProject().getSourceFiles()) {
			currentClass = file.getContainedClass();
			child = diagramNodes.get(currentClass.getName());

			if (currentClass.getBaseClass() != null) {
				parent = diagramNodes.get(currentClass.getBaseClass());
				if (parent == null) continue;
				DiagramLink link = diagram.getFactory().createDiagramLink(parent, child);
				link.setBaseShape(ArrowHeads.Triangle);
				link.setHeadShape(ArrowHeads.None);
				link.setBaseBrush(new SolidBrush(new Color(168, 255, 199)));
				link.setDynamic(true);
				link.setShadowBrush(new SolidBrush(Color.white));
				link.setLocked(true);
			}

			if (currentClass.getBaseInterfaces() != null)
				for (String interfaceName : currentClass.getBaseInterfaces()) {
					parent = diagramNodes.get(interfaceName);
					if (parent == null) continue;
					DiagramLink link = diagram.getFactory().createDiagramLink(parent, child);
					link.setBaseShape(ArrowHeads.Triangle);
					link.setHeadShape(ArrowHeads.None);
					link.setBaseBrush(new SolidBrush(new Color(168, 255, 199)));
					link.setDynamic(true);
					link.setShadowBrush(new SolidBrush((Color.white)));
					link.setLocked(true);
				}

			parent = child;
			if (currentClass.hasAttribute())
				for (Attribute att : currentClass.getAttributes()) {
					child = diagram.findNodeById(att.getType());
					if (child == null) continue;
					DiagramLink link = diagram.getFactory().createDiagramLink(parent, child);
					link.setBaseShape(ArrowHeads.Rhombus);
					link.setHeadShape(ArrowHeads.None);
					link.setBaseBrush(new SolidBrush(new Color(202, 199, 255)));
					link.setDynamic(true);
					link.setShadowBrush(new SolidBrush(Color.white));
					link.setLocked(true);
					link.setRetainForm(true);
				}
		}
	}

	public Diagram getDiagram() {
		return diagram;
	}

	private void addZoomListener() {
		diagramView.addMouseWheelListener(new MouseWheelListener(){
			@Override
			public void mouseWheelMoved(MouseWheelEvent e){
				int notches = e.getWheelRotation();
				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
					float zoomFactor = diagramView.getZoomFactor();
					if (zoomFactor <= 20 && notches > 0) return;
					diagramView.setZoomFactor(zoomFactor - notches);
				}
			}
		});
	}

	private Diagram diagram;
	private DiagramView diagramView;
}