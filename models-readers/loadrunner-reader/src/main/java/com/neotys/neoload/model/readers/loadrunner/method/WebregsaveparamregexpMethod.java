package com.neotys.neoload.model.readers.loadrunner.method;

public class WebregsaveparamregexpMethod extends AbstractWebRegSaveParamMethod {
	
	public WebregsaveparamregexpMethod() {
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
