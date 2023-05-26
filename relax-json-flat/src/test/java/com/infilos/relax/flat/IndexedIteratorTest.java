package com.infilos.relax.flat;

import java.util.*;

import org.junit.*;

public class IndexedIteratorTest extends Assert {

    IndexedIterator<Integer> iterator;
    IndexedIterator<Integer> emptyIterater;

    @Before
    public void setUp() {
        iterator = new IndexedIterator<>(Arrays.asList(1, 2, 3, 4).iterator());
        emptyIterater = new IndexedIterator<>(Collections.emptyIterator());
    }

    @Test
    public void testIterface() {
        assertNotNull(iterator);
    }

    @Test
    public void testRemove() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        iterator = new IndexedIterator<>(list.iterator());
        iterator.next();
        iterator.remove();
        assertEquals(new ArrayList<>(Arrays.asList(2, 3, 4)), list);
    }

    @Test
    public void testHasNext() {
        assertTrue(iterator.hasNext());
        assertFalse(emptyIterater.hasNext());
    }

    @Test
    public void testNext() {
        assertEquals(Integer.valueOf(1), iterator.next());
        iterator.next();
        iterator.next();
        assertEquals(Integer.valueOf(4), iterator.next());
    }

    @Test
    public void testNextException() {
        assertThrows(NoSuchElementException.class, () -> {
            emptyIterater.next();
        });
    }

    @Test
    public void testPeek() {
        assertEquals(Integer.valueOf(1), iterator.peek());
        iterator.next();
        assertEquals(Integer.valueOf(2), iterator.peek());
    }

    @Test
    public void testPeekException() {
        assertThrows(NoSuchElementException.class, () -> {
            emptyIterater.peek();
        });
    }

    @Test
    public void testRemoveException() {
        assertThrows(IllegalStateException.class, () -> {
            iterator.peek();
            iterator.remove();
        });
    }

    @Test
    public void testGetIndex() {
        assertEquals(-1, iterator.getIndex());
        iterator.peek();
        assertEquals(-1, iterator.getIndex());
        iterator.next();
        assertEquals(0, iterator.getIndex());
        iterator.peek();
        assertEquals(0, iterator.getIndex());
        iterator.next();
        assertEquals(1, iterator.getIndex());
        iterator.peek();
        assertEquals(1, iterator.getIndex());
        iterator.next();
        assertEquals(2, iterator.getIndex());
        iterator.peek();
        assertEquals(2, iterator.getIndex());
        iterator.next();
        assertEquals(3, iterator.getIndex());
    }

    @Test
    public void testGetCurrent() {
        assertNull(iterator.getCurrent());
        iterator.peek();
        assertNull(iterator.getCurrent());
        iterator.next();
        assertEquals(1, (int) iterator.getCurrent());
        iterator.peek();
        assertEquals(1, (int) iterator.getCurrent());
        iterator.next();
        assertEquals(2, (int) iterator.getCurrent());
        iterator.peek();
        assertEquals(2, (int) iterator.getCurrent());
        iterator.next();
        assertEquals(3, (int) iterator.getCurrent());
        iterator.peek();
        assertEquals(3, (int) iterator.getCurrent());
        iterator.next();
        assertEquals(4, (int) iterator.getCurrent());
    }

}
