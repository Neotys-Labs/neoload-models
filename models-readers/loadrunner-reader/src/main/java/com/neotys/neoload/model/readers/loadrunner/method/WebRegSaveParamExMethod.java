package com.neotys.neoload.model.readers.loadrunner.method;

public class WebRegSaveParamExMethod extends AbstractWebRegSaveParamMethod {
	
	public WebRegSaveParamExMethod() {
		super();
	}
	
	@Override
	boolean supportLBRB() {
		return true;
	}
	
	@Override
	boolean supportRegExp() {
		return false;
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
