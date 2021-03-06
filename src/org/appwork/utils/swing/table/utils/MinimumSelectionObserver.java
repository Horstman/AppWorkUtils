package org.appwork.utils.swing.table.utils;

import javax.swing.AbstractAction;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.appwork.utils.swing.table.ExtTable;

public class MinimumSelectionObserver implements ListSelectionListener {

    private final ExtTable<?>    table;
    private final AbstractAction action;
    private final int            minSelections;

    /**
     * @param table
     * @param action
     * @param minSelections
     */
    public MinimumSelectionObserver(final ExtTable<?> table, final AbstractAction action, final int minSelections) {
        this.table = table;
        this.action = action;
        this.minSelections = minSelections;
        action.setEnabled(table.getSelectedRowCount() >= minSelections);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
     * .ListSelectionEvent)
     */
    @Override
    public void valueChanged(final ListSelectionEvent e) {
        this.action.setEnabled(this.table.getSelectedRowCount() >= this.minSelections);

    }

}
