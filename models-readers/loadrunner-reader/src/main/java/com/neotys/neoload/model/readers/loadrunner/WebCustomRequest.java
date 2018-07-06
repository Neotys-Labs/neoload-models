package com.neotys.neoload.model.readers.loadrunner;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.repository.*;
import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;

public class WebCustomRequest extends WebRequest {
	
	public static final String LR_HEXA_STR_PATTERN = "\\x";
	
    public static Element toElement(final LoadRunnerReader reader, final String leftBrace, final String rightBrace, final MethodCall method, final List<VariableExtractor> extractors, final List<Validator>validators) {
        Preconditions.checkNotNull(method);
        ImmutablePage.Builder pageBuilder = ImmutablePage.builder();

        pageBuilder.addChilds(buildPostRequest(reader, leftBrace, rightBrace, method, extractors, validators));
        
        MethodUtils.extractItemListAsStringList(leftBrace, rightBrace, method.getParameters(), MethodUtils.ITEM_BOUNDARY.EXTRARES.toString())
				.ifPresent(stringList -> getUrlList(stringList, getUrlFromMethodParameters(leftBrace, rightBrace, method)).stream().forEach(url -> pageBuilder.addChilds(buildGetRequestFromURL(reader, url))));
        
        return pageBuilder.name(MethodUtils.normalizeString(leftBrace, rightBrace, method.getParameters().get(0)))
                .thinkTime(0)
                .build();
    }
    
    /**
     * Build the "model request" for the LR function passed in method
     * @param method represent the LR "web_custom_request" function
     * @return the corresponding request of the model
     */
    public static PostRequest buildPostRequest(final LoadRunnerReader reader, final String leftBrace, final String rightBrace, final MethodCall method, final List<VariableExtractor> extractors, final List<Validator>validators) {
    	URL mainUrl = Preconditions.checkNotNull(getUrlFromMethodParameters(leftBrace, rightBrace, method));

		if (MethodUtils.getParameterWithName(method, "Body").isPresent()) {
			return ImmutablePostTextRequest.builder()
					.name(mainUrl.getPath())
					.path(mainUrl.getPath())
					.server(getServer(reader, mainUrl))
					.httpMethod(getMethod(leftBrace, rightBrace, method))
					.contentType(MethodUtils.getParameterValueWithName(leftBrace, rightBrace, method, "EncType"))
					.data(MethodUtils.getParameterValueWithName(leftBrace, rightBrace, method, "Body").get())
					.addAllExtractors(Optional.ofNullable(extractors).orElse(ImmutableList.of()))
					.addAllValidators(Optional.ofNullable(validators).orElse(ImmutableList.of()))
					.addAllParameters(MethodUtils.queryToParameterList(mainUrl.getQuery()))
					.build();
		}
		if(MethodUtils.getParameterWithName(method, "BodyBinary").isPresent()) {
			return ImmutablePostBinaryRequest.builder()
					.name(mainUrl.getPath())
					.path(mainUrl.getPath())
					.server(getServer(reader, mainUrl))
					.httpMethod(getMethod(leftBrace, rightBrace, method))
					.contentType(MethodUtils.getParameterValueWithName(leftBrace, rightBrace, method, "EncType"))
					.binaryData(extractBinaryBody(leftBrace, rightBrace, method))
					.addAllExtractors(Optional.ofNullable(extractors).orElse(ImmutableList.of()))
					.addAllValidators(Optional.ofNullable(validators).orElse(ImmutableList.of()))
					.addAllParameters(MethodUtils.queryToParameterList(mainUrl.getQuery()))
					.build();
		}

    	logger.warn("There is not any body parameter for the following LR function : " + method.getName());
		return null;
    }
    
    public static byte[] extractBinaryBody(final String leftBrace, final String rightBrace, final MethodCall method) {
		return hexStringToByteArray(MethodUtils.getParameterValueWithName(leftBrace, rightBrace, method, "BodyBinary").orElse(""));
    }
    
    public static byte[] hexStringToByteArray(String s) {
    	int stringPointer = 0;
    	int dataPointer = 0;
    	int globalSize = s.length();
    	int hexaStringSize = LR_HEXA_STR_PATTERN.length() + 2;
    	int nbHexa = StringUtils.countMatches(s, LR_HEXA_STR_PATTERN);
    	int byteSize = (globalSize - nbHexa*hexaStringSize) + nbHexa;
    	byte[] rawData = new byte[byteSize];
    	
    	while (stringPointer<globalSize) {
    		if(stringPointer + hexaStringSize - 1 < globalSize) {
    			String hexaString = s.substring(stringPointer, stringPointer + hexaStringSize);
    			if (hexaString.startsWith(LR_HEXA_STR_PATTERN)) {
    				hexaString = hexaString.replace(LR_HEXA_STR_PATTERN, "");
    				rawData[dataPointer] = (byte) ((Character.digit(hexaString.charAt(0), 16) << 4)
                            + Character.digit(hexaString.charAt(1), 16));
    				stringPointer = hexaStringSize + stringPointer;
    			}else {
    				rawData[dataPointer] = (byte) s.charAt(stringPointer);
    				stringPointer++;
    			}
    		}else {
    			rawData[dataPointer] = (byte) s.charAt(stringPointer);
    			stringPointer++;
    		}
    		dataPointer++;
    	}
        return rawData;
    }
}
