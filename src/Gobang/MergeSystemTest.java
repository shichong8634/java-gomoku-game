package Gobang;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MergeSystemTest {
    @Test
    public void testMergeModeSwitch() {
        Gomokulogic logic = new Gomokulogic();
        MergeSystem merge = new MergeSystem(logic);

        merge.setMerge();
        assertEquals(1, merge.getMode());

        merge.setClassic();
        assertEquals(0, merge.getMode());
    }
}
