package com.minieditor.core;

class SelectionImpl implements Selection {
    private final StringBuilder buffer;   // Reference the underlying buffer of the Engine
    private int beginIndex = 0;
    private int endIndex = 0;

    SelectionImpl(StringBuilder buffer) {
        this.buffer = buffer;
    }

    @Override public int getBeginIndex() { return beginIndex; }
    @Override public int getEndIndex() { return endIndex; }
    @Override public int getBufferBeginIndex() { return 0; }
    @Override public int getBufferEndIndex() { return buffer.length(); }

    @Override
    public void setBeginIndex(int beginIndex) {
        rangeCheck(beginIndex);
        if (beginIndex > endIndex) {
            // Allow settings, but keep the range legal: here simply follow end
            endIndex = beginIndex;
        }
        this.beginIndex = beginIndex;
    }

    @Override
    public void setEndIndex(int endIndex) {
        rangeCheck(endIndex);
        if (endIndex < beginIndex) {
            // Allow setting, but keep range legal
            beginIndex = endIndex;
        }
        this.endIndex = endIndex;
    }

    private void rangeCheck(int idx) {
        if (idx < 0 || idx > buffer.length()) {
            throw new IndexOutOfBoundsException(
                    "Selection index out of bounds: " + idx + " (0.." + buffer.length() + ")");
        }
    }

    // Resets the selection to a certain cursor position (empty selection)
    void collapseTo(int pos) {
        setBeginIndex(pos);
        setEndIndex(pos);
    }
}
