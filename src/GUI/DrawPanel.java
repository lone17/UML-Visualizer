package GUI;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JScrollPane;

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

		initLinks();

		configDiagram();

		addZoomListener();
	}

	private void configDiagram() {

		TreeLayout layout = new TreeLayout();
		layout.setLinkStyle(TreeLayoutLinkType.Straight);
		layout.setLevelDistance(50);
		layout.setNodeDistance(50);
		layout.arrange(diagram);

		diagram.resizeToFitItems(10);
		diagram.setAutoResize(AutoResize.AllDirections);
		diagram.getNodeEffects().add(new AeroEffect());

		ContainerNodeStyle containerNodeStyle = diagram.getContainerNodeStyle();
		containerNodeStyle.setBrush(new SolidBrush(new Color(130, 201, 226)));
		containerNodeStyle.setShadowBrush(new SolidBrush(Color.white));
		containerNodeStyle.setTextBrush(new SolidBrush(new Color(45, 45, 45)));

		TableNodeStyle tableNodeStyle = diagram.getTableNodeStyle();
		tableNodeStyle.setBrush(new SolidBrush(new Color(38, 28, 27)));
//		tableNodeStyle.setTextBrush(new SolidBrush(Color.white));

		diagramView.setBehavior(Behavior.PanAndModify);
		diagramView.setModificationStart(ModificationStart.AutoHandles);

//		diagram.setBackBrush(new SolidBrush(new Color(17, 24, 23)));
		diagram.setSelectAfterCreate(false);
		diagram.setFont(new Font("Arial", Font.PLAIN, 5));
		diagram.setEnableLanes(true);
		diagram.setLinkRouter(new GridRouter());
		diagram.setLinkCrossings(LinkCrossings.Arcs);
		diagramView.setZoomFactor(70);
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

			addRow(node, "Attributes:", true);
			for (Attribute attribute : currentClass.getAttributes()) {
				addRow(node, attribute.toString(), false);
			}

			addRow(node, "Methods:", true);
			for (Method method : currentClass.getMethods()) {
				addRow(node, method.toString(), false);
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
	}

	private void addRow(TableNode node, String content, boolean isTitle) {
		Cell cell = node.getCell(0, node.addRow());
		if (isTitle) {
			cell.setRowSpan(2);
			cell.setTextColor(Color.white);
			cell.setFont(new Font("Arial", Font.BOLD, 4));
			cell.setText(content);
			cell.setTextPadding(new Thickness(6, 2, 50, 2));
			cell.setTextFormat(new TextFormat(Align.Near, Align.Center));
			node.addRow();
		}
		else {
			cell.setTextPadding(new Thickness(8, 2, 5, 2));
//			cell.setBrush(new SolidBrush(new Color(193, 255, 243)));
			cell.setBrush(new SolidBrush(Color.white));
			cell.setText(content);
			cell.setTextFormat(new TextFormat(Align.Near, Align.Center));
		}
	}

	private void initLinks() {

		Class currentClass;
		DiagramNode parent;
		DiagramNode child;


		for (SourceFile file : App.getProject().getSourceFiles()) {
			currentClass = file.getContainedClass();
			child = diagramNodes.get(currentClass.getName());

			if (currentClass.getBaseClass() != null) {
				parent = (ContainerNode) diagramNodes.get(currentClass.getBaseClass());
				if (parent == null) continue;
				addLink(child, parent, true);
			}

			if (currentClass.getBaseInterfaces() != null)
				for (String interfaceName : currentClass.getBaseInterfaces()) {
					parent = diagramNodes.get(interfaceName);
					if (parent == null) continue;
					addLink(child, parent, true);
				}

			parent = child;
			if (currentClass.hasAttribute())
				for (Attribute att : currentClass.getAttributes()) {
					child = diagram.findNodeById(att.getType());
					if (child == null) continue;
					addLink(child, parent, false);
				}
		}
	}

	private void addLink(DiagramNode child, DiagramNode parent, boolean isInheritance) {
		DiagramLink link = diagram.getFactory().createDiagramLink(parent, child);
		if (isInheritance) {
			link.setBaseShape(ArrowHeads.Triangle);
			link.setHeadShape(ArrowHeads.None);
			link.setBaseBrush(new SolidBrush(new Color(132, 238, 125)));
		}
		else {
			link.setBaseShape(ArrowHeads.Rhombus);
			link.setHeadShape(ArrowHeads.None);
			link.setBaseBrush(new SolidBrush(new Color(255, 208, 149)));
		}
		link.setDynamic(true);
		link.setShadowBrush(new SolidBrush(Color.white));
		link.setLocked(true);
	}

	public Diagram getDiagram() {
		return diagram;
	}

	public void focusOn(String nodeName) {
		DiagramNode node = diagramNodes.get(nodeName);
		if (node == null) return;
		diagramView.bringIntoView(node);
		node.setSelected(false);
		node.setSelected(true);
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