package com.neotys.neoload.model.v3.writers.neoload;

import com.google.common.annotations.VisibleForTesting;
import com.neotys.neoload.model.v3.project.Element;
import org.apache.commons.collections4.map.LazyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.lang.reflect.Constructor;
import java.util.*;

public class WriterUtils {
	
	public static final String WEIGHTED_ACTION_XML_TAG_NAME = "weighted-embedded-action";
	public static final String EMBEDDED_ACTION_XML_TAG_NAME = "embedded-action";	
	public static final String IMMUTABLE = "Immutable";
	public static final String NL_VARIABLE_START = "${";
	public static final String NL_VARIABLE_END = "}";
		
    static final Logger LOGGER = LoggerFactory.getLogger(WriterUtils.class);
    
    private static Map<Element, String> elementUids = LazyMap.lazyMap(new HashMap<>(), a -> UUID.randomUUID().toString());
    
    private WriterUtils() {}
    
    public static String getElementUid(final Element element) {
		return elementUids.get(element);
	}

    public static  <T> T getWriterFor(Object object) {
        String elementClassName = object.getClass().getSimpleName();
		String packageName = object.getClass().getPackage().getName();
		String elementLastPackage = packageName.substring(packageName.lastIndexOf('.'));
        if(elementClassName.startsWith(IMMUTABLE)){
        	elementClassName = elementClassName.substring(IMMUTABLE.length());
		}
        Class writerClass;
        try {
            writerClass = Class.forName("com.neotys.neoload.model.v3.writers.neoload"+elementLastPackage+"."+elementClassName+"Writer");
			Constructor<?> constructor = writerClass.getConstructor(Class.forName(packageName + "." + elementClassName));
            return (T)constructor.newInstance(object);
        } catch (Exception e) {
            LOGGER.error("Cannot instanciate Element writer:" + elementClassName,e);
        }
        return null;
    }

    public static void generateEmbeddedAction(final Document document, final org.w3c.dom.Element parentXmlElement, final Element modelElem){
    	generateEmbeddedAction(document, parentXmlElement, modelElem, Optional.empty());
    }
    
    public static void generateEmbeddedAction(final Document document, final org.w3c.dom.Element parentXmlElement,
    		final Element modelElem, final Optional<String> embeddedActionName){
    	generateEmbeddedAction(document, parentXmlElement, modelElem, embeddedActionName, false);
    }
    
    public static void generateEmbeddedAction(final Document document, final org.w3c.dom.Element parentXmlElement,
    		final Element modelElem, final Optional<String> embeddedActionName, final boolean uidAsAttributes){
        final String uid = WriterUtils.getElementUid(modelElem);
    	final org.w3c.dom.Element newElem = document.createElement(embeddedActionName.orElse(EMBEDDED_ACTION_XML_TAG_NAME));
    	if (uidAsAttributes) {
    		newElem.setAttribute(ElementWriter.XML_UID_TAG, uid);
    	}else {
    		newElem.setTextContent(uid);
    	}
    	parentXmlElement.appendChild(newElem);
    }
    
    public static boolean isNLVariable(final String value){
		return value!=null && value.startsWith(NL_VARIABLE_START) 
				&& value.endsWith(NL_VARIABLE_END) 
				&& value.length()>(NL_VARIABLE_START.length()+NL_VARIABLE_END.length());
	}	

	public static String extractVariableName(final String value) {
		return value.substring(NL_VARIABLE_START.length(), value.length()-NL_VARIABLE_END.length());
	}

	@VisibleForTesting
	public static Set<Map.Entry<Element, String>> getGeneratedUids(){
    	return elementUids.entrySet();
	}
}
