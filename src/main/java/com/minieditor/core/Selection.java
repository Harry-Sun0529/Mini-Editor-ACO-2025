package com.minieditor.core;

public interface Selection {
    int getBeginIndex();                      // Starting point of the selection (include start)
    int getEndIndex();                        // End of selection (exclusive end)
    int getBufferBeginIndex();                // Buffer starting point (usually 0)
    int getBufferEndIndex();                  // buffer end point (= current text length)
    void setBeginIndex(int beginIndex);       // Set starting point (needs to be within range)
    void setEndIndex(int endIndex);           // Set the end point (needs to be within the range)
}
