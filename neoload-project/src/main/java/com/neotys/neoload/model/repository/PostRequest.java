package com.neotys.neoload.model.repository;

import java.util.List;

public interface PostRequest extends Request {
	List<Parameter> getPostParameters();
}
