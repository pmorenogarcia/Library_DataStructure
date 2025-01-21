package uoc.ds.pr.util;

import org.junit.Assert;
import org.junit.Test;
import uoc.ds.pr.LibraryPR3;

public class LevelHelperTest {
    @Test
    public void levelHelperTest() {
        Assert.assertEquals(LibraryPR3.Level.ASLAN, LevelHelper.getLevel(999));
        Assert.assertEquals(LibraryPR3.Level.WINDRUNNER, LevelHelper.getLevel(35));
        Assert.assertEquals(LibraryPR3.Level.HOBBIT, LevelHelper.getLevel(18));
        Assert.assertEquals(LibraryPR3.Level.OOMPA_LOOMPA, LevelHelper.getLevel(5));
        Assert.assertEquals(LibraryPR3.Level.MUGGLE, LevelHelper.getLevel(-6));
        Assert.assertEquals(LibraryPR3.Level.TROLL, LevelHelper.getLevel(-20));
    }

}
