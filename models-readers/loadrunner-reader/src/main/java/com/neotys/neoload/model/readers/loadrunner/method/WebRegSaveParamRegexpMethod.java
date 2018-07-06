package com.neotys.neoload.model.readers.loadrunner.method;

public class WebRegSaveParamRegexpMethod extends AbstractWebRegSaveParamMethod {
	
	public WebRegSaveParamRegexpMethod() {
		super();
	}
	
	@Override
	boolean supportLBRB() {
		return false;
	}
	
	@Override
	boolean supportRegExp() {
		return true;
	}
	
	@Override
	boolean supportXPath() {
		return false;
	}
	
	@Override
	boolean supportJsonPath() {
		return false;
	}
}
