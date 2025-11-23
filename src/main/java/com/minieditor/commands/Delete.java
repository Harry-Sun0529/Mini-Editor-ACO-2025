package com.minieditor.commands;

import com.minieditor.core.Engine;

public class Delete implements Command {
    private final Engine engine;

    public Delete(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void execute() {
        engine.delete();
    }
}
