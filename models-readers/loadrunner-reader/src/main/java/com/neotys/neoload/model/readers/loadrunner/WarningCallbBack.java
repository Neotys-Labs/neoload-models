package com.neotys.neoload.model.readers.loadrunner;

import java.util.concurrent.atomic.AtomicBoolean;

public class WarningCallbBack {
	
	private AtomicBoolean isWarning = new AtomicBoolean(false);
	private StringBuilder messageBuilder = new StringBuilder();
	
	public WarningCallbBack() {
		super();
	}
	
	public void addWarningMessage(final String message){
		this.messageBuilder.append(message).append("\n");
		isWarning.set(true);
	}
	
	public boolean isWarning() {
		return isWarning.get();
	}
	
	public String getMessage() {
		return messageBuilder.toString();
	}
}
