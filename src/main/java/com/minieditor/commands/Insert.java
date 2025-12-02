package com.minieditor.commands;

import com.minieditor.core.Engine;
import com.minieditor.recorder.CommandOriginator;
import com.minieditor.recorder.InsertMemento;
import com.minieditor.recorder.Memento;
import com.minieditor.ui.UserInterface;

public class Insert implements CommandOriginator {

    private final Engine engine;
    private final UserInterface ui;

    // last text used during execute(), for memento
    private String text;

    public Insert(Engine engine, UserInterface ui) {
        this.engine = engine;
        this.ui = ui;
    }

    @Override
    public void execute() {
        if (text == null) {
            text = ui.getText();
        }
        engine.insert(text);
    }

    @Override
    public Memento getMemento() {
        return new InsertMemento(text);
    }

    @Override
    public void setMemento(Memento memento) {
        // There is no need for instanceof here, the design ensures that memento comes from Insert itself
        this.text = ((InsertMemento) memento).getText();
    }
}