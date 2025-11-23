package com.minieditor.commands;

import com.minieditor.core.Engine;

public class CutSelectedText implements Command {
    private final Engine engine;
    public CutSelectedText(Engine engine) {
        this.engine = engine;
    }
    @Override
    public void execute() {
        engine.cutSelectedText();
    }
}