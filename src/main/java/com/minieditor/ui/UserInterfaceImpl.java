package com.minieditor.ui;

import com.minieditor.commands.*;
import com.minieditor.core.Engine;
import com.minieditor.recorder.CommandOriginator;
import com.minieditor.recorder.Recorder;

import java.util.HashMap;
import java.util.Map;

public class UserInterfaceImpl implements UserInterface {

    private final Editor editor;
    private final Engine engine;
    private final Recorder recorder;

    // map of command id -> command instance
    private final Map<String, CommandOriginator> commands = new HashMap<>();

    // State: used for command queries (Insert, SelectionChange)
    private String currentText = "";
    private int currentBegin = 0;
    private int currentEnd = 0;

    public UserInterfaceImpl(Editor editor, Engine engine, Recorder recorder) {
        this.editor = editor;
        this.engine = engine;
        this.recorder = recorder;

        // fill the map with all commands, each with an id
        commands.put("insert", new Insert(engine, this, recorder));
        commands.put("delete", new Delete(engine, recorder));
        commands.put("copy", new CopySelectedText(engine, recorder));
        commands.put("cut", new CutSelectedText(engine, recorder));
        commands.put("paste", new PasteClipboard(engine, recorder));
        commands.put("selectionChange", new SelectionChange(engine.getSelection(), this, recorder));
        commands.put("replay", new Replay(recorder));
    }

    private void playCommand(String id) {
        CommandOriginator cmd = commands.get(id);
        if (cmd == null) {
            throw new IllegalArgumentException("Unknown command id: " + id);
        }
        editor.executeCommand(cmd);
    }

    @Override
    public void onInsert(String text) {
        this.currentText = text;
        playCommand("insert");
    }

    @Override
    public void onDelete() {
        playCommand("delete");
    }

    @Override
    public void onCopy() {
        playCommand("copy");
    }

    @Override
    public void onCut() {
        playCommand("cut");
    }

    @Override
    public void onPaste() {
        playCommand("paste");
    }

    @Override
    public void onSelectionChange(int begin, int end) {
        this.currentBegin = begin;
        this.currentEnd = end;
        playCommand("selectionChange");
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
        playCommand("replay");
    }

    // === Query methods used by commands ===

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