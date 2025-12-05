package com.minieditor.commands;

import com.minieditor.core.UndoManager;

public class Undo implements Command {

    private final UndoManager undoManager;

    public Undo(UndoManager undoManager) {
        this.undoManager = undoManager;
    }

    @Override
    public void execute() {
        undoManager.undo();
    }
}