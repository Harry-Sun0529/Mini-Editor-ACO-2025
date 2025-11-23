package com.minieditor.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SelectionImplTest {

    @Test
    void buffer_begin_and_end_should_follow_buffer_length() {
        StringBuilder buffer = new StringBuilder("abc");
        SelectionImpl sel = new SelectionImpl(buffer);

        assertEquals(0, sel.getBufferBeginIndex());
        assertEquals(3, sel.getBufferEndIndex());

        // 当 buffer 变长时，bufferEndIndex 也应该跟着变
        buffer.append("de");
        assertEquals(0, sel.getBufferBeginIndex());
        assertEquals(5, sel.getBufferEndIndex());
    }

    @Test
    void setting_begin_greater_than_end_should_collapse_end_to_begin() {
        StringBuilder buffer = new StringBuilder("abcdef");
        SelectionImpl sel = new SelectionImpl(buffer);

        sel.setEndIndex(2);   // [0,2)
        sel.setBeginIndex(4); // begin > end，应自动把 end 拉到 4

        assertEquals(4, sel.getBeginIndex());
        assertEquals(4, sel.getEndIndex()); // 空选区，光标在 4
    }

    @Test
    void setting_end_less_than_begin_should_collapse_begin_to_end() {
        StringBuilder buffer = new StringBuilder("abcdef");
        SelectionImpl sel = new SelectionImpl(buffer);

        sel.setBeginIndex(4);
        sel.setEndIndex(2); // end < begin，应把 begin 拉回 2

        assertEquals(2, sel.getBeginIndex());
        assertEquals(2, sel.getEndIndex());
    }

    @Test
    void setting_index_out_of_range_should_throw_exception() {
        StringBuilder buffer = new StringBuilder("abc");
        SelectionImpl sel = new SelectionImpl(buffer);

        // 小于 0
        assertThrows(IndexOutOfBoundsException.class,
                () -> sel.setBeginIndex(-1));

        // 大于 buffer.length()
        assertThrows(IndexOutOfBoundsException.class,
                () -> sel.setEndIndex(4)); // buffer 长度是 3
    }

    @Test
    void collapseTo_should_make_empty_selection_at_given_position() {
        StringBuilder buffer = new StringBuilder("abcdef");
        SelectionImpl sel = new SelectionImpl(buffer);

        sel.collapseTo(3);

        assertEquals(3, sel.getBeginIndex());
        assertEquals(3, sel.getEndIndex());
    }
}