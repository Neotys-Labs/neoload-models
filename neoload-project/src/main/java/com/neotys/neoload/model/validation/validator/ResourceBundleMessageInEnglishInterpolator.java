package com.neotys.neoload.model.validation.validator;

import java.util.Locale;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

@Deprecated
final class ResourceBundleMessageInEnglishInterpolator extends ResourceBundleMessageInterpolator {
	private static final String DEFAULT_VALIDATION_MESSAGES = "com.neotys.neoload.model.validation.ValidationMessages";
	
	protected ResourceBundleMessageInEnglishInterpolator() {
		super(new PlatformResourceBundleLocator( DEFAULT_VALIDATION_MESSAGES ));
	}

	@Override
	public String interpolate(String message, Context context) {
		return super.interpolate(message, context, Locale.ENGLISH);
	}

	@Override
	public String interpolate(String message, Context context, Locale locale) {
		return super.interpolate(message, context, Locale.ENGLISH);
	}
}
