package uoc.ds.pr;

import java.time.LocalDate;

import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.adt.nonlinear.HashTable;
import edu.uoc.ds.adt.nonlinear.graphs.*;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.BookWareHouse;
import uoc.ds.pr.util.DSArray;
import uoc.ds.pr.util.DSLinkedList;
import uoc.ds.pr.util.OrderedVector;

import static uoc.ds.pr.LibraryPR3.MAX_NUM_THEMES;


public class LibraryPR2Impl implements Library {
    protected HashTable<String, Reader> readers;
    protected DSArray<Worker> workers;

    protected BookWareHouse bookWareHouse;

    protected DictionaryAVLImpl<String, Copy> catalogedCopies;
    protected int numCatalogedCopies;
    protected DictionaryAVLImpl<String, CatalogedBook> books;
    protected DictionaryAVLImpl<String, CatalogedBook> storedBooks;

    protected OrderedVector<CatalogedBook> mostReadBooks;
    protected OrderedVector<CatalogedBook> mostRatedBooks;
    protected OrderedVector<Reader> mostFrequentReader;
    protected OrderedVector<Reader> readersBestRated;

    protected DSLinkedList<Copy> destroyedCopies;

    protected HashTable<String, Author> authors;
    protected DSArray<Theme> themes;

    protected DictionaryAVLImpl<String, Loan> loans;

    protected UnDirectedGraphImpl<Book, Integer> bookGraph;


    public LibraryPR2Impl() {
        ///  Initialize all variables
        readers = new HashTable<>(MAX_NUM_READERS);
        workers = new DSArray<>(MAX_NUM_WORKERS);
        bookWareHouse = new BookWareHouse();
        catalogedCopies = new DictionaryAVLImpl<>();
        numCatalogedCopies = 0;
        books = new DictionaryAVLImpl<>();
        storedBooks = new DictionaryAVLImpl<>();
        mostReadBooks = new OrderedVector<>(5, CatalogedBook.CMP_R);
        mostRatedBooks = new OrderedVector<>(5, CatalogedBook.CMP_BOOKID);
        mostFrequentReader = new OrderedVector<>(5, Reader.CMP_V);
        readersBestRated = new OrderedVector<>(5, Reader.CMP_L);
        destroyedCopies = new DSLinkedList<>(Copy.CMP);
        authors = new HashTable<>();
        themes = new DSArray<>(MAX_NUM_THEMES);
        loans = new DictionaryAVLImpl<>();
        bookGraph = new UnDirectedGraphImpl<>();
    }

    @Override
    public void addReader(String id, String name, String surname, String docId, LocalDate birthDate, String birthPlace, String address, int points) {
        Reader reader = getReader(id);
        if (reader == null) {
            reader = new Reader(id, name, surname, docId, birthDate, birthPlace, address, points);
            this.readers.put(id, reader);
        } else {
            reader.update(name, surname, docId, birthDate, birthPlace, address, points);
        }
    }

    public void addWorker(String id, String name, String surname) {
        Worker worker = getWorker(id);
        if (worker == null) {
            worker = new Worker(id, name, surname);
            this.workers.put(id, worker);
        } else {
            worker.update(name, surname);
        }
    }

    public void storeCopy(String copyId, String title, String publisher, String edition, int publicationYear, String isbn, String author, String theme) throws AuthorNotFoundException, ThemeNotFoundException {
        ///  Create all the needed instances
        Author authorInst = authors.get(author);
        Theme themeInst = themes.get(theme);
        Book bookInst = new Book(copyId, title, publisher, edition, publicationYear, isbn, authorInst, themeInst);
        CatalogedBook catalogedBookInst = new CatalogedBook(bookInst);

        /// store the copy to the book warehouse and add the book to the needed variable
        bookWareHouse.storeCopy(copyId, title, publisher, edition, publicationYear, isbn, authorInst, themeInst);
        storedBooks.put(isbn, catalogedBookInst);
        authorInst.addBook(bookInst);
    }


    public Copy catalogCopy(String workerId) throws NoBookException, WorkerNotFoundException {
        /// Retrieve the worker that is cataloging the copy
        Worker worker = getWorker2(workerId);

        /// If there are no books on the book warehouse, throw exception
        if(bookWareHouse.isEmpty()) {
            throw new NoBookException();
        }

        /// Retrieve the next copy to catalog
        Copy cp = bookWareHouse.getBookPendingCataloging();

        /// Retrieve (if there is) the CatalogedBook that matches
        /// the ISBN of the copy
        CatalogedBook catalogedBook = getCatalogedBook(cp.getIsbn());

        if (catalogedBook == null) {
            /// If the cataloged book doesn't exist, create a new instance
            catalogedBook = new CatalogedBook(cp);
            books.put(cp.getIsbn(), catalogedBook);
            worker.catalogBook(catalogedBook);
        } else {
            worker.incTotalCatalogBooks();
        }

        /// Add the copy to the Cataloged Book (created or retrieved from before)
        catalogedBook.addCopy(cp);

        /// Update the data structure
        catalogedCopies.put(cp.getCopyId(), cp);
        numCatalogedCopies++;

        return cp;
    }

    @Override
    public Request lendCopy(String loanId, String copyId, String workerId, LocalDate date, LocalDate expirationDate)
            throws CopyNotFoundException, WorkerNotFoundException, MaximumNumberOfBooksException, NoRequestException,
            CopyNotAvailableException {
        /// Retrieve the worker and the copy or throws respective exceptions
        Worker worker = getWorker2(workerId);
        Copy copy = getCopy2(copyId);

        /// Check if the copy is unavailable
        if(!copy.isAvailable()){
            throw new CopyNotAvailableException();
        }
        /// Check if there are requests of that copy
        if (copy.getRequests().isEmpty()) {
            throw new NoRequestException();
        }

        /// Retrieve the reader that has requested the copy,
        /// and has the highest points
        Reader reader = copy.getHighestReader();

        /// Check if it already has the maximum number of books loaned
        if(reader.hasMaximumNumberOfBooks()) {
            throw new MaximumNumberOfBooksException();
        }

        /// Retrieve the request from the reader and the copy
        Request request = reader.getRequest(copyId);

        /// Create a new loan instance and add update the data structure
        Loan loan = reader.addNewLoan(loanId, copy, date, expirationDate);

        loan.addReader(reader);
        worker.addLoan(loan);
        copy.setUnavailable();
        copy.addLoan(loan);

        /// Decrease the number of available copies
        numCatalogedCopies--;

        /// Retrieve the cataloged book of that copy and remove the copy to its copies list
        CatalogedBook catalogedBook = getCatalogedBook(copy.getIsbn());
        catalogedBook.removeCopy(copy);

        /// Add the new loan to the loans
        loans.put(loanId, loan);
        mostReadBooks.update(catalogedBook);
        return request;
    }

    public Loan giveBackBook(String loanId, LocalDate date, CopyReturnStatus status) throws LoanNotFoundException {
        /// Retrieve all the needed data
        Loan loan = getLoan2(loanId);
        Reader reader = loan.getReader();
        Worker worker = loan.getWorker();
        Copy copy = loan.getCopy();
        CatalogedBook catalogedBook = books.get(loan.getIsbn());

        /// Set the total points to increase or decrease the points of the reader
        int totalPoints = 0;

        /// Assign the adequate points to the total points and modify the data structure
        if(status == CopyReturnStatus.GOOD) {
            totalPoints += POINTS_GOOD;
            catalogedBook.incCopies();
            catalogedBook.addCopy(copy);
            numCatalogedCopies++;
        } else if(status == CopyReturnStatus.BAD) {
            totalPoints += POINTS_BAD;
            catalogedBook.incCopies();
            catalogedBook.addCopy(copy);
            numCatalogedCopies++;
        } else if (status == CopyReturnStatus.DESTROYED){
            totalPoints += POINTS_DESTROYED;
            destroyedCopies.insertEnd(copy);
        }

        /// Assign the adequate points to the total points and set the state of the loan
        if (loan.isDelayed(date)) {
            totalPoints += POINTS_DELAYED;
            loan.setState(LoanState.DELAYED);
        } else {
            totalPoints += POINTS_COMPLETED;
            loan.setState(LoanState.COMPLETED);
        }

        /// Add the recently closed loan to the reader and worker
        reader.addClosedLoan(loan);
        worker.addClosedLoan(loan);

        /// Update the Ordered Lists
        Iterator<Reader> bestReaders = mostFrequentReader.values();
        while(bestReaders.hasNext()) {
            Reader currReader = bestReaders.next();
            if(currReader.equals(reader)) {
                readersBestRated.delete(currReader);
                mostFrequentReader.delete(currReader);
            }
        }

        readersBestRated.update(reader);
        mostFrequentReader.update(reader);

        /// Update the points of the reader
        reader.addPoints(totalPoints);

        return loan;
    }

    public int timeToBeCataloged(String copyId, int lotPreparationTime, int bookCatalogTime) throws BookNotFoundException, InvalidLotPreparationTimeException, InvalidCatalogTimeException {
        if (lotPreparationTime < 0) {
            throw new InvalidLotPreparationTimeException();
        }
        if (bookCatalogTime < 0) {
            throw new InvalidCatalogTimeException();
        }

        BookWareHouse.Position position = bookWareHouse.getPosition(copyId);
        if (position == null) {
            throw new BookNotFoundException();
        }

        int previousStacks = position.getNumStack();
        int numberInStack = position.getNum();

        int t1 = previousStacks * (lotPreparationTime + (MAX_BOOK_STACK * bookCatalogTime));
        int t2 = lotPreparationTime + numberInStack + bookCatalogTime;

        return t1 + t2;
    }

    public Iterator<Loan> getAllLoansByReader(String readerId) throws NoLoansException {
        Reader reader = getReader(readerId);
        Iterator<Loan> loanIterator = reader.getAllLoans();
        if (!loanIterator.hasNext()) {
            throw new NoLoansException();
        }

        return loanIterator;
    }

    public Iterator<Loan> getAllLoansByState(String readerId, LoanState state) throws NoLoansException {
        Reader reader = getReader(readerId);
        Iterator<Loan> loanIterator = reader.getAllLoansByState(state);
        if (!loanIterator.hasNext()) {
            throw new NoLoansException();
        }

        return loanIterator;
    }

    public Iterator<Loan> getAllLoansByCopy(String copyId) throws NoLoansException {
        Copy copy = getCopy(copyId);
        Iterator<Loan> loanIt = null;
        if (copy != null) {
            loanIt = copy.getLoanIterator();
            if (!loanIt.hasNext()) {
                throw new NoLoansException();
            }
        } else {
            throw new NoLoansException();
        }

        return loanIt;
    }

    public Reader getReaderTheMost() throws NoReaderException {
        if (mostFrequentReader.isEmpty()) {
            throw new NoReaderException();
        }
        return mostFrequentReader.elementAt(0);
    }

    public Book getMostReadBook() throws NoBookException {
        if (mostReadBooks.isEmpty()) {
            throw new NoBookException();
        }
        CatalogedBook catalogedBook = mostReadBooks.elementAt(0);
        return catalogedBook.getBook();
    }


    /***********************************************************************************/
    /******************** AUX OPERATIONS  **********************************************/
    /***********************************************************************************/
    @Override
    public Reader getReader(String id) {
        return readers.get(id);
    }

    protected Reader getReader2(String id) throws ReaderNotFoundException {
        Reader reader = getReader(id);
        if (reader == null) {
            throw new ReaderNotFoundException();
        }
        return reader;
    }

    @Override
    public int numReaders() {
        return readers.size();
    }

    @Override
    public Worker getWorker(String id) {
        return workers.get(id);
    }

    private Worker getWorker2(String id) throws WorkerNotFoundException {
        Worker worker = getWorker(id);
        if (worker == null) {
            throw new WorkerNotFoundException();
        }
        return worker;
    }

    public Copy getCopy(String bookId) {
        return catalogedCopies.get(bookId);
    }
    public Copy getCopy2(String bookId) throws CopyNotFoundException {
        Copy copy = getCopy(bookId);
        if (copy == null){
            throw new CopyNotFoundException();
        }
        return copy;
    }

    public Loan getLoan(String loanId) {
        return loans.get(loanId);
    }

    public Loan getLoan2(String loanId) throws LoanNotFoundException {
        Loan loan = getLoan(loanId);
        if (loan == null) {
            throw new LoanNotFoundException();
        }
        return loan;
    }


    @Override
    public int numWorkers() {
        return workers.size();
    }

    public int numCopies() {
        return bookWareHouse.numCopies();
    }

    public int numCopies(String isbn) {
        CatalogedBook book = getCatalogedBook(isbn);
        return book.getCopies();
    }

    public int numStacks() {
        return bookWareHouse.numStacks();
    }

    protected CatalogedBook getCatalogedBook(String isbn) {
        return books.get(isbn);
    }

    public CatalogedBook getCatalogedBook2(String isbn) throws BookNotFoundException {
        CatalogedBook catalogedBook = getCatalogedBook(isbn);
        if (catalogedBook == null) {
            throw new BookNotFoundException();
        }
        return catalogedBook;
    }

    public int numCatalogBooks() {
        return books.size();
    }

    public int numCatalogCopies() {
        return numCatalogedCopies;
    }


    public int numCatalogCopiesByWorker(String workerId) {
        Worker worker = this.workers.get(workerId);
        if (worker != null) {
            return worker.totalCatalogBooks();
        }
        return 0;
    }

    public int numCatalogBooksByWorker(String workerId) {
        Worker worker = this.workers.get(workerId);
        if (worker != null) {
            return worker.numCatalogBooks();
        }
        return 0;
    }


    public int numAvailableCopies(String isbn) {
        CatalogedBook catalogedBook = getCatalogedBook(isbn);
        return catalogedBook.getCopies();
    }


    public int numLoans() {
        return loans.size();
    }

    public int numLoansByWorker(String workerId) {
        return getWorker(workerId).numLoans();
    }

    public int numClosedLoansByWorker(String workerId) {
        return getWorker(workerId).numClosedLoans();
    }

    public int numLoansByCopy(String copyId) {
        Copy copy = getCopy(copyId);
        return copy.getLoans().size();
    }

    public int numCurrentLoansByReader(String readerId) {
        return getReader(readerId).numLoans();
    }

    public int numClosedLoansByReader(String readerId) {
        return getReader(readerId).numClosedLoans();
    }

    public int numDestroyedLoans() {
        return destroyedCopies.size();
    }
}
