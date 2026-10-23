package com.neotys.neoload.model.v3.binding.serializer;

import com.google.common.collect.ImmutableBiMap;

/**
 * The Real Browser steps ({@code browser_click}...) are {@link com.neotys.neoload.model.v3.project.userpath.CustomAction}s
 * whose type is given by the step name instead of a {@code type} property. Their name is optional and defaults to the
 * type of the action.
 */
final class RealBrowserSteps {
	private static final ImmutableBiMap<String, String> TYPES_BY_STEP = ImmutableBiMap.<String, String>builder()
			.put("browser_open", "BrowserOpen")
			.put("browser_close", "BrowserClose")
			.put("browser_navigate", "BrowserNavigate")
			.put("browser_click", "BrowserClick")
			.put("browser_type", "BrowserType")
			.put("browser_press", "BrowserPress")
			.put("browser_select_option", "BrowserSelectOption")
			.put("browser_mouse_over", "BrowserMouseOver")
			.put("browser_drag_and_drop", "BrowserDragAndDrop")
			.put("browser_scroll", "BrowserScroll")
			.put("browser_read", "BrowserRead")
			.put("browser_wait_selector", "BrowserWaitSelector")
			.put("browser_evaluate_javascript", "BrowserEvaluateJavascript")
			.put("browser_upload_file", "BrowserUploadFile")
			.put("browser_handle_dialog", "BrowserHandleDialog")
			.put("browser_open_tab", "BrowserOpenTab")
			.put("browser_switch_tab", "BrowserSwitchTab")
			.put("browser_close_tab", "BrowserCloseTab")
			.put("browser_capture_requests", "BrowserCaptureRequests")
			.put("browser_capture_web_vitals", "BrowserCaptureWebVitals")
			.build();

	private RealBrowserSteps() {
		super();
	}

	/** @return the custom action type of the step, or {@code null} when the step is not a Real Browser step */
	static String typeOf(final String stepName) {
		return TYPES_BY_STEP.get(stepName);
	}

	/** @return the step name of the custom action type, or {@code null} when the type is not a Real Browser action */
	static String stepOf(final String type) {
		return TYPES_BY_STEP.inverse().get(type);
	}
}
