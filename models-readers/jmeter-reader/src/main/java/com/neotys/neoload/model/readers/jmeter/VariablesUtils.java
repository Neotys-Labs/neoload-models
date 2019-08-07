package com.neotys.neoload.model.readers.jmeter;

import java.util.HashMap;
import java.util.Map;

public final class VariablesUtils {

    //Attributs
    private static Map<String, String> variablesList;

    public VariablesUtils() {
        variablesList = new HashMap<>();
    }

    public static void addList(String key, String value) {
        variablesList.put(key, value);

    }


    public static String getValue(String key) {
        key = key.split("\\{")[1];
        key = key.split("\\}")[0];
        return variablesList.get(key);
    }

    public static Map<String, String> getVariableList(){
        return variablesList;
    }
}
