package com.minieditor.ui;

public interface UserInterface {

    // Events from the actor
    void onInsert(String text);
    void onDelete();
    void onCopy();
    void onCut();
    void onPaste();
    void onSelectionChange(int begin, int end);
    void onStartRecording();
    void onStopRecording();
    void onReplay();
    void onUndo();
    void onRedo();

    // Queries used by commands
    String getText();
    int getSelectionBegin();
    int getSelectionEnd();
}