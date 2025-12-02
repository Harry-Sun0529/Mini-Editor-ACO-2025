package com.minieditor.ui;

import com.minieditor.commands.*;
import com.minieditor.core.Engine;
import com.minieditor.recorder.CommandOriginator;
import com.minieditor.recorder.Recorder;

public class UserInterfaceImpl implements UserInterface {

    private final Editor editor;
    private final Engine engine;
    private final Recorder recorder;

    // Status: used for command query
    private String currentText = "";
    private int currentBegin = 0;
    private int currentEnd = 0;

    public UserInterfaceImpl(Editor editor, Engine engine, Recorder recorder) {
        this.editor = editor;
        this.engine = engine;
        this.recorder = recorder;
    }

    @Override
    public void onInsert(String text) {
        this.currentText = text;
        CommandOriginator cmd = new Insert(engine, this);
        editor.executeCommand(cmd);
        recorder.save(cmd);
    }

    @Override
    public void onDelete() {
        CommandOriginator cmd = new Delete(engine);
        editor.executeCommand(cmd);
        recorder.save(cmd);
    }

    @Override
    public void onCopy() {
        CommandOriginator cmd = new CopySelectedText(engine);
        editor.executeCommand(cmd);
        recorder.save(cmd);
    }

    @Override
    public void onCut() {
        CommandOriginator cmd = new CutSelectedText(engine);
        editor.executeCommand(cmd);
        recorder.save(cmd);
    }

    @Override
    public void onPaste() {
        CommandOriginator cmd = new PasteClipboard(engine);
        editor.executeCommand(cmd);
        recorder.save(cmd);
    }

    @Override
    public void onSelectionChange(int begin, int end) {
        this.currentBegin = begin;
        this.currentEnd = end;
        CommandOriginator cmd = new SelectionChange(engine.getSelection(), this);
        editor.executeCommand(cmd);
        recorder.save(cmd);
    }

    @Override
    public void onStartRecording() {
        recorder.start();
    }

    @Override
    public void onStopRecording() {
        recorder.stop();
    }

    @Override
    public void onReplay() {
        CommandOriginator cmd = new Replay(recorder);
        editor.executeCommand(cmd);
        // Do not record replay since身
    }

    // === QUERY METHOD ===
    @Override
    public String getText() {
        return currentText;
    }

    @Override
    public int getSelectionBegin() {
        return currentBegin;
    }

    @Override
    public int getSelectionEnd() {
        return currentEnd;
    }
}