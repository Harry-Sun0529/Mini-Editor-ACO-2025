package com.minieditor.core;

import com.minieditor.core.Engine;
import com.minieditor.core.EngineImpl;
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
        Recorder recorder = new RecorderImpl();  // V2 新增
        ui = new UserInterfaceImpl(editor, engine, recorder); // 使用新构造器
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
        ui.onSelectionChange(1, 4); // select "234"
        ui.onCopy();
        ui.onSelectionChange(5, 5); // move cursor to end
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
        ui.onCopy(); // clipboard = "bcd"
        ui.onSelectionChange(2, 5); // select "cde"
        ui.onPaste(); // replace with "bcd"
        assertEquals("abbcdf", engine.getBufferContents());
    }

    @Test
    void sequence_of_commands_should_work_together() {
        ui.onInsert("12345");
        ui.onSelectionChange(0, 2);
        ui.onCut(); // cut "12"
        ui.onSelectionChange(3, 3);
        ui.onPaste(); // paste "12" after "345"
        assertEquals("34512", engine.getBufferContents());
    }

    @Test
    void delete_with_empty_selection_should_do_nothing() {
        ui.onInsert("abc");
        // 默认选区是 [0,0) —— 空选区
        ui.onDelete();
        assertEquals("abc", engine.getBufferContents());
    }

    @Test
    void copy_with_empty_selection_should_clear_clipboard() {
        ui.onInsert("abc");
        ui.onSelectionChange(0, 3);
        ui.onCopy(); // clipboard = "abc"

        ui.onSelectionChange(1, 1); // 空选区
        ui.onCopy(); // 现在应该变成空字符串
        assertEquals("", engine.getClipboardContents());
    }

    @Test
    void paste_with_empty_selection_should_insert_at_cursor() {
        ui.onInsert("abc");
        ui.onSelectionChange(0, 3);
        ui.onCopy(); // clipboard = "abc"

        ui.onSelectionChange(3, 3); // 光标在末尾
        ui.onPaste();  // 变成 "abcabc"
        assertEquals("abcabc", engine.getBufferContents());
    }
}