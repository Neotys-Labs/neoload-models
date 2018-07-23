package com.neotys.neoload.model.repository;

import java.util.List;
import java.util.Optional;

public interface Request extends PageElement {

    enum HttpMethod {
        GET,
        POST,
        HEAD,
        PUT,
        DELETE,
        OPTIONS,
        TRACE,
        CUSTOM
    }

    /**
     * The request full path containing the URL parameters.
     * @return
     */
    Optional<String> getPath();
    Optional<Server> getServer();
    HttpMethod getHttpMethod();
    List<Parameter> getParameters();
    List<VariableExtractor> getExtractors();
    List<Validator> getValidators();
    List<Header> getHeaders();
    Optional<String> getContentType();
}
