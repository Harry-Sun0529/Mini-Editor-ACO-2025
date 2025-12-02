package com.minieditor.commands;

import com.minieditor.recorder.CommandOriginator;
import com.minieditor.recorder.EmptyMemento;
import com.minieditor.recorder.Memento;
import com.minieditor.recorder.Recorder;

public class Replay implements CommandOriginator {

    private final Recorder recorder;

    public Replay(Recorder recorder) {
        this.recorder = recorder;
    }

    @Override
    public void execute() {
        recorder.replay();
    }

    @Override
    public Memento getMemento() {
        return EmptyMemento.INSTANCE;
    }

    @Override
    public void setMemento(Memento memento) {
    }
}