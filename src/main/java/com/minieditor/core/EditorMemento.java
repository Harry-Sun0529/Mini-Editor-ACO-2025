package com.minieditor.core;

public class EditorMemento {

    private final String bufferContent;
    private final int beginIndex;
    private final int endIndex;

    public EditorMemento(String bufferContent, int beginIndex, int endIndex) {
        this.bufferContent = bufferContent;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
    }

    public String getBufferContent() {
        return bufferContent;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }
}