package com.neotys.neoload.model.v3.project;

public enum DependencyType {

    JAVA_FOR_JS_LIBRARY("lib/jslib"),
    JAVA_LIBRARY("lib/extlib"),
    JS_LIBRARY("scripts");

    private final String locationFolderName;

    DependencyType(final String locationFolderName) {
        this.locationFolderName = locationFolderName;
    }

    public String getLocationFolderName() {
        return locationFolderName;
    }

}
