package org.appwork.utils.swing.table;

import java.awt.Color;

public class SelectionHighlighter extends ExtRowHighlighter {

    public SelectionHighlighter(Color borderColor, Color contentColor) {
        super(borderColor, contentColor);
    }

    @Override
    public boolean doHighlight(ExtTable<?> extTable, int row) {
        return extTable.isRowSelected(row) && extTable.getDropLocation() == null;
    }

}
