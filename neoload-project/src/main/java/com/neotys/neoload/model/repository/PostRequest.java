package com.neotys.neoload.model.repository;

import java.util.List;

@Deprecated
public interface PostRequest extends Request {
	List<Parameter> getPostParameters();
}
