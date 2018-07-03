package com.neotys.models.repository;

import java.util.Optional;

public interface PostRequest extends Request {
	Optional<String> getContentType();
}
