package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.PostFormRequest;

public class PostFormRequestWriter extends PostRequestWriter {

	public static final int FORM_CONTENT = 1;
	
	public PostFormRequestWriter(PostFormRequest request) {
		super(request);
	}	
	
	@Override
	protected int getPostType() {
		return FORM_CONTENT;
	}
}
