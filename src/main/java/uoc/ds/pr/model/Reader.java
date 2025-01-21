package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.Set;
import edu.uoc.ds.adt.sequential.SetLinkedListImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.Library;
import uoc.ds.pr.LibraryPR3.Level;
import uoc.ds.pr.exceptions.ReaderAlreadyHasRequest4Copy;
import uoc.ds.pr.util.DSLinkedList;

import java.time.LocalDate;
import java.util.Comparator;
import edu.uoc.ds.adt.nonlinear.PriorityQueue;
import uoc.ds.pr.util.LevelHelper;

import static uoc.ds.pr.Library.MAXIMUM_NUMBER_OF_BOOKS;

public class Reader implements Comparable<Reader> {
    public static final Comparator<Reader> CMP = (r1, r2) -> r1.getId().compareTo(r2.getId());
    public static final Comparator<Reader> CMP_V = (r1, r2)->Double.compare(r1.numAllLoans(), r2.numAllLoans());
    public static final Comparator<Reader> CMP_L = (r1, r2) -> Integer.compare(r2.getPoints(), r1.getPoints());
    public static final Comparator<Reader> CMP_L_INV = (r1, r2) -> Integer.compare(r1.getPoints(), r2.getPoints());


    private String id;
    private String name;
    private String surname;
    private String docId;
    private LocalDate birthDate;
    private String birthPlace;
    private String address;

    private final DSLinkedList<Loan> loans;
    private final Loan[] currentLoans;
    private int numberCurrentLoans;

    private final DSLinkedList<Rating> ratings;
    private final PriorityQueue<Request> requests;
    private int points;
    private Level level;


    public Reader(String id, String name, String surname, String docId,
                  LocalDate birthDate, String birthPlace, String address, int points) {
        setId(id);
        update(name, surname, docId, birthDate, birthPlace, address, points);
        this.loans = new DSLinkedList<>(Loan.CMP);
        this.currentLoans = new Loan[MAXIMUM_NUMBER_OF_BOOKS];
        this.numberCurrentLoans = 0;
        this.ratings = new DSLinkedList<>(Rating.CMP);
        this.requests = new PriorityQueue<>(Request.CMP);
        this.points = points;
        this.level = LevelHelper.getLevel(points);
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getDocId() {
        return docId;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public String getAddress() {
        return address;
    }
    public int getPoints(){
        return points;
    }
    public Level getLevel() {
        return level;
    }
    public Boolean hasRequest(Request request) {

        Iterator<Request> requestIterator = requests.values();
        while(requestIterator.hasNext()) {
            Request currentRequest = requestIterator.next();
            if(request.getReader().equals(currentRequest.getReader()) && request.getCopy().equals(currentRequest.getCopy())) {
                return true;
            }
        }
        return false;

    }
    public Request getRequest(String copyId) {
        Iterator<Request> requestIterator = requests.values();
        while(requestIterator.hasNext()) {
            Request currentRequest = requestIterator.next();
            if(currentRequest.getCopy().getCopyId().equals(copyId)) {
                return currentRequest;
            }
        }
        return null;

    }


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public void addRequest(Request request) throws ReaderAlreadyHasRequest4Copy{
        requests.add(request);
    }
    public void addPoints(int points) {
        this.points += points;
        updateLevel();
    }
    public void updateLevel(){
        level = LevelHelper.getLevel(points);
    }

    public void update(String name, String surname, String docId,
                       LocalDate birthDate, String birthPlace, String address, int points) {
        setName(name);
        setSurname(surname);
        setDocId(docId);
        setBirthDate(birthDate);
        setBirthPlace(birthPlace);
        setAddress(address);
        setPoints(points);
    }

    public boolean hasMaximumNumberOfBooks() {
        return (numberCurrentLoans == MAXIMUM_NUMBER_OF_BOOKS);
    }

    public Loan addNewLoan(String loanId, Copy catalogedCopy, LocalDate date, LocalDate expirationDate) {
        Loan loan = new Loan(loanId, catalogedCopy, date, expirationDate);
        currentLoans[numberCurrentLoans++] = loan;
        return loan;
    }

    public int numLoans() {
        return numberCurrentLoans;
    }

    public int numClosedLoans() {
        return loans.size();
    }

    public void addClosedLoan(Loan loan) {
        updateCurrentLoan(loan);
        loans.insertEnd(loan);
    }

    private void updateCurrentLoan(Loan loan) {
        if (this.currentLoans[0].is(loan)) {
            lshift(0);
        }
        else if (this.currentLoans[1].is(loan)) {
            lshift(1);
        }
        else if (this.currentLoans[2].is(loan)) {
            lshift(2);
        }
        this.numberCurrentLoans--;

    }
    public Boolean hasLoanedCopy(String copyId) {
        for (Loan loan : currentLoans) {
            if (loan == null) {
                continue;
            }
            if (loan.getCopy().getCopyId().equals(copyId)) {
                return true;
            }
        }
        return false;
    }

    public Boolean hasReadBook(String isbn) {
        Iterator<Loan> loanIterator = loans.values();
        while(loanIterator.hasNext()) {
            Loan loan = loanIterator.next();
            if (loan.getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }


    private void lshift(int pos) {
        for (int i = pos; i<MAXIMUM_NUMBER_OF_BOOKS-1; i++) {
            this.currentLoans[i]=this.currentLoans[i+1];
        }
    }

    public Iterator<Loan> getAllLoans() {
        Set<Loan> loanSet = new SetLinkedListImpl<>();
        Iterator<Loan> it = loans.values();
        while (it.hasNext()) {
            loanSet.add(it.next());
        }

        for (Loan currentLoan: currentLoans) {
            if (currentLoan!=null) loanSet.add(currentLoan);
        }
        return loanSet.values();
    }

    public int numAllLoans() {
        return numClosedLoans()+numLoans();

    }

    public Iterator<Loan> getAllLoansByState(Library.LoanState state) {
        Set<Loan> loanSet = new SetLinkedListImpl<>();
        Iterator<Loan> it = loans.values();
        Loan loan = null;
        while (it.hasNext()) {
            loan = it.next();
            if (state.equals(loan.getState())) {
                loanSet.add(loan);
            }
        }

        for (Loan currentLoan: currentLoans) {
            if (currentLoan!=null && state.equals(currentLoan.getState())) {
                loanSet.add(currentLoan);
            }
        }
        return loanSet.values();
    }

    public void addRating(Rating rating) {
        ratings.insertEnd(rating);
    }

    public Boolean hasRequestedBook(String isbn) {
        Iterator<Loan> it = loans.values();
        while (it.hasNext()) {
            Loan loan = it.next();
            if (loan.getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }

    public Iterator<Rating> getAllRatings() {
        return ratings.values();
    }

    public Boolean hasAlreadyRatedBook(Rating rating) {
        if (rating==null) {
            return false;
        }
        return ratings.get(rating) != null;
    }

    @Override
    public int compareTo(Reader o) {
        return this.getId().compareTo(o.getId());
    }
}
