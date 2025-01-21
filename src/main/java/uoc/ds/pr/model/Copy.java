package uoc.ds.pr.model;

import uoc.ds.pr.util.DSLinkedList;
import edu.uoc.ds.traversal.Iterator;
import edu.uoc.ds.adt.nonlinear.PriorityQueue;

import java.util.Comparator;


public class Copy extends Book{
    public static final Comparator<Copy> CMP = (cp1, cp2) -> cp2.getCopyId().compareTo(cp1.getCopyId());

    public DSLinkedList<Loan> loans;
    public PriorityQueue<Request> requests;
    public PriorityQueue<Reader> requestReaders;
    public Boolean available;

    public Copy(String bookId, String title, String publisher, String edition, int publicationYear, String isbn, Author author, Theme theme) {
        super(bookId, title, publisher, edition, publicationYear, isbn, author, theme);
        this.loans = new DSLinkedList<>(Loan.CMP);
        this.requests = new PriorityQueue<>(Request.CMP);
        this.requestReaders = new PriorityQueue<>(Reader.CMP_L);
        this.available = true;
    }

    public String getCopyId() {
        return getBookId();
    }

    public PriorityQueue<Request> getRequests() {
        return requests;
    }

    public void setRequests(PriorityQueue<Request> requests) {
        this.requests = requests;
    }

    public Request getRequest(String readerId) {
        Iterator<Request> reqIt = requests.values();
        while(reqIt.hasNext()) {
            Request req = reqIt.next();
            if(req.getReader().getId().equals(readerId) && req.getCopy().equals(this))  {
                return req;
            }

        }
        return null;

    }
    public void addRequest(Request request) {
        requests.add(request);
        requestReaders.add(request.getReader());
    }

    public DSLinkedList<Loan> getLoans() {
        return loans;
    }
    public Iterator<Loan> getLoanIterator() {
        return loans.values();
    }
    public Loan getLoan(String loanId) {
        Iterator<Loan> loanIt = loans.values();
        while(loanIt.hasNext()){
            Loan loan = loanIt.next();
            if(loan.getLoanId().equals(loanId)) {
                return loan;
            }
        }
        return null;
    }

    public void addLoan(Loan loan) {
        loans.insertEnd(loan);
    }

    public Reader getHighestReader(){
        return requestReaders.poll();
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setUnavailable() {
        available = false;
    }
}
