package com.minieditor.recorder;

import java.util.ArrayList;
import java.util.List;

public class RecorderImpl implements Recorder {

    private boolean recording = false;

    private static class Entry {
        final CommandOriginator cmd;
        final Memento memento;

        Entry(CommandOriginator cmd, Memento memento) {
            this.cmd = cmd;
            this.memento = memento;
        }
    }

    private final List<Entry> history = new ArrayList<>();

    @Override
    public void start() {
        history.clear();   // Clear old history when starting recording
        recording = true;
    }

    @Override
    public void stop() {
        recording = false;
    }

    @Override
    public void save(CommandOriginator cmd) {
        if (!recording || cmd == null) {
            return;
        }
        Memento memento = cmd.getMemento();
        history.add(new Entry(cmd, memento));
    }

    @Override
    public void replay() {
        for (Entry entry : history) {
            entry.cmd.setMemento(entry.memento);
            entry.cmd.execute();
        }
    }
}