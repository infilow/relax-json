package com.infilos.relax.flat;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Enganced iterator with peek/getIndex/getCurrent support during iterating.
 */
public final class IndexedIterator<E> implements Iterator<E> {

    /**
     * Creates an {@link IndexedIterator} by given Iterable.
     *
     * @param <T>  the type of elements
     * @param iterable any Iterable
     * @return an {@link IndexedIterator}
     */
    public static <T> IndexedIterator<T> from(Iterable<T> iterable) {
        return new IndexedIterator<>(iterable.iterator());
    }

    
    private final Iterator<? extends E> iterator;
    private E peek;
    private boolean hasPeek = false;
    private int index = -1;
    private E current = null;

    public IndexedIterator(Iterator<? extends E> iterator) {
        if (iterator == null) {
            throw new NullPointerException();
        }
        this.iterator = iterator;
    }

    private void peeking() {
        peek = iterator.next();
        hasPeek = true;
    }

    /**
     * Returns the index of last returned element. If there is no element has been returned, it returns -1.
     *
     * @return the index of last returned element
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the last returned element. If {@link #next()} has never been called, it returns null.
     *
     * @return the last returned element
     */
    public E getCurrent() {
        return current;
    }

    @Override
    public boolean hasNext() {
        return hasPeek || iterator.hasNext();
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        index++;
        if (hasPeek) {
            hasPeek = false;
            return current = peek;
        } else {
            peeking();
            return next();
        }
    }

    @Override
    public void remove() {
        if (hasPeek) {
            throw new IllegalStateException();
        }

        iterator.remove();
    }

    /**
     * Peeks an element advanced. Warning: remove() is temporarily out of function after a peek() until a next() is called.
     *
     * @return element
     */
    public E peek() {
        if (!hasPeek && hasNext()) {
            peeking();
        }
        if (!hasPeek) {
            throw new NoSuchElementException();
        }

        return peek;
    }
}
