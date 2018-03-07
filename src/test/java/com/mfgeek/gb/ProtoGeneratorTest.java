package com.mfgeek.gb;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ProtoGeneratorTest {

	@Test
	public void testGetIgnores() {
		ProtoGenerator pg = ProtoGenerator.getInstance();
		List<String> is = pg.getIgnores();
		assertNotNull(is);
		assertTrue(is.size() == 2);

	}

}
