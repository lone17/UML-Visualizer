package GUI;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

import javax.swing.*;

import com.mindfusion.drawing.*;
import com.mindfusion.diagramming.*;
import structure.*;

/**
 * Class DrawPanel represents a draw panel
 *
 * @author Vu Minh Hieu
 */
public class DrawPanel extends JScrollPane {

	private Diagram diagram; // the diagram shown in this DrawPanel
	private DiagramView diagramView; // the view of the diagram

	// a HashMap contains all the node mapped by its name
	private HashMap<String, ContainerNode> diagramNodes = new HashMap<>();

	/**
	 * DrawPanel Constructor
	 */
	public DrawPanel() {
		super();
	}

	/**
	 * Draw the DrawPanel into the UI
	 *
	 * @param project the Project to be visualized
	 */
	public void draw(Project project) {

		diagram = new Diagram();
		diagramView = new DiagramView(diagram);

		setViewportView(diagramView);

		initContent(project);

		initLinks();

		configDiagram();

		addZoomListener();
		addMagnifyListener();

	}

	/**
	 * Config the Diagram
	 */
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

	/**
	 * Initial the diagram to be drawn
	 *
	 * @param project the Project to be visualized
	 */
	private void initContent(Project project) {
		diagram.clearAll();

		for (SourceFile file : project.getSourceFiles()) {
			for (Extendable object : file.getContainedExtendables()) {

				ContainerNode container = diagram.getFactory().createContainerNode(0, 15, 0, 0);
				TableNode node = diagram.getFactory().createTableNode(0, 15, 0, 0);

				node.redimTable(1, 0);
				node.setLocked(true);
				node.setCellFrameStyle(CellFrameStyle.Simple);
				node.setCaptionHeight(0);
				node.setCaption(null);

				addRow(node, "Attributes:", true);
				for (Attribute attribute : object.getAttributes()) {
					addRow(node, attribute.toString(), false);
				}

				addRow(node, "Methods:", true);
				for (Method method : object.getMethods()) {
					addRow(node, method.toString(), false);
				}

				node.addRow();

				node.setObstacle(true);
				node.resizeToFitText(true);

				container.setId(object.getName());
				container.setAllowAddChildren(false);
				container.setCaption(object.getName());
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
	}

	/**
	 * Initial links between ContainerNodes
	 */
	private void initLinks() {

		DiagramNode parent;
		DiagramNode child;


		for (SourceFile file : App.getProject().getSourceFiles()) {
			for (Extendable object : file.getContainedExtendables()) {
				child = diagramNodes.get(object.getName());

				if (object.getBaseClass() != null) {
					parent = diagramNodes.get(object.getBaseClass());
					if (parent == null) continue;
					addLink(child, parent, true);
				}

				if (object.getBaseInterfaces() != null)
					for (String interfaceName : object.getBaseInterfaces()) {
						parent = diagramNodes.get(interfaceName);
						if (parent == null) continue;
						addLink(child, parent, true);
					}

				parent = child;
				if (object.isClass() && object.hasAttribute()) {
					structure.Class aClass = (structure.Class) object;
					for (String item : aClass.getAssociations()) {
						child = diagram.findNodeById(item);
						if (child == null) continue;
						addLink(child, parent, false);
					}
				}
			}
		}
	}

	/**
	 * Get the diagram shown in the DrawPanel
	 *
	 * @return the contained diagram
	 */
	public Diagram getDiagram() {
		return diagram;
	}

	/**
	 * Set focus on a node
	 *
	 * @param nodeName the name of the node to be focused on
	 */
	public void focusOn(String nodeName) {
		DiagramNode node = diagramNodes.get(nodeName);
		if (node == null) return;
		diagramView.bringIntoView(node);
		node.setSelected(false);
		node.setSelected(true);
	}

	/**
	 * Add a row to a table node
	 *
	 * @param node the TableNode to be added
	 * @param content the content to be shown on the new row
	 * @param isTitle is the new row is a title row
	 */
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

	/**
	 * Add link between two node
	 *
	 * @param child the child node
	 * @param parent the parent node
	 * @param isInheritance if the link represents a inheritance relationship
	 */
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

	/**
	 * Add a listener to listen to zoom event
	 */
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

	/**
	 * Add a listener to
	 */
	private void addMagnifyListener() {
		diagramView.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e){
				return;
			}

			@Override
			public void mousePressed(MouseEvent e){
				if (SwingUtilities.isRightMouseButton(e)) {
					diagramView.setBehavior(Behavior.Magnify);
					diagramView.updateUI();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e){
				if (SwingUtilities.isRightMouseButton(e)) {
					diagramView.setBehavior(Behavior.PanAndModify);
					diagramView.updateUI();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e){
				return;
			}

			@Override
			public void mouseExited(MouseEvent e){
				return;
			}
		});
	}
}