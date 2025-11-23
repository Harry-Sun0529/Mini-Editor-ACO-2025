package com.minieditor.core;

public interface Engine {
    Selection getSelection();                 // 访问选区对象
    String getBufferContents();               // 读取 buffer
    String getClipboardContents();            // 读取剪贴板
    void cutSelectedText();                   // 剪切：复制选区到剪贴板并删除
    void copySelectedText();                  // 复制：把选区复制到剪贴板
    void pasteClipboard();                    // 粘贴：用剪贴板内容替换当前选区
    void insert(String s);                    // 插入：用 s 替换当前选区
    void delete();                            // 删除：移除选区内容
}
