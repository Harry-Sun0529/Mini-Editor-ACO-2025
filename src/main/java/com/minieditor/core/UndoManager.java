package com.minieditor.core;

import java.util.ArrayDeque;
import java.util.Deque;

public class UndoManager {

    private final Engine engine;

    private final Deque<EditorMemento> pastStates = new ArrayDeque<>();
    private final Deque<EditorMemento> futurStates = new ArrayDeque<>();

    public UndoManager(Engine engine) {
        this.engine = engine;
    }

    public void store() {
        pastStates.push(engine.getMemento());
        futurStates.clear();
    }

    public void undo() {
        if (pastStates.isEmpty()) {
            return;
        }
        // current state goes to futur stack
        futurStates.push(engine.getMemento());
        // restore last past state
        EditorMemento previous = pastStates.pop();
        engine.setMemento(previous);
    }

    public void redo() {
        if (futurStates.isEmpty()) {
            return;
        }
        pastStates.push(engine.getMemento());
        EditorMemento next = futurStates.pop();
        engine.setMemento(next);
    }
}