package com.neotys.neoload.model.writers.neoload;

import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.LazyMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.google.common.hash.Hashing;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.repository.Variable;

public class WriterUtils {
	
	public static final String WEIGHTED_ACTION_XML_TAG_NAME = "weighted-embedded-action";
	public static final String EMBEDDED_ACTION_XML_TAG_NAME = "embedded-action";
	public static final String XML_UID_NAME_ATTR = "uid";
	public static final String IMMUTABLE = "Immutable";
	
    static Logger logger = LoggerFactory.getLogger(NeoLoadWriter.class);
    
    private static Map<Pair<String,Element>, String> elementUids = LazyMap.lazyMap(new HashMap<Pair<String,Element>, String>(), new Transformer<Pair<String,Element>, String>(){
    	@Override
    	public String transform(final Pair<String,Element> element) {
    		return Hashing.sha256().hashString(element.getLeft()+"/"+element.getRight().getName(), StandardCharsets.UTF_8).toString();
    	}
    });
    
    private WriterUtils() {}
    
    public static String getElementUid(final Pair<String, Element> element) {
		return elementUids.get(element);
	}    

    public static ElementWriter getWriterFor(Element element) {
        String elementClassName = element.getClass().getSimpleName();
        if(elementClassName.startsWith(IMMUTABLE)) elementClassName = elementClassName.substring(IMMUTABLE.length());
        Class writerClass;
        try {
            writerClass = Class.forName("com.neotys.neoload.model.writers.neoload."+elementClassName+"Writer");
            Constructor<?> constructor = writerClass.getConstructor(Class.forName("com.neotys.neoload.model.repository." + elementClassName));
            return (ElementWriter)constructor.newInstance(element);
        } catch (Exception e) {
            logger.error("Cannot instanciate Element writer:" + elementClassName + "/" + element.getName(),e);
        }
        return null;
    }

    public static VariableWriter getWriterFor(Variable variable) {
        String variableClassName = variable.getClass().getSimpleName();
        if(variableClassName.startsWith(IMMUTABLE)) variableClassName = variableClassName.substring(IMMUTABLE.length());
        Class writerClass;
        try {
            writerClass = Class.forName("com.neotys.neoload.model.writers.neoload."+variableClassName+"Writer");
            Constructor<?> constructor = writerClass.getConstructor(Class.forName("com.neotys.neoload.model.repository." + variableClassName));
            return (VariableWriter) constructor.newInstance(variable);
        } catch (Exception e) {
            logger.error("Cannot instanciate Variable writer:" + variableClassName + "/" + variable.getName(),e);
        }
        return null;
    }

    public static void generateEmbeddedAction(final Document document, final org.w3c.dom.Element parentXmlElement,
    		Element modelElem, String parentPath){
    	generateEmbeddedAction(document, parentXmlElement, modelElem, parentPath, Optional.empty());
    }
    
    public static void generateEmbeddedAction(final Document document, final org.w3c.dom.Element parentXmlElement,
    		Element modelElem, String parentPath, Optional<String> embeddedActionName){
    	generateEmbeddedAction(document, parentXmlElement, modelElem, parentPath, embeddedActionName, false);
    }
    
    public static void generateEmbeddedAction(final Document document, final org.w3c.dom.Element parentXmlElement,
    		Element modelElem, String parentPath, Optional<String> embeddedActionName, boolean uidAsAttributes){
        final String uid = WriterUtils.getElementUid(new ImmutablePair<>(parentPath, modelElem));
    	final org.w3c.dom.Element newElem = document.createElement(embeddedActionName.orElse(EMBEDDED_ACTION_XML_TAG_NAME));
    	if (uidAsAttributes) {
    		newElem.setAttribute(XML_UID_NAME_ATTR, uid);
    	}else {
    		newElem.setTextContent(uid);
    	}
    	parentXmlElement.appendChild(newElem);
    }

}
