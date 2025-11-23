package com.minieditor.commands;

import com.minieditor.core.Engine;

public class CopySelectedText implements Command {
    private final Engine engine;

    public CopySelectedText(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void execute() {
        engine.copySelectedText();
    }
}