package com.minieditor.ui;

import com.minieditor.commands.Command;

public class Editor {

    public void executeCommand(Command command) {
        command.execute();
    }
}