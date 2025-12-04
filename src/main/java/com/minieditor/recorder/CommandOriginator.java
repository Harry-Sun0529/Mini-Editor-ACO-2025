package com.minieditor.recorder;

import com.minieditor.commands.Command;

public interface CommandOriginator extends Command {
    Memento getMemento();
    void setMemento(Memento memento);
}