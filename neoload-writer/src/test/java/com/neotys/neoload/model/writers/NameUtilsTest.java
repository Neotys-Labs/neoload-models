package com.neotys.neoload.model.writers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class NameUtilsTest {

	@Test
	public void normalize() {
		//'£', '', '$', '\"', '[', ']', '<', '>', '|', '*', '¤', '?', '§', 'µ', '#', '`', '@', '^', '°', '¨', '\\'
		assertThat(NameUtils.normalize("start*end")).isEqualTo("start_end");
		assertThat(NameUtils.normalize("£\u0080$\\[]<>|*¤?§µ#`@°¨\\\\")).isEqualTo("_____________________");
	}
}
