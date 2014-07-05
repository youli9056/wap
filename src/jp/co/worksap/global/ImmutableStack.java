package jp.co.worksap.global;

import java.util.NoSuchElementException;

/**
 * The Stack calss represents an immutable last-in-first-out(LIFO) stack of
 * objects.
 *
 * @author Yuri
 */
public class ImmutableStack<E> {

    private final E data;
    private final ImmutableStack<E> next;
    private final int size;
    //static empty stack
    public final static ImmutableStack EMPTYSTACK = new ImmutableStack();

    private ImmutableStack() {
        this.data = null;
        this.next = null;
        this.size = 0;
    }

    private ImmutableStack(E newElement, ImmutableStack<E> next) {
        this.data = newElement;
        this.next = next;
        this.size = next.size + 1;
    }

    /**
     * Returns the stack that adds an item into the tail of this stack without
     * modifying this stack.
     * <pre>
     * e.g.
     * When this stack represents the stack(2,1,2,2,6)and we push the value 4 into this stack,
     * this method returns a new stack(2,1,2,2,6,4)
     * and this object still represents the stack(2,1,2,2,6).
     * </pre> If the element newElement is null, throws
     * IllegalArgumentException.
     *
     * @param newElement the element to push in
     * @return new stack that adds new element to the tail
     * @throws IllegalArgumentException
     */
    public ImmutableStack<E> push(E newElement) {
        if (null == newElement) {
            throw new IllegalArgumentException();
        }
        return new ImmutableStack(newElement, this);
    }
    /**
     * Returns the stack that removes the object at the tail of this stack
     * without modifying this stack
     * <pre>
     * e.g.
     * When this stack represents the stack(7,1,3,3,5,1),
     * this method returns a new stack(7,1,3,3,5,)
     * and this object still represents the stack(7,1,3,3,5,1).
     * </pre> If this stack is empty, throws java.util.NoSuchElementException.
     *
     * @return ImmutableStack<E> new stack with the last element removed
     * @throws java.util.NoSuchElementException
     */
    public ImmutableStack<E> pop() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return next;
    }

    /**
     * Looks at the object which is the head of this stack without removing it
     * from the stack.
     * <pre>
     * e.g.
     * When this stack represents the stack (7,1,3,3,5,1),
     * this method returns 1 and this object still represents the stack (7,1,3,3,5,1)
     * </pre> If the stack is empty, throws java.util.NoSuchElementException.
     *
     * @return the first element at the end of this stack
     * @throws java.util.NoSuchElementException
     */
    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.data;
    }

    /**
     * Returns whether or not this stack is empty.
     *
     * @return true if this stack is empty; false otherwise.
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Returns the number of objects in this stack.
     *
     * @return the number of elements in this stack
     */
    public int size() {
        return this.size;
    }

    /**
     * Return a stack that elements are reversed. If this stack is empty, return
     * an empty stack.
     *
     * @return
     */
    public ImmutableStack<E> reverse() {
        ImmutableStack<E> result = EMPTYSTACK;
        ImmutableStack<E> tmp = this;
        while (!tmp.isEmpty()) {
            result = result.push(tmp.data);
            tmp = tmp.next;
        }
        return result;
    }
}
