package com.minieditor.core;

import com.minieditor.core.SelectionImpl;

public class EngineImpl implements Engine {
    private final StringBuilder buffer = new StringBuilder();
    private final SelectionImpl selection = new SelectionImpl(buffer);
    private String clipboard = "";

    // ===== Engine interface =====
    @Override public Selection getSelection() { return selection; }
    @Override public String getBufferContents() { return buffer.toString(); }
    @Override public String getClipboardContents() { return clipboard; }

    @Override
    public void insert(String s) {
        if (s == null) s = "";
        // Replace selection with s
        int start = selection.getBeginIndex();
        int end   = selection.getEndIndex();
        buffer.replace(start, end, s);
        // After inserting, the cursor is placed after the inserted text (empty area)
        selection.collapseTo(start + s.length());
    }

    @Override
    public void delete() {
        int start = selection.getBeginIndex();
        int end   = selection.getEndIndex();
        if (start == end) return; // When the selection is empty, V0 does nothing
        buffer.delete(start, end);
        selection.collapseTo(start); // After deletion, the cursor is at the starting point of deletion
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

    @Override
    public EditorMemento getMemento() {
        String content = getBufferContents();
        int begin = selection.getBeginIndex();
        int end = selection.getEndIndex();
        return new EditorMemento(content, begin, end);
    }

    @Override
    public void setMemento(EditorMemento memento) {
        // restore buffer
        buffer.setLength(0);
        buffer.append(memento.getBufferContent());

        // restore selection
        selection.setBeginIndex(memento.getBeginIndex());
        selection.setEndIndex(memento.getEndIndex());
    }
}