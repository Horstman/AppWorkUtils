/**
 * 
 */
package org.appwork.app.gui.copycutpaste;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

import org.appwork.resources.AWUTheme;
import org.appwork.utils.locale.APPWORKUTILS;

/**
 * @author $Author: unknown$
 * 
 */
public class SelectAction extends AbstractAction {

    /**
     * 
     */
    private static final long    serialVersionUID = -4042641089773394231L;
    private final JTextComponent text;

    public SelectAction(final JTextComponent c) {
        super(APPWORKUTILS.T.COPYCUTPASTE_SELECT());
        this.text = c;

        this.putValue(Action.SMALL_ICON, AWUTheme.I().getIcon("select", 16));

        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.rapidshare.utils.event.Event.ActionListener#actionPerformed(com.
     * rapidshare.utils.event.Event.ActionEvent)
     */

    public void actionPerformed(final ActionEvent e) {
        this.text.selectAll();

    }

    @Override
    public boolean isEnabled() {
        return this.text.isEnabled() && this.text.getText().length() > 0;
    }
}
