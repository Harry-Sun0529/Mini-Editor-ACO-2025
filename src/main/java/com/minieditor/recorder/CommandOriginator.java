package com.minieditor.recorder;

/**
 * Commands that carry parameters (like Insert) can expose a Memento
 * so Recorder doesn't depend on concrete command fields.
 */
public interface CommandOriginator {
    Memento getMemento();
    void setMemento(Memento m);
}