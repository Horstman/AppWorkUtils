package org.appwork.utils.swing;

import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

public class SwingUtils {
    /**
     * Calculates the position of a frame to be in the center of an other frame.
     * 
     * @param parentFrame
     * @param frame
     * @return
     */
    public static Point getCenter(final Component parentFrame, final Window frame) {
        final Point point = new Point();
        int x = 0, y = 0;

        if (parentFrame == null || frame == null) {
            point.setLocation(x, y);
            return point;
        }

        x = parentFrame.getLocation().x + parentFrame.getSize().width / 2 - frame.getSize().width / 2;
        y = parentFrame.getLocation().y + parentFrame.getSize().height / 2 - frame.getSize().height / 2;

        point.setLocation(x, y);

        return point;
    }

    /**
     * @param frame
     * @param string
     */
    public static JComponent getComponentByName(final JComponent frame, final String name) {
        JComponent ret = null;
        for (final Component c : frame.getComponents()) {

            if (c instanceof JComponent) {
                if (c.getName() != null && c.getName().equals(name)) {
                    return (JComponent) c;
                } else {
                    ret = SwingUtils.getComponentByName((JComponent) c, name);
                    if (ret != null) { return ret; }

                }
            }
        }
        return null;

    }

    public static Window getWindowForComponent(final Component parentComponent) {
        if (parentComponent == null) { return JOptionPane.getRootFrame(); }
        if (parentComponent instanceof Frame || parentComponent instanceof java.awt.Dialog) { return (Window) parentComponent; }
        return SwingUtils.getWindowForComponent(parentComponent.getParent());
    }

    /**
     * @param btnDetails
     */
    public static <T extends AbstractButton> T toBold(final T button) {
        final Font f = button.getFont();
        button.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
        return button;
    }

    /**
     * @param ret
     * @return
     * @return
     */
    public static <T extends JLabel> T toBold(final T label) {
        final Font f = label.getFont();
        label.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
        return label;
    }

    /**
     * @param label
     */
    public static <T extends JTextComponent> T toBold(final T label) {
        final Font f = label.getFont();
        label.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
        return label;
    }
}
