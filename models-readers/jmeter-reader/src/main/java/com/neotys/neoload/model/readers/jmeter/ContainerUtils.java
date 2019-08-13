package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.v3.project.variable.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains all the list that we use like parameters in the programm like Variables
 */
public final class ContainerUtils {

    //Attributs
    private  static Map<String, String> variablesList ;

    private  static List<Variable> variableContainer;

    ///Constructor
    public ContainerUtils() {

        variablesList = new HashMap<>();
        variableContainer = new ArrayList<>();
    }
    
    //Methods
    public static void addKeyValue(final String key, final String value) {
        variablesList.put(key, value);
    }

    /**
     * This method serve to get the variable store without the '{}'
     * This method will be serve to call an other method which interpret the value to see
     * If a function is used in the value
     * @param key
     * @return
     */
    public static String getValue(String key) {
        key = key.split("\\{")[1];
        key = key.split("\\}")[0];
        return variablesList.get(key);
    }

    public static void addVariable(final List<Variable> variable){ variableContainer.addAll(variable);}

    public static List<Variable> getVariableContainer(){return variableContainer;}

}
