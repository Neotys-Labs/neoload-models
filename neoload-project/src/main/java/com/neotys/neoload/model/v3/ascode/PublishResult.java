package com.neotys.neoload.model.v3.ascode;

/** What a publish run changed. An unchanged contract leaves every flag false. */
public final class PublishResult {
	private final boolean schema;
	private final boolean documentation;
	private final boolean steps;

	public PublishResult(final boolean schema, final boolean documentation, final boolean steps) {
		this.schema = schema;
		this.documentation = documentation;
		this.steps = steps;
	}

	public boolean schemaChanged() {
		return schema;
	}

	public boolean documentationChanged() {
		return documentation;
	}

	public boolean stepsChanged() {
		return steps;
	}

	public boolean changed() {
		return schema || documentation || steps;
	}
}
