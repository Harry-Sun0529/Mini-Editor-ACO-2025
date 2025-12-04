package com.minieditor.commands;

import com.minieditor.core.UndoManager;
import com.minieditor.recorder.CommandOriginator;
import com.minieditor.recorder.EmptyMemento;
import com.minieditor.recorder.Memento;

public class Redo implements CommandOriginator {

    private final UndoManager undoManager;

    public Redo(UndoManager undoManager) {
        this.undoManager = undoManager;
    }

    @Override
    public void execute() {
        undoManager.redo();
    }

    @Override
    public Memento getMemento() {
        return EmptyMemento.INSTANCE;
    }

    @Override
    public void setMemento(Memento memento) {
        // no state to restore
    }
}