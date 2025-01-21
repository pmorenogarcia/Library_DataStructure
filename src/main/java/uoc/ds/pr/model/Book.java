package uoc.ds.pr.model;

import java.util.Comparator;

public class Book extends AbstractBook {
    public static final Comparator<Book> CMP = (b1, b2) -> b2.getIsbn().compareTo(b1.getIsbn());
    public static final Comparator<Book> CMP_T = (b1, b2) -> b2.getBookId().compareTo(b1.getBookId());


    private final String bookId;
    private String edition;
    private String publisher;

    public Book (String bookId, String title, String publisher, String edition, int publicationYear, String isbn, Author author, Theme theme) {
        super(title, publicationYear, isbn, author, theme);
        this.bookId = bookId;
        this.edition = edition;
        this.publisher = publisher;
    }

    public Book(String bookId) {
        super(null, 0, null, null, null);
        this.bookId = bookId;
    }

    public String getBookId() {
        return bookId;
    }
    public String getTheme(){
        return super.getTheme();
    }
    public String getAuthor() {
        return super.getAuthor();
    }
    public Author getAuthorObject() {
        return super.getAuthorObject();
    }
}
