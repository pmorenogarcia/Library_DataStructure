package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;

import java.time.LocalDate;


public interface Library {

    int MAX_NUM_READERS = 24;
    int MAX_NUM_WORKERS = 18;
    int MAX_BOOK_STACK = 10;
    int MAXIMUM_NUMBER_OF_BOOKS = 3;

    int POINTS_DELAYED = -1;
    int POINTS_COMPLETED = 2;
    int POINTS_GOOD = 5;
    int POINTS_BAD = -10;
    int POINTS_DESTROYED = -100;

    enum LoanState {
        INPROGRESS,
        COMPLETED,
        DELAYED
    }

    enum CopyReturnStatus {
        GOOD (0),
        BAD (1),
        DESTROYED (2);
        private final int value;

        CopyReturnStatus(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    /**
     * To simplify the test suite, the addReader operation allows initializing the points that a 'reader' has
     * */
    void addReader(String id, String name, String surname, String docId, LocalDate birthDate, String birthPlace, String address, int points);

    void addWorker(String id, String name, String surname);

    void storeCopy(String copyId, String title, String publisher, String edition, int publicationYear, String isbn, String author, String theme) throws AuthorNotFoundException, ThemeNotFoundException;

    Copy catalogCopy(String workerId) throws NoBookException, WorkerNotFoundException;

    Request lendCopy(String loanId, String copyId, String workerId, LocalDate date, LocalDate expirationDate)
        throws CopyNotFoundException, WorkerNotFoundException, MaximumNumberOfBooksException, NoRequestException, CopyNotAvailableException ;

    Loan giveBackBook(String loanId, LocalDate date, CopyReturnStatus status) throws LoanNotFoundException;

    int timeToBeCataloged(String bookId, int lotPreparationTime, int bookCatalogTime) throws BookNotFoundException, InvalidLotPreparationTimeException, InvalidCatalogTimeException;

    Iterator<Loan> getAllLoansByReader(String readerId) throws NoLoansException;

    Iterator<Loan> getAllLoansByState(String readerId, LoanState state) throws NoLoansException;

    Iterator<Loan> getAllLoansByCopy(String copyId) throws NoLoansException;

    Reader getReaderTheMost() throws NoReaderException;

    Book getMostReadBook() throws NoBookException;

    /***********************************************************************************/
    /******************** AUX OPERATIONS  **********************************************/
    /***********************************************************************************/


    Reader getReader(String id);
    int numReaders();

    Worker getWorker(String id);
    int numWorkers();

    Copy getCopy(String bookId);

    Loan getLoan(String id);

    //
    // Books warehouse
    //

    int numStacks();

    int numCatalogBooks();
    int numCatalogCopies();
    int numCopies();
    int numCopies(String bookId);
    int numCatalogCopiesByWorker(String workerId);
    int numCatalogBooksByWorker(String workerId);

    int numAvailableCopies(String bookId);

    int numLoans();
    int numLoansByWorker(String workerId);
    int numLoansByCopy(String copyId);
    int numCurrentLoansByReader(String readerId);
    int numClosedLoansByWorker(String workerId);
    int numClosedLoansByReader(String readerId);
    int numDestroyedLoans();

}


