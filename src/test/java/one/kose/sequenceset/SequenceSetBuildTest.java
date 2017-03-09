package one.kose.sequenceset;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SequenceSetBuildTest {
    @Test
    public void testSimple() {
        SequenceSet set = SequenceSet.defaults();

        set.add(1L);

        assertEquals("1", set.toString());
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
    public void testConstructSimple() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("1:3");

        set.add(4L);

        assertEquals("1:4", set.toString());
    }

    @Test
    public void testConstructWildCard() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("1:*");

        set.add(4L);

        assertEquals("1:*", set.toString());
    }

    @Test
    public void testSimplify() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("1:3");

        set.add("4:7");

        assertEquals("1:7", set.toString());
    }

    @Test
    public void testWildCardMulti() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("1:*");

        set.add("5:7");

        assertEquals("1:*", set.toString());
    }

    @Test
    public void testCleanUp() throws SequenceSetException {
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
    public void testSort() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("9,4:6,1,2");

        assertEquals("1,2,4:6,9", set.toString());
    }

    @Test
    public void testSortWildCard() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("4:*,2");

        assertEquals("2,4:*", set.toString());
    }

    @Test(expected = SequenceSetException.class)
    public void testInvalidChar() throws SequenceSetException {
        SequenceSet.defaults().add("A");
    }

    @Test(expected = SequenceSetException.class)
    public void testInvalidQuote() throws SequenceSetException {
        SequenceSet.defaults().add("\"\"");
    }
}
