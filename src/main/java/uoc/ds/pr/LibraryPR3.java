package uoc.ds.pr;

import edu.uoc.ds.adt.nonlinear.graphs.Edge;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;

import java.time.LocalDate;

public interface LibraryPR3 extends Library {

    int MAX_NUM_THEMES = 30;

    int MIN_RATE = 1;
    int MAX_RATE = 5;

    enum Level {
        ASLAN (0),
        WINDRUNNER (1),
        FREMEN (2),
        HOBBIT (3),
        OOMPA_LOOMPA (4),
        MUGGLE(5),
        TROLL(6);
        private final int value;

        Level(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    void addTheme(String themeId, String name);
    void addAuthor(String uniqueCode, String name, String surname);
    Iterator<Copy> getCopies(String bookId) throws NoBookException, NoCopiesException;
    void createRequest(String readerId, String copyId, LocalDate date)
            throws ReaderNotFoundException, CopyNotFoundException,
            ReaderAlreadyHasRequest4Copy, ReaderAlreadyHasLoan4Copy;

    Iterator<Book> getBooksByTheme(String themeId) throws NoBookException;
    Iterator<Book> getBooksByAuthor(String uniqueCode) throws NoBookException;
    Level getReaderLevel(String readerId) throws ReaderNotFoundException;
    void addReview(String bookId, String readerId, int rate, String comment)
            throws RateOutOfRangeException, ReaderNotFoundException, BookNotFoundException, ReaderNotAssociatedWithBookException, UserAlreadyRatedBookException;
    Iterator<Copy> getDestroyedCopies() throws NoBookException;
    Iterator<Rating> getReviewsByBook(String bookId) throws BookNotFoundException, NoReviewsException;
    Iterator<CatalogedBook> best5Books() throws NoBookException;
    Iterator<Reader> best5Readers() throws NoReaderException;

    Iterator<Book> getRecommendationsByBook(String bookId) throws NoBookException;
    Iterator<Book> getRecommendationsByReader(String readerId) throws ReaderNotFoundException, NoBookException;
    Iterator<Author> getRecommendedAuthors(String uniqueCode) throws AuthorNotFoundException, NoAuthorException;

    /***********************************************************************************/
    /******************** AUX OPERATIONS  **********************************************/
    /***********************************************************************************/

    int numThemes();
    Theme getTheme(String id);

    int numAuthors();
    Author getAuthor(String id);


    Edge<Integer, Book> getEdge(String bookId1, String bookId2);

}
