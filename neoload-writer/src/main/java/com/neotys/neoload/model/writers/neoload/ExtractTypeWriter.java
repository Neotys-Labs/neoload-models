package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.VariableExtractor.ExtractType;

public class ExtractTypeWriter {

	private static final String XML_ATTR_EXTRACT_TYPE = "extractType";
	private static final String XML_ATTR_EXTRACTTYPEADV = "extractTypeAdv";	
	private static final String XML_ATTR_EXTRACT_TYPE_SIMPLE = "extractTypeSimple";
        
	private ExtractTypeWriter() {		
	}
	
    public static void writeXML(final Element xmlExtractor, final ExtractType extractType) {
    	final String extractTypeString = getExtractTypeInt(extractType)+"";
    	xmlExtractor.setAttribute(XML_ATTR_EXTRACT_TYPE, extractTypeString);
    	xmlExtractor.setAttribute(XML_ATTR_EXTRACTTYPEADV, "0");
    	xmlExtractor.setAttribute(XML_ATTR_EXTRACT_TYPE_SIMPLE, extractTypeString);
    }
    
    private static int getExtractTypeInt(final ExtractType extractType){
    	switch(extractType){
    		case BOTH: 
    			return 5;
    		case JSON: 
    			return 4;
    		case XPATH: 
    			return 3;
    		case HEADERS: 
    			return 1;
    		case BODY: 
    			return 0;    		
    		default: 
    			return 0; 
    	}
    }
}
