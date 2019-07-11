package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.listener.EventListener;

import static com.google.common.base.Preconditions.checkNotNull;

final class EventListenerUtils {

    private static EventListener listener;

    private EventListenerUtils() {
    }

    private static EventListener getEventListener() {
        return checkNotNull(listener, "Event listener has not been initialized.");
    }

    static void setEventListener(EventListener listener) {
        EventListenerUtils.listener = listener;
    }

    static void readSupportedAction(final String actionName) {
        getEventListener().readSupportedAction(actionName);
    }

    static void readSupportedParameterWithWarn(String fileVariable, String scope, String shareMode, String s) {
        getEventListener().readSupportedParameterWithWarn(fileVariable, scope, shareMode, s);
    }

    static void readUnsupportedParameter(String csvDataSet, String string, String shareModeParameter) {
        getEventListener().readUnsupportedParameter(csvDataSet,string,shareModeParameter);
    }

    static void readSupportedFunctionWithWarn(String s, String httpRequest, Integer i, String s1) {
        getEventListener().readSupportedFunctionWithWarn(s,httpRequest,i,s1);
    }

    static void readUnsupportedAction(String s) {
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
