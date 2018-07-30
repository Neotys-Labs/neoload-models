package com.neotys.neoload.model.readers.loadrunner;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.repository.Header;
import com.neotys.neoload.model.repository.ImmutablePage;
import com.neotys.neoload.model.repository.ImmutablePostBinaryRequest;
import com.neotys.neoload.model.repository.ImmutablePostTextRequest;
import com.neotys.neoload.model.repository.Page;
import com.neotys.neoload.model.repository.PostRequest;
import com.neotys.neoload.model.repository.RecordedFiles;
import static com.neotys.neoload.model.readers.loadrunner.MethodUtils.ITEM_BOUNDARY.EXTRARES;
public class WebCustomRequest extends WebRequest {
	
	public static final String LR_HEXA_STR_PATTERN = "\\x";
	
    public static Page toElement(final LoadRunnerVUVisitor visitor, final MethodCall method) {
        Preconditions.checkNotNull(method);
        ImmutablePage.Builder pageBuilder = ImmutablePage.builder();

		final PostRequest postRequest = buildPostRequest(visitor, method);
		if(postRequest != null) {
			pageBuilder.addChilds(postRequest);

			// we use the request headers of the main request for the resources.
			final List<Header> recordedHeaders = getHeadersFromRecordedFile(postRequest.getRecordedFiles().flatMap(RecordedFiles::recordedRequestHeaderFile));

			MethodUtils.extractItemListAsStringList(visitor, method.getParameters(), EXTRARES, Optional.of(pageBuilder))
					.ifPresent(stringList -> getUrlList(stringList, getUrlFromMethodParameters(visitor.getLeftBrace(), visitor.getRightBrace(), method))
							.forEach(url -> pageBuilder.addChilds(buildGetRequestFromURL(visitor, url, Optional.empty(), recordedHeaders))));
		}
        
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

    	final Optional<Properties> snapshotProperties = getSnapshotProperties(visitor, method); 
    	final Optional<RecordedFiles> recordedFiles = getRecordedFilesFromSnapshotProperties(visitor, method, snapshotProperties);
		final List<Header> recordedHeaders = getHeadersFromRecordedFile(recordedFiles.flatMap(RecordedFiles::recordedRequestHeaderFile));

		if (MethodUtils.getParameterWithName(method, "Body").isPresent()) {
			final ImmutablePostTextRequest.Builder builder = ImmutablePostTextRequest.builder()
					.name(mainUrl.getPath())
					.path(mainUrl.getPath())
					.server(visitor.getReader().getServer(mainUrl))
					.httpMethod(getMethod(visitor.getLeftBrace(), visitor.getRightBrace(), method))
					.contentType(MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method, "EncType"))
					.data(MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method, "Body").get())
					.addAllExtractors(visitor.getCurrentExtractors())
					.addAllValidators(visitor.getCurrentValidators())
					.addAllHeaders(visitor.getCurrentHeaders())
					.addAllHeaders(visitor.getGlobalHeaders())
					.addAllHeaders(recordedHeaders)
					.addAllParameters(MethodUtils.queryToParameterList(mainUrl.getQuery()))
                    .recordedFiles(recordedFiles);
			visitor.getCurrentHeaders().clear();
			return builder.build();
		}
		if(MethodUtils.getParameterWithName(method, "BodyBinary").isPresent()) {
				final ImmutablePostBinaryRequest.Builder builder = ImmutablePostBinaryRequest.builder()
					.name(mainUrl.getPath())
					.path(mainUrl.getPath())
					.server(visitor.getReader().getServer(mainUrl))
					.httpMethod(getMethod(visitor.getLeftBrace(), visitor.getRightBrace(), method))
					.contentType(MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method, "EncType"))
					.binaryData(extractBinaryBody(visitor.getLeftBrace(), visitor.getRightBrace(), method))
					.addAllExtractors(visitor.getCurrentExtractors())
					.addAllValidators(visitor.getCurrentValidators())
					.addAllHeaders(visitor.getCurrentHeaders())
					.addAllHeaders(visitor.getGlobalHeaders())
					.addAllHeaders(recordedHeaders)
					.addAllParameters(MethodUtils.queryToParameterList(mainUrl.getQuery()))
					.recordedFiles(recordedFiles);
				visitor.getCurrentHeaders().clear();
				return builder.build();
		}

    	LOGGER.warn("There is not any body parameter for the following LR function : " + method.getName());
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
