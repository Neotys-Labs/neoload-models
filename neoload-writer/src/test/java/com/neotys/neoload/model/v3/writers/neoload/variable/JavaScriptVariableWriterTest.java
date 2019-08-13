package com.neotys.neoload.model.v3.writers.neoload.variable;

import com.google.common.io.Files;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class JavaScriptVariableWriterTest {

    @Test
    public void testWriteXML() throws ParserConfigurationException, TransformerException, IOException {

        final String regexpUUID = "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}";
        final String regexpTS = "\\d{13}";

        final Pattern patternUUID = Pattern.compile(regexpUUID);
        final Pattern patternTS = Pattern.compile(regexpTS);

        final Document doc = generateEmptyDocument();
        final Element root = generateTestRootElement(doc);

        final String tmpDirPath = Files.createTempDir().getAbsolutePath();
        JavaScriptVariableWriter.of(JAVASCRIPT_VARIABLE_TEST).writeXML(doc, root, tmpDirPath);

        final String generatedResult = getXmlString(doc);

        Matcher matcher = patternUUID.matcher(generatedResult);

        // checking
        assertTrue(matcher.find());
        final String uid0 = matcher.group();
        assertTrue(matcher.find());
        final String uidjs = matcher.group();
        assertTrue(matcher.find());
        final String uid1 = matcher.group();
        assertEquals(uidjs, uid1);

        matcher = patternTS.matcher(generatedResult);

        assertTrue(matcher.find());
        final String ts = matcher.group();

        final String recomposedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><test-root><variable-javascript name=\"myVar\" offset=\"1\" order=\"2\" policy=\"5\" range=\"1\" uid=\""
                + uid0
                + "\" whenOutOfValues=\"CYCLE_VALUES\"><column name=\"col_0\" number=\"0\"/><script filename=\"scripts/jsVariable_"
                + uidjs
                + ".js\" name=\"myVar\" ts=\""
                + ts
                + "\" uid=\""
                + uid1
                + "\"/></variable-javascript></test-root>";

        assertEquals(recomposedResult, generatedResult);
    }

}
