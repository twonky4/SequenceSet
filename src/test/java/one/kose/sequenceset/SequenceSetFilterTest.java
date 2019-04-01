package one.kose.sequenceset;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SequenceSetFilterTest {
	@Test
	public void testSimple() {
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
	public void testSimpleWildcard() {
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
	public void testRange() {
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
	public void testRangeWildcardFrom() {
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
	public void testRangeWildcardTo() {
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
	public void testRangeWildcardBoth() {
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
	public void testCounting() {
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
	public void testReverseCounting() {
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
	public void testCountingWildcard() {
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
	public void testComplex() {
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
	public void testComplexWildcardOuter() {
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
	public void testComplexWildcardInner() {
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
