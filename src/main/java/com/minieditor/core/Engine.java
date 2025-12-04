package com.minieditor.core;

public interface Engine {
    Selection getSelection();                 // Access the selection object
    String getBufferContents();               // Read Buffer
    String getClipboardContents();            // Read Clipboard
    void cutSelectedText();                   // Cut: Copy the selection to the clipboard and delete it
    void copySelectedText();                  // Copy: Copy the selection to the clipboard
    void pasteClipboard();                    // Paste: Replace the current selection with the clipboard contents
    void insert(String s);                    // Insert: Replace the current selection with s
    void delete();                            // Delete: Remove the selection content
    EditorMemento getMemento();
    void setMemento(EditorMemento memento);
}
