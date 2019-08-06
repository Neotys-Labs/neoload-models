package com.neotys.neoload.model.readers.jmeter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public final class VariablesUtils {

    //Attributs
    private static Map<String, String> variablesList;
    private static final Logger LOGGER = LoggerFactory.getLogger(VariablesUtils.class);

    public VariablesUtils() {
        variablesList = new HashMap<>();
    }

    public static void addList(String key, String value) {
        variablesList.put(key, value);

    }


    public static String getValue(String key) {
        return variablesList.get(key);
    }
}
