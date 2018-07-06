package com.neotys.neoload.model.readers.loadrunner.method;

public class WebRegSaveParamJsonMethod extends AbstractWebRegSaveParamMethod{

	public WebRegSaveParamJsonMethod() {
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
		return false;
	}
	
	@Override
	boolean supportJsonPath() {
		return true;
	}
}
