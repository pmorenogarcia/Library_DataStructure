package uoc.ds.pr.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uoc.ds.pr.model.Author;
import uoc.ds.pr.data.BooksDataSet;
import uoc.ds.pr.model.Copy;
import uoc.ds.pr.model.Theme;

public class BookWareHouseTest {


    private BookWareHouse bookWareHouse;

    @Before
    public void setUp() {
        bookWareHouse = new BookWareHouse();
    }

    @Test
    public void storeBookTest() {
        Assert.assertEquals(0, bookWareHouse.numCopies());


        addCopiesData(BooksDataSet.booksData1);
        Assert.assertEquals(15, bookWareHouse.numCopies());
        Assert.assertEquals(2, bookWareHouse.numStacks());

        addCopiesData(BooksDataSet.booksData2);
        Assert.assertEquals(4, bookWareHouse.numStacks());
        Assert.assertEquals(31, bookWareHouse.numCopies());

        addCopiesData(BooksDataSet.booksData3);
        Assert.assertEquals(5, bookWareHouse.numStacks());
        Assert.assertEquals(49, bookWareHouse.numCopies());

        addCopiesData(BooksDataSet.booksData4);
        Assert.assertEquals(7, bookWareHouse.numStacks());
        Assert.assertEquals(67, bookWareHouse.numCopies());

        addCopiesData(BooksDataSet.booksData5);
        Assert.assertEquals(9, bookWareHouse.numStacks());
        Assert.assertEquals(86, bookWareHouse.numCopies());

        addCopiesData(BooksDataSet.booksData6);
        Assert.assertEquals(12, bookWareHouse.numStacks());
        Assert.assertEquals(115, bookWareHouse.numCopies());

        addCopiesData(BooksDataSet.booksData7);
        Assert.assertEquals(13, bookWareHouse.numStacks());
        Assert.assertEquals(130, bookWareHouse.numCopies());
        Theme existentialism = new Theme("THEMEX", "existentialism");
        Author franzKafka = new Author("FK", "Franz", "Kafka");

        bookWareHouse.storeCopy("FK1", "Die Verwandlung", "Die Weißen Blätter ",
                "First Edition", 1915, "978-84-8256-840-9", franzKafka, existentialism);

        Assert.assertEquals(14, bookWareHouse.numStacks());
        Assert.assertEquals(131, bookWareHouse.numCopies());
    }

    @Test
    public void catalogingTest() {
        storeBookTest();

        Copy cp1 = bookWareHouse.getBookPendingCataloging();
        Assert.assertEquals(14, bookWareHouse.numStacks());
        Assert.assertEquals(130, bookWareHouse.numCopies());

        Copy cp2 = bookWareHouse.getBookPendingCataloging();
        Assert.assertEquals(14, bookWareHouse.numStacks());
        Assert.assertEquals(129, bookWareHouse.numCopies());

        Copy cp = null;
        for (int i=0; i<8; i++) {
            cp = bookWareHouse.getBookPendingCataloging();
        }

        Assert.assertEquals(13, bookWareHouse.numStacks());
        Assert.assertEquals(121, bookWareHouse.numCopies());
    }

    @Test
    public void getPositionTest() {
        storeBookTest();

        Assert.assertEquals(14, bookWareHouse.numStacks());
        Assert.assertEquals(131, bookWareHouse.numCopies());

        BookWareHouse.Position position = bookWareHouse.getPosition("JV2c");
        Assert.assertEquals(0, position.getNumStack());
        Assert.assertEquals(0, position.getNum());

        position = bookWareHouse.getPosition("JV4c");
        Assert.assertEquals(0, position.getNumStack());
        Assert.assertEquals(5, position.getNum());

        position = bookWareHouse.getPosition("JV7d");
        Assert.assertEquals(0, position.getNumStack());
        Assert.assertEquals(9, position.getNum());

        position = bookWareHouse.getPosition("HP6c");
        Assert.assertEquals(1, position.getNumStack());
        Assert.assertEquals(0, position.getNum());

        position = bookWareHouse.getPosition("JV2a");
        Assert.assertEquals(1, position.getNumStack());
        Assert.assertEquals(9, position.getNum());

        position = bookWareHouse.getPosition("HP6f");
        Assert.assertEquals(2, position.getNumStack());
        Assert.assertEquals(0, position.getNum());

        position = bookWareHouse.getPosition("HP4a");
        Assert.assertEquals(2, position.getNumStack());
        Assert.assertEquals(9, position.getNum());

        position = bookWareHouse.getPosition("JA10");
        Assert.assertEquals(12, position.getNumStack());
        Assert.assertEquals(0, position.getNum());

        position = bookWareHouse.getPosition("JA5");
        Assert.assertEquals(12, position.getNumStack());
        Assert.assertEquals(9, position.getNum());


        position = bookWareHouse.getPosition("FK1");
        Assert.assertEquals(13, position.getNumStack());
        Assert.assertEquals(0, position.getNum());



    }


    private void addCopiesData(String[][] booksData) {

        for (String[] bookData: booksData) {
            bookWareHouse.storeCopy(bookData[0], bookData[1], bookData[2], bookData[3],
                    Integer.parseInt(bookData[4]),
                    bookData[5], new Author("", bookData[6], bookData[6]), new Theme("", bookData[7]));
        }
    }
}
