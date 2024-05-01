package core;

import org.junit.Test;
import static org.junit.Assert.*;
import tileengine.TETile;

public class AutograderBuddyTest {

    @Test
    public void test1() {
        String inputSave = "N1234S";
        TETile[][] worldAfterSave = AutograderBuddy.getWorldFromInput(inputSave);
        assertNotNull("World after save should not be null", worldAfterSave);

    }

    @Test
    public void test2() {
        String inputSave = "N999SDDD:Q";
        TETile[][] worldAfterSave = AutograderBuddy.getWorldFromInput(inputSave);
        assertNotNull("World after save should not be null", worldAfterSave);

        String inputLoadAndMove = "LWWWDDD";
        TETile[][] worldAfterLoadAndMove = AutograderBuddy.getWorldFromInput(inputLoadAndMove);
        assertNotNull("World after load and move should not be null", worldAfterLoadAndMove);
    }

    @Test
    public void test3() {
        String inputSave = "N999SDDD:Q";
        TETile[][] worldAfterSave = AutograderBuddy.getWorldFromInput(inputSave);
        assertNotNull("World after save should not be null", worldAfterSave);

        String inputLoadAndMove = "LWWW:Q";
        TETile[][] worldAfterLoadAndMove = AutograderBuddy.getWorldFromInput(inputLoadAndMove);
        assertNotNull("World after load and move should not be null", worldAfterLoadAndMove);

        String loadAndMoveAgain = "LDDD:Q";
        TETile[][] last = AutograderBuddy.getWorldFromInput(loadAndMoveAgain);
        assertNotNull("World after load and move should not be null", last);
    }

    @Test
    public void test4() {
        String inputSave = "N999SDDD:Q";
        TETile[][] worldAfterSave = AutograderBuddy.getWorldFromInput(inputSave);
        assertNotNull("World after save should not be null", worldAfterSave);

        String save = "L:Q";
        TETile[][] worldAfterLoadAndMove = AutograderBuddy.getWorldFromInput(save);
        assertNotNull("World after load and move should not be null", worldAfterLoadAndMove);

        String saveAgain = "L:Q";
        TETile[][] third = AutograderBuddy.getWorldFromInput(saveAgain);
        assertNotNull("World after load and move should not be null", third);

        String loadAndMove = "LWWWDDD";
        TETile[][] last = AutograderBuddy.getWorldFromInput(loadAndMove);
        assertNotNull("World after load and move should not be null", last);
    }

    @Test
    public void testSameInputTwice() {
        String input1 = "n5643591630821615871swwaawd";
        TETile[][] first = AutograderBuddy.getWorldFromInput(input1);

        String input2 = "n5643591630821615871swwaawd";
        TETile[][] second = AutograderBuddy.getWorldFromInput(input2);

        assertEquals(first, second);
    }
    @Test
    public void testRunSaveLoadRun() {
        String input1 = "n1392967723524655428sddsaawwsaddw";
        TETile[][] first = AutograderBuddy.getWorldFromInput(input1);

        String input2 = "n1392967723524655428sddsaawws:q";
        String input3 = "laddw";
        TETile[][] second = AutograderBuddy.getWorldFromInput(input2);
        TETile[][] third = AutograderBuddy.getWorldFromInput(input3);

        assertEquals(first, third);
    }
}
