package uoc.ds.pr;

import edu.uoc.ds.adt.nonlinear.PriorityQueue;
import edu.uoc.ds.adt.nonlinear.graphs.Edge;
import edu.uoc.ds.adt.nonlinear.graphs.UnDirectedEdgeImpl;
import edu.uoc.ds.adt.nonlinear.graphs.UnDirectedVertexImpl;
import edu.uoc.ds.adt.nonlinear.graphs.Vertex;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.DSLinkedList;
import uoc.ds.pr.util.OrderedVector;

import java.time.LocalDate;

public class LibraryPR3Impl extends  LibraryPR2Impl implements LibraryPR3 {


    public LibraryPR3Impl() {
        super();
    }

    @Override
    public void addTheme(String themeId, String name) {
        /// If the Theme already exists, update it, otherwise, create a new Theme
        if (themes.get(themeId) != null){
            themes.update(themeId, new Theme(themeId, name));
        } else {
            themes.put(themeId, new Theme(themeId, name));
        }
    }

    @Override
    public void addAuthor(String uniqueCode, String name, String surname) {
        /// If the Author already exists, update it, otherwise, create a new Author
        if (authors.containsKey(uniqueCode)){
            Author author = authors.get(uniqueCode);
            author.setName(name);
            author.setLastName(surname);
        } else {
            authors.put(uniqueCode, new Author(uniqueCode, name, surname));
        }
    }

    @Override
    public Iterator<Copy> getCopies(String isbn) throws NoBookException, NoCopiesException {
        /// Retrieve the Cataloged Book or throw exception
        CatalogedBook book = getCatalogedBook(isbn);
        if (book == null){
            throw new NoBookException();
        }

        /// Create a PriorityQueue to add the copies we want to return
        PriorityQueue<Copy> orderedCopies = new PriorityQueue<>(Copy.CMP);
        Iterator<Copy> copyIt = catalogedCopies.values();
        while (copyIt.hasNext()){
            Copy copy = copyIt.next();
            if(copy.getIsbn().equals(isbn)){
                orderedCopies.add(copy);
            }
        }
        if (orderedCopies.isEmpty()){
            throw new NoCopiesException();
        }

        return orderedCopies.values();
    }

    @Override
    public void createRequest(String readerId, String copyId, LocalDate date) throws ReaderNotFoundException, CopyNotFoundException,
            ReaderAlreadyHasRequest4Copy, ReaderAlreadyHasLoan4Copy {
        /// Retrieve reader and copy
        Reader reader = getReader2(readerId);
        Copy copy = getCopy2(copyId);

        /// Create an instance of a Request and checks if the reader already has it
        Request request = new Request(reader, copy, date);
        if(reader.hasRequest(request)){
            throw new ReaderAlreadyHasRequest4Copy();
        } else if (reader.hasLoanedCopy(copyId)) {
            throw new ReaderAlreadyHasLoan4Copy();
        } else {
            request = new Request(reader, copy, date);
            reader.addRequest(request);
            copy.addRequest(request);
        }

    }

    @Override
    public Iterator<Book> getBooksByTheme(String themeId) throws NoBookException {
        OrderedVector<Book> orderedBooks = new OrderedVector<>(300, Book.CMP);
        Iterator<CatalogedBook> bookIt = storedBooks.values();

        /// Iterate all the books and store the ones that match the Theme
        while (bookIt.hasNext()){
            Book book = bookIt.next().getBook();
            if(book.getTheme().equals(themeId)){
                orderedBooks.update(book);
            }
        }
        if(orderedBooks.isEmpty()){
            throw new NoBookException();
        }

        return orderedBooks.values();
    }

    @Override
    public Iterator<Book> getBooksByAuthor(String uniqueCode) throws NoBookException {
        Author author = getAuthor(uniqueCode);
        Iterator<Book> bookIt = author.getBookIterator();

        /// Iterate all the books of an Author
        DSLinkedList<Book> bookList = new DSLinkedList<>(Book.CMP);
        while (bookIt.hasNext()){
            Book book = bookIt.next();
            bookList.insertEnd(book);
        }
        if (bookList.isEmpty()){
            throw new NoBookException();
        }

        return bookList.values();
    }

    @Override
    public Level getReaderLevel(String readerId) throws ReaderNotFoundException {
        Reader reader = getReader2(readerId);
        return reader.getLevel();
    }

    @Override
    public void addReview(String bookId, String readerId, int rate, String comment)
            throws RateOutOfRangeException, ReaderNotFoundException, BookNotFoundException,
            ReaderNotAssociatedWithBookException, UserAlreadyRatedBookException {

        if(rate < 0 || rate > 5){
            throw new RateOutOfRangeException();
        }

        Reader reader = getReader2(readerId);
        CatalogedBook book = getCatalogedBook2(bookId);

        if (book == null){
            throw new BookNotFoundException();
        }
        Rating rating = book.getRating(readerId);

        if(rating != null){
            throw new UserAlreadyRatedBookException();
        } else {
            rating = new Rating(rate, comment, reader, book);

            if (!reader.hasRequestedBook(bookId)){
                throw new ReaderNotAssociatedWithBookException();
            }

            book.addRating(readerId, rating);
            reader.addRating(rating);
            mostRatedBooks.update(book);

            /// Update the book graph relationship
            updateGraphRelationship(reader, book, rate);
        }

    }

    /// Additional method
    public void updateGraphRelationship(Reader reader, CatalogedBook catalogedBook, int rate) {
        Vertex<Book> bookVertex = bookGraph.getVertex(catalogedBook.getBook());
        if (bookVertex == null){
            bookVertex = bookGraph.newVertex(catalogedBook.getBook());
        }

        /// Iterate through all books previously rated by the reader with a specific rate
        Iterator<Rating> ratingIt = reader.getAllRatings();
        while (ratingIt.hasNext()){
            Rating rating = ratingIt.next();
            if (rating.getRate() == rate){
                Book ratedBook = rating.getCatalogedBook().getBook();

                /// Check that it has relation and if it is the same book
                boolean isRelated = ratedBook.getTheme().equals(catalogedBook.getBook().getTheme()) || ratedBook.getAuthor().equals(catalogedBook.getBook().getAuthor());
                boolean isTheSame = ratedBook.equals(catalogedBook.getBook());

                if (isRelated && !isTheSame){
                    /// Create or update the Vertex
                    Vertex<Book> ratedBooxVertex = bookGraph.getVertex(ratedBook);
                    if (ratedBooxVertex == null){
                        ratedBooxVertex = bookGraph.newVertex(ratedBook);
                    }

                    /// Create or update the Edge
                    Edge<Integer, Book> edge = bookGraph.getEdge(ratedBooxVertex, bookVertex);
                    if (edge == null){
                        edge = bookGraph.newEdge(ratedBooxVertex, bookVertex);
                        edge.setLabel(1);
                    } else {
                        edge.setLabel(edge.getLabel() + 1);
                    }
                }
            }
        }
    }

    @Override
    public Iterator<Copy> getDestroyedCopies() throws NoBookException {
        Iterator<Copy> destroyedCopiesIt = destroyedCopies.values();
        if(!destroyedCopiesIt.hasNext()){
            throw new NoBookException();
        }
        return destroyedCopiesIt;
    }

    @Override
    public Iterator<Rating> getReviewsByBook(String bookId)
            throws BookNotFoundException, NoReviewsException {
        CatalogedBook book = getCatalogedBook2(bookId);
        Iterator<Rating> ratingIt = book.getAllRatings();
        if (!ratingIt.hasNext()) {
            throw new NoReviewsException();
        }
        return ratingIt;
    }

    @Override
    public Iterator<CatalogedBook> best5Books() throws NoBookException {
        Iterator<CatalogedBook> best5Books = mostRatedBooks.values();
        if(!best5Books.hasNext()){
            throw new NoBookException();
        }
        return best5Books;
    }

    @Override
    public Iterator<Reader> best5Readers() throws NoReaderException {
        Iterator<Reader> best5Readers = readersBestRated.values();
        if(!best5Readers.hasNext()){
            throw new NoReaderException();
        }

        return best5Readers;
    }

    @Override
    public Iterator<Book> getRecommendationsByBook(String bookId) throws NoBookException {
        //Book book = storedBooks.get(bookId).getBook();
        Book book = getCatalogedBook(bookId).getBook();
        DSLinkedList<Book> bookList = new DSLinkedList<>(Book.CMP);
        if (book == null) {
            throw new NoBookException();
        }

        Vertex<Book> bookVertex = bookGraph.getVertex(book);
        if (bookVertex == null) {
            throw new NoBookException();
        }

        PriorityQueue<UnDirectedEdgeImpl<Integer, Book>> orderedEdges = new PriorityQueue<>(
                (e1, e2) -> e2.getLabel().compareTo(e1.getLabel())
        );

        Iterator<Edge<Integer, Book>> edgeIterator = bookGraph.edges();

        while (edgeIterator.hasNext()) {
            UnDirectedEdgeImpl<Integer, Book> edge = (UnDirectedEdgeImpl<Integer, Book>) edgeIterator.next();

            if (edge.edgeInVertex(bookVertex)) {
                orderedEdges.add(edge);
            }
        }

        /// Create the ordered list
        List<Book> sortedBooks = new DSLinkedList<>(Book.CMP);

        /// Iterate through all acquired Edges
        while (!orderedEdges.isEmpty()){
            UnDirectedEdgeImpl<Integer, Book> edge = orderedEdges.poll();

            Vertex<Book> vertex1 = edge.getVertex1();
            Vertex<Book> vertex2 = edge.getVertex2();

            if (!vertex1.equals(bookVertex)) {
                sortedBooks.insertEnd(vertex1.getValue());
            } else {
                sortedBooks.insertEnd(vertex2.getValue());
            }
        }

        return sortedBooks.values();
    }

    @Override
    public Iterator<Book> getRecommendationsByReader(String readerId) throws ReaderNotFoundException, NoBookException {
        Reader reader = getReader2(readerId);

        PriorityQueue<UnDirectedEdgeImpl<Integer, Book>> orderedEdges = new PriorityQueue<>(
                (e1, e2) -> e2.getLabel().compareTo(e1.getLabel())
        );

        Iterator<Rating> ratingIt = reader.getAllRatings();

        while (ratingIt.hasNext()) {
            Rating rating = ratingIt.next();
            Book currentBook = rating.getCatalogedBook().getBook();
            UnDirectedVertexImpl<Book, Integer> bookVertex = (UnDirectedVertexImpl<Book, Integer>) bookGraph.getVertex(currentBook);

            Iterator<Edge<Integer, Book>> edgeIterator = bookVertex.edges();

            while (edgeIterator.hasNext()) {
                UnDirectedEdgeImpl<Integer, Book> edge = (UnDirectedEdgeImpl<Integer, Book>) edgeIterator.next();
                if (edge.edgeInVertex(bookVertex)) {
                    orderedEdges.add(edge);
                }
            }

        }

        /// Create the ordered list
        DSLinkedList<Book> sortedBooks = new DSLinkedList<>(Book.CMP);

        /// Iterate through all acquired Edges
        while (!orderedEdges.isEmpty()){
            UnDirectedEdgeImpl<Integer, Book> edge = orderedEdges.poll();
            Book book1 = edge.getVertex1().getValue();
            Book book2 = edge.getVertex2().getValue();

            if (!reader.hasReadBook(book1.getIsbn())){
                if (sortedBooks.get(book1) == null) {
                    sortedBooks.insertEnd(book1);
                }
            } else if (!reader.hasReadBook(book2.getIsbn())){
                if (sortedBooks.get(book2) == null) {
                    sortedBooks.insertEnd(book2);
                }
            }

        }

        return sortedBooks.values();
    }

    @Override
    public Iterator<Author> getRecommendedAuthors(String uniqueCode) throws AuthorNotFoundException, NoAuthorException {
        Author author = getAuthor(uniqueCode);
        if (author == null) {
            throw new AuthorNotFoundException();
        }

        PriorityQueue<UnDirectedEdgeImpl<Integer, Book>> orderedEdges = new PriorityQueue<>(
                (e1, e2) -> e2.getLabel().compareTo(e1.getLabel())
        );

        Iterator<Book> booksByAuthorIt = author.getBookIterator();
        while (booksByAuthorIt.hasNext()) {
            Book book = booksByAuthorIt.next();
            Book actualBook = getCatalogedBook(book.getIsbn()).getBook();
            UnDirectedVertexImpl<Book, Integer> bookVertex = (UnDirectedVertexImpl<Book, Integer>) bookGraph.getVertex(actualBook);

            if (bookVertex != null) {
                Iterator<Edge<Integer, Book>> edgeIterator = bookVertex.edges();
                while (edgeIterator.hasNext()) {
                    UnDirectedEdgeImpl<Integer, Book> edge = (UnDirectedEdgeImpl<Integer, Book>) edgeIterator.next();
                    if (edge.edgeInVertex(bookVertex)) {
                        orderedEdges.add(edge);
                    }
                }
            }
        }

        /// Create the ordered list
        DSLinkedList<Author> sortedAuthors = new DSLinkedList<>(Author.CMP);

        /// Iterate through all acquired Edges
        while (!orderedEdges.isEmpty()){
            UnDirectedEdgeImpl<Integer, Book> edge = orderedEdges.poll();
            Book book1 = edge.getVertex1().getValue();
            Book book2 = edge.getVertex2().getValue();

            Author author1 = book1.getAuthorObject();
            Author author2 = book2.getAuthorObject();

            if (!author1.equals(author)) {
                if (sortedAuthors.get(author1) == null) {
                    sortedAuthors.insertEnd(author1);
                }
            } else if (!author2.equals(author)) {
                if (sortedAuthors.get(author2) == null) {
                    sortedAuthors.insertEnd(author2);
                }
            }
        }

        return sortedAuthors.values();
    }



    public int numThemes() {
        return themes.size();
    }

    public Theme getTheme(String id) {
        return themes.get(id);
    }

    public int numAuthors() {
        return authors.size();
    }

    public Author getAuthor(String id) {
        return authors.get(id);
    }

    public Edge<Integer, Book> getEdge(String bookId1, String bookId2) {
        Book book1 = getCatalogedBook(bookId1).getBook();
        Book book2 = getCatalogedBook(bookId2).getBook();


        Vertex<Book> book1Vertex = bookGraph.getVertex(book1);
        Vertex<Book> book2Vertex = bookGraph.getVertex(book2);

        return bookGraph.getEdge(book1Vertex, book2Vertex);

    }


}
