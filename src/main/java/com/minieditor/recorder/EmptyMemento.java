package com.minieditor.recorder;

public final class EmptyMemento implements Memento {
    public static final EmptyMemento INSTANCE = new EmptyMemento();
    private EmptyMemento() {
    }
}