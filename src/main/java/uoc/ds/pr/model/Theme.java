package uoc.ds.pr.model;

import edu.uoc.ds.adt.nonlinear.AVLTree;

public class Theme {
    private String id;
    private final String name;
    private AVLTree<Book> books;

    public Theme(String id, String name){
        this.id = id;
        this.name = name;
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
}
