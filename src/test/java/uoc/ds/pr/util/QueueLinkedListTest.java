package uoc.ds.pr.util;

import edu.uoc.ds.adt.sequential.Queue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QueueLinkedListTest {
    Queue<Integer> queue;

    @Before
    public void setUp() {
        queue = new QueueLinkedList<>();
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(4);
        queue.add(5);
    }

    @Test
    public void addTest() {
        Assert.assertEquals(5, queue.size());
        queue.add(6);
        Assert.assertEquals(6, queue.size());
    }

    @Test
    public void addPollTest() {
        Assert.assertEquals(5, queue.size());
        Assert.assertEquals(1, queue.poll(),0);
        Assert.assertEquals(4, queue.size());

        Assert.assertEquals(2, queue.poll(),0);
        Assert.assertEquals(3, queue.size());

        Assert.assertEquals(3, queue.poll(),0);
        Assert.assertEquals(2, queue.size());

        Assert.assertEquals(4, queue.poll(),0);
        Assert.assertEquals(1, queue.size());

        Assert.assertEquals(5, queue.poll(),0);
        Assert.assertEquals(0, queue.size());

    }

    @Test
    public void addPeekTest() {
        Assert.assertEquals(5, queue.size());
        Assert.assertEquals(1, queue.peek(),0);
        Assert.assertEquals(5, queue.size());
    }
}
