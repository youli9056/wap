/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.co.worksap.global;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Yuri
 */
public class ImmutableQueueTest {
    
    public ImmutableQueueTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    public void test(){
        ImmutableQueue<Integer> p = new ImmutableQueue<Integer>();
        assertEquals(p.size(), 0);
		p = p.enqueue(2);
		assertEquals(p.size(), 1);
		p = p.enqueue(9);
		assertEquals(p.peek(), new Integer(2));
		assertEquals(new Integer(p.size()), new Integer(2));
		p = p.dequeue();
		assertEquals(p.peek(), new Integer(9));
		p = p.enqueue(10);
		assertEquals(p.peek(),new Integer(9));
		p = p.dequeue();
		assertEquals(p.peek(), new Integer(10));
		p = p.dequeue();
		assertEquals(new Integer(p.size()), new Integer(0));
		//p.peek();

    }
    /**
     * Test of enqueue method, of class ImmutableQueue.
     */
    @Test
    public void testEnqueue() {
        test();
    }

}
