package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.HashMap;
import java.util.Map;

public class LoadRunnerSupportedMethods {

	private static final Map<String, LoadRunnerMethod> SUPPORTED_METHODS = new HashMap<>();
	
	static {
		SUPPORTED_METHODS.put("web_url", new WebUrlMethod());
		SUPPORTED_METHODS.put("web_submit_data", new WebSubmitDataMethod());
		SUPPORTED_METHODS.put("web_custom_request", new WebCustomRequestMethod());
		SUPPORTED_METHODS.put("web_link", new WebLinkMethod());
		SUPPORTED_METHODS.put("web_submit_form", new WebSubmitFormMethod());
		
		SUPPORTED_METHODS.put("web_reg_save_param", new WebRegSaveParamMethod());
		SUPPORTED_METHODS.put("web_reg_save_param_ex", new WebRegSaveParamExMethod());
		SUPPORTED_METHODS.put("web_reg_save_param_regexp", new WebRegSaveParamRegexpMethod());
		SUPPORTED_METHODS.put("web_reg_save_param_xpath", new WebRegSaveParamXpathMethod());
		SUPPORTED_METHODS.put("web_reg_save_param_json", new WebRegSaveParamJsonMethod());
		SUPPORTED_METHODS.put("web_reg_find", new WebRegFindMethod());
		
		SUPPORTED_METHODS.put("lr_think_time", new LRThinkTimeMethod());
		SUPPORTED_METHODS.put("lr_start_transaction", new LRStartTransactionMethod());
		SUPPORTED_METHODS.put("lr_end_transaction", new LREndTransactionMethod());
		SUPPORTED_METHODS.put("lr_start_sub_transaction", new LRStartTransactionMethod());
		SUPPORTED_METHODS.put("lr_end_sub_transaction", new LREndTransactionMethod());
		
		SUPPORTED_METHODS.put("web_cache_cleanup", new WebCacheCleanupMethod());
		SUPPORTED_METHODS.put("web_cleanup_cookies", new WebCleanupCookiesMethod());
		SUPPORTED_METHODS.put("web_add_cookie", new WebAddCookieMethod());
		SUPPORTED_METHODS.put("web_add_header", new WebAddHeaderMethod());
		SUPPORTED_METHODS.put("web_add_auto_header", new WebAddAutoHeaderMethod());
		
		SUPPORTED_METHODS.put("lr_eval_string", new LREvalStringMethod());
		SUPPORTED_METHODS.put("lr_save_string", new LRSaveStringMethod());
		SUPPORTED_METHODS.put("atoi", new AtoiMethod());
		SUPPORTED_METHODS.put("sprintf", new SprintfMethod());
		SUPPORTED_METHODS.put("lr_param_sprintf", new SprintfMethod());		
	}
	
	private LoadRunnerSupportedMethods() {
		super();
	}
	
	public static final LoadRunnerMethod get(final String methodName){
		return SUPPORTED_METHODS.get(methodName);
	}

}
