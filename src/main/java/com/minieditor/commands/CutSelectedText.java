package com.minieditor.commands;

import com.minieditor.core.Engine;
import com.minieditor.recorder.CommandOriginator;
import com.minieditor.recorder.EmptyMemento;
import com.minieditor.recorder.Memento;

public class CutSelectedText implements CommandOriginator {

    private final Engine engine;

    public CutSelectedText(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void execute() {
        engine.cutSelectedText();
    }

    @Override
    public Memento getMemento() {
        return EmptyMemento.INSTANCE;
    }

    @Override
    public void setMemento(Memento memento) {
    }
}