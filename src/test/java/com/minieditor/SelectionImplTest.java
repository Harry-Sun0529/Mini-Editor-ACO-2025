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

        // When the buffer becomes longer, bufferEndIndex should also change
        buffer.append("de");
        assertEquals(0, sel.getBufferBeginIndex());
        assertEquals(5, sel.getBufferEndIndex());
    }

    @Test
    void setting_begin_greater_than_end_should_collapse_end_to_begin() {
        StringBuilder buffer = new StringBuilder("abcdef");
        SelectionImpl sel = new SelectionImpl(buffer);

        sel.setEndIndex(2);   // [0,2)
        sel.setBeginIndex(4); // begin > end，should automatically pull end to 4

        assertEquals(4, sel.getBeginIndex());
        assertEquals(4, sel.getEndIndex()); // Empty selection, cursor at 4
    }

    @Test
    void setting_end_less_than_begin_should_collapse_begin_to_end() {
        StringBuilder buffer = new StringBuilder("abcdef");
        SelectionImpl sel = new SelectionImpl(buffer);

        sel.setBeginIndex(4);
        sel.setEndIndex(2); // end < begin，begin should be pulled back to 2

        assertEquals(2, sel.getBeginIndex());
        assertEquals(2, sel.getEndIndex());
    }

    @Test
    void setting_index_out_of_range_should_throw_exception() {
        StringBuilder buffer = new StringBuilder("abc");
        SelectionImpl sel = new SelectionImpl(buffer);

        // Less than 0
        assertThrows(IndexOutOfBoundsException.class,
                () -> sel.setBeginIndex(-1));

        // Greater than buffer.length()
        assertThrows(IndexOutOfBoundsException.class,
                () -> sel.setEndIndex(4)); // Buffer length is 3
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