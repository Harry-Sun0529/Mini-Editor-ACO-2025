package com.minieditor.commands;

import com.minieditor.recorder.Recorder;

public class Replay implements Command {
    private final Recorder recorder;

    public Replay(Recorder recorder) {
        this.recorder = recorder;
    }

    @Override
    public void execute() {
        recorder.replay();
    }
}