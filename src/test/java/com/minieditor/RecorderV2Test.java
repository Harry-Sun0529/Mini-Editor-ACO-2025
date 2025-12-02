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
        assertEquals("abcabc", engine.getBufferContents()); // Inserts are played back once
    }

    @Test
    void stop_should_prevent_further_recording() {
        ui.onStartRecording();
        ui.onInsert("a");
        ui.onStopRecording();

        ui.onInsert("b"); // This should not be recorded

        ui.onReplay();
        assertEquals("aba", engine.getBufferContents()); // Playback only "a"
    }

    @Test
    void replay_should_reproduce_selection_and_cut_paste_sequence() {
        ui.onStartRecording();
        ui.onInsert("12345");
        ui.onSelectionChange(0, 2); // Select "12"
        ui.onCut();                  // Cut -> buffer "345", clipboard "12"
        ui.onSelectionChange(3, 3); // Cursor at end
        ui.onPaste();                // Buffer "34512"
        ui.onStopRecording();

        assertEquals("34512", engine.getBufferContents());

        ui.onReplay(); // Execute the same sequence again
        assertEquals("3451212345", engine.getBufferContents());
    }
}