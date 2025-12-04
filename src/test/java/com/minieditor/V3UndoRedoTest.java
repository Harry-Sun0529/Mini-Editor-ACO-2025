package com.minieditor.core;

import com.minieditor.recorder.Recorder;
import com.minieditor.recorder.RecorderImpl;
import com.minieditor.ui.Editor;
import com.minieditor.ui.UserInterface;
import com.minieditor.ui.UserInterfaceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class V3UndoRedoTest {

    private Engine engine;
    private Recorder recorder;
    private UndoManager undoManager;
    private Editor editor;
    private UserInterface ui;

    @BeforeEach
    void setUp() {
        engine = new EngineImpl();
        recorder = new RecorderImpl();
        undoManager = new UndoManager(engine);
        editor = new Editor();
        ui = new UserInterfaceImpl(editor, engine, recorder, undoManager);
    }

    @Test
    void undo_with_no_previous_action_should_do_nothing() {
        // initial state
        assertEquals("", engine.getBufferContents());
        assertEquals(0, engine.getSelection().getBeginIndex());
        assertEquals(0, engine.getSelection().getEndIndex());

        ui.onUndo();

        // still initial state
        assertEquals("", engine.getBufferContents());
        assertEquals(0, engine.getSelection().getBeginIndex());
        assertEquals(0, engine.getSelection().getEndIndex());
    }

    @Test
    void undo_should_revert_last_insert() {
        ui.onInsert("abc");
        assertEquals("abc", engine.getBufferContents());

        ui.onUndo();

        // back to empty buffer
        assertEquals("", engine.getBufferContents());
    }

    @Test
    void redo_should_reapply_last_undone_insert() {
        ui.onInsert("abc");
        ui.onUndo();
        assertEquals("", engine.getBufferContents());

        ui.onRedo();

        // insert is applied again
        assertEquals("abc", engine.getBufferContents());
    }

    @Test
    void multiple_undos_should_walk_back_history() {
        ui.onInsert("abc");
        ui.onInsert("def");   // buffer: "abcdef"
        assertEquals("abcdef", engine.getBufferContents());

        ui.onUndo();          // undo second insert
        assertEquals("abc", engine.getBufferContents());

        ui.onUndo();          // undo first insert
        assertEquals("", engine.getBufferContents());
    }

    @Test
    void undo_and_redo_should_restore_selection() {
        ui.onInsert("12345");

        int originalBegin = engine.getSelection().getBeginIndex();
        int originalEnd = engine.getSelection().getEndIndex();

        ui.onSelectionChange(1, 3);   // select "23"
        assertEquals(1, engine.getSelection().getBeginIndex());
        assertEquals(3, engine.getSelection().getEndIndex());

        ui.onUndo();
        assertEquals(originalBegin, engine.getSelection().getBeginIndex());
        assertEquals(originalEnd, engine.getSelection().getEndIndex());

        ui.onRedo();
        assertEquals(1, engine.getSelection().getBeginIndex());
        assertEquals(3, engine.getSelection().getEndIndex());
    }

    @Test
    void undo_redo_should_work_with_cut_and_paste() {
        ui.onInsert("abcdef");
        ui.onSelectionChange(1, 4); // select "bcd"
        ui.onCut();                 // buffer: "aef"

        assertEquals("aef", engine.getBufferContents());

        ui.onUndo();                // undo cut -> back to "abcdef"
        assertEquals("abcdef", engine.getBufferContents());

        ui.onRedo();                // redo cut -> "aef" again
        assertEquals("aef", engine.getBufferContents());
    }
}