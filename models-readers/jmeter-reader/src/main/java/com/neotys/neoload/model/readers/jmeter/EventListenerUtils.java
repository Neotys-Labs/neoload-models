package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.listener.EventListener;

import static com.google.common.base.Preconditions.checkNotNull;

/*
We use this class to create a global EventListener, like this, we create one in the JMeter Reader and we an call it
In the others class to infomr the client of the problem or good convertion of the elements
 */
public final class EventListenerUtils {

    //Attributs
    private static EventListener listener;

    //Constructor
    private EventListenerUtils() {
    }

    //Methods
    private static EventListener getEventListener() {
        return checkNotNull(listener, "Event listener has not been initialized.");
    }

    public static void setEventListener(EventListener listener) {
        EventListenerUtils.listener = listener;
    }

    public static void readSupportedAction(final String actionName) {
        getEventListener().readSupportedAction(actionName);
    }

    public static void readSupportedParameterWithWarn(String scriptName, String parameterType, String parameterName, String warning) {
        getEventListener().readSupportedParameterWithWarn(scriptName, parameterType, parameterName, warning);
    }

    public static void readUnsupportedParameter(String scriptName, String parameterType, String parameterName) {
        getEventListener().readUnsupportedParameter(scriptName,parameterType,parameterName);
    }

    public static void readSupportedFunction(final String scriptName, final String functionName) {
        getEventListener().readSupportedFunction(scriptName,functionName,0);
    }

    public static void readUnsupportedFunction(final String scriptName, final String functionName){
        getEventListener().readUnsupportedFunction(scriptName,functionName,1);
    }

    public static void readSupportedFunctionWithWarn(String scriptName, String functionName, String warning) {
        getEventListener().readSupportedFunctionWithWarn(scriptName,functionName,0,warning);
    }

    public static void readUnsupportedAction(String s) {
        getEventListener().readUnsupportedAction(s);
    }

    static void endReadingScripts() {
        getEventListener().endReadingScripts();
    }

    static void startReadingScripts(int ligne) {
        getEventListener().startReadingScripts(ligne);
    }

    static void endScript() {
        getEventListener().endScript();
    }

    static void startScript(String name) {
        getEventListener().startScript(name);
    }
}
