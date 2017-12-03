package GUI;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Class EventHistoryPanel represents the area where all event histories are shown
 *
 * @author Vu Minh Hieu
 * @author Nguyen Xuan Tung
 */
public class EventHistoryPanel extends JScrollPane {

    // the single EventHistoryPanel instance
    private static EventHistoryPanel historyPanel = new EventHistoryPanel();

    private JTextArea textArea; // the area where event information are written

    /**
     * Private EventHistoryPanel constructor
     */
    private EventHistoryPanel() {
        super();

        textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        textArea.setForeground(new Color(66, 66, 66));
        textArea.setEditable(false);

        setMinimumSize(new Dimension(GUI.App.getMainWindow().getWidth(), 40));
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        setViewportView(textArea);
    }

    /**
     * append text to the text area
     *
     * @param content the text content to be added
     */
    public void append(String content) {
        textArea.append(content);
    }

    /**
     * Get the single EventHistoryPanel instance
     *
     * @return the single EventHistoryPanel instance
     */
    public static EventHistoryPanel getInstance() {
        return historyPanel;
    }
}
