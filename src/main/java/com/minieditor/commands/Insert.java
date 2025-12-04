package com.minieditor.commands;

import com.minieditor.core.Engine;
import com.minieditor.recorder.CommandOriginator;
import com.minieditor.recorder.InsertMemento;
import com.minieditor.recorder.Memento;
import com.minieditor.recorder.Recorder;
import com.minieditor.ui.UserInterface;

public class Insert implements CommandOriginator {

    private final Engine engine;
    private final UserInterface ui;
    private final Recorder recorder;

    private String text;

    public Insert(Engine engine, UserInterface ui, Recorder recorder) {
        this.engine = engine;
        this.ui = ui;
        this.recorder = recorder;
    }

    @Override
    public void execute() {
        // Non-replay: get text from UI
        // replay: text has been restored by memento and is no longer taken from the UI
        if (!recorder.isReplaying()) {
            text = ui.getText();
        }
        engine.insert(text);

        // The command decides itself when to save
        recorder.save(this);
    }

    @Override
    public Memento getMemento() {
        return new InsertMemento(text);
    }

    @Override
    public void setMemento(Memento memento) {
        // Design guarantee memento comes from Insert itself
        this.text = ((InsertMemento) memento).getText();
    }
}