package Gobang;

import org.junit.Test;

import static org.junit.Assert.assertNull;

public class EndgameLoaderTest {
    @Test
    public void testLoadEndgameInvalidPath() {
        int[][] board = EndgameLoader.loadEndgame("123.txt");
        assertNull(board);
    }
}
