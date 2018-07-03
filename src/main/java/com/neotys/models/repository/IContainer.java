package com.neotys.models.repository;

import com.neotys.models.core.Element;

import java.util.List;

public interface IContainer extends Element {
    List<Element> getChilds();
}
