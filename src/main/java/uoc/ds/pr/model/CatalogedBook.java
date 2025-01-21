package uoc.ds.pr.model;

import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.util.DSLinkedList;

import java.util.Comparator;

public class CatalogedBook {
    public static final Comparator<CatalogedBook> CMP = (cb1, cb2)->cb1.getIsbn().compareTo(cb2.getIsbn());
    public static final Comparator<CatalogedBook> CMP_BOOKID = (cb1, cb2)->cb1.getBookId().compareTo(cb2.getBookId());
    public static final Comparator<CatalogedBook> CMP_R = (cb1, cb2) -> Float.compare(cb1.getAverageRating(), cb2.getAverageRating());

    public static final Comparator<CatalogedBook> CMP_V = (cb1, cb2) -> Integer.compare(cb1.loanList.size(), cb2.loanList.size());

    public DSLinkedList<Loan> loanList;
    public DSLinkedList<Copy> copyList;
    public DictionaryAVLImpl<String, Rating> ratings;

    private final Book book;

    private int totalCopies;
    private float averageRating;

    public CatalogedBook(Book book) {
        this.book = book;
        this.loanList = new DSLinkedList<>(Loan.CMP);
        this.copyList = new DSLinkedList<>(Copy.CMP);
        this.ratings = new DictionaryAVLImpl<>();
        this.averageRating = 0.0f;
    }

    public CatalogedBook(String bookId) {
        this.book = new Book(bookId);
    }

    public void incCopies() {
        totalCopies++;
    }

    public String getBookId() {
        return (book!=null?book.getBookId():null);
    }

    public String getIsbn() {
        return (book!=null?book.getIsbn():null);
    }
    public String getTitle() {
        return (book!=null?book.getTitle():null);
    }

    public void removeCopy(Copy copy) {
        copyList.remove(copy);
        totalCopies--;
    }
    public int getCopies() {
        return copyList.size();
    }
    public void addCopy(Copy copy) {
        copyList.insertEnd(copy);
        totalCopies++;
    }

    public int numLoans() {
        return this.loanList.size();
    }

    public void addLoan(Loan loan) {
        loanList.insertEnd(loan);
    }

    public Iterator<Loan> getAllLoans() {
        return loanList.values();
    }

    public Book getBook() {
        return book;
    }
    public void addRating(String readerId, Rating rating) {
        ratings.put(readerId, rating);
        updateAverageRating();
    }
    public Rating getRating(String readerId) {
        return this.ratings.get(readerId);
    }
    public Iterator<Rating> getAllRatings() {
        return ratings.values();
    }

    public float getAverageRating() {
        return averageRating;
    }
    public void updateAverageRating() {
        Iterator<Rating> it = ratings.values();
        while(it.hasNext()) {
            Rating rating = it.next();
            float rate = rating.getRate();
            this.averageRating += rate;
        }
        this.averageRating = this.averageRating / ratings.size();
    }

}
