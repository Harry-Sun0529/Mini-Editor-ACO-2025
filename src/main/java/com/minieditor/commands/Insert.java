package com.minieditor.commands;

import com.minieditor.recorder.CommandOriginator;
import com.minieditor.recorder.InsertMemento;
import com.minieditor.recorder.Memento;
import com.minieditor.core.Engine;

public class Insert implements Command, CommandOriginator {
    private final Engine engine;
    private String text; // 不能 final，因为 replay 时需要 setMemento

    public Insert(Engine engine, String text) {
        this.engine = engine;
        this.text = text;
    }

    @Override
    public void execute() {
        engine.insert(text);
    }

    @Override
    public Memento getMemento() {
        return new InsertMemento(text);
    }

    @Override
    public void setMemento(Memento m) {
        this.text = ((InsertMemento) m).getText();
    }
}