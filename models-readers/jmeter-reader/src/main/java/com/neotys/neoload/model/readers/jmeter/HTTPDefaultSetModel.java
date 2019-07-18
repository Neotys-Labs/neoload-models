package com.neotys.neoload.model.readers.jmeter;

import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
abstract class HTTPDefaultSetModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(HTTPSamplerProxyConverter.class);

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
            return checkProtocol().equals("http")? 80:443;
        }
        else{
            return Integer.parseInt(getPort());
        }
    }


}

