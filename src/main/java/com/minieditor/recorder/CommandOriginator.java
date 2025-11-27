package com.minieditor.recorder;

public interface CommandOriginator {
    Memento getMemento();
    void setMemento(Memento m);
}