package com.minieditor.recorder;

public class InsertMemento implements Memento {

    private final String text;

    public InsertMemento(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}