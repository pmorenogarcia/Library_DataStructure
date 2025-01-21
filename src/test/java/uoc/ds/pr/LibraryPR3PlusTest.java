package uoc.ds.pr;

import edu.uoc.ds.adt.nonlinear.graphs.Edge;
import edu.uoc.ds.traversal.Iterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.DateUtils;

public class LibraryPR3PlusTest  {
    private LibraryPR3 theLibrary;
    private Author author1;
    private Author author2;
    private Author author3;

    private void addReaders() {
        theLibrary.addReader("A", "Maria", "Simó", "45423933A", DateUtils.createLocalDate("10-10-1987"), "Montgat", "the street", -5);
        Reader A = theLibrary.getReader("A");

        theLibrary.addReader("B", "Àlex", "Lluna", "664933A", DateUtils.createLocalDate("11-07-1977"), "Tortosa", "the street", 60);
        Reader B = theLibrary.getReader("B");

        theLibrary.addReader("C", "Pepet", "Marieta", "999933A", DateUtils.createLocalDate("12-06-1972"), "Amposta", "the street", 15);
        Reader C = theLibrary.getReader("C");

        Assert.assertEquals(3, theLibrary.numReaders());
    }

    private void addWorkers() {
        theLibrary.addWorker("workerId1", "Pepe", "Gotera");
        Assert.assertEquals(1, theLibrary.numWorkers());
    }

    private void addAuthors() {
        theLibrary.addAuthor("AUTHOR1", "AUTHOR 1","AAA" );
        author1 = theLibrary.getAuthor("AUTHOR1");

        theLibrary.addAuthor("AUTHOR2", "AUTHOR 2","BBB" );
        author2 = theLibrary.getAuthor("AUTHOR2");

        theLibrary.addAuthor("AUTHOR3", "AUTHOR 3","CCC" );
        author3 = theLibrary.getAuthor("AUTHOR3");
    }

    public void addThemes() {
        theLibrary.addTheme("THEME1", "...");
        Theme theme1 = theLibrary.getTheme("THEME1");

        theLibrary.addTheme("THEME2", "...");
        Theme theme2 = theLibrary.getTheme("THEME2");
    }


    private void storeCopies() throws DSException  {
        theLibrary.storeCopy("X1", "THE_TITLE_X", "", "EDITION", 2024, "ISBN_X", "AUTHOR1", "THEME1");
        theLibrary.storeCopy("X2", "THE_TITLE_X", "", "EDITION", 2024, "ISBN_X", "AUTHOR1", "THEME1");
        theLibrary.storeCopy("X3", "THE_TITLE_X", "", "EDITION", 2024, "ISBN_X", "AUTHOR1", "THEME1");
        theLibrary.storeCopy("Y1", "THE_TITLE_Y", "", "EDITION", 2024, "ISBN_Y", "AUTHOR2", "THEME1");
        theLibrary.storeCopy("Y2", "THE_TITLE_Y", "", "EDITION", 2024, "ISBN_Y", "AUTHOR2", "THEME1");
        theLibrary.storeCopy("Y3", "THE_TITLE_Y", "", "EDITION", 2024, "ISBN_Y", "AUTHOR2", "THEME1");
        theLibrary.storeCopy("Z1", "THE_TITLE_Z", "", "EDITION", 2024, "ISBN_Z", "AUTHOR3", "THEME1");
        theLibrary.storeCopy("Z2", "THE_TITLE_Z", "", "EDITION", 2024, "ISBN_Z", "AUTHOR3", "THEME1");
        theLibrary.storeCopy("Z3", "THE_TITLE_Z", "", "EDITION", 2024, "ISBN_Z", "AUTHOR3", "THEME1");
    }

    private void catalogCopies() throws DSException {
        Assert.assertEquals(9, theLibrary.numCopies());
        Assert.assertEquals(0, theLibrary.numCatalogCopies());

        theLibrary.catalogCopy("workerId1");
        theLibrary.catalogCopy("workerId1");
        theLibrary.catalogCopy("workerId1");
        theLibrary.catalogCopy("workerId1");
        theLibrary.catalogCopy("workerId1");
        theLibrary.catalogCopy("workerId1");
        theLibrary.catalogCopy("workerId1");
        theLibrary.catalogCopy("workerId1");
        theLibrary.catalogCopy("workerId1");
        Assert.assertEquals(9, theLibrary.numCatalogCopies());
        Assert.assertEquals(3, theLibrary.numCatalogBooks());
    }

    private void createRequests() throws DSException {
        theLibrary.createRequest("A", "X1", DateUtils.createLocalDate("19-12-2024"));
        theLibrary.createRequest("A", "Y1", DateUtils.createLocalDate("19-12-2024"));
        theLibrary.createRequest("B", "X2", DateUtils.createLocalDate("19-12-2024"));
        theLibrary.createRequest("B", "Y2", DateUtils.createLocalDate("19-12-2024"));
        theLibrary.createRequest("B", "Z1", DateUtils.createLocalDate("19-12-2024"));
    }

    private void lendCopies() throws DSException {
        theLibrary.lendCopy("L1", "X1", "workerId1", DateUtils.createLocalDate("19-12-2024"), DateUtils.createLocalDate("31-12-2024"));
        theLibrary.lendCopy("L2", "Y1", "workerId1", DateUtils.createLocalDate("19-12-2024"), DateUtils.createLocalDate("31-12-2024"));
        theLibrary.lendCopy("L3", "X2", "workerId1", DateUtils.createLocalDate("19-12-2024"), DateUtils.createLocalDate("31-12-2024"));
        theLibrary.lendCopy("L4", "Y2", "workerId1", DateUtils.createLocalDate("19-12-2024"), DateUtils.createLocalDate("31-12-2024"));
        theLibrary.lendCopy("L5", "Z1", "workerId1", DateUtils.createLocalDate("19-12-2024"), DateUtils.createLocalDate("31-12-2024"));
        Assert.assertEquals(5, theLibrary.numLoans());
    }

    private void giveBackCopies() throws DSException {
        theLibrary.giveBackBook("L1", DateUtils.createLocalDate("20-12-2024"), Library.CopyReturnStatus.GOOD);
        theLibrary.giveBackBook("L2", DateUtils.createLocalDate("20-12-2024"), Library.CopyReturnStatus.GOOD);
        theLibrary.giveBackBook("L3", DateUtils.createLocalDate("20-12-2024"), Library.CopyReturnStatus.GOOD);
        theLibrary.giveBackBook("L4", DateUtils.createLocalDate("20-12-2024"), Library.CopyReturnStatus.GOOD);
        theLibrary.giveBackBook("L5", DateUtils.createLocalDate("20-12-2024"), Library.CopyReturnStatus.GOOD);
    }

    private void addReviews() throws  DSException {
        theLibrary.addReview("ISBN_X", "A", 5, "blah blah blah");
        theLibrary.addReview("ISBN_Y", "A", 5, "blah blah blah");

        Edge<Integer, Book> edge =  theLibrary.getEdge("ISBN_X", "ISBN_Y");
        Assert.assertEquals(Integer.valueOf(1), edge.getLabel());

        theLibrary.addReview("ISBN_X", "B", 5, "blah blah blah");
        theLibrary.addReview("ISBN_Y", "B", 5, "blah blah blah");

        edge =  theLibrary.getEdge("ISBN_X", "ISBN_Y");
        Assert.assertEquals(Integer.valueOf(2), edge.getLabel());


        theLibrary.addReview("ISBN_Z", "B", 5, "blah blah blah");

        edge =  theLibrary.getEdge("ISBN_Y", "ISBN_Z");
        Assert.assertEquals(Integer.valueOf(1), edge.getLabel());
    }

    @Before
    public void setUp() throws Exception {
        this.theLibrary = new LibraryPR3Impl();
        addReaders();
        addWorkers();
        addAuthors();
        addThemes();
        storeCopies();
        catalogCopies();
        createRequests();
        lendCopies();
        giveBackCopies();
    }


    @Test
    public void getRecommendationsByBookTest() throws DSException {
        Assert.assertThrows(NoBookException.class, () ->
                this.theLibrary.getRecommendationsByBook("ISBN_X"));

        addReviews();
        Iterator<Book> it = this.theLibrary.getRecommendationsByBook("ISBN_X");
        Assert.assertTrue(it.hasNext());
        Book b = it.next();
        Assert.assertEquals("ISBN_Y", b.getIsbn());
        Edge<Integer, Book> edge1 =  theLibrary.getEdge("ISBN_X", "ISBN_Y");
        Assert.assertEquals(Integer.valueOf(2), edge1.getLabel());

        b = it.next();
        Assert.assertEquals("ISBN_Z", b.getIsbn());
        Edge<Integer, Book> edge2 =  theLibrary.getEdge("ISBN_X", "ISBN_Z");
        Assert.assertEquals(Integer.valueOf(1), edge2.getLabel());


        Assert.assertFalse(it.hasNext());
    }


    @Test
    public void getRecommendationsByReaderTest() throws DSException {
        Assert.assertThrows(ReaderNotFoundException.class, () ->
                this.theLibrary.getRecommendationsByReader("XXX"));

        addReviews();
        Iterator<Book> it = this.theLibrary.getRecommendationsByReader("A");
        Assert.assertTrue(it.hasNext());
        Book b = it.next();
        Assert.assertEquals("ISBN_Z", b.getIsbn());

        Assert.assertFalse(it.hasNext());

    }


    @Test
    public void getRecommendedAuthors() throws DSException {
        Assert.assertThrows(AuthorNotFoundException.class, () ->
                this.theLibrary.getRecommendedAuthors("XXX"));

        addReviews();

        Iterator<Author> it = this.theLibrary.getRecommendedAuthors("AUTHOR1");
        Assert.assertTrue(it.hasNext());
        Author author = it.next();
        Assert.assertEquals("AUTHOR2", author.getUniqueCode());

        author = it.next();
        Assert.assertEquals("AUTHOR3", author.getUniqueCode());
        Assert.assertFalse(it.hasNext());

    }

}
