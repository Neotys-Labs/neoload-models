package com.neotys.neoload.model.readers.loadrunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Item {

    private List<String> attributes;

    public Item() {
        attributes = new ArrayList<>();
    }

    public static Item of(List<String> attributes) {
        Item item = new Item();
        item.getAttributes().addAll(attributes);
        return item;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    Optional<String> getAttribute(String name) {
        return attributes.stream().filter(s -> s.toLowerCase().startsWith(name.toLowerCase()+"=")).map(s -> s.substring((name+"=").length())).findFirst();
    }
}
