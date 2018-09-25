package com.neotys.neoload.model.readers.loadrunner.method;

public class WebregsaveparamexMethod extends AbstractWebRegSaveParamMethod {
	
	public WebregsaveparamexMethod() {
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
