package uoc.ds.pr.model;

public abstract class AbstractBook {
    private final String isbn;
    private final String title;
    private final int publicationYear;
    private final Author author;
    private final Theme theme;

    public AbstractBook(String title, int publicationYear, String isbn, Author author, Theme theme) {
        this.isbn = isbn;
        this.title = title;
        this.publicationYear = publicationYear;
        this.author = author;
        this.theme = theme;


    }


    public String getIsbn() {
        return isbn;
    }
    public String getTitle() {
        return title;
    }
    public String getTheme() {
        return theme.getId();
    }
    public String getAuthor() {
        return author.getUniqueCode();
    }

    public Author getAuthorObject() {
        return author;
    }
}
