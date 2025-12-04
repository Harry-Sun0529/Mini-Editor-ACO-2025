package com.minieditor.commands;

import com.minieditor.core.Engine;
import com.minieditor.recorder.CommandOriginator;
import com.minieditor.recorder.EmptyMemento;
import com.minieditor.recorder.Memento;
import com.minieditor.recorder.Recorder;

public class CutSelectedText implements CommandOriginator {

    private final Engine engine;
    private final Recorder recorder;

    public CutSelectedText(Engine engine, Recorder recorder) {
        this.engine = engine;
        this.recorder = recorder;
    }

    @Override
    public void execute() {
        engine.cutSelectedText();
        recorder.save(this);
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