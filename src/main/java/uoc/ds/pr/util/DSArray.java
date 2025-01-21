package uoc.ds.pr.util;

import edu.uoc.ds.adt.helpers.KeyValue;
import edu.uoc.ds.exceptions.InvalidPositionException;
import edu.uoc.ds.traversal.Iterator;
import edu.uoc.ds.traversal.IteratorArrayImpl;

public class DSArray<E> {
    KeyValue<String, E>[] theArray;
    int num;

    public DSArray(int len) {
        theArray = new KeyValue[len];
    }

    public E get(String id)  {

        for (KeyValue<String, E> kv : theArray) {
            if (kv == null) {
                return null;
            } else if (kv.getKey().equals(id)){
                return kv.getValue();
            }
        }
        return null;
    }

    public void put(String id, E elem) {
        theArray[num++] = new KeyValue<>(id, elem);
    }

    public void update(String id, E elem) {
        for (KeyValue<String, E> kv : theArray) {
            if (kv!=null && kv.getKey().equals(id)){
                kv.setValue(elem);
            }
        }
    }

    public Iterator<E> values() {
        final Iterator<KeyValue<String, E>> it = new IteratorArrayImpl<>(theArray, num,0);

        return new Iterator<E>() {

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public E next() throws InvalidPositionException {
                return it.next().getValue();
            }
        };
    }
    public int size() {
        return num;
    }
}
