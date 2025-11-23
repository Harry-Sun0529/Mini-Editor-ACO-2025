package com.minieditor.commands;

import com.minieditor.core.Engine;

public class PasteClipboard implements Command {
    private final Engine engine;

    public PasteClipboard(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void execute() {
        engine.pasteClipboard();
    }
}