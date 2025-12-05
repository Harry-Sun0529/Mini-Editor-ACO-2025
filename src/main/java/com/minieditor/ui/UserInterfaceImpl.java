package com.minieditor.ui;

import com.minieditor.commands.*;
import com.minieditor.core.Engine;
import com.minieditor.core.UndoManager;
import com.minieditor.recorder.Recorder;

import java.util.HashMap;
import java.util.Map;

/**
 * UserInterfaceImpl acts as the Client and also as an Invoker:
 * it maintains a map of all commands and triggers them by id.
 */
public class UserInterfaceImpl implements UserInterface {

    private final Editor editor;
    private final Engine engine;
    private final Recorder recorder;
    private final UndoManager undoManager;

    // map of command id -> command instance
    private final Map<String, Command> commands = new HashMap<>();

    // State: used for command queries (Insert, SelectionChange)
    private String currentText = "";
    private int currentBegin = 0;
    private int currentEnd = 0;

    public UserInterfaceImpl(Editor editor, Engine engine, Recorder recorder, UndoManager undoManager) {
        this.editor = editor;
        this.engine = engine;
        this.recorder = recorder;
        this.undoManager = undoManager;

        // fill the map with all commands, each with an id
        commands.put("insert", new Insert(engine, this, recorder));
        commands.put("delete", new Delete(engine, recorder));
        commands.put("copy", new CopySelectedText(engine, recorder));
        commands.put("cut", new CutSelectedText(engine, recorder));
        commands.put("paste", new PasteClipboard(engine, recorder));
        commands.put("selectionChange", new SelectionChange(engine.getSelection(), this, recorder));
        commands.put("replay", new Replay(recorder));
        // V3: undo/redo (only depend on UndoManager, no recording)
        commands.put("undo", new Undo(undoManager));
        commands.put("redo", new Redo(undoManager));
    }

    /**
     * Execute the command associated with the given id.
     * If the id is unknown, we fail fast to make the contract explicit.
     */
    private void playCommand(String id) {
        Command cmd = commands.get(id);
        if (cmd == null) {
            throw new IllegalArgumentException("Unknown command id: " + id);
        }
        editor.executeCommand(cmd);
    }

    @Override
    public void onInsert(String text) {
        this.currentText = text;
        undoManager.store();          // V3: store state before change
        playCommand("insert");
    }

    @Override
    public void onDelete() {
        undoManager.store();
        playCommand("delete");
    }

    @Override
    public void onCopy() {
        // copy 不改变 engine 状态，可选是否进入 undo 栈
        playCommand("copy");
    }

    @Override
    public void onCut() {
        undoManager.store();
        playCommand("cut");
    }

    @Override
    public void onPaste() {
        undoManager.store();
        playCommand("paste");
    }

    @Override
    public void onSelectionChange(int begin, int end) {
        this.currentBegin = begin;
        this.currentEnd = end;
        undoManager.store();
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

    @Override
    public void onUndo() {
        playCommand("undo");
    }

    @Override
    public void onRedo() {
        playCommand("redo");
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