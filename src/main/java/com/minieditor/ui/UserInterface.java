package com.minieditor.ui;

public interface UserInterface {
    void onInsert(String text);
    void onDelete();
    void onCopy();
    void onCut();
    void onPaste();
    void onSelectionChange(int begin, int end);

    // V2 additions
    void onStartRecording();
    void onStopRecording();
    void onReplay();
}