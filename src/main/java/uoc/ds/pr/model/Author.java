package uoc.ds.pr.model;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.util.DSLinkedList;

import java.util.Comparator;

public class Author {
    public static final Comparator<Author> CMP = (a1, a2) -> a1.getUniqueCode().compareTo(a2.getUniqueCode());

    ///  ATTRIBUTES OF AUTHOR
    private String authorId;
    private String name;
    private String surname;

    private final DSLinkedList<Book> books;

    public Author(String authorId, String name, String surname) {
        this.authorId = authorId;
        this.name = name;
        this.surname = surname;
        this.books = new DSLinkedList<>(Book.CMP);
    }


    public String getUniqueCode() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return surname;
    }

    public void setLastName(String lastName) {
        this.surname = lastName;
    }

    public Iterator<Book> getBookIterator() {
        return books.values();
    }
    public void addBook(Book book) {
        if(!bookExists(book.getIsbn())) {
            books.insertEnd(book);
        }
    }
    public boolean bookExists(String isbn) {
        Iterator<Book> bookIt = books.values();
        while(bookIt.hasNext()) {
            Book book = bookIt.next();
            if(book.getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }

}
