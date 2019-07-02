package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.listener.EventListener;

public class JMeterConverter {

    private EventListener eventListener;

    public JMeterConverter(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public EventListener getEventListener() {
        return eventListener;
    }
}
