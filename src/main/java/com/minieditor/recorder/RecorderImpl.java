package com.minieditor.recorder;

import java.util.ArrayList;
import java.util.List;

public class RecorderImpl implements Recorder {

    private boolean recording = false;
    private boolean replaying = false;

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
        history.clear(); // start a new recording session
        recording = true;
    }

    @Override
    public void stop() {
        recording = false;
    }

    @Override
    public void save(CommandOriginator cmd) {
        // Do not save if not recording or while replaying
        if (!recording || replaying || cmd == null) {
            return;
        }
        Memento memento = cmd.getMemento();
        history.add(new Entry(cmd, memento));
    }

    @Override
    public boolean isReplaying() {
        return replaying;
    }

    @Override
    public void replay() {
        replaying = true;
        try {
            for (Entry entry : history) {
                entry.cmd.setMemento(entry.memento);
                entry.cmd.execute();
            }
        } finally {
            replaying = false;
        }
    }
}