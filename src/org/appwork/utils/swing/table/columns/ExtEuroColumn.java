package org.appwork.utils.swing.table.columns;

import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JTable;

import org.appwork.utils.storage.DatabaseInterface;
import org.appwork.utils.swing.renderer.RenderLabel;
import org.appwork.utils.swing.table.ExtColumn;
import org.appwork.utils.swing.table.ExtDefaultRowSorter;
import org.appwork.utils.swing.table.ExtTableModel;


public abstract class ExtEuroColumn extends ExtColumn {

    private static final long serialVersionUID = 3468695684952592989L;
    private RenderLabel label;
    final private DecimalFormat format = new DecimalFormat("0.00");

    public ExtEuroColumn(String name, ExtTableModel table, DatabaseInterface database) {
        super(name, table, database);
        this.label = new RenderLabel();
        label.setBorder(null);
        label.setOpaque(false);
        this.setRowSorter(new ExtDefaultRowSorter() {
            /**
             * sorts the icon by hashcode
             */
            @Override
            public int compare(Object o1, Object o2) {
                if (getCent(o1) == getCent(o2)) return 0;
                if (this.isSortOrderToggle()) {
                    return getCent(o1) > getCent(o2) ? -1 : 1;
                } else {
                    return getCent(o1) < getCent(o2) ? -1 : 1;
                }
            }

        });
    }

    abstract protected long getCent(Object o2);

    @Override
    public Object getCellEditorValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isEditable(Object obj) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEnabled(Object obj) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isSortable(Object obj) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void setValue(Object value, Object object) {
        // TODO Auto-generated method stub

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        try {
            label.setText(format.format(getCent(value) / 100.0f) + " €");
        } catch (Exception e) {
            label.setText(format.format("0.0f") + " €");
        }

        return label;
    }
}