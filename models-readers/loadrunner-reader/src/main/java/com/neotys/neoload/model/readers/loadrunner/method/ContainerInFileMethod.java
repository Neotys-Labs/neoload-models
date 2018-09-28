package com.neotys.neoload.model.readers.loadrunner.method;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.Container;

import java.util.List;
import java.util.Map;

public class ContainerInFileMethod implements LoadRunnerMethod {

	private final Map<String, ? extends Container> containers;

	public ContainerInFileMethod(final Map<String, ? extends Container> containers) {
		this.containers = containers;
	}

	boolean containsContainer(final String containerName) {
		return containers.containsKey(containerName);
	}

	@Override
	public List<Element> getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final CPP14Parser.MethodcallContext ctx) {
		return ImmutableList.of(containers.get(method.getName()));
	}
}
