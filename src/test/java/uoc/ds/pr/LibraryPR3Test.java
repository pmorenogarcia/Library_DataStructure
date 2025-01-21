package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.data.BooksMetaData;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.DateUtils;


import static uoc.ds.pr.data.BooksMetaData.addAuthorData;
import static uoc.ds.pr.data.BooksMetaData.addThemeData;

public class LibraryPR3Test extends LibraryPR2Test {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        super.theLibrary = FactoryLibrary.getLibrary();
    }


    @Test
    public void getCopiesTest() throws DSException  {
        Assert.assertThrows(NoBookException.class, () ->
                theLibrary.getCopies("XXXXXX"));
        super.catalogCopyTest();

        Iterator<Copy> it = theLibrary.getCopies("978-0451530960");

        Assert.assertTrue(it.hasNext());
        Copy copy = it.next();
        Assert.assertEquals("JV2c", copy.getCopyId());

        Assert.assertTrue(it.hasNext());
        copy = it.next();
        Assert.assertEquals("JV2b", copy.getCopyId());
    }

    @Test
    public void getBooksByThemeTest() throws DSException {


        super.storeCopyTest();

        Assert.assertThrows(NoBookException.class, () ->
                theLibrary.getBooksByTheme("THEME10"));

        Iterator<Book> it = theLibrary.getBooksByTheme("THEME21");

        Assert.assertTrue(it.hasNext());
        Book book1 = it.next();
        Assert.assertEquals("978-0140433340", book1.getIsbn());
        Assert.assertEquals("Love and Friendship", book1.getTitle());

        Assert.assertTrue(it.hasNext());
        Book book2 = it.next();
        Assert.assertEquals("978-0141439518", book2.getIsbn());
        Assert.assertEquals("Pride and Prejudice", book2.getTitle());

        Assert.assertTrue(it.hasNext());
        Book book3 = it.next();
        Assert.assertEquals("978-0141439587", book3.getIsbn());
        Assert.assertEquals("Emma", book3.getTitle());

        Assert.assertTrue(it.hasNext());
        Book book4 = it.next();
        Assert.assertEquals("978-0141439686", book4.getIsbn());
        Assert.assertEquals("Persuasion", book4.getTitle());

        Assert.assertTrue(it.hasNext());
        Book book5 = it.next();
        Assert.assertEquals("978-0141439808", book5.getIsbn());
        Assert.assertEquals("Mansfield Park", book5.getTitle());

        Assert.assertTrue(it.hasNext());
        Book book6 = it.next();
        Assert.assertEquals("978-0743477116", book6.getIsbn());
        Assert.assertEquals("Romeo and Juliet", book6.getTitle());

        Assert.assertTrue(it.hasNext());
        Book book7 = it.next();
        //Assert.assertEquals("978-0743477543", book7.getIsbn());
        Assert.assertEquals("A Midsummer Night's Dream", book7.getTitle());

        Assert.assertTrue(it.hasNext());
        Book book8 = it.next();
        Assert.assertEquals("978-0743482752", book8.getIsbn());
        Assert.assertEquals("Much Ado About Nothing", book8.getTitle());

        Assert.assertFalse(it.hasNext());

    }

    @Test
    public void getBooksByAuthorTest() throws DSException {

        super.storeCopyTest();

        Assert.assertThrows(NoBookException.class, () ->
                theLibrary.getBooksByAuthor("GAVO"));



        Iterator<Book> it = theLibrary.getBooksByAuthor("JA");

        Assert.assertTrue(it.hasNext());
        Book book1 = it.next();
        Assert.assertEquals("978-0141439518", book1.getIsbn());
        Assert.assertEquals("Pride and Prejudice", book1.getTitle());

        Assert.assertTrue(it.hasNext());
        Book book2 = it.next();
        Assert.assertEquals("978-0141439662", book2.getIsbn());
        Assert.assertEquals("Sense and Sensibility", book2.getTitle());

        Assert.assertTrue(it.hasNext());
        Book book3 = it.next();
        Assert.assertEquals("978-0141439587", book3.getIsbn());
        Assert.assertEquals("Emma", book3.getTitle());

        Assert.assertTrue(it.hasNext());
        Book book4 = it.next();
        Assert.assertEquals("978-0141439808", book4.getIsbn());
        Assert.assertEquals("Mansfield Park", book4.getTitle());

        Assert.assertTrue(it.hasNext());
        Book book5 = it.next();
        Assert.assertEquals("978-0141439686", book5.getIsbn());
        Assert.assertEquals("Persuasion", book5.getTitle());

        Assert.assertTrue(it.hasNext());
        Book book6 = it.next();
        Assert.assertEquals("978-0141439792", book6.getIsbn());
        Assert.assertEquals("Northanger Abbey", book6.getTitle());

        Assert.assertTrue(it.hasNext());
        Book book7 = it.next();
        Assert.assertEquals("978-0140433203", book7.getIsbn());
        Assert.assertEquals("Lady Susan", book7.getTitle());

        Assert.assertTrue(it.hasNext());
        Book book8 = it.next();
        Assert.assertEquals("N/A", book8.getIsbn());
        Assert.assertEquals("The Watsons", book8.getTitle());

        Assert.assertTrue(it.hasNext());
        Book book9 = it.next();
        Assert.assertEquals("978-0140433340", book9.getIsbn());
        Assert.assertEquals("Love and Friendship", book9.getTitle());

        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void getLevelTest() throws DSException {
        Assert.assertThrows(ReaderNotFoundException.class, () ->
                theLibrary.getReaderLevel("XXXX"));

        Assert.assertEquals(LibraryPR3.Level.MUGGLE, theLibrary.getReaderLevel("readerId1"));
        Assert.assertEquals(LibraryPR3.Level.ASLAN, theLibrary.getReaderLevel("readerId2"));
        Assert.assertEquals(LibraryPR3.Level.HOBBIT, theLibrary.getReaderLevel("readerId3"));
        Assert.assertEquals(LibraryPR3.Level.MUGGLE, theLibrary.getReaderLevel("readerId4"));

    }

    @Test
    public void addReviewTest() throws DSException {
        Assert.assertThrows(RateOutOfRangeException.class, () ->
                theLibrary.addReview("978-1605062234", "readerId2", -5, "blah blah blah"));
        Assert.assertThrows(RateOutOfRangeException.class, () ->
                theLibrary.addReview("978-1605062234", "readerId2", 12, "blah blah blah"));

        super.giveBackBookTest();

        Assert.assertThrows(ReaderNotFoundException.class, () ->
                theLibrary.addReview("978-1605062234", "XXXXX", 5, "blah blah blah"));
        Assert.assertThrows(BookNotFoundException.class, () ->
                theLibrary.addReview("XXXXXX", "readerId1", 5, "blah blah blah"));
        Assert.assertThrows(ReaderNotAssociatedWithBookException.class, () ->
                theLibrary.addReview("978-1605062234", "readerId1", 5, "blah blah blah"));

        theLibrary.addReview("978-1605062234", "readerId2", 5, "blah blah blah");

        Assert.assertThrows(UserAlreadyRatedBookException.class, () ->
                theLibrary.addReview("978-1605062234", "readerId2", 5, "blah blah blah"));

    }


    @Test
    public void getDestroyedCopiesTest() throws DSException {
        Assert.assertThrows(NoBookException.class, () ->
                theLibrary.getDestroyedCopies());

        super.giveBackBookTest();

        Iterator<Copy> it = theLibrary.getDestroyedCopies();
        Assert.assertTrue(it.hasNext());
        Copy copy = it.next();
        Assert.assertEquals("JV2b", copy.getCopyId());
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void getReviewsByBookTest() throws DSException {
        Assert.assertThrows(BookNotFoundException.class, () ->
                theLibrary.getReviewsByBook("XXXX"));

        this.addReviewTest();

        Assert.assertThrows(NoReviewsException.class, () ->
                theLibrary.getReviewsByBook("978-1435149408"));

        Iterator<Rating> it = theLibrary.getReviewsByBook("978-1605062234");
        Assert.assertTrue(it.hasNext());
        Rating rating = it.next();
        Assert.assertEquals(5, rating.getRate());
        Assert.assertEquals("blah blah blah", rating.getComment());
        Assert.assertEquals("readerId2", rating.getReader().getId());

        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void best5BooksTest() throws DSException {
        Assert.assertThrows(NoBookException.class, () ->
                theLibrary.best5Books());


        addReviewTest();

        Iterator<CatalogedBook> it = theLibrary.best5Books();
        Assert.assertTrue(it.hasNext());
        CatalogedBook catalogedBook = it.next();
        Assert.assertEquals("978-1605062234", catalogedBook.getIsbn());
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void best5ReadersTest() throws DSException {
        Assert.assertThrows(NoReaderException.class, () ->
                theLibrary.best5Readers());


        addReviewTest();

        Iterator<Reader> it = theLibrary.best5Readers();
        Assert.assertTrue(it.hasNext());
        Reader reader = it.next();
        Assert.assertEquals("readerId1", reader.getId());
        Assert.assertEquals(-1, reader.getPoints());

        Assert.assertTrue(it.hasNext());
        reader = it.next();
        Assert.assertEquals("readerId2", reader.getId());
        Assert.assertEquals(-31, reader.getPoints());

        Assert.assertFalse(it.hasNext());
    }
}
