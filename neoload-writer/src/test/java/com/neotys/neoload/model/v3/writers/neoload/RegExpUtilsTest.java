package com.neotys.neoload.model.v3.writers.neoload;

import com.neotys.neoload.model.v3.util.RegExpUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class RegExpUtilsTest {

    @Test
    public void escapeTest() {
        Assertions.assertThat(RegExpUtils.escape('[')).isEqualTo("\\[");
        Assertions.assertThat(RegExpUtils.escape('a')).isEqualTo("a");
        Assertions.assertThat(RegExpUtils.escape('{')).isEqualTo("\\{");

        Assertions.assertThat(RegExpUtils.escape("nothing to escape")).isEqualTo("nothing to escape");
        Assertions.assertThat(RegExpUtils.escape("must escape { ")).isEqualTo("must escape \\{ ");
        Assertions.assertThat(RegExpUtils.escape("variables should be escaped ${myVar}")).isEqualTo("variables should be escaped \\$\\{myVar\\}");

        Assertions.assertThat(RegExpUtils.escapeExcludingVariables("must escape { ")).isEqualTo("must escape \\{ ");
        Assertions.assertThat(RegExpUtils.escapeExcludingVariables("must escape $ ")).isEqualTo("must escape \\$ ");
        Assertions.assertThat(RegExpUtils.escapeExcludingVariables("variables should not be escaped ${myVar}")).isEqualTo("variables should not be escaped ${myVar}");
        Assertions.assertThat(RegExpUtils.escapeExcludingVariables("Not variables should be escaped ${myVar")).isEqualTo("Not variables should be escaped \\$\\{myVar");
        Assertions.assertThat(RegExpUtils.escapeExcludingVariables("Not variables should be escaped $${myVar}")).isEqualTo("Not variables should be escaped \\$${myVar}");

        Assertions.assertThat(RegExpUtils.escape("Some \t special \n characters \r")).isEqualTo("Some \\t special \\n characters \\r");
    }
}
