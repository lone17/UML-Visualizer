package GUI;

import GUI.tree.*;
import structure.Project;

import javax.swing.*;

/**
 * Class App represents the UML-Visualizer itself
 *
 * @author Vu Minh Hieu
 */
public class App {

    private static Project project; // the project to be visualized
    private static MainWindow mainWindow; // the main window where everything is shown
    private static MenuBar menuBar; // the application's menu bar
    private static TreePanel treePanel; // where the project's tree is shown
    private static DrawPanel drawPanel; // where the project's diagram is drawn
    private static EventHistoryPanel eventHistoryPanel; // event history

    /**
     * Return the main window of the Application
     *
     * @return the main window
     */
    public static MainWindow getMainWindow() {
        return mainWindow;
    }

    /**
     * Return the application's tree panel
     *
     * @return the tree panel
     */
    public static TreePanel getTreePanel() {
        return treePanel;
    }

    /**
     * Return the application's draw panel
     *
     * @return the draw panel
     */
    public static DrawPanel getDrawPanel() {
        return drawPanel;
    }

    /**
     * Return the EventHistoryPanel where event histories are shown
     *
     * @return the application's event history panel
     */
    public static EventHistoryPanel getEventHistoryPanel() {
        return eventHistoryPanel;
    }

    /**
     * Return the application's project
     *
     * @return the application's project
     */
    public static Project getProject() {
        return project;
    }

    /**
     * Set the application's project
     *
     * @param project the desired project
     */
    public static void setProject(Project project) {
        App.project = project;
    }

    /**
     * Run the application
     */
    public static void run() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        mainWindow = MainWindow.getInstance();
        menuBar = MenuBar.getInstance();
        eventHistoryPanel = EventHistoryPanel.getInstance();
        treePanel = new TreePanel();
        drawPanel = new DrawPanel();

        mainWindow.setTop(menuBar);
        mainWindow.setLeft(treePanel);
        mainWindow.setRight(drawPanel);
        mainWindow.setBottom(eventHistoryPanel);
    }

    /**
     * Execute everything
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                App.run();
            }
        });
    }

}
