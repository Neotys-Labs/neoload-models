package com.neotys.neoload.model.readers.jmeter.step.httpRequest;

import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
abstract class HTTPDefaultSetModel {

    //Attributs

    private static final Logger LOGGER = LoggerFactory.getLogger(HTTPDefaultSetModel.class);

    abstract String getName();

    abstract String getDomain();

    abstract String getPath();

    abstract String getPort();

    abstract String getProtocol();

    //Constructor
    static ImmutableHTTPDefaultSetModel.Builder builder() {
        return ImmutableHTTPDefaultSetModel.builder();
    }

    //Methods
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
            try{
                return Integer.parseInt(getPort());
            }catch(Exception e){
                LOGGER.warn("We can't manage the variable into the Port Number \n"
                        + "So We put 0 in value of Port Number",e);
                return 0;
            }
        }
    }


}

