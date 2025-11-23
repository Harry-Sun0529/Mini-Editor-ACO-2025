package com.minieditor.core;

import com.minieditor.core.SelectionImpl;

/**
 * V0 版的最小可用实现：
 * - buffer 用 StringBuilder
 * - clipboard 用 String
 * - 所有操作都围绕 selection 的 [begin,end) 范围进行
 */
public class EngineImpl implements Engine {
    private final StringBuilder buffer = new StringBuilder();
    private final SelectionImpl selection = new SelectionImpl(buffer);
    private String clipboard = "";

    // ===== Engine 接口 =====
    @Override public Selection getSelection() { return selection; }
    @Override public String getBufferContents() { return buffer.toString(); }
    @Override public String getClipboardContents() { return clipboard; }

    @Override
    public void insert(String s) {
        if (s == null) s = "";
        // 用 s 替换选区
        int start = selection.getBeginIndex();
        int end   = selection.getEndIndex();
        buffer.replace(start, end, s);
        // 插入后，光标置于插入文本之后（空选区）
        selection.collapseTo(start + s.length());
    }

    @Override
    public void delete() {
        int start = selection.getBeginIndex();
        int end   = selection.getEndIndex();
        if (start == end) return; // 空选区时，V0 不做任何事
        buffer.delete(start, end);
        selection.collapseTo(start); // 删除后光标位于删除起点
    }

    @Override
    public void copySelectedText() {
        int start = selection.getBeginIndex();
        int end   = selection.getEndIndex();
        clipboard = (start == end) ? "" : buffer.substring(start, end);
    }

    @Override
    public void cutSelectedText() {
        copySelectedText();
        delete();
    }

    @Override
    public void pasteClipboard() {
        insert(clipboard);
    }
}