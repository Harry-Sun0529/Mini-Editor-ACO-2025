package com.minieditor.ui;

import com.minieditor.core.Engine;
import com.minieditor.core.EngineImpl;
import com.minieditor.core.Selection;
import com.minieditor.core.UndoManager;
import com.minieditor.recorder.Recorder;
import com.minieditor.recorder.RecorderImpl;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

/**
 * Mini Editor GUI that edits directly in the text area:
 * - Typing characters in the text area calls UI.onInsert()
 * - Backspace / Delete calls UI.onDelete()
 * - Changing the selection with mouse/keyboard syncs to Engine.Selection
 * - Buttons provide Copy/Cut/Paste/Delete, record/replay, Undo/Redo
 */
public class MiniEditorApp {

    private final Engine engine;
    private final UserInterface ui;

    private final JTextArea bufferArea;

    public MiniEditorApp() {
        // Core model
        this.engine = new EngineImpl();
        Recorder recorder = new RecorderImpl();
        UndoManager undoManager = new UndoManager(engine);
        Editor editor = new Editor();
        this.ui = new UserInterfaceImpl(editor, engine, recorder, undoManager);

        // GUI components
        bufferArea = new JTextArea(15, 50);
        bufferArea.setLineWrap(true);
        bufferArea.setWrapStyleWord(true);

        // Listen to caret and selection changes -> sync to Engine.selection
        bufferArea.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                syncSelectionFromTextArea();
            }
        });

        // Intercept keyboard input: edit directly in the text area, but actually modify the Engine
        bufferArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();

                // Let shortcuts (Cmd/Ctrl) and control characters be handled by the system
                if (e.isControlDown() || e.isMetaDown() || Character.isISOControl(ch)) {
                    return;
                }

                // Normal character insertion
                syncSelectionFromTextArea();
                ui.onInsert(String.valueOf(ch));
                refreshView();

                // Prevent JTextArea from modifying the text a second time
                e.consume();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_BACK_SPACE) {
                    handleBackspace();
                    e.consume();
                } else if (code == KeyEvent.VK_DELETE) {
                    handleDelete();
                    e.consume();
                }
            }
        });
    }

    private void createAndShowGui() {
        JFrame frame = new JFrame("Mini Editor GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Center text area: direct editing
        root.add(new JScrollPane(bufferArea), BorderLayout.CENTER);

        // Bottom button panel
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        // First row: basic editing commands
        JPanel cmdRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton copyButton = new JButton("Copy");
        JButton cutButton = new JButton("Cut");
        JButton pasteButton = new JButton("Paste");
        JButton deleteButton = new JButton("Delete");
        cmdRow1.add(copyButton);
        cmdRow1.add(cutButton);
        cmdRow1.add(pasteButton);
        cmdRow1.add(deleteButton);
        bottom.add(cmdRow1);

        // Second row: record / replay / undo / redo
        JPanel cmdRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton startRecButton = new JButton("Start Rec");
        JButton stopRecButton = new JButton("Stop Rec");
        JButton replayButton = new JButton("Replay");
        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");
        cmdRow2.add(startRecButton);
        cmdRow2.add(stopRecButton);
        cmdRow2.add(replayButton);
        cmdRow2.add(undoButton);
        cmdRow2.add(redoButton);
        bottom.add(cmdRow2);

        root.add(bottom, BorderLayout.SOUTH);

        // Keyboard shortcuts: Cmd+Z / Ctrl+Z -> Undo, Cmd+Shift+Z / Ctrl+Shift+Z / Cmd+Y -> Redo
        int menuMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        InputMap im = bufferArea.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = bufferArea.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, menuMask), "undo");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, menuMask | KeyEvent.SHIFT_DOWN_MASK), "redo");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, menuMask), "redo");

        am.put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.onUndo();
                refreshView();
            }
        });
        am.put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.onRedo();
                refreshView();
            }
        });

        // === Bind button actions ===

        copyButton.addActionListener(e -> {
            syncSelectionFromTextArea();
            ui.onCopy();
        });

        cutButton.addActionListener(e -> {
            syncSelectionFromTextArea();
            ui.onCut();
            refreshView();
        });

        pasteButton.addActionListener(e -> {
            syncSelectionFromTextArea();
            ui.onPaste();
            refreshView();
        });

        deleteButton.addActionListener(e -> {
            syncSelectionFromTextArea();
            ui.onDelete();
            refreshView();
        });

        startRecButton.addActionListener(e -> ui.onStartRecording());
        stopRecButton.addActionListener(e -> ui.onStopRecording());

        replayButton.addActionListener(e -> {
            try {
                ui.onReplay();
            } catch (IndexOutOfBoundsException ex) {
                // Prevent the application from crashing if a recorded selection is out of bounds for the current buffer
                JOptionPane.showMessageDialog(
                        frame,
                        "Replay failed: the macro contains a selection that is beyond the current buffer length.\n\n" +
                                "Hint: try replaying on content whose length is closer to when it was recorded, or record the macro again.",
                        "Replay Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            refreshView();
        });

        undoButton.addActionListener(e -> {
            ui.onUndo();
            refreshView();
        });

        redoButton.addActionListener(e -> {
            ui.onRedo();
            refreshView();
        });

        frame.setContentPane(root);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        refreshView();
        bufferArea.requestFocusInWindow();
    }

    //Sync the current JTextArea selection to Engine.selection:
    //When nothing is selected, begin=end=caret
    private void syncSelectionFromTextArea() {
        int start = bufferArea.getSelectionStart();
        int end = bufferArea.getSelectionEnd();
        int caret = bufferArea.getCaretPosition();

        if (start == end) {
            // No selection => caret position
            start = end = caret;
        }

        // Clamp indices to avoid going out of bounds
        String content = engine.getBufferContents();
        int len = content.length();
        start = Math.max(0, Math.min(start, len));
        end = Math.max(0, Math.min(end, len));

        ui.onSelectionChange(start, end);
    }

    //Handle Backspace: delete the selected range first; if nothing is selected, delete the character before the caret.
    private void handleBackspace() {
        String content = engine.getBufferContents();
        int len = content.length();
        int start = bufferArea.getSelectionStart();
        int end = bufferArea.getSelectionEnd();
        int caret = bufferArea.getCaretPosition();

        if (start == end) { // no selection
            if (caret == 0) {
                return; // nothing to delete at the beginning
            }
            ui.onSelectionChange(caret - 1, caret);
        } else {
            ui.onSelectionChange(start, end);
        }
        ui.onDelete();
        refreshView();
    }

    //Handle Delete: delete the selected range first; if nothing is selected, delete the character after the caret.
    private void handleDelete() {
        String content = engine.getBufferContents();
        int len = content.length();
        int start = bufferArea.getSelectionStart();
        int end = bufferArea.getSelectionEnd();
        int caret = bufferArea.getCaretPosition();

        if (start == end) { // no selection
            if (caret >= len) {
                return; // nothing to delete at the end
            }
            ui.onSelectionChange(caret, caret + 1);
        } else {
            ui.onSelectionChange(start, end);
        }
        ui.onDelete();
        refreshView();
    }

    //Read buffer & selection from Engine and update the text area's content and highlight.
    private void refreshView() {
        String content = engine.getBufferContents();
        bufferArea.setText(content);

        Selection selection = engine.getSelection();
        int len = content.length();
        int begin = Math.max(0, Math.min(selection.getBeginIndex(), len));
        int end = Math.max(0, Math.min(selection.getEndIndex(), len));

        bufferArea.select(begin, end);
        bufferArea.setCaretPosition(end);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MiniEditorApp().createAndShowGui());
    }
}