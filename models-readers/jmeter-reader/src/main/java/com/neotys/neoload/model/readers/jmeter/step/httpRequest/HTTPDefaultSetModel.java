package com.neotys.neoload.model.readers.jmeter.step.httpRequest;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
abstract class HTTPDefaultSetModel {

    abstract String getName();

    abstract String getDomain();

    abstract String getPath();

    abstract String getPort();

    abstract String getProtocol();

    static ImmutableHTTPDefaultSetModel.Builder builder() {
        return ImmutableHTTPDefaultSetModel.builder();
    }

    String checkPath(){
        if (getPath().isEmpty()){
            return "/";
        }
        else{
            return getPath();
        }
    }

    String checkDomain(){
        if( getDomain().isEmpty()){
            return "localhost";
        }
        else{
            return getDomain();
        }
    }

    String checkProtocol(){
        if (getProtocol().isEmpty()){
            return "http";
        }
        else{
            return getProtocol().toLowerCase();
        }
    }

    int checkPort(){
        if(getPort().isEmpty()){
            return "http".equals(checkProtocol()) ? 80:443;
        }
        else{
            return Integer.parseInt(getPort());
        }
    }


}

