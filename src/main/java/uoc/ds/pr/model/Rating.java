package uoc.ds.pr.model;

import uoc.ds.pr.exceptions.RateOutOfRangeException;

import java.util.Comparator;

public class Rating {
    public static final Comparator<Rating> CMP = (rt1, rt2) -> rt1.getReader().getId().compareTo(rt2.getReader().getId());

    private int rate;
    private String comment;
    private Reader reader;
    private CatalogedBook catalogedBook;

    public Rating(int rate, String comment, Reader reader, CatalogedBook catalogedBook) throws RateOutOfRangeException {
        this.rate = rate;
        this.comment = comment;
        this.reader = reader;
        this.catalogedBook = catalogedBook;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public CatalogedBook getCatalogedBook() {
        return catalogedBook;
    }

    public void setCatalogedBook(CatalogedBook catalogedBook) {
        this.catalogedBook = catalogedBook;
    }
}
