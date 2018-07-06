package com.neotys.neoload.model.repository;

import java.util.Optional;

public interface PostRequest extends Request {
	Optional<String> getContentType();
}
