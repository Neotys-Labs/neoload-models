package com.neotys.neoload.model.readers.loadrunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.repository.Page;

public class LRStartSubTransactionMethodTest {
	
	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "");
		
	@Test
	public void subTransactionsReaderTest() {

		try (InputStream targetStream = this.getClass().getResourceAsStream("ActionSubTransaction.c")) {
			final Container myContainer = LOAD_RUNNER_READER.parseCppFile("{", "}", targetStream, "MyContainer", Charsets.UTF_8);
			assertThat(myContainer).isNotNull();
			assertThat(myContainer.getChilds().size()).isEqualTo(1);
			final Container level1 = (Container) myContainer.getChilds().get(0);
			assertThat(level1.getName()).isEqualTo("level_1");
			assertThat(level1.getChilds().size()).isEqualTo(3);
			final Container level2 = (Container) level1.getChilds().get(0);
			final Page page2 = (Page) level1.getChilds().get(1);
			final Container level2b = (Container) level1.getChilds().get(2);
			assertThat(level2.getName()).isEqualTo("level_2a");
			assertThat(page2.getName()).isEqualTo("page#2");
			assertThat(level2b.getName()).isEqualTo("level_2b");

		} catch (IOException e) {
			fail("Error reading test stream", e);
		}
	}}
