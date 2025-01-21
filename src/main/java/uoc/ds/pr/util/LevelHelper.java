package uoc.ds.pr.util;

import uoc.ds.pr.LibraryPR3.Level;

public class LevelHelper {

    public static Level getLevel(int level) {
        if(level < -10){
            return Level.TROLL;
        } else if(level < 0 && level > -10){
            return Level.MUGGLE;
        } else if(level < 10 && level > 0){
            return Level.OOMPA_LOOMPA;
        } else if(level < 20 && level > 10){
            return Level.HOBBIT;
        } else if(level < 30 && level > 20){
            return Level.FREMEN;
        } else if(level < 40 && level > 30){
            return Level.WINDRUNNER;
        } else if(level > 40){
            return Level.ASLAN;
        }
        return null;
    }
}
