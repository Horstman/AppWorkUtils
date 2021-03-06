package org.appwork.utils.swing.table.columns;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JComponent;

import org.appwork.utils.swing.table.ExtColumn;
import org.appwork.utils.swing.table.ExtTable;

public abstract class ExtComponentColumn<T> extends ExtColumn<T> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private MouseAdapter      listener;

    /**
     * @param name
     * @param table
     */
    public ExtComponentColumn(final String name) {
        super(name, null);

        this.listener = new MouseAdapter() {

            private int col = -1;
            private int row = -1;

            @Override
            public void mouseMoved(final MouseEvent e) {
                final ExtTable<T> table = ExtComponentColumn.this.getModel().getTable();
                final int col = table.columnAtPoint(e.getPoint());
                final int row = table.getRowIndexByPoint(e.getPoint());

                final int modelIndex = table.getColumnModel().getColumn(col).getModelIndex();
                if (col != this.col || row != this.row) {
                    if (ExtComponentColumn.this.getModel().getExtColumn(modelIndex) == ExtComponentColumn.this) {
                        ExtComponentColumn.this.onCellUpdate(col, row);
                    } else {
                        ExtComponentColumn.this.stopCellEditing();
                    }
                    this.col = col;
                    this.row = row;
                }
            }
        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.appwork.utils.swing.table.ExtColumn#getCellEditorValue()
     */
    @Override
    public Object getCellEditorValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public final JComponent getEditorComponent(final ExtTable<T> table, final T value, final boolean isSelected, final int row, final int column) {
        if (this.listener != null) {
            this.getModel().getTable().addMouseMotionListener(this.listener);
            this.listener = null;
        }
        return this.getEditorComponent(value, isSelected, row, column);
    }

    /**
     * @param value
     * @param isSelected
     * @param row
     * @param column
     * @return
     */
    protected abstract JComponent getEditorComponent(T value, boolean isSelected, int row, int column);

    @Override
    public final JComponent getRendererComponent(final ExtTable<T> table, final T value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        if (this.listener != null) {
            this.getModel().getTable().addMouseMotionListener(this.listener);
            this.listener = null;
        }

        return this.getRendererComponent(value, isSelected, row, column);
    }

    /**
     * @param value
     * @param isSelected
     * @param row
     * @param column
     * @return
     */
    abstract protected JComponent getRendererComponent(T value, boolean isSelected, int row, int column);

    @Override
    public boolean isCellEditable(final EventObject evt) {
        if (evt instanceof MouseEvent) { return false; }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.appwork.utils.swing.table.ExtColumn#isEditable(java.lang.Object)
     */
    @Override
    public boolean isEditable(final T obj) {
        // TODO Auto-generated method stub
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.appwork.utils.swing.table.ExtColumn#isEnabled(java.lang.Object)
     */
    @Override
    public boolean isEnabled(final T obj) {
        // TODO Auto-generated method stub
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.appwork.utils.swing.table.ExtColumn#isSortable(java.lang.Object)
     */
    @Override
    public boolean isSortable(final T obj) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @param col
     * @param row
     */
    protected void onCellUpdate(final int col, final int row) {
        this.stopCellEditing();
        this.getModel().getTable().editCellAt(row, col);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.appwork.utils.swing.table.ExtColumn#setValue(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public void setValue(final Object value, final T object) {
        // TODO Auto-generated method stub

    }

}
