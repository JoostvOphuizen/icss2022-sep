package nl.han.ica.datastructures;

public class HANStack<T> implements IHANStack<T> {
    private HANLinkedList<T> linkedList;

    public HANStack() {
        linkedList = new HANLinkedList<>();
    }

    @Override
    public void push(T value) {
        linkedList.addFirst(value);
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        T value = linkedList.getFirst();
        linkedList.removeFirst();
        return value;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return linkedList.getFirst();
    }

    private boolean isEmpty() {
        return linkedList.isEmpty();
    }
}
