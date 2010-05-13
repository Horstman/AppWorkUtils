/**
 * Copyright (c) 2009 - 2010 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.utils.swing.dialog
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.utils.swing.dialog;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import org.appwork.utils.BinaryLogic;
import org.appwork.utils.interfaces.ValueConverter;

public class ValueDialog extends AbstractDialog implements KeyListener, MouseListener {

    private static final long serialVersionUID = 9206575398715006581L;
    private String message;
    private JTextPane messageArea;
    private JSlider slider;
    private long min;
    private long max;
    private long step;
    private JTextArea converted;
    private ValueConverter valueconverter;
    // faktor to downscale long to integervalues
    private int faktor = 1;
    private long defaultValue;
    private JTextField editable;

    public ValueDialog(int flag, String title, String message, ImageIcon icon, String okOption, String cancelOption, long defaultValue, long min, long max, long step, ValueConverter valueConverter) {
        super(flag, title, icon, okOption, cancelOption);
        this.message = message;
        while (max > Integer.MAX_VALUE) {
            max /= 2;
            defaultValue /= 2;
            min /= 2;
            step = Math.max(step / 2, 1);
            faktor *= 2;

        }
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.step = step;
        if (valueConverter == null) {
            valueConverter = new ValueConverter() {

                public String toString(long value) {
                    // TODO Auto-generated method stub
                    return (value * faktor) + "";
                }

            };
        }
        this.valueconverter = valueConverter;

        init();
    }

    public JComponent layoutDialogContent() {
        JPanel contentpane = new JPanel(new MigLayout("ins 0,wrap 1", "[fill,grow]"));
        messageArea = new JTextPane();
        messageArea.setBorder(null);
        messageArea.setBackground(null);
        messageArea.setOpaque(false);
        messageArea.setText(this.message);
        messageArea.setEditable(false);
        messageArea.putClientProperty("Synthetica.opaque", Boolean.FALSE);

        contentpane.add(messageArea);
        if (BinaryLogic.containsAll(flagMask, Dialog.STYLE_LARGE)) {
            converted = new JTextArea(valueconverter.toString(defaultValue));
            converted.setEditable(false);
            converted.setBackground(null);
            slider = new JSlider(JSlider.HORIZONTAL, (int) min, (int) max, (int) defaultValue);
            slider.setMajorTickSpacing((int) step);
            slider.setSnapToTicks(true);
            slider.addKeyListener(this);
            slider.addMouseListener(this);
            editable = new JTextField();
            editable.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                    // TODO Auto-generated method stub

                }

                public void focusLost(FocusEvent e) {
                    updateSlider();

                }

            });
            editable.addKeyListener(new KeyListener() {

                public void keyPressed(KeyEvent e) {
                    // TODO Auto-generated method stub

                }

                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        updateSlider();
                    }

                }

                public void keyTyped(KeyEvent e) {
                    // TODO Auto-generated method stub

                }

            });
            slider.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent arg0) {
                    converted.setText(valueconverter.toString((slider.getValue() * faktor)));
                    editable.setText((slider.getValue() * faktor) + "");
                }

            });
            editable.setText(this.defaultValue + "");
            contentpane.add(slider, "split 2,pushy,growy,w 250");
            contentpane.add(editable, "growx,pushx,width 80:n:n");
            contentpane.add(converted, "pushy,growy,w 250");
        } else {
            converted = new JTextArea(valueconverter.toString(defaultValue));
            slider = new JSlider(JSlider.HORIZONTAL, (int) min, (int) max, (int) defaultValue);
            slider.setMajorTickSpacing((int) step);
            slider.setSnapToTicks(true);
            slider.setBorder(BorderFactory.createEtchedBorder());
            slider.addKeyListener(this);
            slider.addMouseListener(this);
            slider.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent arg0) {
                    converted.setText(valueconverter.toString((slider.getValue() * faktor)));
                    editable.setText((slider.getValue() * faktor) + "");
                }

            });

            contentpane.add(slider, "pushy,growy,w 250");
            contentpane.add(converted, "pushy,growy,w 250");
        }

        return contentpane;
    }

    private void updateSlider() {
        // new Thread() {
        // public void run() {
        // new EDTHelper<Object>() {
        //
        // 
        // public Object edtRun() {
        try {
            long value = Long.parseLong(editable.getText());
            slider.setValue((int) (value / faktor));
        } catch (Exception e) {
            if (editable != null) editable.setText((slider.getValue() * faktor) + "");
        }
        // return null;
        // }
        //
        // }.start();
        //
        // }
        // }.start();

    }

    protected void packed() {
        requestFocus();
        slider.requestFocusInWindow();
    }

    public long getReturnValue() {
        if ((this.getReturnmask() & (Dialog.RETURN_OK | Dialog.RETURN_TIMEOUT)) == 0) { return 0; }
        updateSlider();
        return slider.getValue() * faktor;
    }

    public void keyPressed(KeyEvent e) {
        this.cancel();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        this.cancel();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

}
