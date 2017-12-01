package GUI.tree;

/**
 * Class ComponentDetail represents an object that makes up a node
 *
 * @author Nguyen Xuan Tung
 */
public class ComponentDetail{
	// Node's name
	private String name;
	// Node's icon path
	private String icon;

	/**
	 * ComponentDetail constructor
	 *
	 * @param name Node's name
	 * @param icon Node's icon path
	 */
	public ComponentDetail(String name, String icon){
		this.name = name;
		this.icon = icon;
	}

	/**
	 * Get node's name
	 *
	 * @return name
	 */
	public String getName(){
		return name;
	}

	/**
	 * Set node's name
	 *
	 * @param name String
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * Get icon path
	 *
	 * @return icon path
	 */
	public String getIcon(){
		return icon;
	}

	/**
	 * Set icon path
	 *
	 * @param icon path
	 */
	public void setIcon(String icon){
		this.icon = icon;
	}
}
