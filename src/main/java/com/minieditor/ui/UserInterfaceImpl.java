package com.minieditor.ui;

import com.minieditor.commands.*;
import com.minieditor.core.Engine;
import com.minieditor.recorder.Recorder;

public class UserInterfaceImpl implements UserInterface {

    private final Editor editor;
    private final Engine engine;
    private final Recorder recorder;

    public UserInterfaceImpl(Editor editor, Engine engine, Recorder recorder) {
        this.editor = editor;
        this.engine = engine;
        this.recorder = recorder;
    }

    @Override
    public void onInsert(String text) {
        Command cmd = new Insert(engine, text);
        editor.executeCommand(cmd);
        recorder.save(cmd);
    }

    @Override
    public void onDelete() {
        Command cmd = new Delete(engine);
        editor.executeCommand(cmd);
        recorder.save(cmd);
    }

    @Override
    public void onCopy() {
        Command cmd = new CopySelectedText(engine);
        editor.executeCommand(cmd);
        recorder.save(cmd);
    }

    @Override
    public void onCut() {
        Command cmd = new CutSelectedText(engine);
        editor.executeCommand(cmd);
        recorder.save(cmd);
    }

    @Override
    public void onPaste() {
        Command cmd = new PasteClipboard(engine);
        editor.executeCommand(cmd);
        recorder.save(cmd);
    }

    @Override
    public void onSelectionChange(int begin, int end) {
        Command cmd = new SelectionChange(engine.getSelection(), begin, end);
        editor.executeCommand(cmd);
        recorder.save(cmd);
    }

    // -------- V2 new UI actions --------

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
        Command replay = new Replay(recorder);
        editor.executeCommand(replay);
        // replay itself should not be saved, otherwise it will grow infinitely
    }
}