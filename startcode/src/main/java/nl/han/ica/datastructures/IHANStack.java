package nl.han.ica.datastructures;

import java.util.Iterator;

public interface IHANStack<T> extends Iterable<T> {
    /**
     * pushes value T to the top of the stack
     * @param value value to push
     */
    void push(T value);

    /**
     * Pops (and removes) value at top of stack
     * @return popped value
     */
    T pop();

    /**
     * Peeks at the top of the stack. Does not remove anything
     * @return value at the top of the stack
     */
    T peek();

    int size();

    T get(int i);

    @Override
    default Iterator<T> iterator() {
        return new Iterator<T>() {
            private int currentIndex = size() - 1;

            @Override
            public boolean hasNext() {
                return currentIndex >= 0;
            }

            @Override
            public T next() {
                return get(currentIndex--);
            }
        };
    }
}
