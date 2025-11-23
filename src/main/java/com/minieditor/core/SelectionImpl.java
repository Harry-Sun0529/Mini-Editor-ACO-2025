package com.minieditor.core;

class SelectionImpl implements Selection {
    private final StringBuilder buffer;   // 引用 Engine 的底层 buffer
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
            // 允许设置，但保持区间合法：这里简单地把 end 跟着走
            endIndex = beginIndex;
        }
        this.beginIndex = beginIndex;
    }

    @Override
    public void setEndIndex(int endIndex) {
        rangeCheck(endIndex);
        if (endIndex < beginIndex) {
            // 允许设置，但保持区间合法
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

    // 工具方法：把选区重置为某个光标位置（空选区）
    void collapseTo(int pos) {
        setBeginIndex(pos);
        setEndIndex(pos);
    }
}
