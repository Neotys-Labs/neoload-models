package com.neotys.neoload.model.readers.loadrunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import com.neotys.neoload.model.repository.ImmutableParameter;
import com.neotys.neoload.model.repository.Parameter;


public class MethodUtilsTest {

    @Test
    public void getMethodParameterTest() {
        ImmutableMethodCall method = ImmutableMethodCall.builder()
                .name("Test")
                .addParameters("URL=this is my url")
                .build();
        assertThat(MethodUtils.getParameterWithName(method,"URL")).isNotEmpty().hasValue("URL=this is my url");

        method = ImmutableMethodCall.builder()
                .name("Test")
                .addParameters("FirstParameter")
                .addParameters("URL=this is my url")
                .addParameters("test=this is my test")
                .build();
        assertThat(MethodUtils.getParameterWithName(method,"URL")).isNotEmpty().hasValue("URL=this is my url");

        method = ImmutableMethodCall.builder()
                .name("Test")
                .addParameters("FirstParameter")
                .addParameters("URI=this is my url")
                .addParameters("test=this is my test")
                .build();
        assertThat(MethodUtils.getParameterWithName(method,"URL")).isEmpty();
    }

    @Test
    public void unquoteTest() {
        assertThat(MethodUtils.unquote("simple text")).isEqualTo("simple text");
        assertThat(MethodUtils.unquote("\"simple quote")).isEqualTo("\"simple quote");
        assertThat(MethodUtils.unquote("simple text\"")).isEqualTo("simple text\"");
        assertThat(MethodUtils.unquote("\"quoted text\"")).isEqualTo("quoted text");
        assertThat(MethodUtils.unquote("\"\"")).isEqualTo("");
    }
    
    @Test
    public void normalizeVariablesTest() {    
        assertThat(MethodUtils.normalizeVariables("{", "}", "simple {text} exemple")).isEqualTo("simple ${text} exemple");
        assertThat(MethodUtils.normalizeVariables("{", "}", "double {text} {exemple} try")).isEqualTo("double ${text} ${exemple} try");
        assertThat(MethodUtils.normalizeVariables("{", "}", "{start} and end {exemple}")).isEqualTo("${start} and end ${exemple}");
      
        assertThat(MethodUtils.normalizeVariables("{[[", "]]}", "\"more {[[complex exemple]]} try\"")).isEqualTo("\"more ${complex exemple} try\"");

		assertThat(MethodUtils.normalizeVariables("&", "@", "\"a &custom exemple@ try\"")).isEqualTo("\"a ${custom exemple} try\"");
    }

    @Test
	public void unescapeTest() {
    	assertThat(MethodUtils.unescape(null)).isEqualTo(null);
    	assertThat(MethodUtils.unescape("MyString is \\\"value\\\"")).isEqualTo("MyString is \"value\"");
		assertThat(MethodUtils.unescape("MyString is \"value\"")).isEqualTo("MyString is \"value\"");
	}
    
    @Test
    public void queryToParameterListTestWithSingleKeyValue() {
    	String query = "key1=value1";
    	List<Parameter> expectedList = new ArrayList<>();
    	expectedList.add(ImmutableParameter.builder().name("key1").value("value1").build());
    	assertEquals(MethodUtils.queryToParameterList(query), expectedList);
    }
    
    @Test
    public void queryToParameterListTestWithSingleKey() {
    	String query = "key1";
    	List<Parameter> expectedList = new ArrayList<>();
    	expectedList.add(ImmutableParameter.builder().name("key1").build());
    	assertEquals(MethodUtils.queryToParameterList(query), expectedList);
    }
    
    @Test
    public void queryToParameterListTestWithMultiplesKeyValues() {
    	String query = "key1=value1&key2=value2";
    	List<Parameter> expectedList = new ArrayList<>();
    	expectedList.add(ImmutableParameter.builder().name("key1").value("value1").build());
    	expectedList.add(ImmutableParameter.builder().name("key2").value("value2").build());
    	assertEquals(MethodUtils.queryToParameterList(query), expectedList);
    }
    
    @Test
    public void queryToParameterListTestWithMultiplesKeys() {
    	String query = "key1&key2";
    	List<Parameter> expectedList = new ArrayList<>();
    	expectedList.add(ImmutableParameter.builder().name("key1").build());
    	expectedList.add(ImmutableParameter.builder().name("key2").build());
    	assertEquals(MethodUtils.queryToParameterList(query), expectedList);
    }
    
    @Test
    public void queryToParameterListTestWithKeyValueAndOnlyKey() {
    	String query = "key1=value1&key2";
    	List<Parameter> expectedList = new ArrayList<>();
    	expectedList.add(ImmutableParameter.builder().name("key1").value("value1").build());
    	expectedList.add(ImmutableParameter.builder().name("key2").build());
    	assertEquals(MethodUtils.queryToParameterList(query), expectedList);
    }
    
    @Test
    public void queryToParameterListTestWithEncodingCharacters() {
    	String query = "key1=value%201&key2";
    	List<Parameter> expectedList = new ArrayList<>();
    	expectedList.add(ImmutableParameter.builder().name("key1").value("value 1").build());
    	expectedList.add(ImmutableParameter.builder().name("key2").build());
    	assertEquals(MethodUtils.queryToParameterList(query), expectedList);
    }
    
	@Test
	public void extractExtraresAsStringListWithLastBoundary() {
		List<String> input = new ArrayList<>();
		input.add("\"test_nom\"");
		input.add("\"URL=https://server-test.com/test_path?param=value&timestamp=1520850713&gl=FR\"");
		input.add("\"Resource=0\"");
		input.add("\"RecContentType=text/html\"");
		input.add("\"Referer=https://server_test.fr/\"");
		input.add("\"Snapshot=t4.inf\"");
		input.add("\"Mode=HTML\"");
		input.add("EXTRARES");
		input.add("\"Url=https://server-test.com/path/1/photo1.png\"");
		input.add("\"Referer=https://www.server-test.fr/\"");
		input.add("ENDITEM");
		input.add("\"Url=https://server-test.com/path2/photo2.png\"");
		input.add("\"Referer=https://www.server-test.fr/\"");
		input.add("ENDITEM");
		input.add("LAST");
		
		List<String> output = new ArrayList<>();
		output.add("Url=https://server-test.com/path/1/photo1.png");
		output.add("Referer=https://www.server-test.fr/");
		output.add("ENDITEM");
		output.add("Url=https://server-test.com/path2/photo2.png");
		output.add("Referer=https://www.server-test.fr/");
		output.add("ENDITEM");
		
		Optional<List<String>> generatedResult = MethodUtils.extractItemListAsStringList("{", "}", input, MethodUtils.ITEM_BOUNDARY.EXTRARES.toString());
		
		Optional<List<String>> expectedResult = Optional.of(output);
		
		assertEquals(expectedResult, generatedResult);
	}

	@Test
	public void extractExtraresAsStringListWithBoundaryInTheMiddle() {
		List<String> input = ImmutableList.of(
				"\"test_nom\"",
				"\"URL=https://server-test.com/test_path?param=value&timestamp=1520850713&gl=FR\"",
				"EXTRARES",
				"Url=https://server-test.com/path/1/photo1.png",
				"Referer=https://www.server-test.fr/",
				"ENDITEM",
				"LAST",
				"Should Not be extracted"

		);

		List<String> result = ImmutableList.of(
				"Url=https://server-test.com/path/1/photo1.png",
				"Referer=https://www.server-test.fr/",
				"ENDITEM"

		);
		assertThat(MethodUtils.extractItemListAsStringList("{", "}", input, MethodUtils.ITEM_BOUNDARY.EXTRARES.toString()).get()).isEqualTo(result);
	}

	@Test
	public void extractExtraresAsStringListWithoutBoundary() {
		List<String> input = ImmutableList.of(
				"\"test_nom\"",
				"\"URL=https://server-test.com/test_path?param=value&timestamp=1520850713&gl=FR\"",
				"EXTRARES",
				"Url=https://server-test.com/path/1/photo1.png",
				"Referer=https://www.server-test.fr/",
				"ENDITEM"

		);

		List<String> result = ImmutableList.of(
				"Url=https://server-test.com/path/1/photo1.png",
				"Referer=https://www.server-test.fr/",
				"ENDITEM"

		);
		assertThat(MethodUtils.extractItemListAsStringList("{", "}", input, MethodUtils.ITEM_BOUNDARY.EXTRARES.toString()).get()).isEqualTo(result);
	}

	@Test
	public void extractExtraresAsStringListWithoutType() {
		List<String> input = ImmutableList.of(
				"\"test_nom\"",
				"\"URL=https://server-test.com/test_path?param=value&timestamp=1520850713&gl=FR\"",
				"Url=https://server-test.com/path/1/photo1.png",
				"Referer=https://www.server-test.fr/",
				"ENDITEM"

		);

		assertThat(MethodUtils.extractItemListAsStringList("{", "}", input, MethodUtils.ITEM_BOUNDARY.EXTRARES.toString())).isEqualTo(Optional.empty());
	}

    @Test
    public void extractExtraresAsStringListOnlyBoundary() {
        List<String> input = ImmutableList.of("LAST");

        assertThat(MethodUtils.extractItemListAsStringList("{", "}", input, MethodUtils.ITEM_BOUNDARY.EXTRARES.toString())).isEqualTo(Optional.empty());
    }

	@Test
	public void parseItemList() {
		List<String> input = ImmutableList.of(
				"key1=value1",
				"key1_1=value1_1",
				"ENDITEM",
				"key2=value2",
				"Key2_2=value2_2",
				"ENDITEM"

		);

		assertThat(MethodUtils.parseItemList(input).get(0).getAttributes()).isEqualTo(ImmutableList.of("key1=value1", "key1_1=value1_1"));
	}

	@Test
	public void parseItemListWithMoreInfo() {
		List<String> input = ImmutableList.of(
				"key1=value1",
				"key1_1=value1_1",
				"ENDITEM",
				"key2=value2",
				"Key2_2=value2_2",
				"ENDITEM",
				"key3=value3",
				"Key3_2=value3_2"

		);

		assertThat(MethodUtils.parseItemList(input).get(0).getAttributes()).isEqualTo(ImmutableList.of("key1=value1", "key1_1=value1_1"));
	}

	@Test
	public void parseItemListWithoutEndItem() {
		assertThat(MethodUtils.parseItemList(ImmutableList.of())).isEmpty();
	}

	@Test
	public void normalizeNameTest() {
		//'£', '', '$', '\"', '[', ']', '<', '>', '|', '*', '¤', '?', '§', 'µ', '#', '`', '@', '^', '°', '¨', '\\'
		assertThat(MethodUtils.normalizeName("start*end")).isEqualTo("start_end");
		assertThat(MethodUtils.normalizeName("£\u0080$\\[]<>|*¤?§µ#`@°¨\\\\")).isEqualTo("_____________________");
	}

	@Test
	public void parseItemListEmpty() {
		assertThat(MethodUtils.parseItemList(ImmutableList.of())).isEmpty();
	}

}
