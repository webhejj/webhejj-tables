package hu.webhejj.commons.io.table;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class AlphaColumnUtilTest {

	@Test
	public void testToAlphaNegative() {
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
			AlphaColumnUtil.toAlpha(-1);
		});
	}

	@Test
	public void testToAlpha() {
		testToAlpha("A", 0);
		testToAlpha("Z", 25);
		testToAlpha("AA", 26);
		testToAlpha("AZ", 51);
		testToAlpha("BA", 52);
	}

	@Test
	public void testRoundTrip() {
		for (int i = 0; i < 60; i++) {
			Assertions.assertEquals(i, AlphaColumnUtil.toNumeric(AlphaColumnUtil.toAlpha(i)));
		}
	}

	protected void testToAlpha(String expected, int actual) {
		Assertions.assertEquals(expected, AlphaColumnUtil.toAlpha(actual));
	}
}
