package com.minieditor.recorder;

import com.minieditor.commands.Command;

import java.util.ArrayList;
import java.util.List;

public class RecorderImpl implements Recorder {

    private boolean recording = false;

    private static class Entry {
        Command cmd;
        Memento memento;
        Entry(Command cmd, Memento memento) {
            this.cmd = cmd;
            this.memento = memento;
        }
    }

    private final List<Entry> history = new ArrayList<>();

    @Override
    public void start() {
        recording = true;
    }

    @Override
    public void stop() {
        recording = false;
    }

    @Override
    public void save(Command cmd) {
        if (!recording || cmd == null) return;

        Memento m = null;
        if (cmd instanceof CommandOriginator originator) {
            m = originator.getMemento();
        }
        history.add(new Entry(cmd, m));
    }

    @Override
    public void replay() {
        for (Entry e : history) {
            if (e.memento != null && e.cmd instanceof CommandOriginator originator) {
                originator.setMemento(e.memento);
            }
            e.cmd.execute();
        }
    }
}