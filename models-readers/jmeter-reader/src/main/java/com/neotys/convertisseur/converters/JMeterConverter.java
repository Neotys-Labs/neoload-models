package com.neotys.convertisseur.converters;

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
