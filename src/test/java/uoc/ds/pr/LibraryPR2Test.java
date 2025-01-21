package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.BookWareHouseTest;
import uoc.ds.pr.data.BooksDataSet;
import uoc.ds.pr.util.DateUtils;

import static uoc.ds.pr.data.BooksMetaData.addAuthorData;
import static uoc.ds.pr.data.BooksMetaData.addThemeData;
import static uoc.ds.pr.util.BooksDataHelper.addBooksData;

public class LibraryPR2Test {

    protected LibraryPR3 theLibrary;

    @Before
    public void setUp() throws Exception {
        this.theLibrary = FactoryLibrary.getLibrary();
        Assert.assertEquals(17, this.theLibrary.numReaders());
    }

    @After
    public void tearDown() {
        this.theLibrary = null;
    }


    @Test
    public void addReaderTest() {
        Assert.assertEquals(17, this.theLibrary.numReaders());
        theLibrary.addReader("workerId18", "Jesus", "Linda", "979933A", DateUtils.createLocalDate("21-11-2001"), "Genova", "the street", 35);
        Assert.assertEquals(18, this.theLibrary.numReaders());

        theLibrary.addReader("workerId19", "XXXXX", "Linda", "979933A", DateUtils.createLocalDate("21-11-2001"), "Genova", "the street", 50);
        Assert.assertEquals(19, this.theLibrary.numReaders());

        theLibrary.addReader("workerId19", "Carles", "Fluvià", "97333A", DateUtils.createLocalDate("21-11-2011"), "Napoles", "the street", 25);
        Assert.assertEquals(19, this.theLibrary.numReaders());
    }

    @Test
    public void addWorkerTest() {
        Assert.assertEquals(4, this.theLibrary.numWorkers());
        theLibrary.addWorker("workerId5", "Tato", "y Compañía");
        Assert.assertEquals(5, this.theLibrary.numWorkers());

        theLibrary.addWorker("workerId6", "XXXXX", "YYYYY");
        Assert.assertEquals(6, this.theLibrary.numWorkers());

        theLibrary.addWorker("workerId6", "Tania", "y Compañía");
        Assert.assertEquals(6, this.theLibrary.numWorkers());
    }

    @Test
    public void addThemeTest() {
        Assert.assertEquals(0, this.theLibrary.numThemes());
        theLibrary.addTheme("THEME1", "Fiction");
        Assert.assertEquals(1, this.theLibrary.numThemes());

        theLibrary.addTheme("THEME2", "N0n-ficicioocicicoiciocn");
        Assert.assertEquals(2, this.theLibrary.numThemes());

        Theme theme = theLibrary.getTheme("THEME2");
        Assert.assertNotEquals("Non-fiction", theme.getName());

        theLibrary.addTheme("THEME2", "Non-fiction");
        Assert.assertEquals(2, this.theLibrary.numThemes());

        addThemeData(theLibrary);
        Assert.assertEquals(29, this.theLibrary.numThemes());

        theme = theLibrary.getTheme("THEME2");
        Assert.assertEquals("Non-fiction", theme.getName());

    }


    @Test
    public void addAuthorTest() {
        Assert.assertEquals(0, this.theLibrary.numAuthors());
        theLibrary.addAuthor("JV", "Jules", "Verne");
        Assert.assertEquals(1, this.theLibrary.numAuthors());

        theLibrary.addAuthor("J.K.R", "J.K.", "Roooooowling");
        Assert.assertEquals(2, this.theLibrary.numAuthors());

        Author author = theLibrary.getAuthor("J.K.R");
        Assert.assertNotEquals("Rowling", author.getName());

        theLibrary.addAuthor("J.K.R", "J.K.", "Rowling");
        Assert.assertEquals(2, this.theLibrary.numAuthors());

        addAuthorData(theLibrary);
        Assert.assertEquals(8, this.theLibrary.numAuthors());

        author = theLibrary.getAuthor("J.K.R");
        Assert.assertEquals("J.K.", author.getName());

    }


    /**
     * @see BooksDataSet
     * @see BookWareHouseTest#storeBookTest()
     */
    @Test
    public void storeCopyTest() throws DSException {

        addAuthorTest();
        addThemeTest();

        addBooksData(theLibrary, BooksDataSet.booksData1);
        Assert.assertEquals(15, theLibrary.numCopies());
        Assert.assertEquals(2, theLibrary.numStacks());

        addBooksData(theLibrary, BooksDataSet.booksData2);
        Assert.assertEquals(4, theLibrary.numStacks());
        Assert.assertEquals(31, theLibrary.numCopies());

        addBooksData(theLibrary, BooksDataSet.booksData3);
        Assert.assertEquals(5, theLibrary.numStacks());
        Assert.assertEquals(49, theLibrary.numCopies());

        addBooksData(theLibrary, BooksDataSet.booksData4);
        Assert.assertEquals(7, theLibrary.numStacks());
        Assert.assertEquals(67, theLibrary.numCopies());

        addBooksData(theLibrary, BooksDataSet.booksData5);
        Assert.assertEquals(9, theLibrary.numStacks());
        Assert.assertEquals(86, theLibrary.numCopies());

        addBooksData(theLibrary, BooksDataSet.booksData6);
        Assert.assertEquals(12, theLibrary.numStacks());
        Assert.assertEquals(115, theLibrary.numCopies());

        addBooksData(theLibrary, BooksDataSet.booksData7);
        Assert.assertEquals(13, theLibrary.numStacks());
        Assert.assertEquals(130, theLibrary.numCopies());

        theLibrary.addAuthor("FK", "Franz", "Kafka");
        theLibrary.addTheme("THEME30", "existentialism");

        theLibrary.storeCopy("CP1", "Die Verwandlung", "Die Weißen Blätter ",
                "First Edition", 1915, "978-84-8256-840-9", "FK", "THEME30");

        Assert.assertEquals(14, theLibrary.numStacks());
        Assert.assertEquals(131, theLibrary.numCopies());

    }


    @Test
    public void catalogCopyTest() throws DSException {
        Assert.assertThrows(WorkerNotFoundException.class, () ->
                theLibrary.catalogCopy("XXXX"));


        Assert.assertThrows(NoBookException.class, () ->
                theLibrary.catalogCopy("workerId1"));

        storeCopyTest();

        Assert.assertEquals(14, theLibrary.numStacks());
        Assert.assertEquals(131, theLibrary.numCopies());


        Copy cp = null;
        cp = theLibrary.catalogCopy("workerId1");
        Assert.assertEquals("978-0451530960", cp.getIsbn());
        Assert.assertEquals("Twenty Thousand Leagues Under the Sea", cp.getTitle());
        Assert.assertEquals("JV2c", cp.getCopyId());
        Assert.assertEquals(1, theLibrary.numCopies("978-0451530960"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-0451530960", cp.getIsbn());
        Assert.assertEquals("Twenty Thousand Leagues Under the Sea", cp.getTitle());
        Assert.assertEquals("JV2b", cp.getCopyId());
        Assert.assertEquals(2, theLibrary.numCopies("978-0451530960"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-1853260257", cp.getIsbn());
        Assert.assertEquals("The Adventures of Captain Hatteras", cp.getTitle());
        Assert.assertEquals("JV5", cp.getCopyId());
        Assert.assertEquals(1, theLibrary.numCopies("978-1853260257"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-1605062234", cp.getIsbn());
        Assert.assertEquals("The Steam House", cp.getTitle());
        Assert.assertEquals("JV7c", cp.getCopyId());
        Assert.assertEquals(1, theLibrary.numCopies("978-1605062234"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-1103325575", cp.getIsbn());
        Assert.assertEquals("The Begum's Fortune", cp.getTitle());
        Assert.assertEquals("JV8", cp.getCopyId());
        Assert.assertEquals(1, theLibrary.numCopies("978-1103325575"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-1435149408", cp.getIsbn());
        Assert.assertEquals("The Mysterious Island", cp.getTitle());
        Assert.assertEquals("JV4c", cp.getCopyId());
        Assert.assertEquals(1, theLibrary.numCopies("978-1435149408"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-1516887907", cp.getIsbn());
        Assert.assertEquals("Around the World in Eighty Days", cp.getTitle());
        Assert.assertEquals("JV3", cp.getCopyId());
        Assert.assertEquals(1, theLibrary.numCopies("978-1516887907"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-0486268685", cp.getIsbn());
        Assert.assertEquals("Journey to the Center of the Earth", cp.getTitle());
        Assert.assertEquals("JV1", cp.getCopyId());
        Assert.assertEquals(1, theLibrary.numCopies("978-0486268685"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-1435149408", cp.getIsbn());
        Assert.assertEquals("The Mysterious Island", cp.getTitle());
        Assert.assertEquals("JV4a", cp.getCopyId());
        Assert.assertEquals(2, theLibrary.numCopies("978-1435149408"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-1605062234", cp.getIsbn());
        Assert.assertEquals("The Steam House", cp.getTitle());
        Assert.assertEquals("JV7d", cp.getCopyId());
        Assert.assertEquals(2, theLibrary.numCopies("978-1605062234"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-0747581086", cp.getIsbn());
        Assert.assertEquals("Harry Potter and the Half-Blood Prince", cp.getTitle());
        Assert.assertEquals("HP6c", cp.getCopyId());
        Assert.assertEquals(1, theLibrary.numCopies("978-0747581086"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-0747542155", cp.getIsbn());
        Assert.assertEquals("Harry Potter and the Prisoner of Azkaban", cp.getTitle());
        Assert.assertEquals("HP3", cp.getCopyId());
        Assert.assertEquals(1, theLibrary.numCopies("978-0747542155"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-0747581086", cp.getIsbn());
        Assert.assertEquals("Harry Potter and the Half-Blood Prince", cp.getTitle());
        Assert.assertEquals("HP6d", cp.getCopyId());
        Assert.assertEquals(2, theLibrary.numCopies("978-0747581086"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-0747538493", cp.getIsbn());
        Assert.assertEquals("Harry Potter and the Chamber of Secrets", cp.getTitle());
        Assert.assertEquals("HP2", cp.getCopyId());
        Assert.assertEquals(1, theLibrary.numCopies("978-0747538493"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-0747532699", cp.getIsbn());
        Assert.assertEquals("Harry Potter and the Philosopher's Stone", cp.getTitle());
        Assert.assertEquals("HP1", cp.getCopyId());
        Assert.assertEquals(1, theLibrary.numCopies("978-0747532699"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-1605062234", cp.getIsbn());
        Assert.assertEquals("The Steam House", cp.getTitle());
        Assert.assertEquals("JV7b", cp.getCopyId());
        Assert.assertEquals(3, theLibrary.numCopies("978-1605062234"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-1435149408", cp.getIsbn());
        Assert.assertEquals("The Mysterious Island", cp.getTitle());
        Assert.assertEquals("JV4b", cp.getCopyId());
        Assert.assertEquals(3, theLibrary.numCopies("978-1435149408"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-1605062234", cp.getIsbn());
        Assert.assertEquals("The Steam House", cp.getTitle());
        Assert.assertEquals("JV7a", cp.getCopyId());
        Assert.assertEquals(4, theLibrary.numCopies("978-1605062234"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-0199538474", cp.getIsbn());
        Assert.assertEquals("From the Earth to the Moon", cp.getTitle());
        Assert.assertEquals("JV6", cp.getCopyId());
        Assert.assertEquals(1, theLibrary.numCopies("978-0199538474"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-0451530960", cp.getIsbn());
        Assert.assertEquals("Twenty Thousand Leagues Under the Sea", cp.getTitle());
        Assert.assertEquals("JV2a", cp.getCopyId());
        Assert.assertEquals(3, theLibrary.numCopies("978-0451530960"));


        Assert.assertEquals(1, theLibrary.numCatalogBooksByWorker("workerId1"));
        Assert.assertEquals(11, theLibrary.numCatalogBooksByWorker("workerId2"));
        Assert.assertEquals(1, theLibrary.numCatalogCopiesByWorker("workerId1"));
        Assert.assertEquals(19, theLibrary.numCatalogCopiesByWorker("workerId2"));


        cp = theLibrary.catalogCopy("workerId1");
        Assert.assertEquals("978-0747581086", cp.getIsbn());
        Assert.assertEquals("Harry Potter and the Half-Blood Prince", cp.getTitle());
        Assert.assertEquals("HP6f", cp.getCopyId());
        Assert.assertEquals(3, theLibrary.numCopies("978-0747581086"));


        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-0747581086", cp.getIsbn());
        Assert.assertEquals("Harry Potter and the Half-Blood Prince", cp.getTitle());
        Assert.assertEquals("HP6e", cp.getCopyId());
        Assert.assertEquals(4, theLibrary.numCopies("978-0747581086"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-0747581086", cp.getIsbn());
        Assert.assertEquals("Harry Potter and the Half-Blood Prince", cp.getTitle());
        Assert.assertEquals("HP6a", cp.getCopyId());
        Assert.assertEquals(5, theLibrary.numCopies("978-0747581086"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-0747551003", cp.getIsbn());
        Assert.assertEquals("Harry Potter and the Order of the Phoenix", cp.getTitle());
        Assert.assertEquals("HP5", cp.getCopyId());
        Assert.assertEquals(1, theLibrary.numCopies("978-0747551003"));

        cp = theLibrary.catalogCopy("workerId2");
        Assert.assertEquals("978-0747581086", cp.getIsbn());
        Assert.assertEquals("Harry Potter and the Half-Blood Prince", cp.getTitle());
        Assert.assertEquals("HP6b", cp.getCopyId());
        Assert.assertEquals(6, theLibrary.numCopies("978-0747581086"));

        cp = theLibrary.catalogCopy("workerId1");
        Assert.assertEquals("978-0747546245", cp.getIsbn());
        Assert.assertEquals("Harry Potter and the Goblet of Fire", cp.getTitle());
        Assert.assertEquals("HP4", cp.getCopyId());
        Assert.assertEquals(1, theLibrary.numCopies("978-0747546245"));

        cp = theLibrary.catalogCopy("workerId3");
        Assert.assertEquals("978-0747546245", cp.getIsbn());
        Assert.assertEquals("Harry Potter and the Goblet of Fire", cp.getTitle());
        Assert.assertEquals("HP4d", cp.getCopyId());
        Assert.assertEquals(2, theLibrary.numCopies("978-0747546245"));

        cp = theLibrary.catalogCopy("workerId3");
        Assert.assertEquals("978-0747546245", cp.getIsbn());
        Assert.assertEquals("Harry Potter and the Goblet of Fire", cp.getTitle());
        Assert.assertEquals("HP4c", cp.getCopyId());
        Assert.assertEquals(3, theLibrary.numCopies("978-0747546245"));

        cp = theLibrary.catalogCopy("workerId3");
        Assert.assertEquals("978-0747546245", cp.getIsbn());
        Assert.assertEquals("Harry Potter and the Goblet of Fire", cp.getTitle());
        Assert.assertEquals("HP4b", cp.getCopyId());
        Assert.assertEquals(4, theLibrary.numCopies("978-0747546245"));

        cp = theLibrary.catalogCopy("workerId3");
        Assert.assertEquals("978-0747546245", cp.getIsbn());
        Assert.assertEquals("Harry Potter and the Goblet of Fire", cp.getTitle());
        Assert.assertEquals("HP4a", cp.getCopyId());
        Assert.assertEquals(5, theLibrary.numCopies("978-0747546245"));

        Assert.assertEquals(2, theLibrary.numCatalogBooksByWorker("workerId1"));
        Assert.assertEquals(12, theLibrary.numCatalogBooksByWorker("workerId2"));
        Assert.assertEquals(0, theLibrary.numCatalogBooksByWorker("workerId3"));
        Assert.assertEquals(3, theLibrary.numCatalogCopiesByWorker("workerId1"));
        Assert.assertEquals(23, theLibrary.numCatalogCopiesByWorker("workerId2"));
        Assert.assertEquals(4, theLibrary.numCatalogCopiesByWorker("workerId3"));

        Assert.assertEquals(11, theLibrary.numStacks());
        Assert.assertEquals(14, theLibrary.numCatalogBooks());
        Assert.assertEquals(30, theLibrary.numCatalogCopies());
        Assert.assertEquals(101, theLibrary.numCopies());


    }

    @Test
    public void createRequestTest() throws DSException {

        this.catalogCopyTest();

        Assert.assertThrows(ReaderNotFoundException.class, () ->
                theLibrary.createRequest("xxxxx", "JV2c",
                        DateUtils.createLocalDate("01-11-2024")));

        Assert.assertThrows(CopyNotFoundException.class, () ->
                theLibrary.createRequest("readerId1", "YYYYYY",
                        DateUtils.createLocalDate("01-11-2024")));


        theLibrary.createRequest("readerId1", "JV2c",
                DateUtils.createLocalDate("01-11-2024"));

        Assert.assertThrows(ReaderAlreadyHasRequest4Copy.class, () ->
                theLibrary.createRequest("readerId1", "JV2c",
                        DateUtils.createLocalDate("01-11-2024")));


        theLibrary.createRequest("readerId2", "JV2c",
                DateUtils.createLocalDate("01-11-2024"));

        // reader4.points = -7
        theLibrary.createRequest("readerId4", "JV2b",
                DateUtils.createLocalDate("01-11-2024"));

        // reader4.points = 15
        theLibrary.createRequest("readerId3", "JV2b",
                DateUtils.createLocalDate("01-11-2024"));

        // reader4.points = -5
        theLibrary.createRequest("readerId1", "JV2b",
                DateUtils.createLocalDate("01-11-2024"));

        // reader2.points = 60
        theLibrary.createRequest("readerId2", "JV2b",
                DateUtils.createLocalDate("01-11-2024"));

        // reader4.points = 15
        theLibrary.createRequest("readerId3", "JV2a",
                DateUtils.createLocalDate("01-11-2024"));

        // reader1.points = -5
        theLibrary.createRequest("readerId1", "JV2a",
                DateUtils.createLocalDate("01-11-2024"));


        // reader1.points = -5
        theLibrary.createRequest("readerId1", "JV1",
                DateUtils.createLocalDate("01-11-2024"));


        // reader2.points = 60
        theLibrary.createRequest("readerId2", "JV7c",
                DateUtils.createLocalDate("01-11-2024"));

        // reader2.points = 60
        theLibrary.createRequest("readerId2", "JV7d",
                DateUtils.createLocalDate("01-11-2024"));

        // reader2.points = 60
        theLibrary.createRequest("readerId2", "JV3",
                DateUtils.createLocalDate("02-11-2024"));





    }

    @Test
    public void lendCopyTest() throws DSException {

        Assert.assertThrows(CopyNotFoundException.class, () ->
                theLibrary.lendCopy("LOAN1", "JV2a", "workerId1",
                        DateUtils.createLocalDate("01-11-2024"), DateUtils.createLocalDate("15-11-2024")));

        Assert.assertThrows(WorkerNotFoundException.class, () ->
                theLibrary.lendCopy("LOAN1",  "JV2a", "xxxx",
                        DateUtils.createLocalDate("01-11-2024"), DateUtils.createLocalDate("15-11-2024")));

        Assert.assertThrows(CopyNotFoundException.class, () ->
                theLibrary.lendCopy("LOAN1",  "XXXX", "workerId1",
                        DateUtils.createLocalDate("01-11-2024"), DateUtils.createLocalDate("15-11-2024")));

        createRequestTest();

        Assert.assertThrows(NoRequestException.class, () ->
                theLibrary.lendCopy("LOAN1",  "HP6c", "workerId1",
                        DateUtils.createLocalDate("01-11-2024"), DateUtils.createLocalDate("15-11-2024")));


        Assert.assertEquals(30, theLibrary.numCatalogCopies());
        Assert.assertEquals(3, theLibrary.numAvailableCopies("978-0451530960"));
        Request request1 = theLibrary.lendCopy("LOAN1",  "JV2b", "workerId1",
                DateUtils.createLocalDate("01-11-2024"), DateUtils.createLocalDate("15-11-2024"));
        Assert.assertEquals(29, theLibrary.numCatalogCopies());
        Assert.assertEquals(2, theLibrary.numAvailableCopies("978-0451530960"));
        Assert.assertEquals("readerId2", request1.getReader().getId());
        Assert.assertEquals(60, request1.getReader().getPoints());
        Assert.assertEquals(LibraryPR3.Level.ASLAN, request1.getReader().getLevel());

        Assert.assertThrows(CopyNotAvailableException.class, () ->
                theLibrary.lendCopy("LOAN2",  "JV2b", "workerId1",
                        DateUtils.createLocalDate("01-11-2024"), DateUtils.createLocalDate("15-11-2024")));

        Assert.assertEquals(2, theLibrary.numAvailableCopies("978-0451530960"));
        Request request2 = theLibrary.lendCopy("LOAN2",  "JV2a", "workerId1",
                DateUtils.createLocalDate("04-11-2024"), DateUtils.createLocalDate("17-11-2024"));
        Assert.assertEquals(1, theLibrary.numAvailableCopies("978-0451530960"));
        Assert.assertEquals("readerId3", request2.getReader().getId());
        Assert.assertEquals(15, request2.getReader().getPoints());
        Assert.assertEquals(LibraryPR3.Level.HOBBIT, request2.getReader().getLevel());

        Request request3 = theLibrary.lendCopy("LOAN3",  "JV1", "workerId1",
                DateUtils.createLocalDate("04-11-2024"), DateUtils.createLocalDate("17-11-2024"));
        Assert.assertEquals(0, theLibrary.numAvailableCopies("978-0486268685"));
        Assert.assertEquals("readerId1", request3.getReader().getId());
        Assert.assertEquals(-5, request3.getReader().getPoints());
        Assert.assertEquals(LibraryPR3.Level.MUGGLE, request3.getReader().getLevel());

        Assert.assertEquals(4, theLibrary.numAvailableCopies("978-1605062234"));
        Request request4 = theLibrary.lendCopy("LOAN4",  "JV7c", "workerId1",
                DateUtils.createLocalDate("06-11-2024"), DateUtils.createLocalDate("18-11-2024"));
        Assert.assertEquals(3, theLibrary.numAvailableCopies("978-1605062234"));
        Assert.assertEquals("readerId2", request4.getReader().getId());
        Assert.assertEquals(60, request4.getReader().getPoints());
        Assert.assertEquals(LibraryPR3.Level.ASLAN, request4.getReader().getLevel());

        Assert.assertEquals(3, theLibrary.numAvailableCopies("978-1605062234"));
        Request request5 = theLibrary.lendCopy("LOAN5",  "JV7d", "workerId1",
                DateUtils.createLocalDate("06-11-2024"), DateUtils.createLocalDate("18-11-2024"));
        Assert.assertEquals(2, theLibrary.numAvailableCopies("978-1605062234"));
        Assert.assertEquals("readerId2", request5.getReader().getId());
        Assert.assertEquals(60, request5.getReader().getPoints());
        Assert.assertEquals(LibraryPR3.Level.ASLAN, request5.getReader().getLevel());


        Assert.assertEquals(3, theLibrary.numCurrentLoansByReader("readerId2"));


        Assert.assertThrows(MaximumNumberOfBooksException.class, () ->
                theLibrary.lendCopy("LOAN6",  "JV3", "workerId2",
                        DateUtils.createLocalDate("07-11-2024"), DateUtils.createLocalDate("27-11-2024")));


        Assert.assertEquals(1, theLibrary.numAvailableCopies("978-0451530960"));
        Assert.assertEquals(5, theLibrary.numLoansByWorker("workerId1"));
        Assert.assertEquals(1, theLibrary.numCurrentLoansByReader("readerId1"));
        Assert.assertEquals(3, theLibrary.numCurrentLoansByReader("readerId2"));
        Assert.assertEquals(1, theLibrary.numCurrentLoansByReader("readerId3"));
        Assert.assertEquals(5, theLibrary.numLoans());
        Assert.assertEquals(0, theLibrary.numLoansByCopy("JV2c"));
        Assert.assertEquals(1, theLibrary.numLoansByCopy("JV7c"));
        Assert.assertEquals(1, theLibrary.numLoansByCopy("JV1"));
        Assert.assertEquals(25, theLibrary.numCatalogCopies());


    }


    @Test
    public void giveBackBookTest() throws DSException {
        Assert.assertThrows(LoanNotFoundException.class, () ->
                theLibrary.giveBackBook("XXXX",  DateUtils.createLocalDate("10-11-2024"), Library.CopyReturnStatus.GOOD));

        Assert.assertEquals(0, theLibrary.numCatalogCopies());
        lendCopyTest();
        Assert.assertEquals(25, theLibrary.numCatalogCopies());
        Assert.assertEquals(3, theLibrary.numCurrentLoansByReader("readerId2"));
        Assert.assertEquals(0, theLibrary.numClosedLoansByReader("readerId2"));

        Reader readerId2 = theLibrary.getReader("readerId2");
        Assert.assertEquals(60, readerId2.getPoints());


        Assert.assertEquals(2, theLibrary.numAvailableCopies("978-1605062234"));
        Loan loan5 = theLibrary.giveBackBook("LOAN5",
                DateUtils.createLocalDate("10-11-2024"), Library.CopyReturnStatus.GOOD);
        Assert.assertEquals("readerId2", loan5.getReader().getId());
        Assert.assertEquals(Library.LoanState.COMPLETED, loan5.getState());
        Assert.assertEquals(3, theLibrary.numAvailableCopies("978-1605062234"));
        Assert.assertEquals(60 + Library.POINTS_COMPLETED+Library.POINTS_GOOD, readerId2.getPoints());
        Assert.assertEquals(26, theLibrary.numCatalogCopies());
        Assert.assertEquals(0, theLibrary.numClosedLoansByReader("readerId1"));
        Assert.assertEquals(1, theLibrary.numClosedLoansByReader("readerId2"));
        Assert.assertEquals(1, theLibrary.numCurrentLoansByReader("readerId1"));
        Assert.assertEquals(2, theLibrary.numCurrentLoansByReader("readerId2"));
        Assert.assertEquals(1, theLibrary.numClosedLoansByWorker("workerId1"));

        Reader readerId1 = theLibrary.getReader("readerId1");
        Assert.assertEquals(-5, readerId1.getPoints());


        Assert.assertEquals(0, theLibrary.numAvailableCopies("978-0486268685"));

        Loan loan3 = theLibrary.giveBackBook("LOAN3",
                DateUtils.createLocalDate("20-11-2024"), Library.CopyReturnStatus.GOOD);
        Assert.assertEquals("readerId1", loan3.getReader().getId());
        Assert.assertEquals(Library.LoanState.DELAYED, loan3.getState());
        Assert.assertEquals(1, theLibrary.numAvailableCopies("978-0486268685"));
        Assert.assertEquals(-5 + Library.POINTS_DELAYED+Library.POINTS_GOOD, readerId1.getPoints());
        Assert.assertEquals(27, theLibrary.numCatalogCopies());
        Assert.assertEquals(1, theLibrary.numClosedLoansByReader("readerId1"));
        Assert.assertEquals(1, theLibrary.numClosedLoansByReader("readerId2"));
        Assert.assertEquals(2, theLibrary.numClosedLoansByWorker("workerId1"));
        Assert.assertEquals(0, theLibrary.numCurrentLoansByReader("readerId1"));
        Assert.assertEquals(2, theLibrary.numCurrentLoansByReader("readerId2"));

        Assert.assertEquals(1, theLibrary.numAvailableCopies("978-0451530960"));
        Assert.assertEquals(0, theLibrary.numDestroyedLoans());
        Assert.assertEquals(67, readerId2.getPoints());
        Loan loan1 = theLibrary.giveBackBook("LOAN1",
                DateUtils.createLocalDate("15-11-2024"), Library.CopyReturnStatus.DESTROYED);
        Assert.assertEquals("readerId2", loan1.getReader().getId());
        Assert.assertEquals(Library.LoanState.COMPLETED, loan1.getState());
        Assert.assertEquals(67 + Library.POINTS_COMPLETED+Library.POINTS_DESTROYED, readerId2.getPoints());
        Assert.assertEquals(1, theLibrary.numAvailableCopies("978-0451530960"));
        Assert.assertEquals(1, theLibrary.numClosedLoansByReader("readerId1"));
        Assert.assertEquals(2, theLibrary.numClosedLoansByReader("readerId2"));
        Assert.assertEquals(3, theLibrary.numClosedLoansByWorker("workerId1"));
        Assert.assertEquals(0, theLibrary.numCurrentLoansByReader("readerId1"));
        Assert.assertEquals(1, theLibrary.numCurrentLoansByReader("readerId2"));
        Assert.assertEquals(1, theLibrary.numDestroyedLoans());
        Assert.assertEquals(27, theLibrary.numCatalogCopies());
    }

    @Test
    public void timeToBeCatalogedTest() throws DSException {
        Assert.assertThrows(BookNotFoundException.class, () ->
                theLibrary.timeToBeCataloged("XXXX",  12, 3));

        catalogCopyTest();

        Assert.assertThrows(InvalidLotPreparationTimeException.class, () ->
                theLibrary.timeToBeCataloged("JV8",  -1, 3));

        Assert.assertThrows(InvalidCatalogTimeException.class, () ->
                theLibrary.timeToBeCataloged("JV8",  12, -3));

        Assert.assertThrows(InvalidCatalogTimeException.class, () ->
                theLibrary.timeToBeCataloged("JV8",  12, -3));

        int t = theLibrary.timeToBeCataloged("JA10",  8, 12);
//      Assert.assertEquals(244, t);
        Assert.assertEquals(1172, t);


    }


    @Test
    public void getAllLoansByReaderTest() throws DSException {
        Assert.assertThrows(NoLoansException.class, () ->
                theLibrary.getAllLoansByReader("readerId8"));

        giveBackBookTest();

        Iterator<Loan> it = theLibrary.getAllLoansByReader("readerId2");
        Loan l = null;

        Assert.assertTrue(it.hasNext());
        l = it.next();

        Assert.assertEquals("978-1605062234", l.getIsbn());
        Assert.assertEquals("The Steam House", l.getTitle());
        Assert.assertEquals("LOAN5", l.getLoanId());
        Assert.assertEquals(Library.LoanState.COMPLETED, l.getState());


        Assert.assertTrue(it.hasNext());
        l = it.next();

        Assert.assertEquals("978-0451530960", l.getIsbn());
        Assert.assertEquals("Twenty Thousand Leagues Under the Sea", l.getTitle());
        Assert.assertEquals("LOAN1", l.getLoanId());
        Assert.assertEquals(Library.LoanState.COMPLETED, l.getState());


        Assert.assertTrue(it.hasNext());
        l = it.next();

        Assert.assertEquals("The Steam House", l.getTitle());
        Assert.assertEquals("LOAN4", l.getLoanId());
        Assert.assertEquals(Library.LoanState.INPROGRESS, l.getState());


        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void getAllLoansByStateTest() throws DSException {
        Assert.assertThrows(NoLoansException.class, () ->
                theLibrary.getAllLoansByReader("readerId8"));

        giveBackBookTest();

        Iterator<Loan> it = theLibrary.getAllLoansByState("readerId2", Library.LoanState.INPROGRESS);
        Loan l = null;

        Assert.assertTrue(it.hasNext());
        l = it.next();

        Assert.assertEquals("The Steam House", l.getTitle());
        Assert.assertEquals("LOAN4", l.getLoanId());
        Assert.assertEquals(Library.LoanState.INPROGRESS, l.getState());

        Assert.assertFalse(it.hasNext());

    }

    @Test
    public void getAllLoansByBookTest() throws DSException {
        Assert.assertThrows(NoLoansException.class, () ->
                theLibrary.getAllLoansByCopy("JV1"));

        giveBackBookTest();

        Iterator<Loan> it = theLibrary.getAllLoansByCopy("JV1");

        Loan l = null;

        Assert.assertTrue(it.hasNext());
        l = it.next();

        Assert.assertEquals("Journey to the Center of the Earth", l.getTitle());
        Assert.assertEquals("LOAN3", l.getLoanId());
        Assert.assertEquals(Library.LoanState.DELAYED, l.getState());

        Assert.assertFalse(it.hasNext());

    }


    @Test
    public void getReaderTheMost() throws DSException {
        Assert.assertThrows(NoReaderException.class, () ->
                theLibrary.getReaderTheMost());

        giveBackBookTest();

        Reader reader = theLibrary.getReaderTheMost();
        Assert.assertEquals("readerId2", reader.getId());

    }

    @Test
    public void getMostReadBook() throws DSException {
        Assert.assertThrows(NoBookException.class, () ->
                theLibrary.getMostReadBook());

        giveBackBookTest();

        Book book = theLibrary.getMostReadBook();
        Assert.assertEquals("978-0451530960", book.getIsbn());

    }

}