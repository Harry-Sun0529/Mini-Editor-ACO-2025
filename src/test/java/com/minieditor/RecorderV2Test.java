package com.minieditor.recorder;

import com.minieditor.core.Engine;
import com.minieditor.core.EngineImpl;
import com.minieditor.ui.Editor;
import com.minieditor.ui.UserInterface;
import com.minieditor.ui.UserInterfaceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecorderV2Test {

    private Engine engine;
    private Editor editor;
    private Recorder recorder;
    private UserInterface ui;

    @BeforeEach
    void setUp() {
        engine = new EngineImpl();
        editor = new Editor();
        recorder = new RecorderImpl();
        ui = new UserInterfaceImpl(editor, engine, recorder);
    }

    @Test
    void replay_should_repeat_insert_with_text_parameter() {
        ui.onStartRecording();
        ui.onInsert("abc");
        ui.onStopRecording();

        assertEquals("abc", engine.getBufferContents());

        ui.onReplay();
        assertEquals("abcabc", engine.getBufferContents()); // 插入被回放一次
    }

    @Test
    void stop_should_prevent_further_recording() {
        ui.onStartRecording();
        ui.onInsert("a");
        ui.onStopRecording();

        ui.onInsert("b"); // 这条不应被录到

        ui.onReplay();
        assertEquals("aba", engine.getBufferContents()); // 只回放 "a"
    }

    @Test
    void replay_should_reproduce_selection_and_cut_paste_sequence() {
        ui.onStartRecording();
        ui.onInsert("12345");
        ui.onSelectionChange(0, 2); // select "12"
        ui.onCut();                  // cut -> buffer "345", clipboard "12"
        ui.onSelectionChange(3, 3); // cursor at end
        ui.onPaste();                // buffer "34512"
        ui.onStopRecording();

        assertEquals("34512", engine.getBufferContents());

        ui.onReplay(); // 再执行一遍同样序列
        assertEquals("5123412345", engine.getBufferContents());
    }
}