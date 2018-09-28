package com.neotys.neoload.model.readers.loadrunner;

import com.google.common.base.MoreObjects;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.repository.Container;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MutableContainer implements Container {

	private final String name;
	private final List<Element> childs = new ArrayList<>();
	private boolean isShared = false;

	public MutableContainer(final String name){
		this.name = name;
	}

	@Override
	public List<Element> getChilds() {
		return childs;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.empty();
	}

	@Override
	public Element withName(String of) {
		return new MutableContainer(name);
	}

	public void setShared(boolean shared) {
		isShared = shared;
	}

	@Override
	public boolean isShared() {
		return isShared;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("name", name)
				.add("childs", childs)
				.add("isShared", isShared)
				.toString();
	}
}
