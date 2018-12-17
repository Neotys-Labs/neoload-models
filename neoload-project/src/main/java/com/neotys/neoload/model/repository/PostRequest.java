package com.neotys.neoload.model.repository;

import java.util.List;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
public interface PostRequest extends Request {
	List<Parameter> getPostParameters();
}
