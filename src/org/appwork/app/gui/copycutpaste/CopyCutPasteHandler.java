package org.appwork.app.gui.copycutpaste;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 * This class creates a copy paste cut select delete contecxtmenu for every
 * textcomponent.
 * 
 * @author $Author: unknown$
 */
public class CopyCutPasteHandler extends EventQueue {

    public CopyCutPasteHandler() {
        super();
    }

    @Override
    protected void dispatchEvent(AWTEvent event) {
        super.dispatchEvent(event);
        if (!(event instanceof MouseEvent)) return;

        MouseEvent mouseEvent = (MouseEvent) event;
        if (!mouseEvent.isPopupTrigger() && mouseEvent.getButton() != MouseEvent.BUTTON3) return;
        if (mouseEvent.getComponent() == null) return;
        // get deepest component
        // Component c = null;
        Point point = mouseEvent.getPoint();

        Component c;
        if (mouseEvent.getSource() instanceof JDialog) {
            c = SwingUtilities.getDeepestComponentAt((JDialog) mouseEvent.getSource(), (int) point.getX(), (int) point.getY());
        } else if (mouseEvent.getSource() instanceof JFrame) {
            Component source = ((JFrame) mouseEvent.getSource()).getContentPane();
            point.x -= (source.getLocationOnScreen().x - ((JFrame) mouseEvent.getSource()).getLocationOnScreen().x);
            point.y -= (source.getLocationOnScreen().y - ((JFrame) mouseEvent.getSource()).getLocationOnScreen().y);
            c = SwingUtilities.getDeepestComponentAt(source, (int) point.getX(), (int) point.getY());
        } else {
            return;
        }

        // Check if deepest component is a textcomponent
        if (!(c instanceof JTextComponent) && !(c instanceof ContextMenuAdapter)) return;
        if (MenuSelectionManager.defaultManager().getSelectedPath().length > 0) return;

        JPopupMenu menu;
        if (c instanceof ContextMenuAdapter) {
            menu = ((ContextMenuAdapter) c).getPopupMenu();
            if (menu == null) return;
        } else {
            final JTextComponent t = (JTextComponent) c;

            // create menu
            menu = new JPopupMenu();
            menu.add(new CutAction(t));
            menu.add(new CopyAction(t));
            menu.add(new PasteAction(t));
            menu.add(new DeleteAction(t));
            menu.add(new SelectAction(t));
        }
        Point pt = SwingUtilities.convertPoint(mouseEvent.getComponent(), mouseEvent.getPoint(), c);
        menu.show(c, pt.x, pt.y);
    }
}
