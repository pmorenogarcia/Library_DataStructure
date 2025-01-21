package uoc.ds.pr;


import uoc.ds.pr.util.DateUtils;

public class FactoryLibrary {


    public static LibraryPR3 getLibrary() throws Exception {
        LibraryPR3 theLibrary;
        theLibrary = new LibraryPR3Impl();


        ///
        /// READERS
        ///

        theLibrary.addReader("readerId1", "Maria", "Simó", "45423933A", DateUtils.createLocalDate("10-10-1987"), "Montgat", "the street", -5);
        theLibrary.addReader("readerId2", "Àlex", "Lluna", "664933A", DateUtils.createLocalDate("11-07-1977"), "Tortosa", "the street", 60);
        theLibrary.addReader("readerId3", "Pepet", "Marieta", "999933A", DateUtils.createLocalDate("12-06-1972"), "Amposta", "the street", 15);
        theLibrary.addReader("readerId4", "Joana", "Quilez", "199933A", DateUtils.createLocalDate("13-05-1999"), "Paris", "the street", -7);
        theLibrary.addReader("readerId5", "Armand", "Morata", "299933A", DateUtils.createLocalDate("20-12-1992"), "London", "the street", 25);
        theLibrary.addReader("readerId6", "Rut", "Paramio", "399933A", DateUtils.createLocalDate("22-03-1989"), "Girona", "the street", 18);
        theLibrary.addReader("readerId7", "Miriam", "Navarro", "499933A", DateUtils.createLocalDate("07-02-1969"), "Cáceres", "the street", 12);
        theLibrary.addReader("readerId8", "Pedro", "Tirrano", "599933A", DateUtils.createLocalDate("11-02-1945"), "Lleida", "the street", 55);
        theLibrary.addReader("readerId9", "Pedro", "Barón", "699933A", DateUtils.createLocalDate("16-01-1939"), "Guissona", "the street", 30);
        theLibrary.addReader("readerId10", "Emily", "Jones", "799933A", DateUtils.createLocalDate("19-01-1945"), "Juneda", "the street", 90);
        theLibrary.addReader("readerId11", "Maria", "Pérez", "899933A", DateUtils.createLocalDate("11-12-2002"), "Berlin", "the street", 4);
        theLibrary.addReader("readerId12", "Josep", "López", "919933A", DateUtils.createLocalDate("10-04-2001"), "Lille", "the street", 6);
        theLibrary.addReader("readerId13", "Rafael", "Vidal", "929933A", DateUtils.createLocalDate("06-03-2000"), "Dublin", "the street", -30);
        theLibrary.addReader("readerId14", "Joel", "Gràcia", "939933A", DateUtils.createLocalDate("11-05-2010"), "Liverpool", "the street", -20);
        theLibrary.addReader("readerId15", "Josep", "Martí", "949933A", DateUtils.createLocalDate("18-07-2005"), "Manchester", "the street", -10);
        theLibrary.addReader("readerId16", "Pere", "Jila", "959933A", DateUtils.createLocalDate("20-12-2002"), "Lyon", "the street", 5);
        theLibrary.addReader("readerId17", "Luis", "Blasco", "969933A", DateUtils.createLocalDate("29-06-2000"), "Marsella", "the street", 10);

        ///
        /// WORKERS
        ///
        theLibrary.addWorker("workerId1", "Pepe", "Gotera");
        theLibrary.addWorker("workerId2", "Otilio", "Botched Repairs");
        theLibrary.addWorker("workerId3", "Manolo", "y Compañía");
        theLibrary.addWorker("workerId4", "Benito", "y Compañía");

        return theLibrary;
    }



}