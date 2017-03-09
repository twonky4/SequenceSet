package one.kose.sequenceset;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SequenceSetFilterTest {
    @Test
    public void testSimple() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("4");

        assertFalse(set.inList(1L));
        assertFalse(set.inList(2L));
        assertFalse(set.inList(3L));
        assertTrue(set.inList(4L));
        assertFalse(set.inList(5L));
        assertFalse(set.inList(6L));
        assertFalse(set.inList(7L));
        assertFalse(set.inList(8L));
        assertFalse(set.inList(9L));
    }

    @Test
    public void testSimpleWildcard() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("*");

        assertTrue(set.inList(1L));
        assertTrue(set.inList(2L));
        assertTrue(set.inList(3L));
        assertTrue(set.inList(4L));
        assertTrue(set.inList(5L));
        assertTrue(set.inList(6L));
        assertTrue(set.inList(7L));
        assertTrue(set.inList(8L));
        assertTrue(set.inList(9L));
    }

    @Test
    public void testRange() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("3:5");

        assertFalse(set.inList(1L));
        assertFalse(set.inList(2L));
        assertTrue(set.inList(3L));
        assertTrue(set.inList(4L));
        assertTrue(set.inList(5L));
        assertFalse(set.inList(6L));
        assertFalse(set.inList(7L));
        assertFalse(set.inList(8L));
        assertFalse(set.inList(9L));
    }

    @Test
    public void testRangeWildcardFrom() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("*:5");

        assertTrue(set.inList(1L));
        assertTrue(set.inList(2L));
        assertTrue(set.inList(3L));
        assertTrue(set.inList(4L));
        assertTrue(set.inList(5L));
        assertFalse(set.inList(6L));
        assertFalse(set.inList(7L));
        assertFalse(set.inList(8L));
        assertFalse(set.inList(9L));
    }

    @Test
    public void testRangeWildcardTo() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("3:*");

        assertFalse(set.inList(1L));
        assertFalse(set.inList(2L));
        assertTrue(set.inList(3L));
        assertTrue(set.inList(4L));
        assertTrue(set.inList(5L));
        assertTrue(set.inList(6L));
        assertTrue(set.inList(7L));
        assertTrue(set.inList(8L));
        assertTrue(set.inList(9L));
    }

    @Test
    public void testRangeWildcardBoth() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("*:*");

        assertTrue(set.inList(1L));
        assertTrue(set.inList(2L));
        assertTrue(set.inList(3L));
        assertTrue(set.inList(4L));
        assertTrue(set.inList(5L));
        assertTrue(set.inList(6L));
        assertTrue(set.inList(7L));
        assertTrue(set.inList(8L));
        assertTrue(set.inList(9L));
    }

    @Test
    public void testCounting() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("2,4");

        assertFalse(set.inList(1L));
        assertTrue(set.inList(2L));
        assertFalse(set.inList(3L));
        assertTrue(set.inList(4L));
        assertFalse(set.inList(5L));
        assertFalse(set.inList(6L));
        assertFalse(set.inList(7L));
        assertFalse(set.inList(8L));
        assertFalse(set.inList(9L));
    }

    @Test
    public void testReverseCounting() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("3,2,1");

        assertTrue(set.inList(1L));
        assertTrue(set.inList(2L));
        assertTrue(set.inList(3L));
        assertFalse(set.inList(4L));
        assertFalse(set.inList(5L));
        assertFalse(set.inList(6L));
        assertFalse(set.inList(7L));
        assertFalse(set.inList(8L));
        assertFalse(set.inList(9L));
    }

    @Test
    public void testCountingWildcard() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("2,*");

        assertTrue(set.inList(1L));
        assertTrue(set.inList(2L));
        assertTrue(set.inList(3L));
        assertTrue(set.inList(4L));
        assertTrue(set.inList(5L));
        assertTrue(set.inList(6L));
        assertTrue(set.inList(7L));
        assertTrue(set.inList(8L));
        assertTrue(set.inList(9L));
    }

    @Test
    public void testComplex() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("2,4:6,8");

        assertFalse(set.inList(1L));
        assertTrue(set.inList(2L));
        assertFalse(set.inList(3L));
        assertTrue(set.inList(4L));
        assertTrue(set.inList(5L));
        assertTrue(set.inList(6L));
        assertFalse(set.inList(7L));
        assertTrue(set.inList(8L));
        assertFalse(set.inList(9L));
    }

    @Test
    public void testComplexWildcardOuter() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("*:3,5,7:*");

        assertTrue(set.inList(1L));
        assertTrue(set.inList(2L));
        assertTrue(set.inList(3L));
        assertFalse(set.inList(4L));
        assertTrue(set.inList(5L));
        assertFalse(set.inList(6L));
        assertTrue(set.inList(7L));
        assertTrue(set.inList(8L));
        assertTrue(set.inList(9L));
    }

    @Test
    public void testComplexWildcardInner() throws SequenceSetException {
        SequenceSet set = SequenceSet.defaults().add("3:*,*:7");

        assertTrue(set.inList(1L));
        assertTrue(set.inList(2L));
        assertTrue(set.inList(3L));
        assertTrue(set.inList(4L));
        assertTrue(set.inList(5L));
        assertTrue(set.inList(6L));
        assertTrue(set.inList(7L));
        assertTrue(set.inList(8L));
        assertTrue(set.inList(9L));
    }
}
