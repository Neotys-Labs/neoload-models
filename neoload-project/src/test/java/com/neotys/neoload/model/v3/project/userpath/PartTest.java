package com.neotys.neoload.model.v3.project.userpath;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PartTest {
	@Test
	public void constants() {
		assertEquals("content_type", Part.CONTENT_TYPE);
		assertEquals("charset", Part.CHARSET);
		assertEquals("transfer_encoding", Part.TRANSFER_ENCODING);
		assertEquals("value", Part.VALUE);
		assertEquals("filename", Part.FILENAME);
		assertEquals("source_filename", Part.SOURCE_FILENAME);
	}
}
