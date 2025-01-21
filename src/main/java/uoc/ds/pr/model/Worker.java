package uoc.ds.pr.model;

import uoc.ds.pr.util.DSLinkedList;

import javax.xml.catalog.Catalog;

public class Worker {
    private String id;
    private String name;
    private String surname;

    private int totalCatalogBooks;

    private final DSLinkedList<CatalogedBook> catalogedBooks;
    private final DSLinkedList<Copy> catalogedCopies;
    private final DSLinkedList<Loan> loanList;
    private final DSLinkedList<Loan> closedLoanList;

    public Worker(String id, String name, String surname) {
        setId(id);
        update(name, surname);
        catalogedBooks = new DSLinkedList(CatalogedBook.CMP);
        catalogedCopies = new DSLinkedList(Copy.CMP);
        loanList = new DSLinkedList<>(Loan.CMP);
        closedLoanList = new DSLinkedList<>(Loan.CMP);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void update(String name, String surname) {
        setName(name);
        setSurname(surname);
    }

    public void catalogBook(CatalogedBook catalogedBook) {
        incTotalCatalogBooks();
        catalogedBooks.insertEnd(catalogedBook);
    }

    public int numCatalogBooks() {
        return catalogedBooks.size();
    }

    public int totalCatalogBooks() {
        return totalCatalogBooks;
    }
    public void incTotalCatalogBooks() {
        totalCatalogBooks++;
    }
    public void addCopy(Copy copy) {
        catalogedCopies.insertEnd(copy);
    }
    public int numCatalogCopies() {
        return catalogedCopies.size();
    }

    public void addLoan(Loan loan) {
        loanList.insertEnd(loan);
        loan.addWorker(this);
    }

    public void addClosedLoan(Loan loan) {
        closedLoanList.insertEnd(loan);
    }

    public int numLoans() {
        return loanList.size();
    }

    public int numClosedLoans() {
        return closedLoanList.size();
    }

    public Boolean hasCatalogedBook(CatalogedBook catalogedBook) {
        if (catalogedBooks.size() == 0) {
            return false;
        }
        return catalogedBooks.get(catalogedBook) != null;
    }
}