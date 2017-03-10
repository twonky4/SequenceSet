package one.kose.sequenceset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

public class SequenceSetBuildTest {
    @Test
    public void testSimple() {
        SequenceSet set = SequenceSet.defaults();

        set.add(1L);

        assertEquals("1", set.toString());
    }

    @Test
    public void testSignConfig() {
        SequenceSet set = SequenceSet.factory().wildcardAs("?").rangeAs("-").splitAs(";").build();

        set.add(1L);
        set.add(2L);
        set.add(3L);
        set.add(5L);
        set.add("7-?");

        assertEquals("1-3;5;7-?", set.toString());
    }

    @Test
    public void testSignConfigMultiChar() {
        SequenceSet set = SequenceSet.factory().splitAs(", ").build();

        set.add("1");
        set.add("3");
        set.add("5, 6, 7");

        assertEquals("1, 3, 5:7", set.toString());
    }

    @Test
    public void testAddRanges() {
        SequenceSet set = SequenceSet.defaults();

        set.add("1");
        set.add("3:5");
        set.add("7,8,9");

        assertEquals("1,3:5,7:9", set.toString());
    }

    @Test
    public void testSignConfigFail() {
        try {
            SequenceSet.factory().wildcardAs("?").rangeAs("?").splitAs(";").build();

            fail("should fail");
        } catch (SequenceSetInitException e) {
            assertEquals("range sign can not be equals with wildcard sign", e.getMessage());
        }
        try {
            SequenceSet.factory().wildcardAs("?").rangeAs("-").splitAs("-").build();

            fail("should fail");
        } catch (SequenceSetInitException e) {
            assertEquals("split sign can not be equals with range sign", e.getMessage());
        }
        try {
            SequenceSet.factory().wildcardAs("?").rangeAs("-").splitAs("?").build();

            fail("should fail");
        } catch (SequenceSetInitException e) {
            assertEquals("split sign can not be equals with wildcard sign", e.getMessage());
        }
        try {
            SequenceSet.factory().wildcardAs(null).rangeAs("").build();

            fail("should fail");
        } catch (SequenceSetInitException e) {
            assertEquals("range sign can not be empty", e.getMessage());
        }
        try {
            SequenceSet.factory().wildcardAs(null).build();

            fail("should fail");
        } catch (SequenceSetInitException e) {
            assertEquals("wildcard sign can not be empty", e.getMessage());
        }
        try {
            SequenceSet.factory().rangeAs("").build();

            fail("should fail");
        } catch (SequenceSetInitException e) {
            assertEquals("range sign can not be empty", e.getMessage());
        }
        try {
            SequenceSet.factory().rangeAs("0").build();

            fail("should fail");
        } catch (SequenceSetInitException e) {
            assertEquals("range sign can not be a number", e.getMessage());
        }
    }

    @Test
    public void testPairIncrement() {
        SequenceSet set = SequenceSet.defaults();

        set.add(1L);
        set.add(2L);

        assertEquals("1,2", set.toString());
    }

    @Test
    public void testPairNotIncrement() {
        SequenceSet set = SequenceSet.defaults();

        set.add(1L);
        set.add(5L);

        assertEquals("1,5", set.toString());
    }

    @Test
    public void testTreeIncrement() {
        SequenceSet set = SequenceSet.defaults();

        set.add(1L);
        set.add(2L);
        set.add(3L);

        assertEquals("1:3", set.toString());
    }

    @Test
    public void testTreeNotIncrement() {
        SequenceSet set = SequenceSet.defaults();

        set.add(1L);
        set.add(2L);
        set.add(7L);

        assertEquals("1,2,7", set.toString());
    }

    @Test
    public void testDoubleIncrement() {
        SequenceSet set = SequenceSet.defaults();

        set.add(1L);
        set.add(2L);
        set.add(3L);

        set.add(7L);
        set.add(8L);
        set.add(9L);

        set.add(11L);

        assertEquals("1:3,7:9,11", set.toString());
    }

    @Test
    public void testConstructSimple() {
        SequenceSet set = SequenceSet.defaults().add("1:3");

        set.add(4L);

        assertEquals("1:4", set.toString());
    }

    @Test
    public void testConstructWildCard() {
        SequenceSet set = SequenceSet.defaults().add("1:*");

        set.add(4);

        assertEquals("1:*", set.toString());
    }

    @Test
    public void testSimplify() {
        SequenceSet set = SequenceSet.defaults().add("1:3");

        set.add("4:7");
        set.add((String) null);
        set.add("");
        set.add((Long) null);
        set.add((Integer) null);

        assertEquals("1:7", set.toString());
    }

    @Test
    public void testCollection() {
        Collection<Object> c = new ArrayList<Object>();
        c.add(1L);
        c.add("3:5");
        c.add(7);
        c.add(null);
        SequenceSet set = SequenceSet.defaults().add(c);

        assertEquals("1,3:5,7", set.toString());
    }

    @Test
    public void testInvalidCollection() {
        Collection<Object> c = new ArrayList<Object>();
        c.add(5d);
        try {
            SequenceSet.defaults().add(c);

            fail("should fail");
        } catch (SequenceSetException e) {
            assertEquals(0, e.getPosition());
            assertEquals('5', e.getCharacter());
        }
    }

    @Test
    public void testWildCardMulti() {
        SequenceSet set = SequenceSet.defaults().add("1:*");

        set.add("5:7");

        assertEquals("1:*", set.toString());
    }

    @Test
    public void testCleanUp() {
        SequenceSet set = SequenceSet.defaults().add("7:8,1,2,4:5,9");

        assertEquals("1,2,4,5,7:9", set.toString());

        set = SequenceSet.defaults().add("7:8,1:*");

        assertEquals("1:*", set.toString());

        set = SequenceSet.defaults().add("*:8,1:3");

        assertEquals("*:8", set.toString());

        set = SequenceSet.defaults().add("1:3,1:3");

        assertEquals("1:3", set.toString());

        set = SequenceSet.defaults().add("1:3,2:4");

        assertEquals("1:4", set.toString());

        set = SequenceSet.defaults().add("2:4,1:3");

        assertEquals("1:4", set.toString());

        set = SequenceSet.defaults().add("2:3,1:4");

        assertEquals("1:4", set.toString());

        set = SequenceSet.defaults().add("3:5,1:7");

        assertEquals("1:7", set.toString());

        set = SequenceSet.defaults().add("*:4,*:5");

        assertEquals("*:5", set.toString());

        set = SequenceSet.defaults().add("1,4:*,5:*");

        assertEquals("1,4:*", set.toString());

        set = SequenceSet.defaults().add("1,4:*,5,3");

        assertEquals("1,3:*", set.toString());
    }

    @Test
    public void testSort() {
        SequenceSet set = SequenceSet.defaults().add("9,4:6,1,2");

        assertEquals("1,2,4:6,9", set.toString());
    }

    @Test
    public void testSortWildCard() {
        SequenceSet set = SequenceSet.defaults().add("4:*,2");

        assertEquals("2,4:*", set.toString());
    }

    @Test(expected = SequenceSetException.class)
    public void testInvalidChar() {
        SequenceSet.defaults().add("A");
    }

    @Test(expected = SequenceSetException.class)
    public void testInvalidQuote() {
        SequenceSet.defaults().add("\"\"");
    }
}
