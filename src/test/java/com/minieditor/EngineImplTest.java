package com.minieditor.core;

import com.minieditor.recorder.Recorder;
import com.minieditor.recorder.RecorderImpl;
import com.minieditor.ui.Editor;
import com.minieditor.ui.UserInterface;
import com.minieditor.ui.UserInterfaceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineImplTest {

    private Engine engine;
    private Editor editor;
    private UserInterface ui;

    @BeforeEach
    void setUp() {
        engine = new EngineImpl();
        editor = new Editor();
        Recorder recorder = new RecorderImpl();
        UndoManager undoManager = new UndoManager(engine);
        ui = new UserInterfaceImpl(editor, engine, recorder, undoManager);
    }

    @Test
    void insert_command_should_insert_text() {
        ui.onInsert("Hello");
        assertEquals("Hello", engine.getBufferContents());
    }

    @Test
    void delete_command_should_remove_selection() {
        ui.onInsert("abcdef");
        ui.onSelectionChange(2, 5);
        ui.onDelete();
        assertEquals("abf", engine.getBufferContents());
    }

    @Test
    void copy_and_paste_command_should_work() {
        ui.onInsert("12345");
        ui.onSelectionChange(1, 4); // Select "234"
        ui.onCopy();
        ui.onSelectionChange(5, 5); // Move cursor to end
        ui.onPaste();
        assertEquals("12345234", engine.getBufferContents());
    }

    @Test
    void cut_command_should_copy_and_delete() {
        ui.onInsert("abcdef");
        ui.onSelectionChange(1, 4);
        ui.onCut();
        assertEquals("aef", engine.getBufferContents());
        assertEquals("bcd", engine.getClipboardContents());
    }

    @Test
    void paste_should_replace_selection() {
        ui.onInsert("abcdef");
        ui.onSelectionChange(1, 4);
        ui.onCopy(); // Clipboard = "bcd"
        ui.onSelectionChange(2, 5); // Select "cde"
        ui.onPaste(); // Replace with "bcd"
        assertEquals("abbcdf", engine.getBufferContents());
    }

    @Test
    void sequence_of_commands_should_work_together() {
        ui.onInsert("12345");
        ui.onSelectionChange(0, 2);
        ui.onCut(); // Cut "12"
        ui.onSelectionChange(3, 3);
        ui.onPaste(); // Paste "12" after "345"
        assertEquals("34512", engine.getBufferContents());
    }

    @Test
    void delete_with_empty_selection_should_do_nothing() {
        ui.onInsert("abc");
        // The default selection is [0,0) - empty selection
        ui.onDelete();
        assertEquals("abc", engine.getBufferContents());
    }

    @Test
    void copy_with_empty_selection_should_clear_clipboard() {
        ui.onInsert("abc");
        ui.onSelectionChange(0, 3);
        ui.onCopy(); // clipboard = "abc"

        ui.onSelectionChange(1, 1); // Empty constituency
        ui.onCopy(); // Should now become an empty string
        assertEquals("", engine.getClipboardContents());
    }

    @Test
    void paste_with_empty_selection_should_insert_at_cursor() {
        ui.onInsert("abc");
        ui.onSelectionChange(0, 3);
        ui.onCopy(); // clipboard = "abc"

        ui.onSelectionChange(3, 3); // Cursor at end
        ui.onPaste();  // becomes "abcabc"
        assertEquals("abcabc", engine.getBufferContents());
    }
}