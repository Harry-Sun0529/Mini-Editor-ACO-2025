# Mini Text Editor (ACO 2025)

This project was completed by **Weizhuo Sun** as part of the course  
**Object-Oriented Analysis and Design (ACO)** at the University of Rennes 1, ISTIC (2025).

The project implements a miniature text editor following the ACO specifications, using  
the **Command Pattern**, **Memento Pattern**, **Recorder System**, and **Undo/Redo** support.  
A complete Swing-based GUI is also provided.

---

## Description

This repository contains:

- The **core text editor engine**
- A complete **command-based architecture**
- A **macro recorder** for recording and replaying user actions
- **Undo/Redo** logic using the Memento pattern
- A fully functional **GUI built with Java Swing**
- Git tags for switching between different versions

---

## Features

### **Core Engine (Kernel)**

- Maintains:
  - Text buffer  
  - Clipboard  
  - Selection  
- Supports core operations:
  - Insert  
  - Delete  
  - Copy  
  - Cut  
  - Paste  
  - Selection changes  

### **Command Design Pattern**

Each editor operation is implemented as a standalone **Command**, including:

- `Insert`
- `Delete`
- `CopySelectedText`
- `CutSelectedText`
- `PasteClipboard`
- `SelectionChange`
- `StartRecording`
- `StopRecording`
- `Replay`
- `Undo`
- `Redo`

The `Editor` class acts as the **Invoker**, executing commands uniformly.

### **Recorder (Macro Recording System)**

- Supports recording user actions
- Stores commands in execution order
- Can replay entire sequences of actions
- Fully compatible with:
  - Insert
  - Delete
  - Copy
  - Cut
  - Paste
  - Selection Change

### **Undo / Redo (Memento Pattern)**

- Stores snapshots of the engine state
- Multi-step Undo & Redo
- Implemented via an `UndoManager`
- Only stores snapshots for actions that modify the buffer
