package com.neotys.neoload.model.readers.loadrunner.method;

public class WebRegSaveParamMethod extends AbstractWebRegSaveParamMethod {		
	
	public WebRegSaveParamMethod() {
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
