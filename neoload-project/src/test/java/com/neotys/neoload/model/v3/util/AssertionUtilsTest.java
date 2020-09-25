package com.neotys.neoload.model.v3.util;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.userpath.assertion.Assertion;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.project.userpath.assertion.FakeAssertion;

public class AssertionUtilsTest {
    @Test
    public void constants() {
    	assertEquals("assertion_", AssertionUtils.PREFIX_NAME);
    }
    
    @Test
    public void normalyzeTest() {
    	final List<Assertion> assertions = ImmutableList.of(
    			ContentAssertion.builder()
    					.name("assertion_0")
    					.contains("contains_0")
    					.build(), 
    			FakeAssertion.builder()
    					.build(),
    			ContentAssertion.builder()
    					.contains("contains_1")
    					.build(),  
    			ContentAssertion.builder()
    					.name("assertion_2")
    					.contains("contains_2")
    					.build(),  
    			FakeAssertion.builder()
    					.build(),
    			ContentAssertion.builder()
    					.contains("contains_3")
    					.build());
    	
    	final List<Assertion> normalyzedAssertions = AssertionUtils.normalyze(assertions);
    	Assertions.assertThat(normalyzedAssertions.size()).isEqualTo(6);
    	Assertions.assertThat(((ContentAssertion)normalyzedAssertions.get(0)).getName()).isPresent();
    	Assertions.assertThat(((ContentAssertion)normalyzedAssertions.get(0)).getName().get()).isEqualTo("assertion_0");
    	Assertions.assertThat(normalyzedAssertions.get(1)).isInstanceOf(FakeAssertion.class);
    	Assertions.assertThat(((ContentAssertion)normalyzedAssertions.get(2)).getName()).isPresent();
    	Assertions.assertThat(((ContentAssertion)normalyzedAssertions.get(2)).getName().get()).isEqualTo("assertion_1");
    	Assertions.assertThat(((ContentAssertion)normalyzedAssertions.get(3)).getName()).isPresent();
    	Assertions.assertThat(((ContentAssertion)normalyzedAssertions.get(3)).getName().get()).isEqualTo("assertion_2");
    	Assertions.assertThat(normalyzedAssertions.get(4)).isInstanceOf(FakeAssertion.class);
    	Assertions.assertThat(((ContentAssertion)normalyzedAssertions.get(5)).getName()).isPresent();
    	Assertions.assertThat(((ContentAssertion)normalyzedAssertions.get(5)).getName().get()).isEqualTo("assertion_3");
    }
    
    @Test
    public void normalyzeContainsTest() {
    	Assertions.assertThat(AssertionUtils.normalyzeContains(Optional.empty())).isEqualTo("");
    	Assertions.assertThat(AssertionUtils.normalyzeContains(Optional.of("contains"))).isEqualTo("contains");
    }
}
