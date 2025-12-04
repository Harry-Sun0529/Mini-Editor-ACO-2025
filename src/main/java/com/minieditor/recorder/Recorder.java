package com.minieditor.recorder;

import com.minieditor.commands.Command;

public interface Recorder {
    void start();
    void stop();
    void save(CommandOriginator cmd);
    boolean isReplaying();
    void replay();
}