package com.neotys.neoload.model.readers.loadrunner.method;

public class WebregsaveparamxpathMethod extends AbstractWebRegSaveParamMethod {

	public WebregsaveparamxpathMethod() {
		super();
	}
	
	@Override
	boolean supportLBRB() {
		return false;
	}
	
	@Override
	boolean supportRegExp() {
		return false;
	}
	
	@Override
	boolean supportXPath() {
		return true;
	}
	
	@Override
	boolean supportJsonPath() {
		return false;
	}
}
