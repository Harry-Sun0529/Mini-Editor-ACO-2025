package com.minieditor.recorder;

import com.minieditor.commands.Command;

public interface Recorder {
    void start();
    void stop();
    void save(Command cmd);
    void replay();
}