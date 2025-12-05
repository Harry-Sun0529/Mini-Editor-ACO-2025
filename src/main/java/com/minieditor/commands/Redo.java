package com.minieditor.commands;

import com.minieditor.core.UndoManager;

public class Redo implements Command {

    private final UndoManager undoManager;

    public Redo(UndoManager undoManager) {
        this.undoManager = undoManager;
    }

    @Override
    public void execute() {
        undoManager.redo();
    }
}