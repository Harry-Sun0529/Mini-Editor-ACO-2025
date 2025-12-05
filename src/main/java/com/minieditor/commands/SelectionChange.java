package com.minieditor.commands;

import com.minieditor.core.Selection;
import com.minieditor.recorder.CommandOriginator;
import com.minieditor.recorder.Memento;
import com.minieditor.recorder.Recorder;
import com.minieditor.ui.UserInterface;

public class SelectionChange implements CommandOriginator {

    private final Selection selection;
    private final UserInterface ui;
    private final Recorder recorder;

    // state to be saved/restored by memento
    private int begin;
    private int end;

    public SelectionChange(Selection selection, UserInterface ui, Recorder recorder) {
        this.selection = selection;
        this.ui = ui;
        this.recorder = recorder;
    }

    @Override
    public void execute() {
        if (!recorder.isReplaying()) {
            begin = ui.getSelectionBegin();
            end = ui.getSelectionEnd();
        }
        selection.setBeginIndex(begin);
        selection.setEndIndex(end);

        recorder.save(this);
    }

    @Override
    public Memento getMemento() {
        return new SelectionChangeMemento(begin, end);
    }

    @Override
    public void setMemento(Memento memento) {
        SelectionChangeMemento scm = (SelectionChangeMemento) memento;
        this.begin = scm.begin;
        this.end = scm.end;
    }

    private class SelectionChangeMemento implements Memento {
        private final int begin;
        private final int end;

        private SelectionChangeMemento(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }
    }
}