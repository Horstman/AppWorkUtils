package org.appwork.utils.swing.table;

import java.awt.Color;

public class AlternateHighlighter extends ExtRowHighlighter {

    public AlternateHighlighter(Color borderColor, Color contentColor) {
        super(borderColor, contentColor);
    }

    @Override
    public boolean doHighlight(ExtTable<?> extTable, int row) {
        return row % 2 == 0;
    }

}
