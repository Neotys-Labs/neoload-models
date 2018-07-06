package com.neotys.neoload.model.repository;

import com.neotys.neoload.model.core.Element;

import java.util.List;

public interface IContainer extends Element {
    List<Element> getChilds();
}
