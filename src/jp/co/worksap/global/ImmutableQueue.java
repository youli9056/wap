package jp.co.worksap.global;

import java.util.NoSuchElementException;

/**
 * The Queue calss represents an immutable first-in-first-out(FIFO) queue of objects.
 * @param <E>
 * @author Yuri
 */
public class ImmutableQueue<E> {
    private ImmutableStack<E> forward;
    private ImmutableStack<E> backward;
    /**
     * required default constructor.
     */
    public ImmutableQueue(){
        this.forward = ImmutableStack.EMPTYSTACK;
        this.backward = ImmutableStack.EMPTYSTACK;
    }
    public ImmutableQueue(ImmutableQueue originQueue){
        
    }
    private ImmutableQueue(ImmutableQueue originQueue, int from, int to){
        
    }
    public ImmutableQueue(ImmutableStack<E> fward, ImmutableStack<E> bward){
        this.forward = fward;
        this.backward = bward;
    }
    /**
     * Returns the queue that adds an item into the tail of this queue without modifying this queue.
     * <pre>
     * e.g.
     * When this queue represents the queue(2,1,2,2,6)and we enqueue the value 4 into this queue,
     * this method returns a new queue(2,1,2,2,6,4)
     * and this object still represents the queue(2,1,2,2,6).
     * </pre>
     * If the element e is null, throws IllegalArgumentException.
     * @param e
     * @return 
     * @throws IllegalArgumentException
     */
    public ImmutableQueue<E> enqueue(E e){
        if( null == e)
            throw new IllegalArgumentException();
        return new ImmutableQueue<E>(this.forward.push(e),this.backward);
    }
    /**
     * Returns the queue that removes the object at the head of this queue without modifying this queue
     * <pre>
     * e.g.
     * When this queue represents the queue(7,1,3,3,5,1),
     * this method returns a new queue(1,3,3,5,1)
     * and this object still represents the queue(7,1,3,3,5,1).
     * </pre>
     * If this queue is empty, throws java.util.NoSuchElementException.
     * @return ImmutableQueue<E>
     * @throws java.util.NoSuchElementException
     * @param e
     * @return 
     */
    public ImmutableQueue<E> dequeue(){
        if(this.isEmpty())
            throw new NoSuchElementException();
        if(this.backward.isEmpty()){
            this.backward = this.forward.reverse();
            this.forward = ImmutableStack.EMPTYSTACK;
        }
        return new ImmutableQueue<E>(this.forward,this.backward.pop());
    }
 
    /**
     * Looks at the object which is the head of this queue without removing it from the queue.
     * <pre>
     * e.g.
     * When this queue represents the queue (7,1,3,3,5,1),
     * this method returns 7 and this object still represents the queue (7,1,3,3,5,1)
     * </pre>
     * If the queue is empty, throws java.util.NoSuchElementException.
     * @return the first element at the head of this queue
     * @throws java.util.NoSuchElementException
     */
    public E peek(){
        if(this.isEmpty())
            throw new NoSuchElementException();
        if(this.backward.isEmpty()){
            this.backward = this.forward.reverse();
            this.forward = ImmutableStack.EMPTYSTACK;
        }
        return this.backward.peek();
    }
    
    /**
     * Returns the number of objects in this queue.
     * @return  the number of elements in this queue
     */
    public int size(){
        return this.backward.size() + this.forward.size();
    }
    
    public boolean isEmpty(){
        return this.size() == 0;
    }
}
