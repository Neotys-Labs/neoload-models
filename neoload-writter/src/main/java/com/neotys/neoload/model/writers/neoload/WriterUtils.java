package com.neotys.neoload.model.writers.neoload;

import com.google.common.hash.Hashing;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.repository.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class WriterUtils {
	
	public static final String WEIGHTED_ACTION_XML_TAG_NAME = "weighted-embedded-action";
	public static final String EMBEDDED_ACTION_XML_TAG_NAME = "embedded-action";
	public static final String XML_UID_NAME_ATTR = "uid";
	public static final String IMMUTABLE = "Immutable";
	
    static Logger logger = LoggerFactory.getLogger(NeoLoadWriter.class);

    private WriterUtils() {}

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
        String uid = Hashing.sha256().hashString(parentPath+"/"+modelElem.getName(), StandardCharsets.UTF_8).toString();
    	org.w3c.dom.Element newElem = document.createElement(embeddedActionName.orElse(EMBEDDED_ACTION_XML_TAG_NAME));
    	if (uidAsAttributes) {
    		newElem.setAttribute(XML_UID_NAME_ATTR, uid);
    	}else {
    		newElem.setTextContent(uid);
    	}
    	parentXmlElement.appendChild(newElem);
    }

}
