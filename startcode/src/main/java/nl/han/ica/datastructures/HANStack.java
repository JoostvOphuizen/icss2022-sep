package nl.han.ica.datastructures;

public class HANStack<T> implements IHANStack<T> {
    private HANLinkedList<T> linkedList;

    public HANStack() {
        linkedList = new HANLinkedList<>();
    }

    @Override
    public void push(T value) {
        linkedList.addFirst(value);
        //System.out.println("HANSTACK ==== pushed: " + value);
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        T value = linkedList.getFirst();
        linkedList.removeFirst();
        //System.out.println("HANSTACK ==== popped: " + value);
        return value;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        //System.out.println("HANSTACK ==== peeked: " + linkedList.getFirst());
        return linkedList.getFirst();
    }

    @Override
    public int size() {
        return linkedList.size();
    }

    @Override
    public T get(int i) {
        return linkedList.get(i);
    }

    private boolean isEmpty() {
        //System.out.println("HANSTACK ==== isEmpty: " + linkedList.isEmpty());
        return linkedList.isEmpty();
    }

    // for debugging purposes only
    public String showCompleteStack() {
        return linkedList.showCompleteList();
    }
}
