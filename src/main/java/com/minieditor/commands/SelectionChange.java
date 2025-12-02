package com.minieditor.commands;

import com.minieditor.core.Selection;
import com.minieditor.recorder.CommandOriginator;
import com.minieditor.recorder.EmptyMemento;
import com.minieditor.recorder.Memento;
import com.minieditor.ui.UserInterface;

public class SelectionChange implements CommandOriginator {

    private final Selection selection;
    private final UserInterface ui;

    public SelectionChange(Selection selection, UserInterface ui) {
        this.selection = selection;
        this.ui = ui;
    }

    @Override
    public void execute() {
        int begin = ui.getSelectionBegin();
        int end = ui.getSelectionEnd();
        selection.setBeginIndex(begin);
        selection.setEndIndex(end);
    }

    @Override
    public Memento getMemento() {
        return EmptyMemento.INSTANCE;
    }

    @Override
    public void setMemento(Memento memento) {
    }
}