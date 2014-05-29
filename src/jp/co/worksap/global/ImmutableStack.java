package jp.co.worksap.global;

import java.util.NoSuchElementException;

/**
 *
 * @author Yuri
 */
public class ImmutableStack<E> {
    private final E data;
    private final ImmutableStack<E> next;
    private final int size;
    
    public final static ImmutableStack EMPTYSTACK = new ImmutableStack();
    
    private ImmutableStack(){
        this.data = null;
        this.next = null;
        this.size = 0;
    }
    
    private ImmutableStack(E newElement, ImmutableStack<E> next){
        this.data = newElement;
        this.next = next;
        this.size = next.size + 1;
    }
    public ImmutableStack<E> push(E newElement){
        if(null == newElement)
            throw new IllegalArgumentException();
        return new ImmutableStack(newElement,this);
    }
    public ImmutableStack<E> pop(){
        if(isEmpty())
            throw new NoSuchElementException();
        return next;
    }
    public E peek(){
        if(isEmpty())
            throw new NoSuchElementException();
        return this.data;
    }
    public boolean isEmpty(){
        return this.size == 0;
    }
    public int size(){
        return this.size;
    }
    /**
     * Return a stack that elements are reversed.
     * If this stack is empty, return an empty stack
     * @return 
     */
    public ImmutableStack<E> reverse(){
        ImmutableStack<E> result = EMPTYSTACK;
        ImmutableStack<E> tmp = this;
        while(!tmp.isEmpty()){
            result = result.push(tmp.data);
            tmp = tmp.next;
        }
        return result;
    }
}
