package com.minieditor.recorder;

public interface Recorder {

    void start();

    void stop();

    void save(CommandOriginator cmd);

    void replay();
}