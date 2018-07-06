package com.neotys.neoload.model.writers;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class RegExpUtilsTest {

    @Test
    public void escapeExcludingVariablesTest() {
        assertThat(RegExpUtils.escapeExcludingVariables("test")).isEqualTo("test");
        assertThat(RegExpUtils.escapeExcludingVariables("te.st")).isEqualTo("te\\.st");
        assertThat(RegExpUtils.escapeExcludingVariables("${test}")).isEqualTo("${test}");
        assertThat(RegExpUtils.escapeExcludingVariables("$test}")).isEqualTo("\\$test\\}");
        assertThat(RegExpUtils.escapeExcludingVariables("{test}")).isEqualTo("\\{test\\}");
        assertThat(RegExpUtils.escapeExcludingVariables(".\\+*?(){}[]$^|")).isEqualTo("\\.\\\\\\+\\*\\?\\(\\)\\{\\}\\[\\]\\$\\^\\|");

        assertThat(RegExpUtils.escapeExcludingVariables("${myVar$.end}and.end")).isEqualTo("${myVar$.end}and\\.end");
    }
}
