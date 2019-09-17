package com.neotys.neoload.model.readers.loadrunner;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Properties;


import com.google.common.base.Preconditions;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.repository.*;
import org.apache.commons.lang3.StringUtils;

import static com.neotys.neoload.model.readers.loadrunner.MethodUtils.ITEM_BOUNDARY.EXTRARES;
public class WebCustomRequest extends WebRequest {
	
	public static final String LR_HEXA_STR_PATTERN = "\\x";
	
    public static Page toElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
    	
        Preconditions.checkNotNull(method);
        ImmutablePage.Builder pageBuilder = ImmutablePage.builder();

		final Request request = buildRequest(visitor, method);
		visitor.readSupportedFunction(method.getName(), ctx);
		pageBuilder.addChilds(request);

		// we use the request headers of the main request for the resources.
		final List<Header> recordedHeaders = getHeadersFromRecordedFile(request.getRecordedFiles().flatMap(RecordedFiles::recordedRequestHeaderFile));

		MethodUtils.extractItemListAsStringList(visitor, method.getParameters(), EXTRARES, Optional.of(pageBuilder))
				.ifPresent(stringList -> getUrlList(stringList, getUrlFromMethodParameters(visitor.getLeftBrace(), visitor.getRightBrace(), method))
						.forEach(url -> pageBuilder.addChilds(buildGetRequestFromURL(visitor, url, Optional.empty(), recordedHeaders))));
		final String name = MethodUtils.normalizeName(MethodUtils.normalizeString(
				visitor.getLeftBrace(),
				visitor.getRightBrace(),
				method.getParameters().get(0)
		));
		return pageBuilder.name(name)
                .thinkTime(0)               
                .build();		
    }
    
    /**
     * Build the "model request" for the LR function passed in method
     * @param method represent the LR "web_custom_request" function
     * @return the corresponding request of the model
     */
    public static Request buildRequest(final LoadRunnerVUVisitor visitor, final MethodCall method) {
    	URL mainUrl = Preconditions.checkNotNull(getUrlFromMethodParameters(visitor.getLeftBrace(), visitor.getRightBrace(), method));

    	final Optional<Properties> snapshotProperties = getSnapshotProperties(visitor, method); 
    	final Optional<RecordedFiles> recordedFiles = getRecordedFilesFromSnapshotProperties(visitor, snapshotProperties);
		final List<Header> recordedHeaders = getHeadersFromRecordedFile(recordedFiles.flatMap(RecordedFiles::recordedRequestHeaderFile));

		if(getMethod(visitor.getLeftBrace(), visitor.getRightBrace(), method).equals(Request.HttpMethod.GET)) {
			final ImmutableGetPlainRequest.Builder builder = ImmutableGetPlainRequest.builder()
					.name(mainUrl.getPath())
					.path(mainUrl.getPath())
					.server(visitor.getReader().getServer(mainUrl))
					.httpMethod(getMethod(visitor.getLeftBrace(), visitor.getRightBrace(), method))
					.contentType(MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method, "EncType"))
					.addAllExtractors(visitor.getCurrentExtractors())
					.addAllValidators(visitor.getCurrentValidators())
					.addAllHeaders(visitor.getCurrentHeaders())
					.addAllHeaders(visitor.getGlobalHeaders())
					.addAllHeaders(recordedHeaders)
					.addAllParameters(MethodUtils.queryToParameterList(mainUrl.getQuery()))
                    .recordedFiles(recordedFiles);
			visitor.getCurrentValidators().clear();
			visitor.getCurrentHeaders().clear();
			return builder.build();
		} else {
			if (MethodUtils.getParameterWithName(method, "BodyBinary").isPresent()) {
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
                visitor.getCurrentValidators().clear();
				visitor.getCurrentHeaders().clear();
				return builder.build();
			} else {
				final ImmutablePostTextRequest.Builder builder = ImmutablePostTextRequest.builder()
						.name(mainUrl.getPath())
						.path(mainUrl.getPath())
						.server(visitor.getReader().getServer(mainUrl))
						.httpMethod(getMethod(visitor.getLeftBrace(), visitor.getRightBrace(), method))
						.contentType(MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method, "EncType"))
						.data(MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method, "Body").orElse(""))
						.addAllExtractors(visitor.getCurrentExtractors())
						.addAllValidators(visitor.getCurrentValidators())
						.addAllHeaders(visitor.getCurrentHeaders())
						.addAllHeaders(visitor.getGlobalHeaders())
						.addAllHeaders(recordedHeaders)
						.addAllParameters(MethodUtils.queryToParameterList(mainUrl.getQuery()))
						.recordedFiles(recordedFiles);
                visitor.getCurrentValidators().clear();
				visitor.getCurrentHeaders().clear();
				return builder.build();
			}
		}
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
