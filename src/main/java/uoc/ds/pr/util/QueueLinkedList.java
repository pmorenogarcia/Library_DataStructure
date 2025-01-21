package uoc.ds.pr.util;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.Queue;

public class QueueLinkedList<E> extends LinkedList<E> implements Queue<E> {

    public QueueLinkedList() {
        super();
    }
    @Override
    public void add(E e) {
        super.insertEnd(e);

    }

    @Override
    public E poll() {
        return super.deleteFirst();
    }

    @Override
    public E peek() {
        LinkedNode<E> primer = this.last.getNext();
        return primer.getElem();
    }
}
