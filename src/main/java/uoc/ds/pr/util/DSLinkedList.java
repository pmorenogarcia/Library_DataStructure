package uoc.ds.pr.util;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.traversal.Iterator;
import edu.uoc.ds.traversal.Traversal;

import java.util.Comparator;

public class DSLinkedList<E> extends LinkedList<E> {
    Comparator<E> comparator;

    public DSLinkedList(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public E get(E pElem, Comparator<E> comparator) {
        Iterator<E> it = super.values();
        boolean found = false;
        E elem = null;
        while (!found && it.hasNext()) {
            elem = it.next();
            found = comparator.compare(elem, pElem) == 0;
        }
        return (found?elem: null);
    }

    public E get(E pElem) {
        return get(pElem, this.comparator);
    }

    public E remove(E pElem) {
        Traversal<E> traversal = super.positions();
        boolean found = false;
        Position<E> position = null;
        E elem = null;

        while (!found && traversal.hasNext()) {
            position = traversal.next();
            found = comparator.compare(position.getElem(), pElem) == 0;
        }
        if (found) {
            elem = position.getElem();
            super.delete(position);
        }
        return elem;
    }


}
