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

    public static String checkValue(String key) {
        if (variablesList.containsKey(key)) {
            StringBuilder value = new StringBuilder();
            int i = 0;
            try {
                Integer.parseInt(variablesList.get(key));
                return variablesList.get(key);
            } catch (Exception e) {
                String brutValue = variablesList.get(key).split(",")[1];
                while (brutValue.charAt(i) != ')') {
                    value.append(brutValue.charAt(i));
                    i++;
                }
                return value.toString();
            }
        }else return null;
    }

//    public static int getValue(String key){
//        String value = checkValue(key);
//        if (value != null){
//        }
//    }
}
