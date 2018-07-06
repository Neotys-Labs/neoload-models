package com.neotys.neoload.model.readers.loadrunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.neotys.neoload.model.repository.ImmutableRegexpValidator;
import com.neotys.neoload.model.repository.ImmutableTextValidator;
import com.neotys.neoload.model.repository.RegexpValidator;
import com.neotys.neoload.model.repository.Validator;

public class WebRegFindTest {


	@Test
	public void toElementTest1() {
		List<String> inputStrList = new ArrayList<>();
		inputStrList.add("\"Text/BIN/DIG/ALNUMIC=test_str\"");
		inputStrList.add("\"LAST\"");

		MethodCall input = ImmutableMethodCall.builder().addAllParameters(inputStrList).name("Text/BIN/DIG/ALNUMIC=test_str").build();
		
		Validator expectedGenerator = ImmutableTextValidator.builder()
				.name("Text/BIN/DIG/ALNUMIC=test_str_0")
				.validationText("test_str")
				.haveToContains(true)
				.build();
		
		Validator generatedExtractor = WebRegFind.toValidator("{", "}", input);
		assertEquals(expectedGenerator, generatedExtractor);
	}

	
	@Test
	public void toElementTest2() {
		List<String> inputStrList = new ArrayList<>();
		inputStrList.add("\"TextPfx/BIN/DIG/ALNUMLC=start_str\"");
		inputStrList.add("\"TextSfx=end_str\"");
		inputStrList.add("\"LAST\"");

		MethodCall input = ImmutableMethodCall.builder().addAllParameters(inputStrList).name("TextPfx/BIN/DIG/ALNUMLC=start_str").build();
		
		Validator expectedGenerator = ImmutableRegexpValidator.builder()
				.name("TextPfx/BIN/DIG/ALNUMLC=start_str_1")
				.validationRegex("\\Qstart_str\\E.*\\Qend_str\\E")
				.haveToContains(true)
				.build();
		
		Validator generatedExtractor = WebRegFind.toValidator("{", "}", input);
		assertEquals(expectedGenerator, generatedExtractor);
	}

	@Test
	public void toElementTest3() {
		List<String> inputStrList = new ArrayList<>();
		inputStrList.add("\"Fail=Found\"");
		inputStrList.add("\"Search=Headers\"");
		inputStrList.add("\"SaveCount=3\"");
		inputStrList.add("\"Text=specific test\"");
		inputStrList.add("\"LAST\"");

		MethodCall input = ImmutableMethodCall.builder().addAllParameters(inputStrList).name("Fail=Found").build();
		
		Validator expectedGenerator = ImmutableTextValidator.builder()
				.name("Fail=Found_2")
				.validationText("specific test")
				.haveToContains(false)
				.build();
		
		Validator generatedExtractor = WebRegFind.toValidator("{", "}", input);
		assertEquals(expectedGenerator, generatedExtractor);
	}
	
	@Test
	public void testRegex1() {
		List<String> inputStrList = new ArrayList<>();
		inputStrList.add("\"TextPfx/BIN/DIG/ALNUMLC=start_str\"");
		inputStrList.add("\"TextSfx=end_str\"");
		inputStrList.add("\"LAST\"");

		MethodCall input = ImmutableMethodCall.builder().addAllParameters(inputStrList).name("Fail=Found").build();

		RegexpValidator generatedExtractor = (RegexpValidator) WebRegFind.toValidator("{", "}", input);
		String regexPattern = generatedExtractor.getValidationRegex();
		
		String textToTest = "du texte qui sert à combler etstart_str et dutexte qui doit être trouvéend_str et du texte qui sert aussi à combler";
		Pattern r = Pattern.compile(regexPattern);
		Matcher m = r.matcher(textToTest);
		
		assertTrue(m.find());
	}
	
	@Test
	public void testRegex2() {
		List<String> inputStrList = new ArrayList<>();
		inputStrList.add("\"TextPfx/BIN/DIG/ALNUMLC=start_str\"");
		inputStrList.add("\"TextSfx=end_str\"");
		inputStrList.add("\"LAST\"");

		MethodCall input = ImmutableMethodCall.builder().addAllParameters(inputStrList).name("Fail=Found").build();

		RegexpValidator generatedExtractor = (RegexpValidator) WebRegFind.toValidator("{", "}", input);
		String regexPattern = generatedExtractor.getValidationRegex();
		
		String textToTest = "du texte qui sert à combler etstar_str et dutexte qui ne doit pas être trouvéend_str et du texte qui sert aussi à combler";
		Pattern r = Pattern.compile(regexPattern);
		Matcher m = r.matcher(textToTest);
		
		assertFalse(m.find());
	}
}
