package com.neotys.neoload.model.readers.loadrunner;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.repository.ImmutablePage;
import com.neotys.neoload.model.repository.Page;

public class WebSubmitForm extends WebRequest {

	private WebSubmitForm() {
	}

	public static Page toElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);
		final ImmutablePage.Builder pageBuilder = ImmutablePage.builder();
		visitor.readSupportedFunction(method.getName(), ctx);
		final String name = MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0));
		pageBuilder.addChilds(buildPostSubmitFormRequest(visitor, name));
		return pageBuilder.name(name).thinkTime(0).build();
	}
}
