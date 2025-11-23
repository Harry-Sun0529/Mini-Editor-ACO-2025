package com.minieditor.commands;

import com.minieditor.core.Selection;

public class SelectionChange implements Command {
    private final Selection selection;
    private final int begin;
    private final int end;

    public SelectionChange(Selection selection, int begin, int end) {
        this.selection = selection;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public void execute() {
        selection.setBeginIndex(begin);
        selection.setEndIndex(end);
    }
}