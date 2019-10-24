package com.neotys.neoload.model.readers.jmeter.variable;

import com.neotys.neoload.model.readers.jmeter.ContainerUtils;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.variable.ConstantVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.jmeter.testelement.TestElement.GUI_CLASS;

/**
 * This class convert UserSefinedVariable of JMter into Variables of neoload
 */
public class UserDefineVariableConverter implements BiFunction<Arguments, HashTree, List<Variable>> {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDefineVariableConverter.class);

    private static final String NAME_GROUP = "name";
    private static final String DEFAULT_VALUE_GROUP = "defaultvalue";
    private static final String REGEX = "(?<" + NAME_GROUP + ">\\$\\{__\\w+\\([^,]+,)(?<" + DEFAULT_VALUE_GROUP + ">[^\\)]*(\\)\\})*)\\)\\}";

    // (?<name>\$\{__\w+\([\w ]+,)(?<default_value>[^\)]*(\)\})*)\)\}
    private static final Pattern FUNCTION_PATTERN = Pattern.compile(REGEX);

    //Constructor
    UserDefineVariableConverter() {
    }

    //Methods

    /**
     * In this method, there is not UserDefinedVariable Element in the HashTree of JMeter
     * It's a config element, so it's very too general and we have to check is the name of the gui_class is UserDefinedVariable
     * and after that, we use the same solution, to browse the properties like in CSVDataSet
     *
     * @param jMeterProperties
     * @param hashTree
     * @return
     */
    public List<Variable> apply(final Arguments jMeterProperties, final HashTree hashTree) {
        final List<Variable> variableList = new ArrayList<>();
        if (ArgumentsPanel.class.getName().equals(jMeterProperties.getPropertyAsString(GUI_CLASS))) {
            final Map<String, String> variableMap = jMeterProperties.getArgumentsAsMap();
            for(Entry<String, String> entry : variableMap.entrySet()) {

                final String value = entry.getValue();
                final String parsedValue = parseValue(value);
                final ConstantVariable.Builder builder = (new ConstantVariable.Builder())
                        .name(entry.getKey())
                        .description(value)
                        .value(parsedValue);

                variableList.add(builder.build());
                ContainerUtils.addKeyValue(entry.getKey(), parsedValue);
            }
        }
        LOGGER.info("The conversion of User Defined Variable is a Success");
        EventListenerUtils.readSupportedFunction("UserDefinedVariable", "Constant String variable");
        return variableList;
    }

    private String parseMatch(final String value) {
        checkNotNull(value);

        final Matcher matcher = FUNCTION_PATTERN.matcher(value);

        String parsedValue = "";
        if (matcher.lookingAt()) {
            // function detected
            if (matcher.group(NAME_GROUP).startsWith("${__P(")) {
                // function P detected
                parsedValue = parseMatch(matcher.group(DEFAULT_VALUE_GROUP));
            } else {
                // other function
                parsedValue = value;
            }
        } else {
            // no function. Store as is.
            parsedValue = value;
        }

        return parsedValue;
    }

    private String parseValue(final String value) {
        checkNotNull(value);
        StringBuilder resultBuilder = new StringBuilder();

        final String[] segments = value.split(REGEX);

        if (segments.length == 0) {
            resultBuilder.append(parseMatch(value));
        } else {
            for (int i = 0; i < segments.length; i++) {
                resultBuilder.append(segments[i]);
                final String val = value.substring(value.indexOf(segments[i]) + segments[i].length());
                resultBuilder.append(parseMatch(val));
            }
        }

        return resultBuilder.toString();
    }
}
