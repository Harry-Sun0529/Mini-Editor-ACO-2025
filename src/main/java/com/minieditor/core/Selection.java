package com.minieditor.core;

public interface Selection {
    int getBeginIndex();                      // 选区起始（含）
    int getEndIndex();                        // 选区结束（不含）
    int getBufferBeginIndex();                // buffer 起点（通常 0）
    int getBufferEndIndex();                  // buffer 终点（= 当前文本长度）
    void setBeginIndex(int beginIndex);       // 设置起点（需在范围内）
    void setEndIndex(int endIndex);           // 设置终点（需在范围内）
}
