package com.neotys.neoload.model.readers.loadrunner;

import java.net.URL;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.repository.ImmutablePage;
import com.neotys.neoload.model.repository.ImmutablePostBinaryRequest;
import com.neotys.neoload.model.repository.ImmutablePostTextRequest;
import com.neotys.neoload.model.repository.PostRequest;

public class WebCustomRequest extends WebRequest {
	
	public static final String LR_HEXA_STR_PATTERN = "\\x";
	
    public static Element toElement(final LoadRunnerVUVisitor visitor, final MethodCall method) {
        Preconditions.checkNotNull(method);
        ImmutablePage.Builder pageBuilder = ImmutablePage.builder();

        pageBuilder.addChilds(buildPostRequest(visitor, method));
        
        MethodUtils.extractItemListAsStringList(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters(), MethodUtils.ITEM_BOUNDARY.EXTRARES.toString())
				.ifPresent(stringList -> getUrlList(stringList, getUrlFromMethodParameters(visitor.getLeftBrace(), visitor.getRightBrace(), method)).stream().forEach(url -> pageBuilder.addChilds(buildGetRequestFromURL(visitor, url))));
        
        return pageBuilder.name(MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0)))
                .thinkTime(0)
                .build();
    }
    
    /**
     * Build the "model request" for the LR function passed in method
     * @param method represent the LR "web_custom_request" function
     * @return the corresponding request of the model
     */
    public static PostRequest buildPostRequest(final LoadRunnerVUVisitor visitor, final MethodCall method) {
    	URL mainUrl = Preconditions.checkNotNull(getUrlFromMethodParameters(visitor.getLeftBrace(), visitor.getRightBrace(), method));

		if (MethodUtils.getParameterWithName(method, "Body").isPresent()) {
			return ImmutablePostTextRequest.builder()
					.name(mainUrl.getPath())
					.path(mainUrl.getPath())
					.server(getServer(visitor.getReader(), mainUrl))
					.httpMethod(getMethod(visitor.getLeftBrace(), visitor.getRightBrace(), method))
					.contentType(MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method, "EncType"))
					.data(MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method, "Body").get())
					.addAllExtractors(Optional.ofNullable(visitor.getCurrentExtractors()).orElse(ImmutableList.of()))
					.addAllValidators(Optional.ofNullable(visitor.getCurrentValidators()).orElse(ImmutableList.of()))
					.addAllHeaders(Optional.ofNullable(visitor.getCurrentHeaders()).orElse(ImmutableList.of()))
					.addAllParameters(MethodUtils.queryToParameterList(mainUrl.getQuery()))
					.build();
		}
		if(MethodUtils.getParameterWithName(method, "BodyBinary").isPresent()) {
			return ImmutablePostBinaryRequest.builder()
					.name(mainUrl.getPath())
					.path(mainUrl.getPath())
					.server(getServer(visitor.getReader(), mainUrl))
					.httpMethod(getMethod(visitor.getLeftBrace(), visitor.getRightBrace(), method))
					.contentType(MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method, "EncType"))
					.binaryData(extractBinaryBody(visitor.getLeftBrace(), visitor.getRightBrace(), method))
					.addAllExtractors(Optional.ofNullable(visitor.getCurrentExtractors()).orElse(ImmutableList.of()))
					.addAllValidators(Optional.ofNullable(visitor.getCurrentValidators()).orElse(ImmutableList.of()))
					.addAllHeaders(Optional.ofNullable(visitor.getCurrentHeaders()).orElse(ImmutableList.of()))
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
