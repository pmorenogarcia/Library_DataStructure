package uoc.ds.pr.model;

import java.time.LocalDate;
import java.util.Comparator;

public class Request {
    public static final Comparator<Request> CMP = (rq1, rq2) -> rq1.getReader().getId().compareTo(rq2.getReader().getId());
    private Reader reader;
    private Copy copy;
    private final LocalDate date;

    public Request(Reader reader, Copy copy, LocalDate date) {
        this.reader = reader;
        this.copy = copy;
        this.date = date;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }
}
