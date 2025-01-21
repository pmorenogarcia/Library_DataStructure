package uoc.ds.pr.data;

import org.junit.Assert;
import uoc.ds.pr.LibraryPR3;
import uoc.ds.pr.model.Theme;

public interface BooksMetaData {
    String[][] themeData1 = {
           // {"THEME1", "Fiction"},
           // {"THEME2", "Non-fiction"},
            {"THEME3", "Mystery"},
            {"THEME4", "Fantasy"},
            {"THEME5", "Science fiction"},
            {"THEME6", "Romance"},
            {"THEME7", "Thriller"},
            {"THEME8", "Biography"},
            {"THEME9", "Historical fiction"},
            {"THEME10", "Poetry"},
            {"THEME11", "Adventures"},
            {"THEME12", "social injustice"},
            {"THEME13", "social class"},
            {"THEME14", "personal growth"},
            {"THEME15", "social class"},
            {"THEME16", "social reform"},
            {"THEME17", "British society"},
            {"THEME18", "social criticism"},
            {"THEME19", "Tragedy; Revenge; Madness"},
            {"THEME20", "Ambition; Guilt; Fate"},
            {"THEME21", "Love / Tragedy"},
            {"THEME22", "Jealousy; Betrayal; Race"},
            {"THEME23", "Love II"},
            {"THEME24", "Betrayal; Power; Political Corruption"},
            {"THEME25", "Forgiveness; Revenge; Power"},
            {"THEME26", "Madness; Family Conflict; Loyalty"},
            {"THEME27", "Love III"},
            {"THEME28", "Love IV"},
            {"THEME29", "Family; Society; Romance"}
    };

    String[][] authors = {
          //  {"JV", "Jules", "Verne"},
          //  {"J.K.R", "J.K.", "Rowling"},
            {"KF", "Ken", "Follet"},
            {"MT", "Mark", "Twain"},
            {"CHD", "Charles", "Dickens"},
            {"WS", "William", "Shakespeare"},
            {"JA", "Jane", "Austen"},
            {"GAVO", "Gabriel", "García Márquez"}

    };

    static void addThemeData(LibraryPR3 theLibrary) {
        for (String[] theme: BooksMetaData.themeData1) {
            theLibrary.addTheme(theme[0], theme[1]);
        }
    }

    static void addAuthorData(LibraryPR3 theLibrary) {
        for (String[] author: BooksMetaData.authors) {
            theLibrary.addAuthor(author[0], author[1], author[2]);
        }
    }

}
