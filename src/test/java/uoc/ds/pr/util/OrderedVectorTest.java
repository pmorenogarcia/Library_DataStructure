package uoc.ds.pr.util;

import edu.uoc.ds.traversal.Iterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;


public class OrderedVectorTest {
    private static final int LEN = 10;
    OrderedVector<Integer> v;
    Comparator<Integer> CMP = (arg0, arg1) -> arg0.compareTo(arg1);

    @Before
    public void setUp() {
        v = new OrderedVector<Integer>(LEN, CMP);
        v.update(7);
        v.update(9);
        v.update(5);
        v.update(2);
        v.update(3);
        v.update(1);
        v.update(4);
        v.update(6);
        v.update(11);
        v.update(12);
    }

    @Test
    public void test() {

        Assert.assertEquals(LEN, v.size());
        Assert.assertTrue(v.isFull());
        v.delete(1);
        Assert.assertFalse(v.isFull());
        Assert.assertEquals(LEN-1, v.size());

        Iterator<Integer> it = v.values();

        Assert.assertEquals(12, it.next().intValue());
        Assert.assertEquals(11, it.next().intValue());
        Assert.assertEquals(9, it.next().intValue());
        Assert.assertEquals(7, it.next().intValue());
        Assert.assertEquals(6, it.next().intValue());
        Assert.assertEquals(5, it.next().intValue());
        Assert.assertEquals(4, it.next().intValue());
        Assert.assertEquals(3, it.next().intValue());
        Assert.assertEquals(2, it.next().intValue());
        Assert.assertFalse(it.hasNext());

        v.update(8);
        Assert.assertTrue(v.isFull());

        it = v.values();
        Assert.assertEquals(12, it.next().intValue());
        Assert.assertEquals(11, it.next().intValue());
        Assert.assertEquals(9, it.next().intValue());
        Assert.assertEquals(8, it.next().intValue());
        Assert.assertEquals(7, it.next().intValue());
        Assert.assertEquals(6, it.next().intValue());
        Assert.assertEquals(5, it.next().intValue());
        Assert.assertEquals(4, it.next().intValue());
        Assert.assertEquals(3, it.next().intValue());
        Assert.assertEquals(2, it.next().intValue());
        Assert.assertFalse(it.hasNext());

        v.delete(3);
        Assert.assertFalse(v.isFull());
        it = v.values();
        Assert.assertEquals(12, it.next().intValue());
        Assert.assertEquals(11, it.next().intValue());
        Assert.assertEquals(9, it.next().intValue());
        Assert.assertEquals(8, it.next().intValue());
        Assert.assertEquals(7, it.next().intValue());
        Assert.assertEquals(6, it.next().intValue());
        Assert.assertEquals(5, it.next().intValue());
        Assert.assertEquals(4, it.next().intValue());
        Assert.assertEquals(2, it.next().intValue());
        Assert.assertFalse(it.hasNext());

        v.update(25);
        Assert.assertTrue(v.isFull());
        Assert.assertEquals(LEN, v.size());
        Assert.assertFalse(it.hasNext());
        it = v.values();
        Assert.assertEquals(25, it.next().intValue());
        Assert.assertEquals(12, it.next().intValue());
        Assert.assertEquals(11, it.next().intValue());
        Assert.assertEquals(9, it.next().intValue());
        Assert.assertEquals(8, it.next().intValue());
        Assert.assertEquals(7, it.next().intValue());
        Assert.assertEquals(6, it.next().intValue());
        Assert.assertEquals(5, it.next().intValue());
        Assert.assertEquals(4, it.next().intValue());
        Assert.assertEquals(2, it.next().intValue());
        Assert.assertFalse(it.hasNext());

        v.update(32);
        Assert.assertTrue(v.isFull());
        Assert.assertEquals(LEN, v.size());
        Assert.assertFalse(it.hasNext());
        it = v.values();
        Assert.assertEquals(32, it.next().intValue());
        Assert.assertEquals(25, it.next().intValue());
        Assert.assertEquals(12, it.next().intValue());
        Assert.assertEquals(11, it.next().intValue());
        Assert.assertEquals(9, it.next().intValue());
        Assert.assertEquals(8, it.next().intValue());
        Assert.assertEquals(7, it.next().intValue());
        Assert.assertEquals(6, it.next().intValue());
        Assert.assertEquals(5, it.next().intValue());
        Assert.assertEquals(4, it.next().intValue());
        Assert.assertFalse(it.hasNext());

        v.update(15);
        Assert.assertTrue(v.isFull());
        Assert.assertEquals(LEN, v.size());
        Assert.assertFalse(it.hasNext());
        it = v.values();
        Assert.assertEquals(32, it.next().intValue());
        Assert.assertEquals(25, it.next().intValue());
        Assert.assertEquals(15, it.next().intValue());
        Assert.assertEquals(12, it.next().intValue());
        Assert.assertEquals(11, it.next().intValue());
        Assert.assertEquals(9, it.next().intValue());
        Assert.assertEquals(8, it.next().intValue());
        Assert.assertEquals(7, it.next().intValue());
        Assert.assertEquals(6, it.next().intValue());
        Assert.assertEquals(5, it.next().intValue());
        Assert.assertFalse(it.hasNext());

        v.update(3);
        it = v.values();
        Assert.assertEquals(32, it.next().intValue());
        Assert.assertEquals(25, it.next().intValue());
        Assert.assertEquals(15, it.next().intValue());
        Assert.assertEquals(12, it.next().intValue());
        Assert.assertEquals(11, it.next().intValue());
        Assert.assertEquals(9, it.next().intValue());
        Assert.assertEquals(8, it.next().intValue());
        Assert.assertEquals(7, it.next().intValue());
        Assert.assertEquals(6, it.next().intValue());
        Assert.assertEquals(5, it.next().intValue());
        Assert.assertFalse(it.hasNext());

        v.update(40);
        it = v.values();
        Assert.assertEquals(40, it.next().intValue());
        Assert.assertEquals(32, it.next().intValue());
        Assert.assertEquals(25, it.next().intValue());
        Assert.assertEquals(15, it.next().intValue());
        Assert.assertEquals(12, it.next().intValue());
        Assert.assertEquals(11, it.next().intValue());
        Assert.assertEquals(9, it.next().intValue());
        Assert.assertEquals(8, it.next().intValue());
        Assert.assertEquals(7, it.next().intValue());
        Assert.assertEquals(6, it.next().intValue());
        Assert.assertFalse(it.hasNext());

    }


}
